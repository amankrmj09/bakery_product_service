# 🚀 Product Service

![Java](https://img.shields.io/badge/Java-25-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.x-brightgreen.svg)
![Database](https://img.shields.io/badge/Database-MongoDB%20%7C%20Elasticsearch-blue.svg)

The **Product Service** is a core microservice of the Shah's Bakery Platform, managing the product catalogue, categories, inventory, storefront configurations, tax rates, file media uploads, and fast search indexing via Elasticsearch.

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
- **Data Storage**: MongoDB for primary document persistence, Elasticsearch for fuzzy search indexing, Redis for caching.
- **Communication**: REST API for synchronous communication, Kafka for asynchronous event publishing (e.g., product updates, review processing), Eureka for service discovery.
- **Key Design Patterns**: CQRS (Command/Query separation for products), Strategy Pattern (Coupon Validation), DTO & MapStruct Mappers, Repository Pattern.
- **Object Storage**: Direct Cloudflare R2 / S3 integrations for media upload and storage.

## ✨ Features
- **Product Management**: Full CQRS product commands (create, update, patch status, toggle featured, delete) and queries (filtering by category, price range, tags, allergens, sales, batch fetching).
- **Category Management**: Hierarchical bakery product categories, active filtering, reordering, status toggling, and category statistics.
- **Inventory Tracking**: Stock level tracking, stock reservation/release/consumption, low-stock/out-of-stock/expired item alerts, and minimum stock level management.
- **Storefront Configuration**: Dynamic frontpage campaign, hero banner, special offers, and coupon validation strategies.
- **Tax Rate Configuration**: Dynamic tax rate management.
- **Media Uploads**: Multipart media upload integration with Cloudflare R2 / S3 storage.
- **Fuzzy Search**: High-performance search powered by Elasticsearch for products and categories.

## 📁 Folder Structure

```text
bakery_product_service/
├── .env
├── .env.example
├── Dockerfile
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── src/
│   ├── main/
│   │   ├── java/com/blubugtech/bakery_product_service/
│   │   │   ├── BakeryProductServiceApplication.java
│   │   │   ├── cache/                  # Redis caching managers & implementations
│   │   │   │   ├── ProductCacheManager.java
│   │   │   │   └── ProductCacheManagerImpl.java
│   │   │   ├── constants/              # Application-wide constants
│   │   │   │   └── ProductConstants.java
│   │   │   ├── controller/             # REST API Controllers
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── InventoryController.java
│   │   │   │   ├── ProductCommandController.java
│   │   │   │   ├── ProductQueryController.java
│   │   │   │   ├── StorefrontController.java
│   │   │   │   ├── TaxRateController.java
│   │   │   │   └── UploadController.java
│   │   │   ├── dto/                    # Data Transfer Objects
│   │   │   │   ├── CategoryStatResponse.java
│   │   │   │   ├── CategoryStatisticsResponse.java
│   │   │   │   ├── InventoryStatisticsResponse.java
│   │   │   │   ├── category/           # Request/Response DTOs for categories
│   │   │   │   ├── inventory/          # Request/Response DTOs for inventory
│   │   │   │   ├── media/              # Request/Response DTOs for media uploads
│   │   │   │   └── product/            # Request/Response DTOs for products
│   │   │   ├── entity/                 # MongoDB Entities (Category, Inventory, Product, TaxRate)
│   │   │   ├── exception/              # Exceptions & Global Exception Handler
│   │   │   ├── integration/            # Messaging & External Storage Integrations
│   │   │   │   ├── EventPublisher.java
│   │   │   │   ├── kafka/              # Kafka producers & event consumers
│   │   │   │   └── storage/            # Cloudflare R2 / S3 storage services
│   │   │   ├── mapper/                 # MapStruct Mappers (Category, Inventory, Product)
│   │   │   ├── model/                  # Domain Models (Storefront)
│   │   │   ├── repository/             # Spring Data MongoDB Repositories
│   │   │   ├── search/                 # Elasticsearch Integration
│   │   │   │   ├── document/           # Category & Product Search Documents
│   │   │   │   ├── repository/         # Elasticsearch Repositories
│   │   │   │   └── service/            # Category & Product Search Services
│   │   │   └── service/                # Business Logic Services & Implementations
│   │   │       ├── impl/               # Service Implementations
│   │   │       └── strategy/           # Coupon & Offer Validation Strategies
│   │   └── resources/
│   │       ├── application.yml         # Base Configuration
│   │       ├── application-dev.yml     # Development Profile
│   │       ├── application-docker.yml  # Docker Profile
│   │       ├── application-prod.yml    # Production Profile
│   │       ├── application-test.yml    # Test Profile
│   │       └── logback-spring.xml      # Logback Configuration
│   └── test/
│       ├── java/                       # Unit & Integration Tests
│       └── resources/                  # Test Configuration Files
```

## 🌐 API Reference
For detailed API request/response schemas, endpoints, query parameters, and authentication requirements, refer to [API_REFERENCE.md](API_REFERENCE.md).

**Key Controller Base Paths:**
- Categories: `/api/categories`
- Products (Command): `/api/products`
- Products (Query): `/api/products`
- Inventory: `/api/inventory`
- Storefront: `/api/storefront`
- Tax Rates: `/api/taxes`
- File Uploads: `/api/uploads`

## ⚙️ Configuration
Environment configurations are loaded via environment variables or `.env` file (copied from `.env.example`).

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
| `REDIS_HOST` | Redis Host | `localhost` |
| `REDIS_PORT_PRODUCT` | Redis Port | `6379` |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka bootstrap servers | `localhost:9092` |
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
   Set up your `.env` file based on `.env.example`. Make sure backing services are running.

3. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```

## 🧪 Testing
To execute unit and integration tests:
```bash
./gradlew test
```

## 🛠️ Dependencies
- **Framework:** Spring Boot 3.5.x
- **Database & Search:** MongoDB (Primary DB), Elasticsearch (Search Index), Redis (Cache)
- **Messaging:** Spring Kafka
- **Object Storage:** AWS SDK S3 (Cloudflare R2 Integration)
- **Utilities:** MapStruct, Lombok, Spring Security

## 🔗 Related Links
- [Parent Repository](https://github.com/amankrmj09/Blu_s_Bakery)
- [API Reference](./API_REFERENCE.md)
