# Multi-stage build for Kotlin Compose JS
FROM gradle:8.4-jdk17 AS builder

WORKDIR /app

# Install Node + Yarn (required for Kotlin JS / Webpack)
RUN apt-get update && apt-get install -y curl
RUN curl -fsSL https://deb.nodesource.com/setup_18.x | bash -
RUN apt-get install -y nodejs
RUN npm install -g yarn

# Copy gradle config
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle.properties ./
COPY gradle/ gradle/
COPY composeApp/build.gradle.kts composeApp/
COPY gradlew ./
RUN chmod +x gradlew

# Pre-fetch dependencies
RUN ./gradlew dependencies --no-daemon || true

# Copy source
COPY composeApp/src/ composeApp/src/

# Build JS production bundle
RUN ./gradlew :composeApp:jsBrowserProductionWebpack --no-daemon --parallel
# Debug: List contents of build output
RUN ls -l /app/composeApp/build/dist/js/productionExecutable/

# Production stage
FROM nginx:alpine

# Copy local build output (not from builder)
COPY composeApp/build/dist/js/productionExecutable/ /usr/share/nginx/html/
COPY default.conf /etc/nginx/conf.d/default.conf

# Debug: List contents of web root
RUN ls -l /usr/share/nginx/html/

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
