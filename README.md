# Bakery Product Service

## Overview
Maintains product catalog, inventory levels, pricing, and availability.

## Features
- CRUD for bakery products
- Stock and pricing management
- Product search and categorization

## Dependencies
- Spring WebFlux
- Spring Data JPA
- Spring Security
- Spring Boot Actuator

## Key Endpoints
- `/api/products/`
- `/api/products/{productId}`

## Running
./gradlew bootRun

text
Runs on port 8086 by default.

## Documentation
Swagger UI: `http://localhost:8086/swagger-ui.html`

---