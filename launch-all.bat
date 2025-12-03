@echo off
echo ========================================
echo  Notes App - Multi-Platform Launcher
echo ========================================
echo.
echo Starting all platforms...
echo.

REM Start Web version in a new window
echo [1/3] Launching Web version...
start "Notes App - Web" cmd /k "gradlew.bat :composeApp:jsBrowserDevelopmentRun --continuous"

REM Wait a moment
timeout /t 2 /nobreak >nul

REM Start Desktop version in a new window
echo [2/3] Launching Desktop version...
start "Notes App - Desktop" cmd /k "gradlew.bat :composeApp:run"

REM Wait a moment
timeout /t 2 /nobreak >nul

REM Start Android build (requires connected device or emulator)
echo [3/3] Building Android version...
echo Note: Make sure you have an Android device connected or emulator running
start "Notes App - Android" cmd /k "gradlew.bat :composeApp:installDebug"

echo.
echo ========================================
echo All platforms are launching!
echo ========================================
echo.
echo Web:     Will open in your browser automatically
echo Desktop: Separate window will appear
echo Android: Will install on connected device/emulator
echo.
echo Press any key to exit this launcher...
pause >nul
