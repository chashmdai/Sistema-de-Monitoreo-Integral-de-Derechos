@echo off
setlocal

set "SIGER_ROOT=C:\Users\Benja\Documents\SIGER"
set "SIGER_HOST=127.0.0.1"
set "SIGER_PORT=8765"

title SIGER Dashboard
chcp 65001 >nul

if not exist "%SIGER_ROOT%\scripts\dashboard_server.py" (
    echo No se encontro el proyecto SIGER en:
    echo   %SIGER_ROOT%
    echo.
    echo Edita SIGER_ROOT dentro de este archivo .bat si moviste la carpeta.
    pause
    exit /b 1
)

cd /d "%SIGER_ROOT%"

set "PYTHON_CMD=python"
where python >nul 2>nul
if errorlevel 1 (
    where py >nul 2>nul
    if errorlevel 1 (
        echo No se encontro Python en PATH.
        echo Instala Python o agrega python.exe al PATH.
        pause
        exit /b 1
    )
    set "PYTHON_CMD=py -3"
)

echo.
echo ============================================================
echo   SIGER Dashboard
echo ============================================================
echo.
echo Abriendo: http://%SIGER_HOST%:%SIGER_PORT%/
echo.
echo Mantener esta ventana abierta mantiene vivo el panel.
echo Para apagar el panel, presiona Ctrl+C o cierra esta ventana.
echo.

%PYTHON_CMD% "%SIGER_ROOT%\scripts\dashboard_server.py" --host "%SIGER_HOST%" --port "%SIGER_PORT%"

echo.
echo Dashboard detenido.
pause
