# 🧁 Product Service

![Java](https://img.shields.io/badge/Java-21%2B-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)

Welcome to the **Product Service**, a core component of the Shah's Bakery Microservice Platform.

## 📑 Table of Contents
- [Features](#-features)
- [Folder Structure](#-folder-structure)
- [Dependencies](#-dependencies)
- [Endpoints](#-endpoints)
- [How to Run](#-how-to-run)
- [Related Links](#-related-links)

## ✨ Features
- Complete CRUD operations for bakery products.
- Stock and inventory level tracking.
- Fast fuzzy search and category filtering via Elasticsearch.
- Direct Cloudflare R2 / S3 integrations for image storage.
- Dynamic Site Configuration tracking.

## 📁 Folder Structure
The main `src/main/java` directory is organized as follows:
```text
src/
└── main/
    └── java/.../bakery_product_service/
        ├── controller/ # REST endpoints for products, categories, inventory, and uploads.
        ├── document/   # Elasticsearch document models for fast search indexing.
        ├── dto/        # Data Transfer Objects.
        ├── entity/     # Database entities mapping to MongoDB.
        ├── exception/  # Custom exceptions.
        ├── model/      # Non-persistent or supporting domain models.
        ├── repository/ # Interfaces for MongoDB and Elasticsearch access.
        └── service/    # Core logic including Product management, R2 storage uploads, and Site Config.
```

## 🛠️ Dependencies
- **Framework:** Spring Boot
- **Database & Search:** MongoDB (Primary DB), Elasticsearch (Search Index)
- **Key Modules:** Eureka Client, Spring Data JPA

## 🌐 Endpoints
> [!NOTE]
> For complete and detailed API definitions, please refer to the OpenAPI Reference available via the API Gateway's Swagger UI.

- `GET /api/categories` - Retrieves a list of all active categories.
- `GET /api/products` - Retrieves a paginated list of all products.
- `GET /api/products/{id}` - Fetches details for a specific product.
- `POST /api/products` - Adds a new product to the catalogue (Admin only).

## 🚀 How to Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/amankrmj01/bakery_product_service.git
   cd bakery_product_service
   ```

2. **Configure Environment:**
   Ensure your `.env` or `application.yml` properties (including DB credentials) are set.

3. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```

## 🔗 Related Links
- [Main Platform README](../README.md)