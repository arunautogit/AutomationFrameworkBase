FROM maven:3.9.5-eclipse-temurin-17-alpine

WORKDIR /app

# Install Chromium, ChromeDriver, and other dependencies
RUN apk add --no-cache \
    chromium \
    chromium-chromedriver \
    xvfb \
    ttf-freefont \
    fontconfig

# Set environment variables for headless Chrome
ENV CHROME_BIN=/usr/bin/chromium-browser
ENV CHROME_DRIVER=/usr/lib/chromium/chromedriver
ENV DISPLAY=:99

# Copy all project files into the container
COPY pom.xml .
COPY testng.xml .
COPY src ./src

# Download Maven dependencies and compile the code
RUN mvn dependency:go-offline
RUN mvn clean compile

# Create directories for test outputs (reports, screenshots, videos)
RUN mkdir -p /app/test-output/screenshots /app/test-output/videos /app/test-output/logs

# Default command: run tests using Maven
CMD ["mvn", "test"]