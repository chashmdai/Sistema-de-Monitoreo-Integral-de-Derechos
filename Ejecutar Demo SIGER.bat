@echo off
setlocal

set "SIGER_ROOT=C:\Users\Benja\Documents\SIGER"
title Demo SIGER
chcp 65001 >nul

if not exist "%SIGER_ROOT%\scripts\demo-siger.ps1" (
    echo No se encontro la demo de SIGER en:
    echo   %SIGER_ROOT%
    echo.
    echo Edita SIGER_ROOT dentro de este archivo .bat si moviste la carpeta.
    pause
    exit /b 1
)

where pwsh >nul 2>nul
if errorlevel 1 (
    echo No se encontro PowerShell 7 ^(pwsh^) en PATH.
    echo Instala PowerShell 7 o ejecuta la demo desde una terminal compatible.
    pause
    exit /b 1
)

cd /d "%SIGER_ROOT%"
pwsh -NoProfile -ExecutionPolicy Bypass -File "%SIGER_ROOT%\scripts\demo-siger.ps1"

echo.
pause
