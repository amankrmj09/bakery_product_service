# 🚀 Product Service

![Java](https://img.shields.io/badge/Java-25-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.x-brightgreen.svg)
![Database](https://img.shields.io/badge/Database-MongoDB%20%7C%20Elasticsearch-blue.svg)

The **Product Service** is a core component of the Shah's Bakery Microservice Platform, handling the product catalog, inventory management, and fast fuzzy searching capabilities.

## 📑 Table of Contents
- [Architecture & Design](#-architecture--design)
- [Features](#-features)
- [Folder Structure](#-folder-structure)
- [API Reference](#-api-reference)
- [Configuration](#-configuration)
- [How to Run Locally](#-how-to-run-locally)
- [Testing](#-testing)
- [Dependencies](#-dependencies)
- [Related Links](#-related-links)

## 🏗️ Architecture & Design
Provide a brief overview of the architecture of this service.
- **Data Storage**: MongoDB for primary data storage, Elasticsearch for fast search indexing, Redis for caching.
- **Communication**: REST API for synchronous communication, Kafka for async event driven architecture, Eureka for service discovery.
- **Key Design Patterns**: MVC, Repository Pattern, DTO pattern, Feign clients for inter-service communication.
- **Object Storage**: Direct Cloudflare R2 / S3 integrations for image storage.

## ✨ Features
List the core capabilities and features of this service.
- Complete CRUD operations for bakery products.
- Stock and inventory level tracking.
- Fast fuzzy search and category filtering via Elasticsearch.
- Direct Cloudflare R2 / S3 integrations for image storage.
- Dynamic Site Configuration tracking.

## 📁 Folder Structure
The source code under `src/main/java` is organized as follows:
```text
src/
└── main/
    └── java/.../bakery_product_service/
        ├── controller/ # REST endpoints for products, categories, inventory, and uploads
        ├── document/   # Elasticsearch document models for fast search indexing
        ├── dto/        # Data Transfer Objects
        ├── entity/     # Database entities mapping to MongoDB
        ├── exception/  # Custom exceptions and global exception handler
        ├── model/      # Non-persistent or supporting domain models
        ├── repository/ # Interfaces for MongoDB and Elasticsearch access
        └── service/    # Core logic including Product management, R2 storage uploads, and Site Config
```

## 🌐 API Reference
> [!NOTE]
> For detailed API definitions, request/response bodies, and schemas, please refer to the OpenAPI Reference available via the API Gateway's Swagger UI.

**Key Endpoints:**
- `GET /api/categories` - Retrieves a list of all active categories.
- `GET /api/products` - Retrieves a paginated list of all products.
- `GET /api/products/{id}` - Fetches details for a specific product.
- `POST /api/products` - Adds a new product to the catalogue (Admin only).

## ⚙️ Configuration
List required environment variables and configurations.
You can copy `.env.example` to `.env` and fill in the values.

| Variable | Description | Default / Example |
|----------|-------------|-------------------|
| `SERVER_PORT` | Port for the service | `8080` |
| `ACTIVE_PROFILE` | Spring active profile | `dev` |
| `CONFIG_SERVER_URL` | Spring Cloud Config Server URL | `http://localhost:8888` |
| `EUREKA_URL`| Eureka Service Registry URL | `http://localhost:8761/eureka/` |
| `PRODUCT_DB_URL` | MongoDB Connection URL | `mongodb://localhost:27017/product_db` |
| `PRODUCT_DB_USER` | MongoDB Username | `admin` |
| `PRODUCT_DB_PASSWORD` | MongoDB Password | `password` |
| `ELASTICSEARCH_URIS` | Elasticsearch cluster URIs | `http://localhost:9200` |
| `ELASTIC_PASSWORD` | Elasticsearch Password | |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka bootstrap servers | `localhost:9092` |
| `REDIS_HOST` | Redis Host | `localhost` |
| `REDIS_PORT_PRODUCT` | Redis Port | `6379` |
| `R2_ACCESS_KEY` | Cloudflare R2 Access Key | |
| `R2_SECRET_KEY` | Cloudflare R2 Secret Key | |
| `R2_BUCKET` | Cloudflare R2 Bucket Name | |
| `R2_ENDPOINT` | Cloudflare R2 Endpoint URL | |
| `R2_CDN_URL` | Cloudflare R2 CDN URL | |
| `PRODUCT_LOW_STOCK` | Threshold for low stock | `10` |

## 🚀 How to Run Locally

### Prerequisites
- JDK 25
- Gradle
- MongoDB
- Elasticsearch
- Kafka & Redis

### Steps
1. **Clone the repository:**
   ```bash
   git clone https://github.com/amankrmj01/bakery_product_service.git
   cd bakery_product_service
   ```

2. **Configure Environment:**
   Set up your `.env` file based on `.env.example`. Make sure backing services (like MongoDB, Elasticsearch, Redis, and Kafka) are running.
   You can use the provided Docker Compose file:
   ```bash
   docker-compose -f docker-compose.yml up -d
   ```
   *(Ensure to use the correct compose file if you have a specific one for this service)*

3. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```

## 🧪 Testing
To run the test suite:
```bash
./gradlew test
```

## 🛠️ Dependencies
- **Framework:** Spring Boot 3.5.15
- **Database & Search:** MongoDB (Primary DB), Elasticsearch (Search Index)
- **Key Modules:** Spring Web, Spring Data MongoDB, Spring Data Elasticsearch, Spring Kafka, Eureka Client
- **Other Utilities:** AWS SDK S3 (for R2 integration), MapStruct, Lombok

## 🔗 Related Links
- [Main Platform README](../README.md)