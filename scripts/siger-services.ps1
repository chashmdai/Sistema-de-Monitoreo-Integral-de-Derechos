[CmdletBinding()]
param(
    [Parameter(Position = 0)]
    [ValidateSet('start', 'stop', 'restart', 'status', 'logs', 'doctor', 'dashboard', 'dashboard-stop')]
    [string] $Command = 'status',

    [string[]] $Services = @(),
    [string[]] $Tier = @(),
    [switch] $IncludeOptional,
    [switch] $UseExampleEnv,
    [string] $Profile = 'local',
    [string] $JavaHome,
    [string] $MavenXmx = '384m',
    [string] $AppXmx = '512m',
    [switch] $NoHeapLimit,
    [switch] $DryRun,
    [switch] $NoHttp,
    [switch] $Follow,
    [switch] $NoWait,
    [int] $HealthTimeoutSeconds = 90,
    [int] $StartupDelaySeconds = 1,
    [int] $DashboardPort = 8765,
    [switch] $NoOpen
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$Root = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$RunRoot = Join-Path $Root '_runtime-logs'
$StateFile = Join-Path $RunRoot 'siger-services.json'
$CatalogFile = Join-Path $PSScriptRoot 'services.json'
$DefaultServicesRoot = 'services'

# ----------------------------------------------------------------------------------
# JDK: el proyecto apunta a Java 21. Si el 'java' del PATH es otro (p.ej. 25),
# resolvemos un JDK 21 y lo inyectamos como JAVA_HOME a los procesos Maven.
# Prioridad: -JavaHome > $env:SIGER_JAVA_HOME > autodeteccion de jdk-21* > PATH.
# ----------------------------------------------------------------------------------

function Resolve-JavaHome {
    if (-not [string]::IsNullOrWhiteSpace($JavaHome)) {
        if (Test-Path -LiteralPath (Join-Path $JavaHome 'bin\java.exe')) { return $JavaHome }
        throw "JavaHome invalido: '$JavaHome' (no existe bin\java.exe)."
    }
    if ($env:SIGER_JAVA_HOME -and (Test-Path -LiteralPath (Join-Path $env:SIGER_JAVA_HOME 'bin\java.exe'))) {
        return $env:SIGER_JAVA_HOME
    }
    $bases = @($env:ProgramFiles, ${env:ProgramFiles(x86)}, (Join-Path $env:LOCALAPPDATA 'Programs'))
    $candidates = foreach ($base in $bases) {
        if ($base -and (Test-Path -LiteralPath $base)) {
            Get-ChildItem -Path $base -Directory -Recurse -Depth 1 -Filter 'jdk-21*' -ErrorAction SilentlyContinue
        }
    }
    $jdk = @($candidates | Where-Object { Test-Path -LiteralPath (Join-Path $_.FullName 'bin\java.exe') } |
        Sort-Object Name -Descending | Select-Object -First 1)
    if ($jdk.Count -gt 0) { return $jdk[0].FullName }
    return $null
}

$ResolvedJavaHome = Resolve-JavaHome

# ----------------------------------------------------------------------------------
# Catalogo (services.json es la fuente unica de verdad)
# ----------------------------------------------------------------------------------

function Get-Catalog {
    if (-not (Test-Path -LiteralPath $CatalogFile)) {
        throw "No existe $CatalogFile. Es la fuente de verdad del ecosistema."
    }
    return (Get-Content -Raw -LiteralPath $CatalogFile | ConvertFrom-Json)
}

function Resolve-WorkspacePath([string] $Path, [string] $DefaultRelativePath) {
    $candidate = if ([string]::IsNullOrWhiteSpace($Path)) { $DefaultRelativePath } else { $Path }
    if ([System.IO.Path]::IsPathRooted($candidate)) { return $candidate }
    return Join-Path $Root $candidate
}

function Get-ServicesRoot {
    $catalog = Get-Catalog
    $configured = $null
    $pathsProp = $catalog.PSObject.Properties['paths']
    if ($null -ne $pathsProp -and $null -ne $pathsProp.Value) {
        $servicesRootProp = $pathsProp.Value.PSObject.Properties['servicesRoot']
        if ($null -ne $servicesRootProp) { $configured = [string] $servicesRootProp.Value }
    }
    return Resolve-WorkspacePath $configured $DefaultServicesRoot
}

function Get-ServiceDirectory([string] $ServiceName) {
    return Join-Path (Get-ServicesRoot) $ServiceName
}

function Get-ServiceCatalog {
    $catalog = Get-Catalog
    $servicesRoot = Get-ServicesRoot
    foreach ($row in $catalog.services) {
        # 'kind' distingue servicios Java (Maven/Spring) de frontends 'web' (npm).
        $kind = if ($row.PSObject.Properties['kind'] -and $row.kind) { [string] $row.kind } else { 'java' }
        $relPath = if ($row.PSObject.Properties['path'] -and $row.path) { [string] $row.path } else { $null }
        $dir = if ($relPath) { Resolve-WorkspacePath $relPath $null } else { Join-Path $servicesRoot $row.name }
        $url = if ($row.PSObject.Properties['url'] -and $row.url) { [string] $row.url } else { "http://localhost:$($row.port)/" }
        # Un frontend 'web' no expone /actuator/health: su salud es que el puerto responda.
        $healthUrl = if ($kind -eq 'web') { $url } else { "http://localhost:$($row.port)/actuator/health" }
        $command = if ($row.PSObject.Properties['command'] -and $row.command) { [string] $row.command } else { 'npm run dev' }
        [pscustomobject]@{
            Name        = $row.name
            Module      = $row.module
            DisplayName = $row.displayName
            Tier        = $row.tier
            Port        = $row.port
            Optional    = [bool] $row.optional
            StartLevel  = [int] $row.startLevel
            Db          = $row.db
            Directory   = $dir
            HealthUrl   = $healthUrl
            Kind        = $kind
            Url         = $url
            Command     = $command
        }
    }
}

function Resolve-SelectedServices {
    $catalog = @(Get-ServiceCatalog)
    $byName = @{}
    foreach ($item in $catalog) { $byName[$item.Name] = $item }

    if ($Services.Count -gt 0) {
        $result = foreach ($name in $Services) {
            if (-not $byName.ContainsKey($name)) {
                $valid = ($catalog | ForEach-Object Name) -join ', '
                throw "Servicio desconocido '$name'. Servicios validos: $valid"
            }
            $byName[$name]
        }
        return @($result)
    }

    if ($Tier.Count -gt 0) {
        return @($catalog | Where-Object { $Tier -contains $_.Tier })
    }

    return @($catalog | Where-Object { -not $_.Optional -or $IncludeOptional })
}

# ----------------------------------------------------------------------------------
# Estado de procesos
# ----------------------------------------------------------------------------------

function Read-State {
    if (-not (Test-Path -LiteralPath $StateFile)) { return @() }
    $raw = Get-Content -Raw -LiteralPath $StateFile
    if ([string]::IsNullOrWhiteSpace($raw)) { return @() }
    return @($raw | ConvertFrom-Json)
}

function Write-State([object[]] $Items) {
    New-Item -ItemType Directory -Force -Path $RunRoot | Out-Null
    $aliveItems = @($Items)
    if ($aliveItems.Count -eq 0) {
        if (Test-Path -LiteralPath $StateFile) { Remove-Item -LiteralPath $StateFile }
        return
    }
    $aliveItems | ConvertTo-Json -Depth 8 | Set-Content -LiteralPath $StateFile -Encoding UTF8
}

function Test-ProcessAlive([int] $ProcessId) {
    return $null -ne (Get-Process -Id $ProcessId -ErrorAction SilentlyContinue)
}

function Test-LocalPortOpen([int] $Port) {
    $client = [System.Net.Sockets.TcpClient]::new()
    try {
        $task = $client.ConnectAsync([System.Net.IPAddress]::Loopback, $Port)
        if (-not $task.Wait(250)) { return $false }
        return $client.Connected
    } catch {
        return $false
    } finally {
        $client.Dispose()
    }
}

# ----------------------------------------------------------------------------------
# Carga de .env y launch.json
# ----------------------------------------------------------------------------------

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

function Resolve-LaunchPath([string] $MaybePath, [string] $ServiceDir) {
    if ([string]::IsNullOrWhiteSpace($MaybePath)) { return $null }
    $resolved = $MaybePath.Replace('${workspaceFolder}', $ServiceDir)
    if ([System.IO.Path]::IsPathRooted($resolved)) { return $resolved }
    return Join-Path $ServiceDir $resolved
}

function Get-OptionalProperty([object] $Object, [string] $Name) {
    if ($null -eq $Object) { return $null }
    $prop = $Object.PSObject.Properties[$Name]
    if ($null -eq $prop) { return $null }
    return $prop.Value
}

function Get-LaunchConfig([string] $ServiceDir) {
    $launchPath = Join-Path $ServiceDir '.vscode\launch.json'
    if (-not (Test-Path -LiteralPath $launchPath)) {
        return [pscustomobject]@{ Path = $null; Name = $null; EnvFile = Join-Path $ServiceDir '.env'; Env = @{}; Args = $null }
    }

    $json = Get-Content -Raw -LiteralPath $launchPath | ConvertFrom-Json
    $config = @($json.configurations | Where-Object { $_.type -eq 'java' } | Select-Object -First 1)
    if ($config.Count -eq 0) { throw "No hay configuracion Java en $launchPath" }

    $cfg = $config[0]
    $launchEnv = Get-OptionalProperty $cfg 'env'
    $env = @{}
    if ($null -ne $launchEnv) {
        foreach ($prop in $launchEnv.PSObject.Properties) { $env[$prop.Name] = [string] $prop.Value }
    }

    $envFile = Resolve-LaunchPath (Get-OptionalProperty $cfg 'envFile') $ServiceDir
    if ([string]::IsNullOrWhiteSpace($envFile)) { $envFile = Join-Path $ServiceDir '.env' }

    $args = $null
    $launchArgs = Get-OptionalProperty $cfg 'args'
    if ($null -ne $launchArgs) {
        $args = if ($launchArgs -is [array]) { ($launchArgs -join ' ') } else { [string] $launchArgs }
    }

    [pscustomobject]@{ Path = $launchPath; Name = (Get-OptionalProperty $cfg 'name'); EnvFile = $envFile; Env = $env; Args = $args }
}

function Get-EffectiveServiceConfig([object] $Spec) {
    $launch = Get-LaunchConfig $Spec.Directory
    $envFile = $launch.EnvFile
    $envSource = 'env'

    if (-not (Test-Path -LiteralPath $envFile)) {
        $examplePath = Join-Path $Spec.Directory '.env.example'
        if ($UseExampleEnv -and (Test-Path -LiteralPath $examplePath)) {
            $envFile = $examplePath
            $envSource = 'env.example'
        } else {
            throw "Falta $envFile. Crea .env desde .env.example o usa -UseExampleEnv si corresponde."
        }
    }

    $envVars = Read-DotEnv $envFile
    foreach ($key in $launch.Env.Keys) { $envVars[$key] = $launch.Env[$key] }

    if (-not [string]::IsNullOrWhiteSpace($Profile)) { $envVars['SPRING_PROFILES_ACTIVE'] = $Profile }

    if (-not $NoHeapLimit) {
        $existingMavenOpts = ''
        if ($envVars.ContainsKey('MAVEN_OPTS')) { $existingMavenOpts = $envVars['MAVEN_OPTS'] }
        $envVars['MAVEN_OPTS'] = "$existingMavenOpts -Xmx$MavenXmx".Trim()
    }

    # Forzar el JDK objetivo (21) para que Maven compile y ejecute con el correcto,
    # sin depender del 'java' del PATH global.
    if ($ResolvedJavaHome) {
        $envVars['JAVA_HOME'] = $ResolvedJavaHome
        $basePath = if ($envVars.ContainsKey('PATH')) { $envVars['PATH'] } else { $env:PATH }
        $envVars['PATH'] = (Join-Path $ResolvedJavaHome 'bin') + ';' + $basePath
    }

    [pscustomobject]@{ EnvFile = $envFile; EnvSource = $envSource; Env = $envVars; Launch = $launch }
}

function Get-MavenCommand([string] $ServiceDir) {
    $wrapper = Join-Path $ServiceDir 'mvnw.cmd'
    if (Test-Path -LiteralPath $wrapper) { return $wrapper }
    return 'mvn'
}

function Get-MavenArguments([object] $EffectiveConfig) {
    $args = @('-ntp')
    $launchArgs = $EffectiveConfig.Launch.Args
    if (-not [string]::IsNullOrWhiteSpace($launchArgs) -and $launchArgs -notmatch '^\s*--spring\.profiles\.active=') {
        $args += "-Dspring-boot.run.arguments=$launchArgs"
    }
    if (-not $NoHeapLimit) { $args += "-Dspring-boot.run.jvmArguments=-Xmx$AppXmx" }
    $args += 'spring-boot:run'
    return $args
}

# ----------------------------------------------------------------------------------
# Health
# ----------------------------------------------------------------------------------

function Get-HealthStatus([string] $Url, [bool] $Alive) {
    if ($NoHttp -or -not $Alive) { return '' }
    try {
        $response = Invoke-RestMethod -Uri $Url -TimeoutSec 2
        if ($null -ne $response.status) { return [string] $response.status }
        return 'HTTP OK'
    } catch {
        return 'sin respuesta'
    }
}

function Wait-ServiceHealthy([object] $Spec, [int] $TimeoutSeconds) {
    $kind = if ($Spec.PSObject.Properties['Kind']) { [string] $Spec.Kind } else { 'java' }
    $limit = (Get-Date).AddSeconds($TimeoutSeconds)
    while ((Get-Date) -lt $limit) {
        if (Test-LocalPortOpen $Spec.Port) {
            # Un frontend 'web' (Vite) no tiene /actuator/health: el puerto abierto basta.
            if ($kind -eq 'web') { return $true }
            try {
                $response = Invoke-RestMethod -Uri $Spec.HealthUrl -TimeoutSec 2 -ErrorAction Stop
                if ([string] $response.status -eq 'UP') { return $true }
            } catch {
                # aun arrancando
            }
        }
        Start-Sleep -Milliseconds 1500
    }
    return $false
}

# ----------------------------------------------------------------------------------
# Start / Stop / Restart
# ----------------------------------------------------------------------------------

function Get-NpmCommand {
    foreach ($c in @('npm.cmd', 'npm')) {
        $cmd = Get-Command $c -ErrorAction SilentlyContinue
        if ($cmd) { return $cmd.Source }
    }
    return 'npm.cmd'
}

# Arranca un frontend 'web' (Vite/npm): instala dependencias la primera vez,
# lanza 'npm run dev' y, cuando el puerto responde, abre el navegador.
function Start-WebService([object] $Spec, [string] $LogDir, [hashtable] $StateByService, [ref] $NewState) {
    if (-not (Test-Path -LiteralPath (Join-Path $Spec.Directory 'package.json'))) {
        Write-Warning "Se omite $($Spec.Name): no existe package.json en $($Spec.Directory)"
        return $false
    }
    if ($StateByService.ContainsKey($Spec.Name) -and (Test-ProcessAlive ([int] $StateByService[$Spec.Name].pid))) {
        Write-Host "Ya esta arriba: $($Spec.Name) PID=$($StateByService[$Spec.Name].pid)"
        return $true
    }

    $npm = Get-NpmCommand
    $cmdParts = @($Spec.Command -split '\s+' | Where-Object { $_ })
    $npmArgs = @($cmdParts | Select-Object -Skip 1)   # 'npm run dev' -> @('run','dev')
    $stdout = Join-Path $LogDir "$($Spec.Name).out.log"
    $stderr = Join-Path $LogDir "$($Spec.Name).err.log"

    if ($DryRun) {
        Write-Host "DRY $($Spec.Name) (web)"
        Write-Host "  dir: $($Spec.Directory)"
        Write-Host "  cmd: $npm $($npmArgs -join ' ')"
        Write-Host "  url: $($Spec.Url)"
        return $true
    }

    # Primera vez: instalar dependencias (node_modules ausente). Bloquea por diseno.
    if (-not (Test-Path -LiteralPath (Join-Path $Spec.Directory 'node_modules'))) {
        Write-Host "Instalando dependencias de $($Spec.Name) (npm install, puede tardar)..." -ForegroundColor Yellow
        $installLog = Join-Path $LogDir "$($Spec.Name).install.log"
        Push-Location $Spec.Directory
        try { & $npm install *>&1 | Tee-Object -FilePath $installLog | Out-Null }
        finally { Pop-Location }
    }

    '' | Set-Content -LiteralPath $stdout -Encoding UTF8
    '' | Set-Content -LiteralPath $stderr -Encoding UTF8

    $process = Start-Process `
        -FilePath $npm `
        -WorkingDirectory $Spec.Directory `
        -ArgumentList $npmArgs `
        -RedirectStandardOutput $stdout `
        -RedirectStandardError $stderr `
        -WindowStyle Hidden `
        -PassThru

    $record = [pscustomobject]@{
        service   = $Spec.Name
        pid       = $process.Id
        port      = $Spec.Port
        healthUrl = $Spec.HealthUrl
        directory = $Spec.Directory
        envFile   = $null
        launch    = $null
        stdout    = $stdout
        stderr    = $stderr
        startedAt = (Get-Date).ToString('o')
    }
    $NewState.Value = @($NewState.Value | Where-Object { $_.service -ne $Spec.Name }) + $record
    Write-State $NewState.Value
    Write-Host "Arrancado: $($Spec.Name) PID=$($process.Id) puerto=$($Spec.Port) (web)"

    # Espera a que el dev server levante el puerto y abre la ventana del navegador.
    if (-not $NoOpen) {
        Write-Host "  esperando que $($Spec.Url) responda..." -NoNewline
        if (Wait-ServiceHealthy $Spec 40) {
            Write-Host ' OK, abriendo navegador' -ForegroundColor Green
            Start-Process $Spec.Url
        } else {
            Write-Host ' sin respuesta aun (no abro navegador)' -ForegroundColor Yellow
        }
    }
    return $true
}

function Start-OneService([object] $Spec, [string] $LogDir, [hashtable] $StateByService, [ref] $NewState) {
    $kind = if ($Spec.PSObject.Properties['Kind']) { [string] $Spec.Kind } else { 'java' }
    if ($kind -eq 'web') {
        return (Start-WebService $Spec $LogDir $StateByService $NewState)
    }

    if (-not (Test-Path -LiteralPath (Join-Path $Spec.Directory 'pom.xml'))) {
        Write-Warning "Se omite $($Spec.Name): no existe pom.xml en $($Spec.Directory)"
        return $false
    }

    if ($StateByService.ContainsKey($Spec.Name) -and (Test-ProcessAlive ([int] $StateByService[$Spec.Name].pid))) {
        Write-Host "Ya esta arriba: $($Spec.Name) PID=$($StateByService[$Spec.Name].pid)"
        return $true
    }

    try {
        $effective = Get-EffectiveServiceConfig $Spec
    } catch {
        Write-Warning "Se omite $($Spec.Name): $($_.Exception.Message)"
        return $false
    }

    $mvn = Get-MavenCommand $Spec.Directory
    $mvnArgs = @(Get-MavenArguments $effective)
    $stdout = Join-Path $LogDir "$($Spec.Name).out.log"
    $stderr = Join-Path $LogDir "$($Spec.Name).err.log"

    if ($DryRun) {
        $launchName = if ($effective.Launch.Name) { $effective.Launch.Name } else { 'sin launch.json' }
        Write-Host "DRY $($Spec.Name)"
        Write-Host "  dir: $($Spec.Directory)"
        Write-Host "  launch: $launchName"
        Write-Host "  env: $($effective.EnvFile) ($($effective.EnvSource), $(@($effective.Env.Keys).Count) keys)"
        Write-Host "  cmd: $mvn $($mvnArgs -join ' ')"
        return $true
    }

    '' | Set-Content -LiteralPath $stdout -Encoding UTF8
    '' | Set-Content -LiteralPath $stderr -Encoding UTF8

    $process = Start-Process `
        -FilePath $mvn `
        -WorkingDirectory $Spec.Directory `
        -ArgumentList $mvnArgs `
        -RedirectStandardOutput $stdout `
        -RedirectStandardError $stderr `
        -WindowStyle Hidden `
        -PassThru `
        -Environment $effective.Env

    $record = [pscustomobject]@{
        service   = $Spec.Name
        pid       = $process.Id
        port      = $Spec.Port
        healthUrl = $Spec.HealthUrl
        directory = $Spec.Directory
        envFile   = $effective.EnvFile
        launch    = $effective.Launch.Path
        stdout    = $stdout
        stderr    = $stderr
        startedAt = (Get-Date).ToString('o')
    }
    $NewState.Value = @($NewState.Value | Where-Object { $_.service -ne $Spec.Name }) + $record
    Write-State $NewState.Value

    Write-Host "Arrancado: $($Spec.Name) PID=$($process.Id) puerto=$($Spec.Port)"
    return $true
}

function Start-SigerServices {
    $selected = @(Resolve-SelectedServices)
    if ($selected.Count -eq 0) { Write-Host 'No hay servicios seleccionados.'; return }

    New-Item -ItemType Directory -Force -Path $RunRoot | Out-Null
    $logDir = Join-Path $RunRoot (Get-Date -Format 'yyyyMMdd-HHmmss')
    if (-not $DryRun) { New-Item -ItemType Directory -Force -Path $logDir | Out-Null }

    $state = @(Read-State)
    $stateByService = @{}
    foreach ($item in $state) { $stateByService[$item.service] = $item }
    $newState = [ref] @($state | Where-Object { Test-ProcessAlive ([int] $_.pid) })

    # Arranque escalonado por startLevel; dentro de cada nivel se espera health UP
    # antes de pasar al siguiente (a menos que -NoWait o -DryRun).
    $levels = @($selected | ForEach-Object StartLevel | Sort-Object -Unique)
    foreach ($level in $levels) {
        $batch = @($selected | Where-Object { $_.StartLevel -eq $level })
        Write-Host ''
        Write-Host "== Nivel $level ==" -ForegroundColor Cyan
        $started = @()
        foreach ($spec in $batch) {
            if (Start-OneService $spec $logDir $stateByService $newState) { $started += $spec }
            Start-Sleep -Seconds $StartupDelaySeconds
        }

        if (-not $DryRun -and -not $NoWait -and $started.Count -gt 0 -and $level -lt ($levels | Select-Object -Last 1)) {
            foreach ($spec in $started) {
                Write-Host "  esperando health UP de $($spec.Name)..." -NoNewline
                if (Wait-ServiceHealthy $spec $HealthTimeoutSeconds) {
                    Write-Host ' UP' -ForegroundColor Green
                } else {
                    Write-Host " sin UP en ${HealthTimeoutSeconds}s (sigo igual)" -ForegroundColor Yellow
                }
            }
        }
    }

    if (-not $DryRun) {
        Write-Host ''
        Write-Host "Logs: $logDir"
        Write-Host "Estado: $StateFile"
    }
}

function Stop-SigerServices {
    $state = @(Read-State)
    if ($state.Count -eq 0) { Write-Host "No hay servicios registrados en $StateFile"; return }

    $selectedNames = if ($Services.Count -gt 0) { $Services }
        elseif ($Tier.Count -gt 0) { @(Resolve-SelectedServices | ForEach-Object Name) }
        else { @($state | ForEach-Object service) }

    $remaining = @()
    foreach ($item in $state) {
        if ($selectedNames -notcontains $item.service) { $remaining += $item; continue }

        if (-not (Test-ProcessAlive ([int] $item.pid))) {
            Write-Host "Ya estaba detenido: $($item.service) PID=$($item.pid)"
            continue
        }

        Write-Host "Deteniendo: $($item.service) PID=$($item.pid)"
        if ($IsWindows) { & taskkill.exe /PID $item.pid /T /F | Out-Null }
        else { Stop-Process -Id $item.pid -Force }

        if (Test-ProcessAlive ([int] $item.pid)) {
            Write-Warning "No se pudo detener $($item.service); queda en estado."
            $remaining += $item
        }
    }
    Write-State $remaining
}

function Restart-SigerServices {
    Stop-SigerServices
    Start-Sleep -Seconds 2
    Start-SigerServices
}

# ----------------------------------------------------------------------------------
# Status / Logs
# ----------------------------------------------------------------------------------

function Show-SigerStatus {
    $state = @(Read-State)
    if ($state.Count -eq 0) { Write-Host "No hay servicios registrados en $StateFile"; return }

    $selected = if ($Services.Count -gt 0) { @($state | Where-Object { $Services -contains $_.service }) } else { $state }

    $selected |
        Sort-Object port |
        ForEach-Object {
            $alive = Test-ProcessAlive ([int] $_.pid)
            [pscustomobject]@{
                Service = $_.service
                PID     = $_.pid
                Port    = $_.port
                Process = if ($alive) { 'running' } else { 'stopped' }
                Health  = Get-HealthStatus $_.healthUrl $alive
            }
        } |
        Format-Table -AutoSize
}

function Show-SigerLogs {
    $state = @(Read-State)
    if ($state.Count -eq 0) { Write-Host "No hay servicios registrados en $StateFile"; return }

    $selected = if ($Services.Count -gt 0) { @($state | Where-Object { $Services -contains $_.service }) } else { $state }

    $paths = @()
    foreach ($item in $selected) {
        foreach ($path in @($item.stdout, $item.stderr)) {
            if (Test-Path -LiteralPath $path) { $paths += $path }
        }
    }
    if ($paths.Count -eq 0) { Write-Host 'No se encontraron logs para los servicios seleccionados.'; return }

    Write-Host 'Leyendo logs:'
    $paths | ForEach-Object { Write-Host "  $_" }
    if ($Follow) { Get-Content -LiteralPath $paths -Tail 120 -Wait }
    else { Get-Content -LiteralPath $paths -Tail 120 }
}

# ----------------------------------------------------------------------------------
# Doctor (diagnostico del entorno)
# ----------------------------------------------------------------------------------

function Get-EnvActiveValue([hashtable] $Vars, [string[]] $Names) {
    foreach ($name in $Names) {
        if ($Vars.ContainsKey($name) -and -not [string]::IsNullOrWhiteSpace($Vars[$name])) {
            return $Vars[$name]
        }
    }
    return $null
}

function Get-LocalYmlOverrides([string] $ServiceName) {
    # Claves sensibles con valor literal en application-local.yml que pisan el .env.
    $path = Join-Path (Get-ServiceDirectory $ServiceName) 'src\main\resources\application-local.yml'
    $found = @()
    if (-not (Test-Path -LiteralPath $path)) { return $found }
    foreach ($line in Get-Content -LiteralPath $path) {
        if ($line -match '^\s*(username|password|secreto-activo|secreto-previo|secreto|secret|clave|cifrado|consumo|roles-[a-z-]+)\s*:\s*(\S.*?)\s*$') {
            $key = $Matches[1].ToLower()
            $value = $Matches[2].Trim()
            if ($value.StartsWith('${') -or $value.StartsWith('#')) { continue }
            $sev = if ($key -match 'secret|secreto|clave') { 'ERROR' } else { 'AVISO' }
            $found += [pscustomobject]@{ Key = $key; Sev = $sev }
        }
    }
    return $found
}

function Invoke-Doctor {
    $catalog = Get-Catalog
    $report = [System.Collections.Generic.List[object]]::new()

    function Add-Check([string] $Name, [string] $Status, [string] $Detail) {
        $report.Add([pscustomobject]@{ Check = $Name; Status = $Status; Detail = $Detail })
    }

    # Toolchain — Java que realmente usaran los servicios (JAVA_HOME inyectado)
    $javaExe = if ($ResolvedJavaHome) { Join-Path $ResolvedJavaHome 'bin\java.exe' } else { (Get-Command java -ErrorAction SilentlyContinue).Source }
    if ($javaExe -and (Test-Path -LiteralPath $javaExe)) {
        $ver = (& $javaExe -version 2>&1 | Select-Object -First 1)
        $is21 = "$ver" -match '"21\.' -or "$ver" -match 'version "21'
        $origen = if ($ResolvedJavaHome) { "JAVA_HOME=$ResolvedJavaHome" } else { 'java del PATH' }
        Add-Check 'Java (arranque)' $(if ($is21) { 'OK' } else { 'AVISO' }) "$ver | $origen"
    } else {
        Add-Check 'Java (arranque)' 'FALTA' 'No se encontro java ni un JDK 21 (se requiere JDK 21).'
    }

    $mvn = Get-Command mvn -ErrorAction SilentlyContinue
    if ($mvn) { Add-Check 'Maven' 'OK' $mvn.Source }
    else { Add-Check 'Maven' 'AVISO' 'mvn no esta en PATH; los servicios con mvnw.cmd igual funcionan.' }

    # Infra
    foreach ($infra in $catalog.infra) {
        if (Test-LocalPortOpen $infra.port) { Add-Check $infra.name 'OK' "Puerto $($infra.port) abierto." }
        else { Add-Check $infra.name 'CAIDO' "Puerto $($infra.port) cerrado. $($infra.requiredBy)" }
    }

    # .env por servicio + coherencia JWT
    $secrets = @{}
    foreach ($svc in $catalog.services) {
        # Los frontends 'web' no usan .env del ecosistema; se omiten del chequeo.
        if ($svc.PSObject.Properties['kind'] -and $svc.kind -eq 'web') { continue }
        $dir = Get-ServiceDirectory $svc.name
        $envPath = Join-Path $dir '.env'
        if (-not (Test-Path -LiteralPath $envPath)) {
            $hasExample = Test-Path -LiteralPath (Join-Path $dir '.env.example')
            $msg = if ($svc.optional) { 'opcional, sin .env' } else { 'falta .env' }
            if ($hasExample) { $msg += ' (hay .env.example)' }
            Add-Check "env: $($svc.name)" ($(if ($svc.optional) { 'AVISO' } else { 'FALTA' })) $msg
            continue
        }
        $vars = Read-DotEnv $envPath
        $secret = Get-EnvActiveValue $vars $catalog.jwt.secretVars
        if ($null -ne $secret) { $secrets[$svc.name] = $secret }
        Add-Check "env: $($svc.name)" 'OK' "$envPath"
    }

    # Coherencia del secreto JWT entre servicios que lo declaran
    $distinct = @($secrets.Values | Sort-Object -Unique)
    if ($secrets.Count -eq 0) {
        Add-Check 'JWT coherente' 'AVISO' 'No se pudo leer ningun JWT_SECRET de los .env.'
    } elseif ($distinct.Count -eq 1) {
        Add-Check 'JWT coherente' 'OK' "$($secrets.Count) servicios comparten el mismo JWT_SECRET."
    } else {
        $names = ($secrets.Keys | Sort-Object) -join ', '
        Add-Check 'JWT coherente' 'ERROR' "Hay $($distinct.Count) secretos distintos entre: $names. El Gateway rechazara tokens."
    }

    # Valores hardcodeados en application-local.yml que pisan el .env
    foreach ($svc in $catalog.services) {
        $overrides = @(Get-LocalYmlOverrides $svc.name)
        if ($overrides.Count -eq 0) { continue }
        $keys = ($overrides | ForEach-Object Key | Sort-Object -Unique) -join ', '
        $sev = if ($overrides.Sev -contains 'ERROR') { 'ERROR' } else { 'AVISO' }
        Add-Check "local yml: $($svc.name)" $sev "application-local.yml hardcodea $keys y pisa el .env. Parametrizar a variable de entorno."
    }

    # Puertos en uso por algo ajeno
    foreach ($svc in $catalog.services) {
        if (Test-LocalPortOpen $svc.port) {
            Add-Check "puerto $($svc.port)" 'EN USO' "$($svc.name) (o algo) escucha en $($svc.port)."
        }
    }

    $report | Format-Table -AutoSize
    Write-Host ''
    $bad = @($report | Where-Object { $_.Status -in @('FALTA', 'CAIDO', 'ERROR') })
    if ($bad.Count -eq 0) { Write-Host 'Diagnostico: sin bloqueantes.' -ForegroundColor Green }
    else { Write-Host "Diagnostico: $($bad.Count) item(s) requieren atencion." -ForegroundColor Yellow }
}

# ----------------------------------------------------------------------------------
# Dashboard
# ----------------------------------------------------------------------------------

function Start-SigerDashboard {
    $python = Get-Command python -ErrorAction SilentlyContinue
    if ($null -eq $python) { throw 'No encontre python en PATH.' }

    $dashboardScript = Join-Path $PSScriptRoot 'dashboard_server.py'
    $args = @($dashboardScript, '--host', '127.0.0.1', '--port', [string] $DashboardPort)
    if ($NoOpen) { $args += '--no-open' }
    & $python.Source @args
}

function Stop-SigerDashboard {
    $connection = Get-NetTCPConnection -LocalPort $DashboardPort -State Listen -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($null -eq $connection) { Write-Host "No hay dashboard escuchando en el puerto $DashboardPort."; return }
    Write-Host "Deteniendo dashboard PID=$($connection.OwningProcess) puerto=$DashboardPort"
    Stop-Process -Id $connection.OwningProcess -Force
}

# ----------------------------------------------------------------------------------

switch ($Command) {
    'start' { Start-SigerServices }
    'stop' { Stop-SigerServices }
    'restart' { Restart-SigerServices }
    'status' { Show-SigerStatus }
    'logs' { Show-SigerLogs }
    'doctor' { Invoke-Doctor }
    'dashboard' { Start-SigerDashboard }
    'dashboard-stop' { Stop-SigerDashboard }
}
