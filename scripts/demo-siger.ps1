#requires -Version 7.0
<#
 Demo guiada de SIGER para audiencia no tecnica.
 Ejecuta el E2E fuerte y muestra un resumen funcional, sin obligar a leer logs tecnicos.
#>

[CmdletBinding()]
param(
    [int] $TimeoutSec = 60,
    [switch] $AbrirReporte
)

$ErrorActionPreference = 'Stop'
Set-StrictMode -Version Latest

$Root = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$RunId = Get-Date -Format 'yyyyMMdd-HHmmss'
$RunDir = Join-Path $Root "_runtime-logs\e2e\$RunId"
$ReportPath = Join-Path $RunDir 'report.json'
$SalidaTecnica = Join-Path $RunDir 'salida-tecnica.log'
$ErroresTecnicos = Join-Path $RunDir 'errores-tecnicos.log'
$E2E = Join-Path $PSScriptRoot 'e2e.ps1'

function Linea([string] $Texto = '') { Write-Host $Texto }
function Titulo([string] $Texto) {
    Linea ''
    Write-Host $Texto -ForegroundColor Cyan
}
function Bien([string] $Texto) { Write-Host "  OK  $Texto" -ForegroundColor Green }
function Aviso([string] $Texto) { Write-Host "  AV  $Texto" -ForegroundColor Yellow }
function Mal([string] $Texto) { Write-Host "  NO  $Texto" -ForegroundColor Red }

function ValorPaso([object] $Report, [string] $Paso) {
    $item = @($Report.results | Where-Object { $_.Paso -eq $Paso } | Select-Object -First 1)
    if ($item.Count -eq 0) { return $null }
    return $item[0]
}

function ValorContexto([object] $Report, [string] $Nombre) {
    if ($null -eq $Report.context) { return $null }
    $prop = $Report.context.PSObject.Properties[$Nombre]
    if ($null -eq $prop) { return $null }
    return $prop.Value
}

function Mostrar-Backends {
    $catalogPath = Join-Path $Root 'scripts\services.json'
    $statePath = Join-Path $Root '_runtime-logs\siger-services.json'
    if (-not (Test-Path -LiteralPath $catalogPath)) { return }

    $catalog = Get-Content -Raw -LiteralPath $catalogPath | ConvertFrom-Json
    $state = @()
    if (Test-Path -LiteralPath $statePath) {
        try { $state = @(Get-Content -Raw -LiteralPath $statePath | ConvertFrom-Json) } catch { $state = @() }
    }

    $required = @(
        'smid-api-gateway',
        'smid-auth',
        'catalogo-service',
        'personas-service',
        'requerimientos-service',
        'casos-service',
        'vulneraciones-service',
        'productos-service'
    )

    Titulo 'Backend en ejecucion'
    Write-Host ('  {0,-24} {1,6} {2,8} {3,-8}' -f 'Servicio', 'Puerto', 'PID', 'Health') -ForegroundColor DarkGray
    foreach ($name in $required) {
        $svc = @($catalog.services | Where-Object { $_.name -eq $name } | Select-Object -First 1)
        if ($svc.Count -eq 0) { continue }
        $servicePid = ''
        $row = @($state | Where-Object { $_.service -eq $name } | Select-Object -First 1)
        if ($row.Count -gt 0) { $servicePid = [string] $row[0].pid }
        $health = 'N/D'
        try {
            $h = Invoke-RestMethod -Uri "http://localhost:$($svc[0].port)/actuator/health" -TimeoutSec 2
            if ($h.status) { $health = [string] $h.status }
        } catch {
            $health = 'DOWN'
        }
        $color = if ($health -eq 'UP') { 'Green' } else { 'Yellow' }
        Write-Host ('  {0,-24} {1,6} {2,8} {3,-8}' -f $name, $svc[0].port, $servicePid, $health) -ForegroundColor $color
    }
}

function Mostrar-Resumen([object] $Report) {
    $login = ValorPaso $Report 'Login'
    $req = ValorPaso $Report 'Requerimiento'
    $caso = ValorPaso $Report 'Caso'
    $fir = ValorPaso $Report 'FIR'
    $semilla = ValorPaso $Report 'Tarea semilla async'
    $producto = ValorPaso $Report 'Producto'
    $tarea = ValorPaso $Report 'Tarea'
    $cierre = ValorPaso $Report 'Cierre caso'

    Titulo 'Resultado de la demo'
    Bien 'La plataforma completo el flujo principal de atencion.'
    if ($login) { Bien "Ingreso con usuario semilla: $($login.Valor)" }
    if ($req) { Bien "Se ingreso y asigno un requerimiento: $($req.Valor)" }
    if ($caso) { Bien "El sistema abrio automaticamente un caso: $($caso.Valor)" }
    if ($fir) { Bien "Se genero y cerro la ficha reservada: $($fir.Valor)" }
    if ($semilla -and $semilla.Estado -eq 'OK') {
        Bien "Productos recibio el evento del caso y creo la tarea: $($semilla.Valor)"
    }
    if ($producto) { Bien "Se creo y emitio un producto: $($producto.Valor)" }
    if ($tarea) { Bien "Se creo y completo una tarea manual: $($tarea.Valor)" }
    if ($cierre) { Bien "El caso termino en estado: $($cierre.Valor)" }

    Titulo 'Identificadores tecnicos finales'
    $ids = @(
        [pscustomobject]@{ Nombre = 'Sede'; Valor = (ValorContexto $Report 'SedeAlt') },
        [pscustomobject]@{ Nombre = 'Unidad'; Valor = (ValorContexto $Report 'UnidadAlt') },
        [pscustomobject]@{ Nombre = 'Usuario'; Valor = (ValorContexto $Report 'UsuarioAlt') },
        [pscustomobject]@{ Nombre = 'Requirente'; Valor = (ValorContexto $Report 'RequirenteAlt') },
        [pscustomobject]@{ Nombre = 'NNA'; Valor = (ValorContexto $Report 'NnaAlt') },
        [pscustomobject]@{ Nombre = 'Requerimiento'; Valor = (ValorContexto $Report 'ReqAlt') },
        [pscustomobject]@{ Nombre = 'Caso'; Valor = (ValorContexto $Report 'CasoAlt') },
        [pscustomobject]@{ Nombre = 'Ficha reservada'; Valor = (ValorContexto $Report 'FichaAlt') },
        [pscustomobject]@{ Nombre = 'Tarea semilla'; Valor = (ValorContexto $Report 'TareaSemillaAlt') },
        [pscustomobject]@{ Nombre = 'Producto'; Valor = (ValorContexto $Report 'ProductoAlt') },
        [pscustomobject]@{ Nombre = 'Tarea manual'; Valor = (ValorContexto $Report 'TareaAlt') }
    )
    foreach ($id in $ids) {
        if (-not [string]::IsNullOrWhiteSpace([string] $id.Valor)) {
            Linea ("  {0,-16} {1}" -f $id.Nombre, $id.Valor)
        }
    }

    Titulo 'Que demuestra esto'
    Linea '  1. Un usuario real del sistema puede iniciar sesion.'
    Linea '  2. Se registra una persona adulta y un NNA.'
    Linea '  3. Se crea un requerimiento y se asigna.'
    Linea '  4. El caso nace por evento, no por carga manual.'
    Linea '  5. Vulneraciones y productos reaccionan al caso abierto.'
    Linea '  6. El sistema valida errores esperados: sin token, inexistente y transicion invalida.'

    Titulo 'Evidencia'
    Linea "  Reporte:    $($Report.report)"
    Linea "  Bitacora:   $($Report.transcript)"
    Linea "  Salida raw: $SalidaTecnica"
}

function Convertir-LineaE2E([string] $Linea) {
    if ([string]::IsNullOrWhiteSpace($Linea)) { return $null }

    if ($Linea -match '^== Preflight ==') { return 'Revisando base de datos, mensajeria y servicios.' }
    if ($Linea -match 'JWT_SECRET coherente') { return 'Chequeos previos aprobados.' }

    if ($Linea -match '^== 0\)') { return 'Iniciando sesion con usuario de prueba.' }
    if ($Linea -match 'Autenticado:') { return 'Usuario autenticado correctamente.' }
    if ($Linea -match 'sede\.altKey=([0-9a-fA-F-]+)\s+unidad\.altKey=([0-9a-fA-F-]+)') {
        return "Alcance territorial: sede UUID $($Matches[1]); unidad UUID $($Matches[2])."
    }

    if ($Linea -match '^== 1\)') { return 'Consultando catalogo de derechos.' }
    if ($Linea -match "Derecho '([^']+)'") { return "Derecho seleccionado: $($Matches[1])." }

    if ($Linea -match '^== 2\)') { return 'Registrando personas de prueba.' }
    if ($Linea -match 'Requirente:\s+([0-9a-fA-F-]+)') { return "Persona adulta registrada. UUID $($Matches[1])." }
    if ($Linea -match 'NNA:\s+([0-9a-fA-F-]+)') { return "NNA registrado. UUID $($Matches[1])." }

    if ($Linea -match '^== 3\)') { return 'Ingresando y asignando el requerimiento.' }
    if ($Linea -match 'Requerimiento\s+([^\s]+)\s+\(estado=') { return "Requerimiento creado: $($Matches[1])." }
    if ($Linea -match 'requerimiento\.altKey=([0-9a-fA-F-]+)') { return "UUID del requerimiento: $($Matches[1])." }
    if ($Linea -match 'Admisibilidad -> ASIGNADO') { return 'Requerimiento asignado; el sistema emite evento para abrir caso.' }

    if ($Linea -match '^== 4\)') { return 'Esperando que el expediente nazca automaticamente.' }
    if ($Linea -match 'Expediente\s+([^\s]+)\s+\(estado=') { return "Caso abierto automaticamente: $($Matches[1])." }
    if ($Linea -match 'caso\.altKey=([0-9a-fA-F-]+)') { return "UUID del caso: $($Matches[1])." }
    if ($Linea -match 'Transici.n -> EN_INVESTIGACION') { return 'Caso paso a investigacion.' }

    if ($Linea -match '^== 5\)') { return 'Generando ficha reservada asociada al caso.' }
    if ($Linea -match 'FIR\s+([^\s]+)\s+\(estado=') { return "Ficha reservada creada: $($Matches[1])." }
    if ($Linea -match 'ficha\.altKey=([0-9a-fA-F-]+)') { return "UUID de la ficha reservada: $($Matches[1])." }
    if ($Linea -match 'FIR -> CERRADA') { return 'Ficha reservada cerrada.' }

    if ($Linea -match '^== 6\)') { return 'Revisando productos y tareas.' }
    if ($Linea -match "Tarea-semilla detectada: '([^']+)'") { return "Tarea automatica creada por evento: $($Matches[1])." }
    if ($Linea -match 'tareaSemilla\.altKey=([0-9a-fA-F-]+)') { return "UUID de tarea semilla: $($Matches[1])." }
    if ($Linea -match 'Producto creado') { return 'Producto creado.' }
    if ($Linea -match 'producto\.altKey=([0-9a-fA-F-]+)') { return "UUID del producto: $($Matches[1])." }
    if ($Linea -match 'Producto -> EMITIDO \| numeroProducto=([^\s]+)') { return "Producto emitido: $($Matches[1])." }
    if ($Linea -match 'Tarea creada') { return 'Tarea manual creada.' }
    if ($Linea -match 'tarea\.altKey=([0-9a-fA-F-]+)') { return "UUID de tarea manual: $($Matches[1])." }
    if ($Linea -match 'Tarea -> COMPLETADA') { return 'Tarea manual completada.' }

    if ($Linea -match '^== 7\)') { return 'Cerrando el caso.' }
    if ($Linea -match 'Caso -> CERRADO') { return 'Caso cerrado correctamente.' }

    if ($Linea -match '^== 8\)') { return 'Validando controles de seguridad y errores esperados.' }
    if ($Linea -match 'Sin token -> 401') { return 'Control validado: sin sesion, el sistema rechaza la solicitud.' }
    if ($Linea -match 'altKey inexistente -> 404') { return 'Control validado: recurso inexistente responde correctamente.' }
    if ($Linea -match 'Transici.n inv.lida -> 409') { return 'Control validado: transicion invalida bloqueada.' }

    if ($Linea -match 'E2E completado sin fallos') { return 'Prueba tecnica finalizada sin fallos.' }
    return $null
}

function Mostrar-AvanceDesdeLog([string] $Path, [ref] $LineasLeidas, [System.Collections.Generic.HashSet[string]] $MensajesVistos) {
    if (-not (Test-Path -LiteralPath $Path)) { return }
    $lineas = @(Get-Content -LiteralPath $Path -ErrorAction SilentlyContinue)
    for ($i = $LineasLeidas.Value; $i -lt $lineas.Count; $i++) {
        $mensaje = Convertir-LineaE2E $lineas[$i]
        if ($mensaje -and $MensajesVistos.Add($mensaje)) {
            Write-Host "  -> $mensaje" -ForegroundColor Gray
        }
    }
    $LineasLeidas.Value = $lineas.Count
}

try { Clear-Host } catch { }
Write-Host '============================================================' -ForegroundColor Magenta
Write-Host '  DEMO SIGER - Flujo completo de atencion' -ForegroundColor Magenta
Write-Host '============================================================' -ForegroundColor Magenta
Linea ''
Linea 'Esta demo ejecuta una prueba automatica del sistema completo:'
Linea 'login, personas, requerimiento, caso, ficha reservada, productos y tareas.'
Linea ''
Aviso 'No cierres esta ventana mientras la demo esta corriendo.'
Linea ''

Mostrar-Backends

New-Item -ItemType Directory -Force -Path $RunDir | Out-Null
$pwsh = (Get-Command pwsh -ErrorAction SilentlyContinue)
if ($null -eq $pwsh) { $pwsh = Get-Command powershell -ErrorAction Stop }

Titulo 'Ejecutando prueba automatica'
Linea 'Esto puede tardar unos segundos. Si algo falla, se mostrara una explicacion simple.'
Linea ''

$args = @(
    '-NoProfile',
    '-ExecutionPolicy', 'Bypass',
    '-File', $E2E,
    '-TimeoutSec', [string] $TimeoutSec,
    '-RequireAsync',
    '-RunId', $RunId
)

$process = Start-Process `
    -FilePath $pwsh.Source `
    -ArgumentList $args `
    -WorkingDirectory $Root `
    -PassThru `
    -RedirectStandardOutput $SalidaTecnica `
    -RedirectStandardError $ErroresTecnicos `
    -WindowStyle Hidden

$lineasLeidas = [ref] 0
$mensajesVistos = [System.Collections.Generic.HashSet[string]]::new()
while (-not $process.HasExited) {
    Mostrar-AvanceDesdeLog $SalidaTecnica $lineasLeidas $mensajesVistos
    Start-Sleep -Milliseconds 800
}
$process.WaitForExit()
Mostrar-AvanceDesdeLog $SalidaTecnica $lineasLeidas $mensajesVistos

if (-not (Test-Path -LiteralPath $ReportPath)) {
    Mal 'La demo no genero reporte.'
    Linea "Revisa la salida tecnica: $SalidaTecnica"
    if (Test-Path -LiteralPath $ErroresTecnicos) { Linea "Errores: $ErroresTecnicos" }
    exit 1
}

$report = Get-Content -Raw -LiteralPath $ReportPath | ConvertFrom-Json
if ($process.ExitCode -eq 0 -and [int] $report.failures -eq 0) {
    Mostrar-Resumen $report
    if ([int] $report.warnings -gt 0) {
        Aviso "La prueba termino con $($report.warnings) aviso(s), pero sin fallos."
    }
    if ($AbrirReporte) { Start-Process -FilePath $ReportPath | Out-Null }
    Linea ''
    Write-Host 'DEMO COMPLETADA CORRECTAMENTE' -ForegroundColor Green
    exit 0
}

Titulo 'La demo no se pudo completar'
Mal "Fallos detectados: $($report.failures)"
if ([int] $report.warnings -gt 0) { Aviso "Avisos: $($report.warnings)" }

$fallidos = @($report.results | Where-Object { $_.Estado -eq 'FAIL' })
if ($fallidos.Count -gt 0) {
    Linea ''
    Linea 'Pasos con problema:'
    foreach ($f in $fallidos) {
        Linea "  - $($f.Paso): $($f.Valor)"
    }
}

Linea ''
Linea "Reporte: $($report.report)"
Linea "Bitacora: $($report.transcript)"
Linea "Salida tecnica: $SalidaTecnica"
exit 1
