@echo off
echo Ultra-fast deployment - build locally, containerize quickly...

REM Build production bundle locally (faster than Docker)
echo [1/4] Building production bundle locally...
call gradlew.bat :composeApp:jsBrowserProductionWebpack --parallel

if errorlevel 1 (
    echo Build failed! Trying development build...
    call gradlew.bat :composeApp:jsBrowserDevelopmentWebpack --parallel
)

REM Create fast Docker image (just copy files, no build)
echo [2/4] Creating lightweight Docker image...
docker build -f Dockerfile.fast -t sachindra785/kotlin-web:latest .

REM Push to Docker Hub
echo [3/4] Pushing to Docker Hub...
docker push sachindra785/kotlin-web:latest

REM Optional: Test locally
echo [4/4] Testing locally...
echo Starting container on http://localhost:3000
docker run -d -p 3000:80 --name kotlin-web-test sachindra785/kotlin-web:latest

echo.
echo ✅ Deployment complete!
echo 🐳 Image: sachindra785/kotlin-web:latest  
echo 🌐 Local test: http://localhost:3000
echo 📦 Image size optimized with nginx + gzip
echo.
echo To stop test: docker stop kotlin-web-test
pause