# Optimized multi-stage build for production
FROM gradle:8.4-jdk17-alpine AS builder

# Set working directory
WORKDIR /app

# Copy gradle files first for better caching
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle/ gradle/
COPY composeApp/build.gradle.kts composeApp/
COPY gradle.properties* ./

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
COPY <<EOF /etc/nginx/conf.d/default.conf
server {
    listen 80;
    server_name localhost;
    
    location / {
        root /usr/share/nginx/html;
        index index.html;
        try_files \$uri \$uri/ /index.html;
    }
    
    # Enable gzip compression
    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml application/xml+rss text/javascript application/wasm;
}
EOF

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]