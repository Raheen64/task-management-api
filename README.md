# Task Management REST API

[![Java CI with Maven](https://github.com/Raheen64/task-management-api/actions/workflows/ci.yml/badge.svg)](https://github.com/Raheen64/task-management-api/actions/workflows/ci.yml)

A secure task management backend built with Java and Spring Boot, featuring JWT authentication, PostgreSQL persistence, task CRUD operations, filtering, pagination, and Docker-based development.

## Overview

The Task Management REST API allows users to register, authenticate, and manage their own tasks through HTTP endpoints. It uses Spring Security for request authentication, JSON Web Tokens (JWT) for stateless authentication, and Spring Data JPA for database access.

This project demonstrates backend development fundamentals, REST API design, relational database integration, API testing, and automated build workflows.

## Features

- User registration and login
- Password hashing using BCrypt
- JWT-based stateless authentication
- Create, read, update, and delete tasks
- User-specific task access and data isolation
- Filter tasks by title and completion status
- Pagination and sorting using Spring Data
- Request validation
- Centralized API exception handling
- PostgreSQL database integration
- Interactive API documentation using Swagger UI
- Docker and Docker Compose configuration
- Automated tests using JUnit 5, Mockito, and MockMvc
- GitHub Actions CI workflow

## Technology Stack

| Category | Technology |
|---|---|
| Language | Java 17 |
| Backend framework | Spring Boot 3.3.7 |
| Security | Spring Security, JWT |
| ORM | Spring Data JPA, Hibernate |
| Database | PostgreSQL |
| Database administration | pgAdmin 4 |
| API testing | Postman |
| API documentation | Springdoc OpenAPI, Swagger UI |
| Testing | JUnit 5, Mockito, MockMvc |
| Build tool | Maven Wrapper |
| Containerization | Docker, Docker Compose |
| CI | GitHub Actions |

## Architecture

```text
Postman / API Client
        |
        v
Spring Boot REST API
        |
        v
Spring Security + JWT Filter
        |
        v
Controllers
        |
        v
Service Layer
        |
        v
Spring Data JPA Repositories
        |
        v
PostgreSQL Database
        |
        v
Users and Tasks
```

## Project Structure

```text
src/
├── main/
│   ├── java/com/raheen/taskapi/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── exception/
│   │   ├── repository/
│   │   ├── security/
│   │   ├── service/
│   │   └── TaskapiApplication.java
│   └── resources/
│       └── application.properties
└── test/
    ├── java/com/raheen/taskapi/
    └── resources/
```

- **Controllers:** Handle HTTP requests and responses.
- **Services:** Implement application and business logic.
- **Repositories:** Access users and tasks through Spring Data JPA.
- **Entities:** Represent persistent database records.
- **DTOs:** Define request and response data structures.
- **Security:** Handles JWT validation and authentication.
- **Exception handling:** Converts application errors into HTTP responses.

## API Endpoints

Base URL for local development: `http://localhost:8081`

| Method | Endpoint | Purpose | Authentication |
|---|---|---|---|
| `POST` | `/api/auth/register` | Register a user | Not required |
| `POST` | `/api/auth/login` | Authenticate and obtain a JWT | Not required |
| `GET` | `/api/tasks` | List the authenticated user's tasks | Required |
| `POST` | `/api/tasks` | Create a task | Required |
| `PUT` | `/api/tasks/{id}` | Update a task | Required |
| `DELETE` | `/api/tasks/{id}` | Delete a task | Required |

### Task filtering and pagination

The task listing endpoint supports optional filters and Spring Data pagination parameters.

**Example:**
`GET /api/tasks?title=report&completed=false&page=0&size=10&sort=createdAt,desc`

| Parameter | Description |
|---|---|
| `title` | Filter by a case-insensitive title substring |
| `completed` | Filter by completion status |
| `page` | Zero-based page index |
| `size` | Number of records per page |
| `sort` | Sort field and direction |

## Getting Started

### Prerequisites

- Java 17 JDK
- Git
- PostgreSQL, or Docker Desktop
- Postman or another HTTP client

### 1. Clone the repository

```bash
git clone https://github.com/Raheen64/task-management-api.git
cd task-management-api
```

### 2. Configure the application

The application requires database credentials and a JWT signing secret.

For **Windows PowerShell**, set the required environment variables explicitly:

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:postgresql://localhost:5432/taskdb"
$env:SPRING_DATASOURCE_USERNAME = "postgres"
$env:DB_PASSWORD = "your_local_database_password"
$env:JWT_SECRET = "replace_with_a_strong_random_secret_of_at_least_64_bytes"
```

> **Security Note:** Use a securely generated random secret of at least 64 bytes for HS512. Never commit actual secrets to GitHub.

Create a PostgreSQL database named `taskdb` if it does not already exist. You can manage the database using pgAdmin 4.

### 3. Run the application

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

The application is configured to run on port `8081` locally.

### 4. Open API documentation

Once the application has started, visit:
`http://localhost:8081/swagger-ui/index.html`

Use Swagger UI or Postman to interact with the API.

## Running with Docker

Docker Compose is configured to run the API and PostgreSQL as separate containers.

Create a local `.env` file in the repository root containing:

```env
DB_PASSWORD=replace_with_a_strong_database_password
JWT_SECRET=replace_with_a_strong_random_secret_of_at_least_64_bytes
```

Ensure that `docker-compose.yml` passes both `DB_PASSWORD` and `JWT_SECRET` into the application container. Keep `.env` excluded from version control.

Build and start the services:

```bash
docker compose up --build
```

The API is exposed on port `8080` through the Docker port mapping.

**Swagger UI:**
`http://localhost:8080/swagger-ui/index.html`

Stop the containers:

```bash
docker compose down
```

The PostgreSQL data volume is retained unless it is explicitly removed.

## Testing

Run the test suite:

```powershell
.\mvnw.cmd clean test
```

The project includes unit and slice tests for controller behavior, request validation, authentication requirements, invalid token rejection, and cross-user task access boundaries.

The test suite uses an H2 in-memory database and MockMvc to achieve 100% test success across all test cases.

## Testing with Postman

Recommended workflow:

1. Register a user using `POST /api/auth/register`.
2. Log in using `POST /api/auth/login`.
3. Copy the JWT returned in the response.
4. Select **Bearer Token** in Postman's **Authorization** tab and paste the token.
5. Create a task and retrieve the task list.
6. Test title and completion filters, pagination, task updates, and deletion.
7. Verify that requests without valid authentication are rejected with `401 Unauthorized`.
8. Create two separate users and verify that one cannot access or modify the other's tasks (`404 Not Found`).

## Database Management with pgAdmin 4

Use pgAdmin 4 to connect to the PostgreSQL server and inspect the `taskdb` database.

Verify that:
- User records are persisted after registration.
- Passwords are stored as BCrypt hashes rather than plaintext.
- Tasks are linked to their owning users.
- Task updates and deletions are reflected in the database.

## Continuous Integration

The GitHub Actions workflow in `.github/workflows/ci.yml` is configured to run Maven tests on pushes and pull requests targeting `main` or `master`. Build and test results are automatically validated in the cloud on every push.

## Current Scope and Future Improvements

Potential improvements include:
- Database schema migrations using Flyway
- Maximum page-size limits and stronger request validation
- Login rate limiting
- Structured application logging
- PostgreSQL-backed integration tests using Testcontainers
- Expanded CI checks and deployment automation

## Author

**Raheen Siddiqui**  
GitHub: [@Raheen64](https://github.com/Raheen64)