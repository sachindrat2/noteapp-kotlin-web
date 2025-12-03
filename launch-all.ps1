# Notes App - Multi-Platform Launcher (PowerShell)
Write-Host "========================================" -ForegroundColor Cyan
Write-Host " Notes App - Multi-Platform Launcher" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$projectPath = "C:\Users\s-thakur00\Downloads\Notesapp\Notesapp"
Set-Location $projectPath

Write-Host "Starting all platforms..." -ForegroundColor Yellow
Write-Host ""

# Start Web version in a new PowerShell window
Write-Host "[1/3] Launching Web version..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location '$projectPath'; Write-Host 'Starting Web Browser version...' -ForegroundColor Cyan; .\gradlew.bat :composeApp:jsBrowserDevelopmentRun --continuous"

Start-Sleep -Seconds 2

# Start Desktop version in a new PowerShell window
Write-Host "[2/3] Launching Desktop version..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location '$projectPath'; Write-Host 'Starting Desktop version...' -ForegroundColor Cyan; .\gradlew.bat :composeApp:run"

Start-Sleep -Seconds 2

# Android build (optional - only if device/emulator is connected)
Write-Host "[3/3] Android build ready..." -ForegroundColor Green
Write-Host ""
Write-Host "To install on Android:" -ForegroundColor Yellow
Write-Host "  1. Connect an Android device or start an emulator" -ForegroundColor White
Write-Host "  2. Run: .\gradlew.bat :composeApp:installDebug" -ForegroundColor White
Write-Host ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Platforms launching!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Web:     Will open in browser (http://localhost:8080)" -ForegroundColor White
Write-Host "Desktop: Separate application window" -ForegroundColor White
Write-Host "Android: Use command above when device is ready" -ForegroundColor White
Write-Host ""
Write-Host "Press any key to exit this launcher..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
