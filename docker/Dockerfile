# Stage 1: Build the application
FROM maven:3.9.5-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copy pom.xml and download dependencies (caching layer)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and compile
COPY src ./src
RUN mvn clean compile

# Stage 2: Runtime image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Install Chromium browser and ChromeDriver for headless execution
RUN apk add --no-cache \
    chromium \
    chromium-chromedriver \
    xvfb \
    ttf-freefont \
    fontconfig

# Set environment variables for Chrome/Chromium
ENV CHROME_BIN=/usr/bin/chromium-browser
ENV CHROME_DRIVER=/usr/lib/chromium/chromedriver

# Copy built artifacts from build stage
COPY --from=build /app/target /app/target
COPY --from=build /app/test-output /app/test-output

# Copy test resources (if any)
COPY src/test/resources /app/src/test/resources

# Set display port for headless browser (Xvfb not strictly needed with --headless=new)
ENV DISPLAY=:99

# Create directories for reports and screenshots
RUN mkdir -p /app/test-output/screenshots /app/test-output/videos /app/test-output/logs

# Default command: run tests with Maven
CMD ["mvn", "test"]