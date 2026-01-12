📘 Project README
🧩 Project Overview

This project is a Spring Boot RESTful backend application designed with clean architecture principles, JWT-based authentication, role-based authorization, and CQRS (Command Query Responsibility Segregation).

It demonstrates best practices in:

Secure API design

Layered architecture

Separation of concerns

Testability and scalability

🛠️ Technology Stack
Layer	Technology
Language	Java 17
Framework	Spring Boot 3.x
Security	Spring Security + JWT
ORM	Spring Data JPA
Database	H2 (Dev) / Any SQL (Prod-ready)
API Docs	OpenAPI 3 (Swagger UI)
Build Tool	Maven
Testing	JUnit 5, Mockito
📁 Project Structure
src/main/java
 └── org.test.app
     ├── config          # Security, JWT, OpenAPI, filters
     ├── controllers
     ├── services
     ├── entities        # JPA entities
     ├── repositories   # Spring Data repositories
     ├── security       # JWT, UserDetails, filters
     ├── dto            # Request / Response DTOs
     ├── exception      # Global exception handling
     └── AppApplication # Main class

⚙️ Setup Instructions

1️.Prerequisites

Java 17+

Maven 3.9+

IDE (Eclipse )

2️.Clone Repository
git clone <repository-url>
cd app

3️.Configure Application

Default configuration is H2 in-memory DB.

application.yml (example):

server:
  port: 8080
  servlet:
    context-path: /app

spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driverClassName: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  ttlSeconds: 3600


🔐 JWT secret is generated dynamically at runtime and stored in config_prop table (not hardcoded)

4.Run the Application
mvn clean spring-boot:run


Application will start at:

http://localhost:8080/app

🔐 Security Design
Authentication

Stateless JWT authentication

Token issued on successful login

Token validated by JwtAuthenticationFilter

Authorization

Role-based access using:

@PreAuthorize("hasRole('ADMIN')")

Roles
Role			Description
USER			Basic access
PREMIUM_USER	Discounted access
ADMIN			Full system control

🧠 Key Design Decisions
✅ CQRS Pattern

Command Controllers → Write operations

Query Controllers → Read operations

Improves scalability and clarity

✅ DTO-Based API

No entity exposure

Prevents over-posting & leakage

✅ Centralized Exception Handling

Handled via @ControllerAdvice:

BadRequestException

IllegalArgumentException

IllegalStateException

UsernameNotFoundException

✅ Secure Configuration Storage

Secrets (JWT key) stored in DB (config_prop)

Generated only once

Avoids hardcoding sensitive values

📘 API Documentation (Swagger)

Swagger UI:

http://localhost:8080/app/swagger-ui.html


🔑 Authentication APIs
🔐 Login

POST /api/v1/auth/login

{
  "username": "useername",
  "password": "pwd"
}


Response

{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}

👤 Register User (ADMIN only)

POST /api/v1/auth/admin/register

{
  "username": "premium-user",
  "password": "",
  "role": "PREMIUM_USER"
}

📦 Product APIs
➕ Create Product (ADMIN)

POST /api/v1/products

{
  "name": "Laptop",
  "price": 1200,
  "available": true
}

🔍 Search Products

GET /api/v1/products/search

Query Params

name, minPrice, maxPrice, available, page, size, sortBy, sortDir

🛒 Order APIs
🧾 Place Order

POST /api/v1/orders

{
  "productId": 1,
  "quantity": 2
}

💸 Discount Rules
User Type	Discount
USER	No discount
PREMIUM_USER	10%
Any order > 500	Extra 5%

Discounts are calculated server-side to avoid tampering.



👨‍💻 Author

Anwar
Software Development Manager
Spring Boot | Security | Fintech | POS Systems