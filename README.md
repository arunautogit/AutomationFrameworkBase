# AutomationFrameworkBase

## Requirements

- Java 17
- Maven 3.9+
- Chrome or Firefox installed locally, or Docker Desktop

## Run locally

```bash
mvn clean test -Denv=qa
```

Optional system properties:

```bash
mvn clean test -Denv=qa -Dbrowser=chrome -Dheadless=true
```

## Run with Docker

Build and run the self-contained browser image:

```bash
docker build -f docker/Dockerfile -t automation-framework .
docker run --rm automation-framework
```

Run against Selenium Grid with Docker Compose:

```bash
docker compose -f docker/docker-compose.yml up --build --abort-on-container-exit
```

The framework now supports remote Selenium execution through `REMOTE_URL` or `-DremoteUrl=...`.

## GitHub Actions

The scheduled workflow is defined in `.github/workflows/schedule.yml` and can also be started manually with `workflow_dispatch`.
