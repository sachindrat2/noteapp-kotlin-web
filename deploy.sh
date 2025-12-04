#!/bin/bash

# Build the Kotlin/JS production bundle
echo "Building Kotlin/JS production bundle..."
./gradlew :composeApp:jsBrowserProductionWebpack

# Build Docker image
echo "Building Docker image..."
docker build -t sachindra785/kotlin-web:latest .

# Push to Docker Hub (requires login)
echo "Pushing to Docker Hub..."
docker push sachindra785/kotlin-web:latest

echo "Deployment complete! Image available at: sachindra785/kotlin-web:latest"