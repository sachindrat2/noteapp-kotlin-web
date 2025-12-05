# Optimized multi-stage build for production
FROM gradle:8.4-jdk17-alpine AS builder

# Set working directory
WORKDIR /app

# Copy gradle files first for better caching
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle/ gradle/
COPY composeApp/build.gradle.kts composeApp/
COPY gradle.properties* ./
COPY gradlew gradlew.bat ./

# Download dependencies first (cached layer)
RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY composeApp/src/ composeApp/src/

# Build production bundle with optimizations
RUN ./gradlew :composeApp:jsBrowserProductionWebpack --no-daemon --parallel

# Lightweight production stage
FROM nginx:alpine

# Copy built app
COPY --from=builder /app/composeApp/build/kotlin-webpack/js/productionExecutable/ /usr/share/nginx/html/


# Copy nginx config for SPA
COPY default.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]