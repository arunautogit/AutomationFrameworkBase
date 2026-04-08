FROM maven:3.9.9-eclipse-temurin-17

WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends \
    chromium \
    chromium-driver \
    fonts-liberation \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

ENV CHROME_BIN=/usr/bin/chromium
ENV CHROME_DRIVER=/usr/bin/chromedriver

COPY pom.xml ./
COPY testng.xml ./
COPY src ./src

RUN mvn -B -q dependency:go-offline
RUN mvn -B -q -DskipTests clean test-compile

RUN mkdir -p /app/test-output/screenshots /app/test-output/videos /app/test-output/logs

CMD ["mvn", "test", "-Denv=qa", "-Dbrowser=chrome", "-Dheadless=true"]
