# 📋 Task Management API

A robust RESTful backend application built with **Java 17** and **Spring Boot**, designed for high-performance task tracking, dynamic filtering, pagination, and persistent storage with PostgreSQL. Fully containerized with Docker.

[Features](#-key-features) • [Tech Stack](#️-tech-stack) • [API Endpoints](#-api-endpoints-overview) • [Getting Started](#-getting-started) • [Docker Setup](#-running-with-docker)

## 🌟 Overview

The **Task Management API** provides a secure and scalable backend solution for organizing, tracking, and filtering tasks. Built following modern software design patterns, it includes automated testing (JUnit 5 & Mockito), robust database integration with Spring Data JPA, and environment-secured configurations.

## 🛠️ Tech Stack

| Component | Technology | 
| ----- | ----- | 
| **Language** | Java 17 | 
| **Framework** | Spring Boot (Spring Web, Spring Data JPA) | 
| **Database** | PostgreSQL | 
| **Containerization** | Docker & Docker Compose | 
| **Testing** | JUnit 5, Mockito, MockMvc | 
| **Build Tool** | Maven (`mvnw`) | 
| **Documentation** | Springdoc OpenAPI (Swagger UI) | 

## 🚀 Key Features

* **Task CRUD Operations:** Complete support for creating, reading, updating, and deleting tasks.

* **Search & Filter:** Advanced query filtering by title and status.

* **Pagination Support:** Efficient handling of large datasets using server-side pagination.

* **Automated Unit Testing:** High-coverage unit and controller testing suite using JUnit 5 and Mockito.

* **Secure Environment Configuration:** Protected database credentials utilizing environment variables and local `.env` protection.

* **Container Ready:** Seamless multi-container orchestration via Docker Compose.

## 📋 API Endpoints Overview

| Method | Endpoint | Description | 
| ----- | ----- | ----- | 
| **GET** | `/api/tasks` | Retrieve all tasks (supports pagination and filtering) | 
| **POST** | `/api/tasks` | Create a new task | 
| **PUT** | `/api/tasks/{id}` | Update an existing task | 
| **DELETE** | `/api/tasks/{id}` | Delete a task | 

## ⚙️ Getting Started

### Prerequisites

* Java 17 JDK installed

* Maven installed

* Local PostgreSQL instance or Docker

### Local Installation & Execution

1. **Clone the repository:**

   ```
   git clone https://github.com/Raheen64/task-management-api.git
   cd task-management-api
   
   ```

2. **Configure Environment Variables:**
   Create a `.env` file in the root directory and add your database password:

   ```
   DB_PASSWORD=your_secure_password
   
   ```

3. **Run the Application:**

   ```
   ./mvnw clean spring-boot:run
   
   ```

4. **Access Swagger UI:**
   Open your browser and navigate to:
   `http://localhost:8081/swagger-ui/index.html`

## 🐳 Running with Docker

To spin up both PostgreSQL and the Spring Boot application simultaneously:

1. **Build and start containers:**

   ```
   docker-compose up --build
   
   ```

2. **Access Swagger UI via Docker Port Mapping:**
   `http://localhost:8080/swagger-ui/index.html`

3. **Stop containers:**

   ```
   docker-compose down
   
   ```

## 🧪 Running Unit Tests

Execute the automated test suite using Maven:

```
./mvnw clean test

```

## 👨‍💻 Author

**Raheen**

* GitHub: [@Raheen64](https://github.com/Raheen64)