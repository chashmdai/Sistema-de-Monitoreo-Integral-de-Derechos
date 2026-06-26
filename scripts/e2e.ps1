#requires -Version 7.0
<#
=====================================================================================
 SMID — Flujo end-to-end del ecosistema completo (excepto instituciones)
 PowerShell 7

 Recorre la cadena real, usando el JWT emitido por auth (no firma tokens a mano):

   auth (login)
     -> catálogo (derecho + causa)
     -> personas (requirente ADULTO + NNA)
     -> requerimientos (crear -> NNA -> enviar -> admisibilidad ASIGNACION)
          [evento requerimiento.asignado por RabbitMQ]
     -> casos (materialización del expediente -> detalle -> INICIAR_INVESTIGACION)
          [evento caso.abierto por RabbitMQ]
     -> vulneraciones / FIR (materialización -> vulneración -> antecedente -> CERRAR)
     -> productos (tarea-semilla async opcional; producto INFORME -> EMITIR; tarea -> COMPLETAR)
     -> casos (CERRAR)
     -> verificaciones negativas (401 / 404 / 409)

 La fuente de puertos y rutas es scripts/services.json (no se hardcodean).
 La contraseña sale de services/smid-auth/.env (SEED_PASSWORD) o de services.json.

 PRERREQUISITOS: los 8 servicios arriba + MySQL + RabbitMQ. Levántalos con:
   .\scripts\siger-services.ps1 start         (o el dashboard)
=====================================================================================
#>

[CmdletBinding()]
param(
    [switch] $Direct,                 # por defecto se usa el Gateway
    [string] $Email,
    [string] $Password,
    [int]    $TimeoutSec = 40,        # espera máx. para materializaciones asíncronas
    [int]    $PreflightTimeoutSec = 3,
    [switch] $NoPreflight,
    [switch] $RequireAsync,
    [string] $RunId,
    [string] $ReportPath
)

$ErrorActionPreference = 'Stop'
Set-StrictMode -Version Latest

# ----------------------------------------------------------------------------------
# Configuración desde services.json (fuente única) y .env
# ----------------------------------------------------------------------------------
$Root = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$catalog = Get-Content -Raw (Join-Path $PSScriptRoot 'services.json') | ConvertFrom-Json
$DefaultServicesRoot = 'services'

if ([string]::IsNullOrWhiteSpace($RunId)) { $RunId = Get-Date -Format 'yyyyMMdd-HHmmss' }
$RunRoot = Join-Path $Root '_runtime-logs\e2e'
$RunDir = Join-Path $RunRoot $RunId
New-Item -ItemType Directory -Force -Path $RunDir | Out-Null
if ([string]::IsNullOrWhiteSpace($ReportPath)) { $ReportPath = Join-Path $RunDir 'report.json' }
$TranscriptPath = Join-Path $RunDir 'transcript.log'
$script:TranscriptStarted = $false
try {
    Start-Transcript -Path $TranscriptPath -Force | Out-Null
    $script:TranscriptStarted = $true
} catch {
    Write-Warning "No se pudo iniciar transcript: $($_.Exception.Message)"
}

function Resolve-WorkspacePath([string] $Path, [string] $DefaultRelativePath) {
    $candidate = if ([string]::IsNullOrWhiteSpace($Path)) { $DefaultRelativePath } else { $Path }
    if ([System.IO.Path]::IsPathRooted($candidate)) { return $candidate }
    return Join-Path $Root $candidate
}

function Get-ServicesRoot {
    $configured = $null
    $pathsProp = $catalog.PSObject.Properties['paths']
    if ($null -ne $pathsProp -and $null -ne $pathsProp.Value) {
        $servicesRootProp = $pathsProp.Value.PSObject.Properties['servicesRoot']
        if ($null -ne $servicesRootProp) { $configured = [string] $servicesRootProp.Value }
    }
    return Resolve-WorkspacePath $configured $DefaultServicesRoot
}

$ServicesRoot = Get-ServicesRoot

function ServiceDir([string]$name) { Join-Path $ServicesRoot $name }

$UseGateway = -not $Direct
$gatewayPort = ($catalog.services | Where-Object name -eq 'smid-api-gateway').port
$GatewayBase = "http://localhost:$gatewayPort"

function Svc([string]$name) { $catalog.services | Where-Object name -eq $name | Select-Object -First 1 }

function Base([string]$name) {
    $s = Svc $name
    $route = $s.gatewayRoute                     # p.ej. /api/casos
    if ($UseGateway) { return "$GatewayBase$route" }
    $internal = $route -replace '^/api', ''      # p.ej. /casos
    return "http://localhost:$($s.port)$internal"
}

if (-not $Email) {
    $auth = $catalog.auth
    $Email = $auth.email
}
if (-not $Password) {
    # SEED_PASSWORD del .env de auth; si no, el default de services.json
    $authEnv = Join-Path (ServiceDir 'smid-auth') '.env'
    if (Test-Path -LiteralPath $authEnv) {
        $linea = Get-Content -LiteralPath $authEnv | Where-Object { $_ -match '^\s*(export\s+)?SEED_PASSWORD\s*=' } | Select-Object -First 1
        if ($linea) { $Password = ($linea -split '=', 2)[1].Trim().Trim('"').Trim("'") }
    }
    if (-not $Password) { $Password = $catalog.auth.defaultPassword }
}

# ----------------------------------------------------------------------------------
# Utilidades de salida y HTTP
# ----------------------------------------------------------------------------------
$script:Resultados = [System.Collections.Generic.List[object]]::new()
$script:Fallos = 0
$script:Avisos = 0
$script:StepStart = $null
$script:Ctx = @{}

function Seccion([string]$t) {
    Write-Host ''
    Write-Host "== $t ==" -ForegroundColor Cyan
    $script:StepStart = [System.Diagnostics.Stopwatch]::StartNew()
}
function Ok([string]$m)   { Write-Host "  + $m" -ForegroundColor Green }
function Info([string]$m) { Write-Host "  - $m" -ForegroundColor DarkGray }
function Warn([string]$m) { Write-Host "  ! $m" -ForegroundColor Yellow; $script:Avisos++ }
function Fail([string]$m) { Write-Host "  x $m" -ForegroundColor Red; $script:Fallos++ }

function Registrar([string]$paso, [string]$valor, [string]$estado = 'OK') {
    $duracionMs = if ($null -ne $script:StepStart) { [int] $script:StepStart.ElapsedMilliseconds } else { $null }
    $script:Resultados.Add([pscustomobject]@{
        Paso       = $paso
        Estado     = $estado
        Valor      = $valor
        DuracionMs = $duracionMs
    })
}

function Read-DotEnv([string] $Path) {
    $vars = @{}
    if (-not (Test-Path -LiteralPath $Path)) { return $vars }
    foreach ($line in Get-Content -LiteralPath $Path) {
        $trimmed = $line.Trim()
        if ($trimmed.Length -eq 0 -or $trimmed.StartsWith('#')) { continue }
        if ($trimmed.StartsWith('export ')) { $trimmed = $trimmed.Substring(7).Trim() }
        $equalsAt = $trimmed.IndexOf('=')
        if ($equalsAt -le 0) { continue }
        $key = $trimmed.Substring(0, $equalsAt).Trim()
        $value = $trimmed.Substring($equalsAt + 1).Trim()
        if ($value.Length -ge 2) {
            $first = $value.Substring(0, 1)
            $last = $value.Substring($value.Length - 1, 1)
            if (($first -eq '"' -and $last -eq '"') -or ($first -eq "'" -and $last -eq "'")) {
                $value = $value.Substring(1, $value.Length - 2)
            }
        }
        $vars[$key] = $value
    }
    return $vars
}

function Get-EnvActiveValue([hashtable] $Vars, [string[]] $Names) {
    foreach ($name in $Names) {
        if ($Vars.ContainsKey($name) -and -not [string]::IsNullOrWhiteSpace($Vars[$name])) {
            return $Vars[$name]
        }
    }
    return $null
}

function Test-LocalPortOpen([string] $HostName, [int] $Port, [int] $TimeoutMs = 750) {
    $client = [System.Net.Sockets.TcpClient]::new()
    try {
        $task = $client.ConnectAsync($HostName, $Port)
        if (-not $task.Wait($TimeoutMs)) { return $false }
        return $client.Connected
    } catch {
        return $false
    } finally {
        $client.Dispose()
    }
}

function Get-ServiceHealth([object] $Spec) {
    $url = "http://localhost:$($Spec.port)/actuator/health"
    try {
        $response = Invoke-RestMethod -Uri $url -TimeoutSec $PreflightTimeoutSec -ErrorAction Stop
        $status = if ($null -ne $response.status) { [string] $response.status } else { 'HTTP OK' }
        return [pscustomobject]@{ Name = $Spec.name; Url = $url; Ok = ($status -eq 'UP' -or $status -eq 'HTTP OK'); Status = $status }
    } catch {
        return [pscustomobject]@{ Name = $Spec.name; Url = $url; Ok = $false; Status = $_.Exception.Message }
    }
}

function Invoke-Preflight {
    Seccion 'Preflight'
    $bloqueantes = 0

    foreach ($infra in $catalog.infra) {
        $ok = Test-LocalPortOpen $infra.host ([int] $infra.port)
        if ($ok) { Ok "$($infra.name) puerto $($infra.port) abierto" }
        else { Fail "$($infra.name) puerto $($infra.port) cerrado"; $bloqueantes++ }
    }

    $requiredNames = @(
        'smid-auth',
        'catalogo-service',
        'personas-service',
        'requerimientos-service',
        'casos-service',
        'vulneraciones-service',
        'productos-service'
    )
    if ($UseGateway) { $requiredNames += 'smid-api-gateway' }

    foreach ($name in $requiredNames) {
        $health = Get-ServiceHealth (Svc $name)
        if ($health.Ok) { Ok "$name health=$($health.Status)" }
        else { Fail "$name no esta saludable: $($health.Status)"; $bloqueantes++ }
    }

    $secretByService = @{}
    foreach ($name in $requiredNames) {
        $envPath = Join-Path (ServiceDir $name) '.env'
        $vars = Read-DotEnv $envPath
        $secret = Get-EnvActiveValue $vars $catalog.jwt.secretVars
        if ($secret) { $secretByService[$name] = $secret }
    }
    $distinctSecrets = @($secretByService.Values | Sort-Object -Unique)
    if ($distinctSecrets.Count -eq 1) {
        Ok "JWT_SECRET coherente en $($secretByService.Count) servicio(s)"
    } elseif ($distinctSecrets.Count -gt 1) {
        Fail "JWT_SECRET no coincide entre: $(($secretByService.Keys | Sort-Object) -join ', ')"
        $bloqueantes++
    } else {
        Warn 'No se pudo verificar JWT_SECRET desde .env.'
    }

    Registrar 'Preflight' "bloqueantes=$bloqueantes; avisos=$script:Avisos" $(if ($bloqueantes -eq 0) { 'OK' } else { 'FAIL' })
    if ($bloqueantes -gt 0) {
        throw "Preflight falló con $bloqueantes bloqueante(s). Levanta servicios con .\scripts\siger-services.ps1 start y revisa .\scripts\siger-services.ps1 doctor."
    }
}

function Write-E2EReport([int] $ExitCode) {
    $ctxPublico = @{}
    foreach ($key in $script:Ctx.Keys) {
        if ($key -eq 'Token') { continue }
        $ctxPublico[$key] = $script:Ctx[$key]
    }
    $report = [pscustomobject]@{
        runId       = $RunId
        generatedAt = (Get-Date).ToString('o')
        exitCode    = $ExitCode
        access      = if ($UseGateway) { 'gateway' } else { 'direct' }
        gatewayBase = $GatewayBase
        timeoutSec  = $TimeoutSec
        requireAsync = [bool] $RequireAsync
        failures    = $script:Fallos
        warnings    = $script:Avisos
        context     = $ctxPublico
        results     = @($script:Resultados)
        report      = $ReportPath
        transcript  = $TranscriptPath
    }
    $report | ConvertTo-Json -Depth 8 | Set-Content -LiteralPath $ReportPath -Encoding UTF8
    Write-Host ''
    Write-Host "Reporte: $ReportPath" -ForegroundColor DarkGray
    Write-Host "Transcript: $TranscriptPath" -ForegroundColor DarkGray
}

trap {
    Write-Host ''
    Write-Host "E2E abortado: $($_.Exception.Message)" -ForegroundColor Red
    if ($script:Fallos -eq 0) { $script:Fallos++ }
    Registrar 'Abortado' $_.Exception.Message 'FAIL'
    Write-E2EReport 1
    if ($script:TranscriptStarted) { Stop-Transcript | Out-Null }
    exit 1
}

function Invoke-Json {
    param([Parameter(Mandatory)][string]$Method, [Parameter(Mandatory)][string]$Uri, [object]$Body, [string]$Token, [switch]$Quiet)
    $headers = @{ Accept = 'application/json' }
    if ($Token) { $headers['Authorization'] = "Bearer $Token" }
    try {
        if ($null -ne $Body) {
            $json = $Body | ConvertTo-Json -Depth 12
            return Invoke-RestMethod -Method $Method -Uri $Uri -Headers $headers -ContentType 'application/json' -Body $json
        }
        return Invoke-RestMethod -Method $Method -Uri $Uri -Headers $headers
    } catch {
        if (-not $Quiet) {
            Write-Host "  x $Method $Uri" -ForegroundColor Red
            if ($_.ErrorDetails.Message) { Write-Host "    $($_.ErrorDetails.Message)" -ForegroundColor DarkYellow }
        }
        throw
    }
}

function Get-Status {
    param([Parameter(Mandatory)][string]$Method, [Parameter(Mandatory)][string]$Uri, [object]$Body, [string]$Token)
    $headers = @{ Accept = 'application/json' }
    if ($Token) { $headers['Authorization'] = "Bearer $Token" }
    $p = @{ Method = $Method; Uri = $Uri; Headers = $headers; SkipHttpErrorCheck = $true }
    if ($null -ne $Body) { $p['ContentType'] = 'application/json'; $p['Body'] = ($Body | ConvertTo-Json -Depth 12) }
    return [int](Invoke-WebRequest @p).StatusCode
}

function Esperar-Condicion {
    param([Parameter(Mandatory)][scriptblock]$Accion, [int]$TimeoutSeg = $TimeoutSec, [int]$IntervaloMs = 1500)
    $limite = (Get-Date).AddSeconds($TimeoutSeg)
    while ((Get-Date) -lt $limite) {
        $r = & $Accion
        if ($r) { return $r }
        Start-Sleep -Milliseconds $IntervaloMs
    }
    return $null
}

# ----------------------------------------------------------------------------------
Write-Host ''
Write-Host '############################################################' -ForegroundColor Magenta
Write-Host '#   SMID — E2E del ecosistema (auth -> ... -> productos)   #' -ForegroundColor Magenta
Write-Host '############################################################' -ForegroundColor Magenta
Info ("Acceso: " + ($(if ($UseGateway) { "Gateway ($GatewayBase)" } else { 'Directo a cada servicio' })))
Info "Usuario: $Email"
Info "RunId: $RunId"
Info "Reporte: $ReportPath"

$ctx = $script:Ctx

if (-not $NoPreflight) { Invoke-Preflight }
else { Warn 'Preflight omitido por -NoPreflight.'; Registrar 'Preflight' 'omitido' 'WARN' }

# ----------------------------------------------------------------------------------
# 0) LOGIN (auth 6.1)
# ----------------------------------------------------------------------------------
Seccion '0) Login en auth'
$login = Invoke-Json -Method Post -Uri "$(Base 'smid-auth')/login" -Body @{ email = $Email; password = $Password }
$ctx.Token      = $login.accessToken
$ctx.UsuarioAlt = $login.usuario.altKey
$ctx.SedeAlt    = $login.usuario.sede.altKey
$ctx.UnidadAlt  = $login.usuario.unidad.altKey
if (-not $ctx.Token) { throw 'No se recibió accessToken.' }
Ok "Autenticado: $($login.usuario.nombres) $($login.usuario.apellidos) | alcance=$($login.usuario.alcance) | roles=[$($login.usuario.roles -join ', ')]"
Info "sede.altKey=$($ctx.SedeAlt)  unidad.altKey=$($ctx.UnidadAlt)"
Registrar 'Login' "$($login.usuario.nombres) $($login.usuario.apellidos) [$($login.usuario.alcance)]"

# ----------------------------------------------------------------------------------
# 1) CATÁLOGO (6.7): derecho + causa
# ----------------------------------------------------------------------------------
Seccion '1) Catálogo: derecho y causa'
$derechos = Invoke-Json -Method Get -Uri "$(Base 'catalogo-service')/derechos?formato=plano" -Token $ctx.Token
if (-not $derechos -or @($derechos).Count -eq 0) { throw 'El catálogo no devolvió derechos (¿semilla V2?).' }
$ctx.DerechoAlt = $null; $ctx.CausaAlt = $null
foreach ($d in $derechos) {
    $causas = Invoke-Json -Method Get -Uri "$(Base 'catalogo-service')/derechos/$($d.altKey)/causas" -Token $ctx.Token
    if ($causas -and @($causas).Count -gt 0) { $ctx.DerechoAlt = $d.altKey; $ctx.CausaAlt = $causas[0].altKey; Ok "Derecho '$($d.codigo)' con causa '$($causas[0].codigo)'"; break }
}
if (-not $ctx.DerechoAlt) { $ctx.DerechoAlt = $derechos[0].altKey; Ok "Derecho '$($derechos[0].codigo)' (sin causa)" }
Registrar 'Catalogo' "derecho=$($ctx.DerechoAlt); causa=$($ctx.CausaAlt)"

# ----------------------------------------------------------------------------------
# 2) PERSONAS (6.2): requirente + NNA
# ----------------------------------------------------------------------------------
Seccion '2) Personas: requirente (ADULTO) y NNA'
$suf = Get-Random -Minimum 1000 -Maximum 9999
$req = Invoke-Json -Method Post -Uri "$(Base 'personas-service')" -Token $ctx.Token -Body @{
    tipo = 'ADULTO'; nombres = "Requirente $suf"; apellidoPaterno = 'Pérez'
    contactos = @(@{ tipo = 'TELEFONO'; valor = '+56 9 1234 5678' })
}
$ctx.RequirenteAlt = $req.altKey; Ok "Requirente: $($ctx.RequirenteAlt)"
$nna = Invoke-Json -Method Post -Uri "$(Base 'personas-service')" -Token $ctx.Token -Body @{
    tipo = 'NNA'; nombres = "Camila $suf"; apellidoPaterno = 'Reyes'; fechaNacimiento = '2015-03-10'
}
$ctx.NnaAlt = $nna.altKey; Ok "NNA: $($ctx.NnaAlt)"
Registrar 'Personas' "requirente=$($ctx.RequirenteAlt); nna=$($ctx.NnaAlt)"

# ----------------------------------------------------------------------------------
# 3) REQUERIMIENTOS (6.3)
# ----------------------------------------------------------------------------------
Seccion '3) Requerimiento: crear -> NNA -> enviar -> asignar'
$r = Invoke-Json -Method Post -Uri "$(Base 'requerimientos-service')" -Token $ctx.Token -Body @{
    canal = 'WEB'; complejidad = 'MEDIANA'; urgencia = 'AMARILLO'
    idUnidadDestinoAlt = $ctx.UnidadAlt; resumen = 'Caso E2E del ecosistema completo'
    idRequirenteAlt = $ctx.RequirenteAlt; esBeta = $null
}
$ctx.ReqAlt = $r.altKey
$ctx.RequerimientoFolio = $r.folio
Ok "Requerimiento $($r.folio) (estado=$($r.estado)) | requiereFichaReservada=$($r.requiereFichaReservada)"
Info "requerimiento.altKey=$($ctx.ReqAlt)"
Invoke-Json -Method Post -Uri "$(Base 'requerimientos-service')/$($ctx.ReqAlt)/nna" -Token $ctx.Token -Body @{
    idPersonaAlt = $ctx.NnaAlt; derechos = @(@{ idDerechoAlt = $ctx.DerechoAlt; idCausaAlt = $ctx.CausaAlt })
} | Out-Null
Ok 'NNA + derecho asociados'
$ri = Invoke-Json -Method Post -Uri "$(Base 'requerimientos-service')/$($ctx.ReqAlt)/enviar" -Token $ctx.Token
Ok "Enviado -> $($ri.estado)"
$ra = Invoke-Json -Method Post -Uri "$(Base 'requerimientos-service')/$($ctx.ReqAlt)/admisibilidad" -Token $ctx.Token -Body @{
    accion = 'ASIGNACION'; idProfesionalAsignadoAlt = $ctx.UsuarioAlt; observacion = 'Deriva para investigación (E2E)'
}
Ok "Admisibilidad -> $($ra.estado) (emite requerimiento.asignado)"
Registrar 'Requerimiento' "$($r.folio) [$($ra.estado)]"

# ----------------------------------------------------------------------------------
# 4) CASOS (6.4): materialización asíncrona
# ----------------------------------------------------------------------------------
Seccion '4) Casos: esperar materialización (RabbitMQ)'
$ctx.CasoAlt = Esperar-Condicion {
    $pg = Invoke-Json -Method Get -Uri "$(Base 'casos-service')?pagina=0&tamano=100" -Token $ctx.Token
    foreach ($it in $pg.contenido) {
        $det = Invoke-Json -Method Get -Uri "$(Base 'casos-service')/$($it.altKey)" -Token $ctx.Token
        if ($det.idRequerimientoOrigen -eq $ctx.ReqAlt) { return $det.altKey }
    }
    $null
}
if (-not $ctx.CasoAlt) { throw "El caso no se materializó en $TimeoutSec s. Revisa requerimientos EVENTOS_TRANSPORTE=rabbitmq y casos EVENTOS_CONSUMO=rabbitmq sobre el mismo broker/exchange." }
$caso = Invoke-Json -Method Get -Uri "$(Base 'casos-service')/$($ctx.CasoAlt)" -Token $ctx.Token
$ctx.CasoNumero = $caso.numeroExpediente
Ok "Expediente $($caso.numeroExpediente) (estado=$($caso.estado))"
Info "caso.altKey=$($ctx.CasoAlt)"
Info "historial: $((($caso.historial | ForEach-Object { $_.accion }) -join ' -> '))"
$t1 = Invoke-Json -Method Post -Uri "$(Base 'casos-service')/$($ctx.CasoAlt)/transiciones" -Token $ctx.Token -Body @{ accion = 'INICIAR_INVESTIGACION'; observacion = 'Inicio (E2E)' }
Ok "Transición -> $($t1.estado)"
Registrar 'Caso' "$($caso.numeroExpediente) [$($t1.estado)]"

# ----------------------------------------------------------------------------------
# 5) VULNERACIONES / FIR (6.5): materialización asíncrona (si requiereFichaReservada)
# ----------------------------------------------------------------------------------
Seccion '5) Vulneraciones / FIR'
if (-not $r.requiereFichaReservada) {
    Warn 'El requerimiento no exige ficha reservada; se omite la FIR (no es un fallo).'
    Registrar 'FIR' 'omitida (no requiere)' 'WARN'
} else {
    $ficha = Esperar-Condicion {
        $pg = Invoke-Json -Method Get -Uri "$(Base 'vulneraciones-service')/fichas?pagina=0&tamano=100" -Token $ctx.Token
        ($pg.contenido | Where-Object { $_.idCasoAlt -eq $ctx.CasoAlt } | Select-Object -First 1)
    }
    if (-not $ficha) {
        $msg = "La FIR no se materializó en $TimeoutSec s (¿vulneraciones EVENTOS_CONSUMO=rabbitmq?)."
        if ($RequireAsync) {
            Fail $msg
            Registrar 'FIR' 'no materializada' 'FAIL'
        } else {
            Warn "$msg Sigo sin marcar fallo duro."
            Registrar 'FIR' 'no materializada' 'WARN'
        }
    } else {
        $ctx.FichaAlt = $ficha.altKey
        $ctx.FichaNumero = $ficha.numeroFicha
        Ok "FIR $($ficha.numeroFicha) (estado=$($ficha.estado))"
        Info "ficha.altKey=$($ctx.FichaAlt)"
        Invoke-Json -Method Post -Uri "$(Base 'vulneraciones-service')/fichas/$($ctx.FichaAlt)/vulneraciones" -Token $ctx.Token -Body @{
            idDerechoAlt = $ctx.DerechoAlt; idCausaAlt = $ctx.CausaAlt; idNnaAlt = $ctx.NnaAlt
            gravedad = 'GRAVE'; relato = 'Relato reservado de prueba E2E'; fechaHecho = '2027-04-20'
        } | Out-Null
        Ok 'Vulneración registrada'
        Invoke-Json -Method Post -Uri "$(Base 'vulneraciones-service')/fichas/$($ctx.FichaAlt)/antecedentes" -Token $ctx.Token -Body @{
            tipo = 'ESCOLAR'; descripcion = 'Antecedente reservado E2E'; fecha = '2027-03-15'; fuente = 'Establecimiento educacional'
        } | Out-Null
        Ok 'Antecedente registrado'
        $fc = Invoke-Json -Method Post -Uri "$(Base 'vulneraciones-service')/fichas/$($ctx.FichaAlt)/transiciones" -Token $ctx.Token -Body @{ accion = 'CERRAR'; observacion = 'Cierre FIR (E2E)' }
        Ok "FIR -> $($fc.estado)"
        Registrar 'FIR' "$($ficha.numeroFicha) [$($fc.estado)]"
    }
}

# ----------------------------------------------------------------------------------
# 6) PRODUCTOS (6.6)
# ----------------------------------------------------------------------------------
Seccion '6) Productos: tarea-semilla (async) + producto + tarea'
$semilla = Esperar-Condicion -TimeoutSeg ([Math]::Min($TimeoutSec, 20)) -Accion {
    $pg = Invoke-Json -Method Get -Uri "$(Base 'productos-service')/tareas?idCaso=$($ctx.CasoAlt)&pagina=0&tamano=50" -Token $ctx.Token
    if ($pg.contenido -and @($pg.contenido).Count -gt 0) { return $pg.contenido[0] }
    $null
}
if ($semilla) { Ok "Tarea-semilla detectada: '$($semilla.titulo)' (de caso.abierto)" } else { Warn 'Sin tarea-semilla async (opcional).' }
if ($semilla) {
    $ctx.TareaSemillaAlt = $semilla.altKey
    Info "tareaSemilla.altKey=$($ctx.TareaSemillaAlt)"
    Registrar 'Tarea semilla async' "$($semilla.titulo)"
} elseif ($RequireAsync) {
    Fail 'No se detectó tarea-semilla async de productos.'
    Registrar 'Tarea semilla async' 'no detectada' 'FAIL'
} else {
    Registrar 'Tarea semilla async' 'no detectada (opcional)' 'WARN'
}

$prod = Invoke-Json -Method Post -Uri "$(Base 'productos-service')/productos" -Token $ctx.Token -Body @{
    idCaso = $ctx.CasoAlt; tipo = 'INFORME'; titulo = "Informe E2E $suf"; descripcion = 'Generado por el E2E'
}
$ctx.ProductoAlt = $prod.altKey; Ok "Producto creado (estado=$($prod.estado))"
Info "producto.altKey=$($ctx.ProductoAlt)"
$pr = Invoke-Json -Method Post -Uri "$(Base 'productos-service')/productos/$($ctx.ProductoAlt)/transiciones" -Token $ctx.Token -Body @{ accion = 'ENVIAR_REVISION'; observacion = 'A revisión (E2E)' }
Ok "Producto -> $($pr.estado)"
try {
    $pe = Invoke-Json -Method Post -Uri "$(Base 'productos-service')/productos/$($ctx.ProductoAlt)/transiciones" -Token $ctx.Token -Body @{ accion = 'EMITIR'; observacion = 'Visado (E2E)' } -Quiet
    $ctx.ProductoNumero = $pe.numeroProducto
    Ok "Producto -> $($pe.estado) | numeroProducto=$($pe.numeroProducto)"
    Registrar 'Producto' "$($pe.numeroProducto) [$($pe.estado)]"
} catch {
    Warn 'EMITIR no autorizado para este usuario (revisa ROLES_COORDINACION de productos). Producto queda EN_REVISION.'
    Registrar 'Producto' 'EN_REVISION (EMITIR requiere Coordinacion)' 'WARN'
}

$tarea = Invoke-Json -Method Post -Uri "$(Base 'productos-service')/tareas" -Token $ctx.Token -Body @{
    idCaso = $ctx.CasoAlt; titulo = "Solicitar informe escuela $suf"; descripcion = 'Tarea E2E'
    responsableAlt = $ctx.UsuarioAlt; prioridad = 'ALTA'
}
$ctx.TareaAlt = $tarea.altKey; Ok "Tarea creada (estado=$($tarea.estado))"
Info "tarea.altKey=$($ctx.TareaAlt)"
$tt = Invoke-Json -Method Post -Uri "$(Base 'productos-service')/tareas/$($ctx.TareaAlt)/transiciones" -Token $ctx.Token -Body @{ accion = 'TOMAR'; observacion = 'Tomo la tarea (E2E)' }
Ok "Tarea -> $($tt.estado)"
$tc = Invoke-Json -Method Post -Uri "$(Base 'productos-service')/tareas/$($ctx.TareaAlt)/transiciones" -Token $ctx.Token -Body @{ accion = 'COMPLETAR'; observacion = 'Completada (E2E)' }
Ok "Tarea -> $($tc.estado)"
Registrar 'Tarea' "[$($tc.estado)]"

# ----------------------------------------------------------------------------------
# 7) CASOS: cierre
# ----------------------------------------------------------------------------------
Seccion '7) Casos: CERRAR'
$tcaso = Invoke-Json -Method Post -Uri "$(Base 'casos-service')/$($ctx.CasoAlt)/transiciones" -Token $ctx.Token -Body @{ accion = 'CERRAR'; observacion = 'Cierre del caso (E2E)' }
Ok "Caso -> $($tcaso.estado) | cerradoEn=$($tcaso.cerradoEn)"
Registrar 'Cierre caso' "$($tcaso.estado)"

# ----------------------------------------------------------------------------------
# 8) VERIFICACIONES NEGATIVAS
# ----------------------------------------------------------------------------------
Seccion '8) Verificaciones negativas'
$c401 = Get-Status -Method Get -Uri "$(Base 'casos-service')/$($ctx.CasoAlt)"
if ($c401 -eq 401) { Ok 'Sin token -> 401'; Registrar 'Negativa 401' 'OK' } else { Fail "Sin token: esperaba 401, obtuve $c401"; Registrar 'Negativa 401' "obtuve $c401" 'FAIL' }
$c404 = Get-Status -Method Get -Uri "$(Base 'casos-service')/00000000-0000-0000-0000-000000000000" -Token $ctx.Token
if ($c404 -eq 404) { Ok 'altKey inexistente -> 404'; Registrar 'Negativa 404' 'OK' } else { Fail "Inexistente: esperaba 404, obtuve $c404"; Registrar 'Negativa 404' "obtuve $c404" 'FAIL' }
$c409 = Get-Status -Method Post -Uri "$(Base 'casos-service')/$($ctx.CasoAlt)/transiciones" -Token $ctx.Token -Body @{ accion = 'DERIVAR_A_SEGUIMIENTO'; observacion = 'Transición inválida desde CERRADO' }
if ($c409 -eq 409) { Ok 'Transición inválida -> 409'; Registrar 'Negativa 409' 'OK' } else { Fail "Transición inválida: esperaba 409, obtuve $c409"; Registrar 'Negativa 409' "obtuve $c409" 'FAIL' }

# ----------------------------------------------------------------------------------
# RESUMEN
# ----------------------------------------------------------------------------------
Seccion 'RESUMEN'
$script:Resultados |
    Select-Object Paso, Estado, Valor, @{ Name = 'ms'; Expression = { $_.DuracionMs } } |
    Format-Table -AutoSize
if ($script:Fallos -eq 0) {
    $mensaje = if ($script:Avisos -gt 0) { "E2E completado sin fallos ($script:Avisos aviso(s))." } else { 'E2E completado sin fallos.' }
    Write-Host $mensaje -ForegroundColor Green
    Write-E2EReport 0
    if ($script:TranscriptStarted) { Stop-Transcript | Out-Null }
    exit 0
} else {
    Write-Host "E2E completado con $($script:Fallos) verificación(es) fallida(s) y $($script:Avisos) aviso(s)." -ForegroundColor Yellow
    Write-E2EReport 1
    if ($script:TranscriptStarted) { Stop-Transcript | Out-Null }
    exit 1
}
