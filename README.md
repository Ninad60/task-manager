# Task Manager Microservice

A RESTful Task Manager microservice built with Spring Boot, demonstrating a complete CI/CD pipeline using GitHub Actions.

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 3.2.5
- **Database:** H2 (in-memory)
- **Build Tool:** Maven
- **Containerisation:** Docker
- **CI/CD:** GitHub Actions
- **Code Analysis:** SonarCloud + JaCoCo

## API Endpoints

| Method | Endpoint                    | Description             |
|--------|-----------------------------|-------------------------|
| GET    | `/api/tasks`                | Get all tasks           |
| GET    | `/api/tasks/{id}`           | Get task by ID          |
| POST   | `/api/tasks`                | Create a new task       |
| PUT    | `/api/tasks/{id}`           | Update a task           |
| DELETE | `/api/tasks/{id}`           | Delete a task           |
| GET    | `/api/tasks/status/{status}`| Filter by status        |
| GET    | `/api/tasks/priority/{pri}` | Filter by priority      |
| GET    | `/api/tasks/search?keyword=`| Search by title keyword |
| GET    | `/actuator/health`          | Health check            |

## How to Build

```bash
# Clone the repository
git clone https://github.com/<your-username>/task-manager.git
cd task-manager

# Build with Maven
mvn clean package
```

## How to Test

```bash
# Run all tests (unit + integration)
mvn verify

# Run tests with coverage report
mvn verify
# Coverage report at: target/site/jacoco/index.html
```

## How to Deploy

### Option 1: Run with Docker Compose (recommended)
```bash
docker compose up --build -d
```

### Option 2: Run the JAR directly
```bash
mvn clean package -DskipTests
java -jar target/task-manager-1.0.0.jar
```

### Option 3: Pull from GHCR
```bash
./deploy.sh <your-github-username>
```

The application runs on `http://localhost:8080`.

## CI/CD Pipeline

The pipeline is defined in `.github/workflows/ci-cd.yml` and consists of five stages:

1. **Build** — Compiles the code and packages a JAR artefact using Maven
2. **Test** — Runs unit and integration tests with JaCoCo coverage
3. **Code Analysis** — SonarCloud static analysis with quality gate enforcement
4. **Docker Build & Push** — Builds a Docker image and pushes to GitHub Container Registry
5. **Deploy** — Pulls the image and runs it with Docker Compose, then verifies health

Each stage depends on the previous one. If any stage fails, subsequent stages do not execute.

## Test Strategy

Tests follow the **Test Pyramid**:

- **Unit Tests** (base): Service-layer logic tested with Mockito (6 tests)
- **Controller Tests** (middle): HTTP-layer tests with MockMvc (4 tests)
- **Integration Tests** (top): Full Spring Boot context with H2 database (3 tests)

## Project Structure

```
task-manager/
├── .github/workflows/ci-cd.yml   # Pipeline definition
├── src/main/java/                 # Application source code
├── src/test/java/                 # Test source code
├── Dockerfile                     # Multi-stage Docker build
├── docker-compose.yml             # Container orchestration
├── deploy.sh                      # Local deployment script
├── pom.xml                        # Maven configuration
└── README.md                      # This file
```
