# Bakery Product Service API Reference

This document provides a comprehensive reference to the **Bakery Product Service REST API**, reflecting all Spring REST Controllers (`@RestController`), request/response DTOs, endpoint parameters, and security requirements.

---

## 📑 Table of Contents
- [1. System & Monitoring (Actuator)](#1-system--monitoring-actuator)
- [2. Categories API](#2-categories-api)
- [3. Product Command API](#3-product-command-api)
- [4. Product Query API](#4-product-query-api)
- [5. Inventory API](#5-inventory-api)
- [6. Storefront API](#6-storefront-api)
- [7. Tax Rates API](#7-tax-rates-api)
- [8. Uploads API](#8-uploads-api)
- [Data Transfer Objects (DTOs)](#data-transfer-objects-dtos)
- [Error Responses](#error-responses)

---

## 1. System & Monitoring (Actuator)
**Base Path:** `/actuator`

Standard Spring Boot Actuator endpoints for health check, runtime information, and Prometheus metrics scraping.

### 1.1 Health Check
- **Method:** `GET`
- **Path:** `/actuator/health`
- **Access Level:** `Public`
- **Response:** `200 OK`

### 1.2 Application Information
- **Method:** `GET`
- **Path:** `/actuator/info`
- **Access Level:** `Public`
- **Response:** `200 OK`

### 1.3 Prometheus Metrics
- **Method:** `GET`
- **Path:** `/actuator/prometheus`
- **Access Level:** `Public`
- **Response:** `200 OK`

---

## 2. Categories API
**Base Path:** `/api/categories`  
**Controller:** `CategoryController`

### 2.1 Get All Categories
- **Method:** `GET`
- **Path:** `/api/categories`
- **Access Level:** `Public`
- **Query Parameters:**
  - `page` (int, default: `0`)
  - `size` (int, default: `20`)
  - `sortBy` (string, default: `displayOrder`)
  - `sortDir` (string, default: `ASC`)
- **Response:** `200 OK` — `PagedModel<[`CategoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/category/CategoryResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "categoryresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d1",
          "name": "Cakes",
          "description": "Delicious freshly baked cakes",
          "displayOrder": 1,
          "active": true,
          "isTopCategory": false,
          "mediaUrls": ["https://cdn.example.com/cake.jpg"],
          "iconClass": "fa-cake",
          "productCount": 15,
          "activeProductCount": 12,
          "createdAt": "2026-01-15T10:30:00",
          "updatedAt": "2026-02-01T14:20:00"
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```
- **Error Responses:** `400 Bad Request`

### 2.2 Get Active Categories Only
- **Method:** `GET`
- **Path:** `/api/categories/active`
- **Access Level:** `Public`
- **Query Parameters:** `page`, `size`, `sortBy`, `sortDir`
- **Response:** `200 OK` — `PagedModel<[`CategoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/category/CategoryResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "categoryresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d1",
          "name": "Cakes",
          "description": "Delicious freshly baked cakes",
          "displayOrder": 1,
          "active": true,
          "isTopCategory": false,
          "mediaUrls": ["https://cdn.example.com/cake.jpg"],
          "iconClass": "fa-cake",
          "productCount": 15,
          "activeProductCount": 12,
          "createdAt": "2026-01-15T10:30:00",
          "updatedAt": "2026-02-01T14:20:00"
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```

### 2.3 Get Categories with Products
- **Method:** `GET`
- **Path:** `/api/categories/with-products`
- **Access Level:** `Public`
- **Query Parameters:** `page`, `size`, `sortBy`, `sortDir`
- **Response:** `200 OK` — `PagedModel<[`CategoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/category/CategoryResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "categoryresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d1",
          "name": "Cakes",
          "description": "Delicious freshly baked cakes",
          "displayOrder": 1,
          "active": true,
          "isTopCategory": false,
          "mediaUrls": ["https://cdn.example.com/cake.jpg"],
          "iconClass": "fa-cake",
          "productCount": 15,
          "activeProductCount": 12,
          "createdAt": "2026-01-15T10:30:00",
          "updatedAt": "2026-02-01T14:20:00"
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```

### 2.4 Get Categories with Active Products
- **Method:** `GET`
- **Path:** `/api/categories/with-active-products`
- **Access Level:** `Public`
- **Query Parameters:** `page`, `size`, `sortBy`, `sortDir`
- **Response:** `200 OK` — `PagedModel<[`CategoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/category/CategoryResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "categoryresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d1",
          "name": "Cakes",
          "description": "Delicious freshly baked cakes",
          "displayOrder": 1,
          "active": true,
          "isTopCategory": false,
          "mediaUrls": ["https://cdn.example.com/cake.jpg"],
          "iconClass": "fa-cake",
          "productCount": 15,
          "activeProductCount": 12,
          "createdAt": "2026-01-15T10:30:00",
          "updatedAt": "2026-02-01T14:20:00"
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```

### 2.5 Get Top Categories with Top Products
- **Method:** `GET`
- **Path:** `/api/categories/top-with-products`
- **Access Level:** `Public`
- **Query Parameters:**
  - `productLimit` (int, default: `5`)
  - `page` (int, default: `0`)
  - `size` (int, default: `20`)
  - `sortBy` (string, default: `displayOrder`)
  - `sortDir` (string, default: `ASC`)
- **Response:** `200 OK` — `PagedModel<[`CategoryWithTopProductsResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/category/CategoryWithTopProductsResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "categorywithtopproductsresponseList": [
        {
          "category": {
            "id": "...",
            "name": "..."
          },
          "topProducts": [
            {
              "id": "...",
              "name": "..."
            }
          ]
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```

### 2.6 Get Category by ID
- **Method:** `GET`
- **Path:** `/api/categories/{categoryId}`
- **Access Level:** `Public`
- **Response:** `200 OK` — [`CategoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/category/CategoryResponse.java)

  **Example Response Body:**
  ```json
  {
    "id": "64f1a2b3c4d5e6f7a8b9c0d1",
    "name": "Cakes",
    "description": "Delicious freshly baked cakes",
    "displayOrder": 1,
    "active": true,
    "isTopCategory": false,
    "mediaUrls": ["https://cdn.example.com/cake.jpg"],
    "iconClass": "fa-cake",
    "productCount": 15,
    "activeProductCount": 12,
    "createdAt": "2026-01-15T10:30:00",
    "updatedAt": "2026-02-01T14:20:00"
  }
  ```
- **Error Responses:** `404 Not Found`

### 2.7 Create Category
- **Method:** `POST`
- **Path:** `/api/categories`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Request Body:** [`CategoryRequest`](./src/main/java/com/blubugtech/bakery_product_service/dto/category/CategoryRequest.java)

  **Example Request Body:**
  ```json
  {
    "name": "Cakes",
    "description": "Delicious freshly baked cakes",
    "displayOrder": 1,
    "active": true,
    "isTopCategory": false,
    "mediaUrls": ["https://cdn.example.com/cake.jpg"],
    "iconClass": "fa-cake"
  }
  ```
- **Response:** `201 Created` — [`CategoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/category/CategoryResponse.java)

  **Example Response Body:**
  ```json
  {
    "id": "64f1a2b3c4d5e6f7a8b9c0d1",
    "name": "Cakes",
    "description": "Delicious freshly baked cakes",
    "displayOrder": 1,
    "active": true,
    "isTopCategory": false,
    "mediaUrls": ["https://cdn.example.com/cake.jpg"],
    "iconClass": "fa-cake",
    "productCount": 15,
    "activeProductCount": 12,
    "createdAt": "2026-01-15T10:30:00",
    "updatedAt": "2026-02-01T14:20:00"
  }
  ```
- **Error Responses:** `400 Bad Request` (Validation Failed), `403 Forbidden`

### 2.8 Update Category
- **Method:** `PUT`
- **Path:** `/api/categories/{categoryId}`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Request Body:** [`CategoryRequest`](./src/main/java/com/blubugtech/bakery_product_service/dto/category/CategoryRequest.java)

  **Example Request Body:**
  ```json
  {
    "name": "Cakes",
    "description": "Delicious freshly baked cakes",
    "displayOrder": 1,
    "active": true,
    "isTopCategory": false,
    "mediaUrls": ["https://cdn.example.com/cake.jpg"],
    "iconClass": "fa-cake"
  }
  ```
- **Response:** `200 OK` — [`CategoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/category/CategoryResponse.java)

  **Example Response Body:**
  ```json
  {
    "id": "64f1a2b3c4d5e6f7a8b9c0d1",
    "name": "Cakes",
    "description": "Delicious freshly baked cakes",
    "displayOrder": 1,
    "active": true,
    "isTopCategory": false,
    "mediaUrls": ["https://cdn.example.com/cake.jpg"],
    "iconClass": "fa-cake",
    "productCount": 15,
    "activeProductCount": 12,
    "createdAt": "2026-01-15T10:30:00",
    "updatedAt": "2026-02-01T14:20:00"
  }
  ```
- **Error Responses:** `400 Bad Request`, `403 Forbidden`, `404 Not Found`

### 2.9 Delete Category
- **Method:** `DELETE`
- **Path:** `/api/categories/{categoryId}`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Response:** `200 OK` — `MessageResponse`

  **Example Response Body:**
  ```json
  {
    "message": "Operation successful"
  }
  ```
- **Error Responses:** `403 Forbidden`, `404 Not Found`

### 2.10 Search Categories
- **Method:** `GET`
- **Path:** `/api/categories/search`
- **Access Level:** `Public`
- **Query Parameters:** `query` (required), `page`, `size`, `sortBy`, `sortDir`
- **Response:** `200 OK` — `PagedModel<[`CategoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/category/CategoryResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "categoryresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d1",
          "name": "Cakes",
          "description": "Delicious freshly baked cakes",
          "displayOrder": 1,
          "active": true,
          "isTopCategory": false,
          "mediaUrls": ["https://cdn.example.com/cake.jpg"],
          "iconClass": "fa-cake",
          "productCount": 15,
          "activeProductCount": 12,
          "createdAt": "2026-01-15T10:30:00",
          "updatedAt": "2026-02-01T14:20:00"
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```

### 2.11 Admin Search Categories
- **Method:** `GET`
- **Path:** `/api/categories/admin/search`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Query Parameters:** `query` (required), `page`, `size`, `sortBy`, `sortDir`
- **Response:** `200 OK` — `PagedModel<[`CategoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/category/CategoryResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "categoryresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d1",
          "name": "Cakes",
          "description": "Delicious freshly baked cakes",
          "displayOrder": 1,
          "active": true,
          "isTopCategory": false,
          "mediaUrls": ["https://cdn.example.com/cake.jpg"],
          "iconClass": "fa-cake",
          "productCount": 15,
          "activeProductCount": 12,
          "createdAt": "2026-01-15T10:30:00",
          "updatedAt": "2026-02-01T14:20:00"
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```

### 2.12 Toggle Category Active Status
- **Method:** `POST`
- **Path:** `/api/categories/{categoryId}/toggle-status`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Response:** `200 OK` — [`CategoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/category/CategoryResponse.java)

  **Example Response Body:**
  ```json
  {
    "id": "64f1a2b3c4d5e6f7a8b9c0d1",
    "name": "Cakes",
    "description": "Delicious freshly baked cakes",
    "displayOrder": 1,
    "active": true,
    "isTopCategory": false,
    "mediaUrls": ["https://cdn.example.com/cake.jpg"],
    "iconClass": "fa-cake",
    "productCount": 15,
    "activeProductCount": 12,
    "createdAt": "2026-01-15T10:30:00",
    "updatedAt": "2026-02-01T14:20:00"
  }
  ```
- **Error Responses:** `403 Forbidden`, `404 Not Found`

### 2.13 Reorder Categories
- **Method:** `POST`
- **Path:** `/api/categories/reorder`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Request Body:** `Map<String, Integer>` (Mapping of categoryId -> new displayOrder)

  **Example Request Body:**
  ```json
  {
    "id1": 10,
    "id2": 20
  }
  ```
- **Response:** `200 OK` — `MessageResponse`

  **Example Response Body:**
  ```json
  {
    "message": "Operation successful"
  }
  ```
- **Error Responses:** `400 Bad Request`, `403 Forbidden`

### 2.14 Get Category Statistics
- **Method:** `GET`
- **Path:** `/api/categories/statistics`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Response:** `200 OK` — [`CategoryStatisticsResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/category/CategoryStatisticsResponse.java)

  **Example Response Body:**
  ```json
  {
    "totalCategories": 8,
    "activeCategories": 7,
    "inactiveCategories": 1,
    "topCategories": 3,
    "categoryStats": [
      {
        "categoryId": "64f1a2b3c4d5e6f7a8b9c0d1",
        "categoryName": "Cakes",
        "productCount": 15,
        "activeProductCount": 12
      }
    ]
  }
  ```

---

## 3. Product Command API
**Base Path:** `/api/products`  
**Controller:** `ProductCommandController`

### 3.1 Create Product
- **Method:** `POST`
- **Path:** `/api/products`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Request Body:** [`ProductRequest`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductRequest.java)

  **Example Request Body:**
  ```json
  {
    "sku": "CAKE-CHOCO-001",
    "name": "Chocolate Fudge Cake",
    "description": "Rich dark chocolate layer cake",
    "shortDescription": "Rich chocolate cake",
    "categoryId": "64f1a2b3c4d5e6f7a8b9c0d1",
    "price": 25.99,
    "discountPrice": 22.99,
    "costPrice": 12.00,
    "taxClass": "STANDARD",
    "taxRate": 5.0,
    "metaTitle": "Buy Chocolate Fudge Cake",
    "metaDescription": "Order fresh chocolate fudge cake online",
    "maxOrderQuantity": 5,
    "status": "ACTIVE",
    "isFeatured": true,
    "preparationTimeMinutes": 60,
    "shelfLifeHours": 72,
    "unit": "piece",
    "calories": "450 kcal",
    "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
    "allergens": ["Dairy", "Eggs", "Gluten"],
    "tags": ["chocolate", "cake", "bestseller"],
    "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
    "initialStock": 50,
    "minimumStock": 5,
    "reorderLevel": 10
  }
  ```
- **Response:** `201 Created` — [`ProductResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductResponse.java)

  **Example Response Body:**
  ```json
  {
    "id": "64f1a2b3c4d5e6f7a8b9c0d2",
    "sku": "CAKE-CHOCO-001",
    "name": "Chocolate Fudge Cake",
    "description": "Rich dark chocolate layer cake",
    "shortDescription": "Rich chocolate cake",
    "category": {
      "id": "64f1a2b3c4d5e6f7a8b9c0d1",
      "name": "Cakes",
      "iconClass": "fa-cake"
    },
    "price": 25.99,
    "discountPrice": 22.99,
    "costPrice": 12.00,
    "effectivePrice": 22.99,
    "taxClass": "STANDARD",
    "taxRate": 5.0,
    "metaTitle": "Buy Chocolate Fudge Cake",
    "metaDescription": "Order fresh chocolate fudge cake online",
    "maxOrderQuantity": 5,
    "status": "ACTIVE",
    "isFeatured": true,
    "isActive": true,
    "preparationTimeMinutes": 60,
    "shelfLifeHours": 72,
    "unit": "piece",
    "calories": "450 kcal",
    "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
    "allergens": ["Dairy", "Eggs", "Gluten"],
    "tags": ["chocolate", "cake", "bestseller"],
    "inventory": {
      "currentStock": 45,
      "availableStock": 40,
      "isLowStock": false,
      "isOutOfStock": false,
      "status": "IN_STOCK"
    },
    "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
    "primaryImageUrl": "https://cdn.example.com/choco-cake.jpg",
    "isAvailable": true,
    "isOnSale": true,
    "createdAt": "2026-01-15T10:30:00",
    "updatedAt": "2026-02-01T14:20:00",
    "averageRating": 4.8,
    "totalReviews": 24
  }
  ```
- **Error Responses:** `400 Bad Request` (Validation Failed), `403 Forbidden`

### 3.2 Update Product
- **Method:** `PUT`
- **Path:** `/api/products/{productId}`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Request Body:** [`ProductRequest`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductRequest.java)

  **Example Request Body:**
  ```json
  {
    "sku": "CAKE-CHOCO-001",
    "name": "Chocolate Fudge Cake",
    "description": "Rich dark chocolate layer cake",
    "shortDescription": "Rich chocolate cake",
    "categoryId": "64f1a2b3c4d5e6f7a8b9c0d1",
    "price": 25.99,
    "discountPrice": 22.99,
    "costPrice": 12.00,
    "taxClass": "STANDARD",
    "taxRate": 5.0,
    "metaTitle": "Buy Chocolate Fudge Cake",
    "metaDescription": "Order fresh chocolate fudge cake online",
    "maxOrderQuantity": 5,
    "status": "ACTIVE",
    "isFeatured": true,
    "preparationTimeMinutes": 60,
    "shelfLifeHours": 72,
    "unit": "piece",
    "calories": "450 kcal",
    "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
    "allergens": ["Dairy", "Eggs", "Gluten"],
    "tags": ["chocolate", "cake", "bestseller"],
    "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
    "initialStock": 50,
    "minimumStock": 5,
    "reorderLevel": 10
  }
  ```
- **Response:** `200 OK` — [`ProductResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductResponse.java)

  **Example Response Body:**
  ```json
  {
    "id": "64f1a2b3c4d5e6f7a8b9c0d2",
    "sku": "CAKE-CHOCO-001",
    "name": "Chocolate Fudge Cake",
    "description": "Rich dark chocolate layer cake",
    "shortDescription": "Rich chocolate cake",
    "category": {
      "id": "64f1a2b3c4d5e6f7a8b9c0d1",
      "name": "Cakes",
      "iconClass": "fa-cake"
    },
    "price": 25.99,
    "discountPrice": 22.99,
    "costPrice": 12.00,
    "effectivePrice": 22.99,
    "taxClass": "STANDARD",
    "taxRate": 5.0,
    "metaTitle": "Buy Chocolate Fudge Cake",
    "metaDescription": "Order fresh chocolate fudge cake online",
    "maxOrderQuantity": 5,
    "status": "ACTIVE",
    "isFeatured": true,
    "isActive": true,
    "preparationTimeMinutes": 60,
    "shelfLifeHours": 72,
    "unit": "piece",
    "calories": "450 kcal",
    "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
    "allergens": ["Dairy", "Eggs", "Gluten"],
    "tags": ["chocolate", "cake", "bestseller"],
    "inventory": {
      "currentStock": 45,
      "availableStock": 40,
      "isLowStock": false,
      "isOutOfStock": false,
      "status": "IN_STOCK"
    },
    "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
    "primaryImageUrl": "https://cdn.example.com/choco-cake.jpg",
    "isAvailable": true,
    "isOnSale": true,
    "createdAt": "2026-01-15T10:30:00",
    "updatedAt": "2026-02-01T14:20:00",
    "averageRating": 4.8,
    "totalReviews": 24
  }
  ```
- **Error Responses:** `400 Bad Request`, `403 Forbidden`, `404 Not Found`

### 3.3 Update Product Status
- **Method:** `PATCH`
- **Path:** `/api/products/{productId}/status`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Request Body:**
  ```json
  {
    "status": "ACTIVE"
  }
  ```
  *(Status options: `ACTIVE`, `INACTIVE`, `DISCONTINUED`, `OUT_OF_STOCK`)*
- **Response:** `200 OK` — [`ProductResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductResponse.java)

  **Example Response Body:**
  ```json
  {
    "id": "64f1a2b3c4d5e6f7a8b9c0d2",
    "sku": "CAKE-CHOCO-001",
    "name": "Chocolate Fudge Cake",
    "description": "Rich dark chocolate layer cake",
    "shortDescription": "Rich chocolate cake",
    "category": {
      "id": "64f1a2b3c4d5e6f7a8b9c0d1",
      "name": "Cakes",
      "iconClass": "fa-cake"
    },
    "price": 25.99,
    "discountPrice": 22.99,
    "costPrice": 12.00,
    "effectivePrice": 22.99,
    "taxClass": "STANDARD",
    "taxRate": 5.0,
    "metaTitle": "Buy Chocolate Fudge Cake",
    "metaDescription": "Order fresh chocolate fudge cake online",
    "maxOrderQuantity": 5,
    "status": "ACTIVE",
    "isFeatured": true,
    "isActive": true,
    "preparationTimeMinutes": 60,
    "shelfLifeHours": 72,
    "unit": "piece",
    "calories": "450 kcal",
    "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
    "allergens": ["Dairy", "Eggs", "Gluten"],
    "tags": ["chocolate", "cake", "bestseller"],
    "inventory": {
      "currentStock": 45,
      "availableStock": 40,
      "isLowStock": false,
      "isOutOfStock": false,
      "status": "IN_STOCK"
    },
    "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
    "primaryImageUrl": "https://cdn.example.com/choco-cake.jpg",
    "isAvailable": true,
    "isOnSale": true,
    "createdAt": "2026-01-15T10:30:00",
    "updatedAt": "2026-02-01T14:20:00",
    "averageRating": 4.8,
    "totalReviews": 24
  }
  ```
- **Error Responses:** `400 Bad Request`, `403 Forbidden`, `404 Not Found`

### 3.4 Toggle Featured Product Status
- **Method:** `POST`
- **Path:** `/api/products/{productId}/toggle-featured`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Response:** `200 OK` — [`ProductResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductResponse.java)

  **Example Response Body:**
  ```json
  {
    "id": "64f1a2b3c4d5e6f7a8b9c0d2",
    "sku": "CAKE-CHOCO-001",
    "name": "Chocolate Fudge Cake",
    "description": "Rich dark chocolate layer cake",
    "shortDescription": "Rich chocolate cake",
    "category": {
      "id": "64f1a2b3c4d5e6f7a8b9c0d1",
      "name": "Cakes",
      "iconClass": "fa-cake"
    },
    "price": 25.99,
    "discountPrice": 22.99,
    "costPrice": 12.00,
    "effectivePrice": 22.99,
    "taxClass": "STANDARD",
    "taxRate": 5.0,
    "metaTitle": "Buy Chocolate Fudge Cake",
    "metaDescription": "Order fresh chocolate fudge cake online",
    "maxOrderQuantity": 5,
    "status": "ACTIVE",
    "isFeatured": true,
    "isActive": true,
    "preparationTimeMinutes": 60,
    "shelfLifeHours": 72,
    "unit": "piece",
    "calories": "450 kcal",
    "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
    "allergens": ["Dairy", "Eggs", "Gluten"],
    "tags": ["chocolate", "cake", "bestseller"],
    "inventory": {
      "currentStock": 45,
      "availableStock": 40,
      "isLowStock": false,
      "isOutOfStock": false,
      "status": "IN_STOCK"
    },
    "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
    "primaryImageUrl": "https://cdn.example.com/choco-cake.jpg",
    "isAvailable": true,
    "isOnSale": true,
    "createdAt": "2026-01-15T10:30:00",
    "updatedAt": "2026-02-01T14:20:00",
    "averageRating": 4.8,
    "totalReviews": 24
  }
  ```
- **Error Responses:** `403 Forbidden`, `404 Not Found`

### 3.5 Delete Product
- **Method:** `DELETE`
- **Path:** `/api/products/{productId}`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Response:** `200 OK` — `MessageResponse`

  **Example Response Body:**
  ```json
  {
    "message": "Operation successful"
  }
  ```
- **Error Responses:** `403 Forbidden`, `404 Not Found`

---

## 4. Product Query API
**Base Path:** `/api/products`  
**Controller:** `ProductQueryController`

### 4.1 Get All Products
- **Method:** `GET`
- **Path:** `/api/products`
- **Access Level:** `Public`
- **Query Parameters:** `page`, `size`, `sortBy` (default: `name`), `sortDir` (default: `ASC`)
- **Response:** `200 OK` — `PagedModel<[`ProductResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "productresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d2",
          "sku": "CAKE-CHOCO-001",
          "name": "Chocolate Fudge Cake",
          "description": "Rich dark chocolate layer cake",
          "shortDescription": "Rich chocolate cake",
          "category": {
            "id": "64f1a2b3c4d5e6f7a8b9c0d1",
            "name": "Cakes",
            "iconClass": "fa-cake"
          },
          "price": 25.99,
          "discountPrice": 22.99,
          "costPrice": 12.00,
          "effectivePrice": 22.99,
          "taxClass": "STANDARD",
          "taxRate": 5.0,
          "metaTitle": "Buy Chocolate Fudge Cake",
          "metaDescription": "Order fresh chocolate fudge cake online",
          "maxOrderQuantity": 5,
          "status": "ACTIVE",
          "isFeatured": true,
          "isActive": true,
          "preparationTimeMinutes": 60,
          "shelfLifeHours": 72,
          "unit": "piece",
          "calories": "450 kcal",
          "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
          "allergens": ["Dairy", "Eggs", "Gluten"],
          "tags": ["chocolate", "cake", "bestseller"],
          "inventory": {
            "currentStock": 45,
            "availableStock": 40,
            "isLowStock": false,
            "isOutOfStock": false,
            "status": "IN_STOCK"
          },
          "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
          "primaryImageUrl": "https://cdn.example.com/choco-cake.jpg",
          "isAvailable": true,
          "isOnSale": true,
          "createdAt": "2026-01-15T10:30:00",
          "updatedAt": "2026-02-01T14:20:00",
          "averageRating": 4.8,
          "totalReviews": 24
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```

### 4.2 Get Active Products
- **Method:** `GET`
- **Path:** `/api/products/active`
- **Access Level:** `Public`
- **Query Parameters:** `page`, `size`, `sortBy`, `sortDir`
- **Response:** `200 OK` — `PagedModel<[`ProductResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "productresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d2",
          "sku": "CAKE-CHOCO-001",
          "name": "Chocolate Fudge Cake",
          "description": "Rich dark chocolate layer cake",
          "shortDescription": "Rich chocolate cake",
          "category": {
            "id": "64f1a2b3c4d5e6f7a8b9c0d1",
            "name": "Cakes",
            "iconClass": "fa-cake"
          },
          "price": 25.99,
          "discountPrice": 22.99,
          "costPrice": 12.00,
          "effectivePrice": 22.99,
          "taxClass": "STANDARD",
          "taxRate": 5.0,
          "metaTitle": "Buy Chocolate Fudge Cake",
          "metaDescription": "Order fresh chocolate fudge cake online",
          "maxOrderQuantity": 5,
          "status": "ACTIVE",
          "isFeatured": true,
          "isActive": true,
          "preparationTimeMinutes": 60,
          "shelfLifeHours": 72,
          "unit": "piece",
          "calories": "450 kcal",
          "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
          "allergens": ["Dairy", "Eggs", "Gluten"],
          "tags": ["chocolate", "cake", "bestseller"],
          "inventory": {
            "currentStock": 45,
            "availableStock": 40,
            "isLowStock": false,
            "isOutOfStock": false,
            "status": "IN_STOCK"
          },
          "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
          "primaryImageUrl": "https://cdn.example.com/choco-cake.jpg",
          "isAvailable": true,
          "isOnSale": true,
          "createdAt": "2026-01-15T10:30:00",
          "updatedAt": "2026-02-01T14:20:00",
          "averageRating": 4.8,
          "totalReviews": 24
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```

### 4.3 Get Available Products
- **Method:** `GET`
- **Path:** `/api/products/available`
- **Access Level:** `Public`
- **Query Parameters:** `page`, `size`, `sortBy`, `sortDir`
- **Response:** `200 OK` — `PagedModel<[`ProductResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "productresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d2",
          "sku": "CAKE-CHOCO-001",
          "name": "Chocolate Fudge Cake",
          "description": "Rich dark chocolate layer cake",
          "shortDescription": "Rich chocolate cake",
          "category": {
            "id": "64f1a2b3c4d5e6f7a8b9c0d1",
            "name": "Cakes",
            "iconClass": "fa-cake"
          },
          "price": 25.99,
          "discountPrice": 22.99,
          "costPrice": 12.00,
          "effectivePrice": 22.99,
          "taxClass": "STANDARD",
          "taxRate": 5.0,
          "metaTitle": "Buy Chocolate Fudge Cake",
          "metaDescription": "Order fresh chocolate fudge cake online",
          "maxOrderQuantity": 5,
          "status": "ACTIVE",
          "isFeatured": true,
          "isActive": true,
          "preparationTimeMinutes": 60,
          "shelfLifeHours": 72,
          "unit": "piece",
          "calories": "450 kcal",
          "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
          "allergens": ["Dairy", "Eggs", "Gluten"],
          "tags": ["chocolate", "cake", "bestseller"],
          "inventory": {
            "currentStock": 45,
            "availableStock": 40,
            "isLowStock": false,
            "isOutOfStock": false,
            "status": "IN_STOCK"
          },
          "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
          "primaryImageUrl": "https://cdn.example.com/choco-cake.jpg",
          "isAvailable": true,
          "isOnSale": true,
          "createdAt": "2026-01-15T10:30:00",
          "updatedAt": "2026-02-01T14:20:00",
          "averageRating": 4.8,
          "totalReviews": 24
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```

### 4.4 Get Featured Products
- **Method:** `GET`
- **Path:** `/api/products/featured`
- **Access Level:** `Public`
- **Query Parameters:** `page`, `size`, `sortBy` (default: `createdAt`), `sortDir` (default: `DESC`)
- **Response:** `200 OK` — `PagedModel<[`ProductResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "productresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d2",
          "sku": "CAKE-CHOCO-001",
          "name": "Chocolate Fudge Cake",
          "description": "Rich dark chocolate layer cake",
          "shortDescription": "Rich chocolate cake",
          "category": {
            "id": "64f1a2b3c4d5e6f7a8b9c0d1",
            "name": "Cakes",
            "iconClass": "fa-cake"
          },
          "price": 25.99,
          "discountPrice": 22.99,
          "costPrice": 12.00,
          "effectivePrice": 22.99,
          "taxClass": "STANDARD",
          "taxRate": 5.0,
          "metaTitle": "Buy Chocolate Fudge Cake",
          "metaDescription": "Order fresh chocolate fudge cake online",
          "maxOrderQuantity": 5,
          "status": "ACTIVE",
          "isFeatured": true,
          "isActive": true,
          "preparationTimeMinutes": 60,
          "shelfLifeHours": 72,
          "unit": "piece",
          "calories": "450 kcal",
          "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
          "allergens": ["Dairy", "Eggs", "Gluten"],
          "tags": ["chocolate", "cake", "bestseller"],
          "inventory": {
            "currentStock": 45,
            "availableStock": 40,
            "isLowStock": false,
            "isOutOfStock": false,
            "status": "IN_STOCK"
          },
          "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
          "primaryImageUrl": "https://cdn.example.com/choco-cake.jpg",
          "isAvailable": true,
          "isOnSale": true,
          "createdAt": "2026-01-15T10:30:00",
          "updatedAt": "2026-02-01T14:20:00",
          "averageRating": 4.8,
          "totalReviews": 24
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```

### 4.5 Get Products On Sale
- **Method:** `GET`
- **Path:** `/api/products/on-sale`
- **Access Level:** `Public`
- **Query Parameters:** `page`, `size`, `sortBy`, `sortDir`
- **Response:** `200 OK` — `PagedModel<[`ProductResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "productresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d2",
          "sku": "CAKE-CHOCO-001",
          "name": "Chocolate Fudge Cake",
          "description": "Rich dark chocolate layer cake",
          "shortDescription": "Rich chocolate cake",
          "category": {
            "id": "64f1a2b3c4d5e6f7a8b9c0d1",
            "name": "Cakes",
            "iconClass": "fa-cake"
          },
          "price": 25.99,
          "discountPrice": 22.99,
          "costPrice": 12.00,
          "effectivePrice": 22.99,
          "taxClass": "STANDARD",
          "taxRate": 5.0,
          "metaTitle": "Buy Chocolate Fudge Cake",
          "metaDescription": "Order fresh chocolate fudge cake online",
          "maxOrderQuantity": 5,
          "status": "ACTIVE",
          "isFeatured": true,
          "isActive": true,
          "preparationTimeMinutes": 60,
          "shelfLifeHours": 72,
          "unit": "piece",
          "calories": "450 kcal",
          "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
          "allergens": ["Dairy", "Eggs", "Gluten"],
          "tags": ["chocolate", "cake", "bestseller"],
          "inventory": {
            "currentStock": 45,
            "availableStock": 40,
            "isLowStock": false,
            "isOutOfStock": false,
            "status": "IN_STOCK"
          },
          "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
          "primaryImageUrl": "https://cdn.example.com/choco-cake.jpg",
          "isAvailable": true,
          "isOnSale": true,
          "createdAt": "2026-01-15T10:30:00",
          "updatedAt": "2026-02-01T14:20:00",
          "averageRating": 4.8,
          "totalReviews": 24
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```

### 4.6 Get Recently Added Products
- **Method:** `GET`
- **Path:** `/api/products/recent`
- **Access Level:** `Public`
- **Query Parameters:** `days` (default: `7`), `page`, `size`, `sortBy`, `sortDir`
- **Response:** `200 OK` — `PagedModel<[`ProductResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "productresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d2",
          "sku": "CAKE-CHOCO-001",
          "name": "Chocolate Fudge Cake",
          "description": "Rich dark chocolate layer cake",
          "shortDescription": "Rich chocolate cake",
          "category": {
            "id": "64f1a2b3c4d5e6f7a8b9c0d1",
            "name": "Cakes",
            "iconClass": "fa-cake"
          },
          "price": 25.99,
          "discountPrice": 22.99,
          "costPrice": 12.00,
          "effectivePrice": 22.99,
          "taxClass": "STANDARD",
          "taxRate": 5.0,
          "metaTitle": "Buy Chocolate Fudge Cake",
          "metaDescription": "Order fresh chocolate fudge cake online",
          "maxOrderQuantity": 5,
          "status": "ACTIVE",
          "isFeatured": true,
          "isActive": true,
          "preparationTimeMinutes": 60,
          "shelfLifeHours": 72,
          "unit": "piece",
          "calories": "450 kcal",
          "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
          "allergens": ["Dairy", "Eggs", "Gluten"],
          "tags": ["chocolate", "cake", "bestseller"],
          "inventory": {
            "currentStock": 45,
            "availableStock": 40,
            "isLowStock": false,
            "isOutOfStock": false,
            "status": "IN_STOCK"
          },
          "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
          "primaryImageUrl": "https://cdn.example.com/choco-cake.jpg",
          "isAvailable": true,
          "isOnSale": true,
          "createdAt": "2026-01-15T10:30:00",
          "updatedAt": "2026-02-01T14:20:00",
          "averageRating": 4.8,
          "totalReviews": 24
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```

### 4.7 Get Product by ID
- **Method:** `GET`
- **Path:** `/api/products/{productId}`
- **Access Level:** `Public`
- **Response:** `200 OK` — [`ProductResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductResponse.java)

  **Example Response Body:**
  ```json
  {
    "id": "64f1a2b3c4d5e6f7a8b9c0d2",
    "sku": "CAKE-CHOCO-001",
    "name": "Chocolate Fudge Cake",
    "description": "Rich dark chocolate layer cake",
    "shortDescription": "Rich chocolate cake",
    "category": {
      "id": "64f1a2b3c4d5e6f7a8b9c0d1",
      "name": "Cakes",
      "iconClass": "fa-cake"
    },
    "price": 25.99,
    "discountPrice": 22.99,
    "costPrice": 12.00,
    "effectivePrice": 22.99,
    "taxClass": "STANDARD",
    "taxRate": 5.0,
    "metaTitle": "Buy Chocolate Fudge Cake",
    "metaDescription": "Order fresh chocolate fudge cake online",
    "maxOrderQuantity": 5,
    "status": "ACTIVE",
    "isFeatured": true,
    "isActive": true,
    "preparationTimeMinutes": 60,
    "shelfLifeHours": 72,
    "unit": "piece",
    "calories": "450 kcal",
    "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
    "allergens": ["Dairy", "Eggs", "Gluten"],
    "tags": ["chocolate", "cake", "bestseller"],
    "inventory": {
      "currentStock": 45,
      "availableStock": 40,
      "isLowStock": false,
      "isOutOfStock": false,
      "status": "IN_STOCK"
    },
    "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
    "primaryImageUrl": "https://cdn.example.com/choco-cake.jpg",
    "isAvailable": true,
    "isOnSale": true,
    "createdAt": "2026-01-15T10:30:00",
    "updatedAt": "2026-02-01T14:20:00",
    "averageRating": 4.8,
    "totalReviews": 24
  }
  ```
- **Error Responses:** `404 Not Found`

### 4.8 Get Products by Batch of IDs
- **Method:** `POST`
- **Path:** `/api/products/batch`
- **Access Level:** `Public`
- **Query Parameters:** `page`, `size`, `sortBy`, `sortDir`
- **Request Body:** `List<String>` (array of product IDs)

  **Example Request Body:**
  ```json
  [
    "id1",
    "id2"
  ]
  ```
- **Response:** `200 OK` — `PagedModel<[`ProductResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "productresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d2",
          "sku": "CAKE-CHOCO-001",
          "name": "Chocolate Fudge Cake",
          "description": "Rich dark chocolate layer cake",
          "shortDescription": "Rich chocolate cake",
          "category": {
            "id": "64f1a2b3c4d5e6f7a8b9c0d1",
            "name": "Cakes",
            "iconClass": "fa-cake"
          },
          "price": 25.99,
          "discountPrice": 22.99,
          "costPrice": 12.00,
          "effectivePrice": 22.99,
          "taxClass": "STANDARD",
          "taxRate": 5.0,
          "metaTitle": "Buy Chocolate Fudge Cake",
          "metaDescription": "Order fresh chocolate fudge cake online",
          "maxOrderQuantity": 5,
          "status": "ACTIVE",
          "isFeatured": true,
          "isActive": true,
          "preparationTimeMinutes": 60,
          "shelfLifeHours": 72,
          "unit": "piece",
          "calories": "450 kcal",
          "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
          "allergens": ["Dairy", "Eggs", "Gluten"],
          "tags": ["chocolate", "cake", "bestseller"],
          "inventory": {
            "currentStock": 45,
            "availableStock": 40,
            "isLowStock": false,
            "isOutOfStock": false,
            "status": "IN_STOCK"
          },
          "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
          "primaryImageUrl": "https://cdn.example.com/choco-cake.jpg",
          "isAvailable": true,
          "isOnSale": true,
          "createdAt": "2026-01-15T10:30:00",
          "updatedAt": "2026-02-01T14:20:00",
          "averageRating": 4.8,
          "totalReviews": 24
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```

### 4.9 Get Product by SKU
- **Method:** `GET`
- **Path:** `/api/products/sku/{sku}`
- **Access Level:** `Public`
- **Response:** `200 OK` — [`ProductResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductResponse.java)

  **Example Response Body:**
  ```json
  {
    "id": "64f1a2b3c4d5e6f7a8b9c0d2",
    "sku": "CAKE-CHOCO-001",
    "name": "Chocolate Fudge Cake",
    "description": "Rich dark chocolate layer cake",
    "shortDescription": "Rich chocolate cake",
    "category": {
      "id": "64f1a2b3c4d5e6f7a8b9c0d1",
      "name": "Cakes",
      "iconClass": "fa-cake"
    },
    "price": 25.99,
    "discountPrice": 22.99,
    "costPrice": 12.00,
    "effectivePrice": 22.99,
    "taxClass": "STANDARD",
    "taxRate": 5.0,
    "metaTitle": "Buy Chocolate Fudge Cake",
    "metaDescription": "Order fresh chocolate fudge cake online",
    "maxOrderQuantity": 5,
    "status": "ACTIVE",
    "isFeatured": true,
    "isActive": true,
    "preparationTimeMinutes": 60,
    "shelfLifeHours": 72,
    "unit": "piece",
    "calories": "450 kcal",
    "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
    "allergens": ["Dairy", "Eggs", "Gluten"],
    "tags": ["chocolate", "cake", "bestseller"],
    "inventory": {
      "currentStock": 45,
      "availableStock": 40,
      "isLowStock": false,
      "isOutOfStock": false,
      "status": "IN_STOCK"
    },
    "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
    "primaryImageUrl": "https://cdn.example.com/choco-cake.jpg",
    "isAvailable": true,
    "isOnSale": true,
    "createdAt": "2026-01-15T10:30:00",
    "updatedAt": "2026-02-01T14:20:00",
    "averageRating": 4.8,
    "totalReviews": 24
  }
  ```
- **Error Responses:** `404 Not Found`

### 4.10 Get Products by Category
- **Method:** `GET`
- **Path:** `/api/products/category/{categoryId}`
- **Access Level:** `Public`
- **Query Parameters:** `page`, `size`, `sortBy`, `sortDir`
- **Response:** `200 OK` — `PagedModel<[`ProductResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "productresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d2",
          "sku": "CAKE-CHOCO-001",
          "name": "Chocolate Fudge Cake",
          "description": "Rich dark chocolate layer cake",
          "shortDescription": "Rich chocolate cake",
          "category": {
            "id": "64f1a2b3c4d5e6f7a8b9c0d1",
            "name": "Cakes",
            "iconClass": "fa-cake"
          },
          "price": 25.99,
          "discountPrice": 22.99,
          "costPrice": 12.00,
          "effectivePrice": 22.99,
          "taxClass": "STANDARD",
          "taxRate": 5.0,
          "metaTitle": "Buy Chocolate Fudge Cake",
          "metaDescription": "Order fresh chocolate fudge cake online",
          "maxOrderQuantity": 5,
          "status": "ACTIVE",
          "isFeatured": true,
          "isActive": true,
          "preparationTimeMinutes": 60,
          "shelfLifeHours": 72,
          "unit": "piece",
          "calories": "450 kcal",
          "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
          "allergens": ["Dairy", "Eggs", "Gluten"],
          "tags": ["chocolate", "cake", "bestseller"],
          "inventory": {
            "currentStock": 45,
            "availableStock": 40,
            "isLowStock": false,
            "isOutOfStock": false,
            "status": "IN_STOCK"
          },
          "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
          "primaryImageUrl": "https://cdn.example.com/choco-cake.jpg",
          "isAvailable": true,
          "isOnSale": true,
          "createdAt": "2026-01-15T10:30:00",
          "updatedAt": "2026-02-01T14:20:00",
          "averageRating": 4.8,
          "totalReviews": 24
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```

### 4.11 Search Products
- **Method:** `GET`
- **Path:** `/api/products/search`
- **Access Level:** `Public`
- **Query Parameters:** `query` (required), `page`, `size`, `sortBy`, `sortDir`
- **Response:** `200 OK` — `PagedModel<[`ProductResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "productresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d2",
          "sku": "CAKE-CHOCO-001",
          "name": "Chocolate Fudge Cake",
          "description": "Rich dark chocolate layer cake",
          "shortDescription": "Rich chocolate cake",
          "category": {
            "id": "64f1a2b3c4d5e6f7a8b9c0d1",
            "name": "Cakes",
            "iconClass": "fa-cake"
          },
          "price": 25.99,
          "discountPrice": 22.99,
          "costPrice": 12.00,
          "effectivePrice": 22.99,
          "taxClass": "STANDARD",
          "taxRate": 5.0,
          "metaTitle": "Buy Chocolate Fudge Cake",
          "metaDescription": "Order fresh chocolate fudge cake online",
          "maxOrderQuantity": 5,
          "status": "ACTIVE",
          "isFeatured": true,
          "isActive": true,
          "preparationTimeMinutes": 60,
          "shelfLifeHours": 72,
          "unit": "piece",
          "calories": "450 kcal",
          "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
          "allergens": ["Dairy", "Eggs", "Gluten"],
          "tags": ["chocolate", "cake", "bestseller"],
          "inventory": {
            "currentStock": 45,
            "availableStock": 40,
            "isLowStock": false,
            "isOutOfStock": false,
            "status": "IN_STOCK"
          },
          "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
          "primaryImageUrl": "https://cdn.example.com/choco-cake.jpg",
          "isAvailable": true,
          "isOnSale": true,
          "createdAt": "2026-01-15T10:30:00",
          "updatedAt": "2026-02-01T14:20:00",
          "averageRating": 4.8,
          "totalReviews": 24
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```

### 4.12 Get Products by Price Range
- **Method:** `GET`
- **Path:** `/api/products/price-range`
- **Access Level:** `Public`
- **Query Parameters:** `minPrice` (required), `maxPrice` (required), `page`, `size`, `sortBy` (default: `price`), `sortDir`
- **Response:** `200 OK` — `PagedModel<[`ProductResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "productresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d2",
          "sku": "CAKE-CHOCO-001",
          "name": "Chocolate Fudge Cake",
          "description": "Rich dark chocolate layer cake",
          "shortDescription": "Rich chocolate cake",
          "category": {
            "id": "64f1a2b3c4d5e6f7a8b9c0d1",
            "name": "Cakes",
            "iconClass": "fa-cake"
          },
          "price": 25.99,
          "discountPrice": 22.99,
          "costPrice": 12.00,
          "effectivePrice": 22.99,
          "taxClass": "STANDARD",
          "taxRate": 5.0,
          "metaTitle": "Buy Chocolate Fudge Cake",
          "metaDescription": "Order fresh chocolate fudge cake online",
          "maxOrderQuantity": 5,
          "status": "ACTIVE",
          "isFeatured": true,
          "isActive": true,
          "preparationTimeMinutes": 60,
          "shelfLifeHours": 72,
          "unit": "piece",
          "calories": "450 kcal",
          "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
          "allergens": ["Dairy", "Eggs", "Gluten"],
          "tags": ["chocolate", "cake", "bestseller"],
          "inventory": {
            "currentStock": 45,
            "availableStock": 40,
            "isLowStock": false,
            "isOutOfStock": false,
            "status": "IN_STOCK"
          },
          "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
          "primaryImageUrl": "https://cdn.example.com/choco-cake.jpg",
          "isAvailable": true,
          "isOnSale": true,
          "createdAt": "2026-01-15T10:30:00",
          "updatedAt": "2026-02-01T14:20:00",
          "averageRating": 4.8,
          "totalReviews": 24
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```
- **Error Responses:** `400 Bad Request`

### 4.13 Get Products by Tag
- **Method:** `GET`
- **Path:** `/api/products/tag/{tag}`
- **Access Level:** `Public`
- **Query Parameters:** `page`, `size`, `sortBy`, `sortDir`
- **Response:** `200 OK` — `PagedModel<[`ProductResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "productresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d2",
          "sku": "CAKE-CHOCO-001",
          "name": "Chocolate Fudge Cake",
          "description": "Rich dark chocolate layer cake",
          "shortDescription": "Rich chocolate cake",
          "category": {
            "id": "64f1a2b3c4d5e6f7a8b9c0d1",
            "name": "Cakes",
            "iconClass": "fa-cake"
          },
          "price": 25.99,
          "discountPrice": 22.99,
          "costPrice": 12.00,
          "effectivePrice": 22.99,
          "taxClass": "STANDARD",
          "taxRate": 5.0,
          "metaTitle": "Buy Chocolate Fudge Cake",
          "metaDescription": "Order fresh chocolate fudge cake online",
          "maxOrderQuantity": 5,
          "status": "ACTIVE",
          "isFeatured": true,
          "isActive": true,
          "preparationTimeMinutes": 60,
          "shelfLifeHours": 72,
          "unit": "piece",
          "calories": "450 kcal",
          "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
          "allergens": ["Dairy", "Eggs", "Gluten"],
          "tags": ["chocolate", "cake", "bestseller"],
          "inventory": {
            "currentStock": 45,
            "availableStock": 40,
            "isLowStock": false,
            "isOutOfStock": false,
            "status": "IN_STOCK"
          },
          "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
          "primaryImageUrl": "https://cdn.example.com/choco-cake.jpg",
          "isAvailable": true,
          "isOnSale": true,
          "createdAt": "2026-01-15T10:30:00",
          "updatedAt": "2026-02-01T14:20:00",
          "averageRating": 4.8,
          "totalReviews": 24
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```

### 4.14 Get Products Without Allergen
- **Method:** `GET`
- **Path:** `/api/products/without-allergen/{allergen}`
- **Access Level:** `Public`
- **Query Parameters:** `page`, `size`, `sortBy`, `sortDir`
- **Response:** `200 OK` — `PagedModel<[`ProductResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "productresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d2",
          "sku": "CAKE-CHOCO-001",
          "name": "Chocolate Fudge Cake",
          "description": "Rich dark chocolate layer cake",
          "shortDescription": "Rich chocolate cake",
          "category": {
            "id": "64f1a2b3c4d5e6f7a8b9c0d1",
            "name": "Cakes",
            "iconClass": "fa-cake"
          },
          "price": 25.99,
          "discountPrice": 22.99,
          "costPrice": 12.00,
          "effectivePrice": 22.99,
          "taxClass": "STANDARD",
          "taxRate": 5.0,
          "metaTitle": "Buy Chocolate Fudge Cake",
          "metaDescription": "Order fresh chocolate fudge cake online",
          "maxOrderQuantity": 5,
          "status": "ACTIVE",
          "isFeatured": true,
          "isActive": true,
          "preparationTimeMinutes": 60,
          "shelfLifeHours": 72,
          "unit": "piece",
          "calories": "450 kcal",
          "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
          "allergens": ["Dairy", "Eggs", "Gluten"],
          "tags": ["chocolate", "cake", "bestseller"],
          "inventory": {
            "currentStock": 45,
            "availableStock": 40,
            "isLowStock": false,
            "isOutOfStock": false,
            "status": "IN_STOCK"
          },
          "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
          "primaryImageUrl": "https://cdn.example.com/choco-cake.jpg",
          "isAvailable": true,
          "isOnSale": true,
          "createdAt": "2026-01-15T10:30:00",
          "updatedAt": "2026-02-01T14:20:00",
          "averageRating": 4.8,
          "totalReviews": 24
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```

---

## 5. Inventory API
**Base Path:** `/api/inventory`  
**Controller:** `InventoryController`

### 5.1 Get All Inventory Items
- **Method:** `GET`
- **Path:** `/api/inventory`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Query Parameters:** `page`, `size`, `sortBy` (default: `id`), `sortDir`
- **Response:** `200 OK` — `PagedModel<[`InventoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/InventoryResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "inventoryresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d3",
          "productId": "64f1a2b3c4d5e6f7a8b9c0d2",
          "productName": "Chocolate Fudge Cake",
          "currentStock": 45,
          "reservedStock": 5,
          "availableStock": 40,
          "minimumStock": 5,
          "status": "IN_STOCK"
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```
- **Error Responses:** `403 Forbidden`

### 5.2 Admin Search Inventory
- **Method:** `GET`
- **Path:** `/api/inventory/admin/search`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Query Parameters:** `query` (required), `page`, `size`, `sortBy`, `sortDir`
- **Response:** `200 OK` — `PagedModel<[`InventoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/InventoryResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "inventoryresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d3",
          "productId": "64f1a2b3c4d5e6f7a8b9c0d2",
          "productName": "Chocolate Fudge Cake",
          "currentStock": 45,
          "reservedStock": 5,
          "availableStock": 40,
          "minimumStock": 5,
          "status": "IN_STOCK"
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```
- **Error Responses:** `403 Forbidden`

### 5.3 Get Inventory by Product ID
- **Method:** `GET`
- **Path:** `/api/inventory/product/{productId}`
- **Access Level:** `Public`
- **Response:** `200 OK` — [`InventoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/InventoryResponse.java)

  **Example Response Body:**
  ```json
  {
    "id": "64f1a2b3c4d5e6f7a8b9c0d3",
    "productId": "64f1a2b3c4d5e6f7a8b9c0d2",
    "productName": "Chocolate Fudge Cake",
    "currentStock": 45,
    "reservedStock": 5,
    "availableStock": 40,
    "minimumStock": 5,
    "status": "IN_STOCK"
  }
  ```
- **Error Responses:** `404 Not Found`

### 5.4 Get Inventory by Product SKU
- **Method:** `GET`
- **Path:** `/api/inventory/sku/{sku}`
- **Access Level:** `Public`
- **Response:** `200 OK` — [`InventoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/InventoryResponse.java)

  **Example Response Body:**
  ```json
  {
    "id": "64f1a2b3c4d5e6f7a8b9c0d3",
    "productId": "64f1a2b3c4d5e6f7a8b9c0d2",
    "productName": "Chocolate Fudge Cake",
    "currentStock": 45,
    "reservedStock": 5,
    "availableStock": 40,
    "minimumStock": 5,
    "status": "IN_STOCK"
  }
  ```
- **Error Responses:** `404 Not Found`

### 5.5 Get Low Stock Items
- **Method:** `GET`
- **Path:** `/api/inventory/low-stock`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Query Parameters:** `page`, `size`, `sortBy`, `sortDir`
- **Response:** `200 OK` — `PagedModel<[`InventoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/InventoryResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "inventoryresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d3",
          "productId": "64f1a2b3c4d5e6f7a8b9c0d2",
          "productName": "Chocolate Fudge Cake",
          "currentStock": 45,
          "reservedStock": 5,
          "availableStock": 40,
          "minimumStock": 5,
          "status": "IN_STOCK"
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```
- **Error Responses:** `403 Forbidden`

### 5.6 Get Out of Stock Items
- **Method:** `GET`
- **Path:** `/api/inventory/out-of-stock`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Query Parameters:** `page`, `size`, `sortBy`, `sortDir`
- **Response:** `200 OK` — `PagedModel<[`InventoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/InventoryResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "inventoryresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d3",
          "productId": "64f1a2b3c4d5e6f7a8b9c0d2",
          "productName": "Chocolate Fudge Cake",
          "currentStock": 45,
          "reservedStock": 5,
          "availableStock": 40,
          "minimumStock": 5,
          "status": "IN_STOCK"
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```
- **Error Responses:** `403 Forbidden`

### 5.7 Get Items Needing Reorder
- **Method:** `GET`
- **Path:** `/api/inventory/needs-reorder`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Query Parameters:** `page`, `size`, `sortBy`, `sortDir`
- **Response:** `200 OK` — `PagedModel<[`InventoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/InventoryResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "inventoryresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d3",
          "productId": "64f1a2b3c4d5e6f7a8b9c0d2",
          "productName": "Chocolate Fudge Cake",
          "currentStock": 45,
          "reservedStock": 5,
          "availableStock": 40,
          "minimumStock": 5,
          "status": "IN_STOCK"
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```
- **Error Responses:** `403 Forbidden`

### 5.8 Get Expired Items
- **Method:** `GET`
- **Path:** `/api/inventory/expired`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Query Parameters:** `page`, `size`, `sortBy`, `sortDir`
- **Response:** `200 OK` — `PagedModel<[`InventoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/InventoryResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "inventoryresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d3",
          "productId": "64f1a2b3c4d5e6f7a8b9c0d2",
          "productName": "Chocolate Fudge Cake",
          "currentStock": 45,
          "reservedStock": 5,
          "availableStock": 40,
          "minimumStock": 5,
          "status": "IN_STOCK"
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```
- **Error Responses:** `403 Forbidden`

### 5.9 Get Items Expiring Soon
- **Method:** `GET`
- **Path:** `/api/inventory/expiring-soon`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Query Parameters:** `hours` (default: `24`), `page`, `size`, `sortBy`, `sortDir`
- **Response:** `200 OK` — `PagedModel<[`InventoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/InventoryResponse.java)>`

  **Example Response Body:**
  ```json
  {
    "_embedded": {
      "inventoryresponseList": [
        {
          "id": "64f1a2b3c4d5e6f7a8b9c0d3",
          "productId": "64f1a2b3c4d5e6f7a8b9c0d2",
          "productName": "Chocolate Fudge Cake",
          "currentStock": 45,
          "reservedStock": 5,
          "availableStock": 40,
          "minimumStock": 5,
          "status": "IN_STOCK"
        }
      ]
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
  }
  ```
- **Error Responses:** `403 Forbidden`

### 5.10 Update Inventory
- **Method:** `PUT`
- **Path:** `/api/inventory/product/{productId}`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Request Body:** [`InventoryUpdateRequest`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/InventoryUpdateRequest.java)

  **Example Request Body:**
  ```json
  {
    "currentStock": 50,
    "reservedStock": 5,
    "minimumStock": 10,
    "maximumStock": 100,
    "reorderLevel": 15,
    "reorderQuantity": 50,
    "autoReorderEnabled": true,
    "trackExpiry": true,
    "expiryDate": "2026-12-31T23:59:59",
    "supplierInfo": "Supplier A",
    "storageLocation": "Aisle 4, Shelf 2",
    "notes": "Regular update"
  }
  ```
- **Response:** `200 OK` — [`InventoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/InventoryResponse.java)

  **Example Response Body:**
  ```json
  {
    "id": "64f1a2b3c4d5e6f7a8b9c0d3",
    "productId": "64f1a2b3c4d5e6f7a8b9c0d2",
    "productName": "Chocolate Fudge Cake",
    "currentStock": 45,
    "reservedStock": 5,
    "availableStock": 40,
    "minimumStock": 5,
    "status": "IN_STOCK"
  }
  ```
- **Error Responses:** `400 Bad Request`, `403 Forbidden`, `404 Not Found`

### 5.11 Add Stock (Restock)
- **Method:** `POST`
- **Path:** `/api/inventory/product/{productId}/add-stock`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Request Body:** [`StockAdjustmentRequest`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/StockAdjustmentRequest.java)

  **Example Request Body:**
  ```json
  {
    "quantity": 10,
    "notes": "Restock from bakery kitchen batch #42"
  }
  ```
- **Response:** `200 OK` — [`InventoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/InventoryResponse.java)

  **Example Response Body:**
  ```json
  {
    "id": "64f1a2b3c4d5e6f7a8b9c0d3",
    "productId": "64f1a2b3c4d5e6f7a8b9c0d2",
    "productName": "Chocolate Fudge Cake",
    "currentStock": 45,
    "reservedStock": 5,
    "availableStock": 40,
    "minimumStock": 5,
    "status": "IN_STOCK"
  }
  ```
- **Error Responses:** `400 Bad Request`, `403 Forbidden`, `404 Not Found`

### 5.12 Reserve Stock
- **Method:** `POST`
- **Path:** `/api/inventory/product/{productId}/reserve`
- **Access Level:** `ADMIN` or `SYSTEM` (`@PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")`)
- **Request Body:** [`StockAdjustmentRequest`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/StockAdjustmentRequest.java)

  **Example Request Body:**
  ```json
  {
    "quantity": 10,
    "notes": "Restock from bakery kitchen batch #42"
  }
  ```
- **Response:** `200 OK` — [`StockOperationResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/StockOperationResponse.java)

  **Example Response Body:**
  ```json
  {
    "success": true,
    "productId": "64f1a2b3c4d5e6f7a8b9c0d2",
    "quantity": 5,
    "message": "Stock reserved successfully"
  }
  ```
- **Error Responses:** `400 Bad Request`, `403 Forbidden`, `404 Not Found`, `409 Conflict` (Insufficient Stock)

### 5.13 Release Reserved Stock
- **Method:** `POST`
- **Path:** `/api/inventory/product/{productId}/release-reserved`
- **Access Level:** `ADMIN` or `SYSTEM` (`@PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")`)
- **Request Body:** [`StockAdjustmentRequest`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/StockAdjustmentRequest.java)

  **Example Request Body:**
  ```json
  {
    "quantity": 10,
    "notes": "Restock from bakery kitchen batch #42"
  }
  ```
- **Response:** `200 OK` — [`StockOperationResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/StockOperationResponse.java)

  **Example Response Body:**
  ```json
  {
    "success": true,
    "productId": "64f1a2b3c4d5e6f7a8b9c0d2",
    "quantity": 5,
    "message": "Stock reserved successfully"
  }
  ```
- **Error Responses:** `400 Bad Request`, `403 Forbidden`, `404 Not Found`

### 5.14 Consume Stock
- **Method:** `POST`
- **Path:** `/api/inventory/product/{productId}/consume`
- **Access Level:** `ADMIN` or `SYSTEM` (`@PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")`)
- **Request Body:** [`StockAdjustmentRequest`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/StockAdjustmentRequest.java)

  **Example Request Body:**
  ```json
  {
    "quantity": 10,
    "notes": "Restock from bakery kitchen batch #42"
  }
  ```
- **Response:** `200 OK` — [`StockOperationResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/StockOperationResponse.java)

  **Example Response Body:**
  ```json
  {
    "success": true,
    "productId": "64f1a2b3c4d5e6f7a8b9c0d2",
    "quantity": 5,
    "message": "Stock reserved successfully"
  }
  ```
- **Error Responses:** `400 Bad Request`, `403 Forbidden`, `404 Not Found`, `409 Conflict` (Insufficient Stock)

### 5.15 Check Stock Availability
- **Method:** `GET`
- **Path:** `/api/inventory/product/{productId}/availability`
- **Access Level:** `Public`
- **Query Parameters:** `quantity` (int, required)
- **Response:** `200 OK` — [`StockAvailabilityResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/StockAvailabilityResponse.java)

  **Example Response Body:**
  ```json
  {
    "productId": "64f1a2b3c4d5e6f7a8b9c0d2",
    "requestedQuantity": 2,
    "availableStock": 40,
    "sufficient": true
  }
  ```

### 5.16 Get Available Stock
- **Method:** `GET`
- **Path:** `/api/inventory/product/{productId}/available-stock`
- **Access Level:** `Public`
- **Response:** `200 OK` — [`StockAvailabilityResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/StockAvailabilityResponse.java)

  **Example Response Body:**
  ```json
  {
    "productId": "64f1a2b3c4d5e6f7a8b9c0d2",
    "requestedQuantity": 2,
    "availableStock": 40,
    "sufficient": true
  }
  ```

### 5.17 Bulk Update Minimum Stock
- **Method:** `POST`
- **Path:** `/api/inventory/bulk-update-minimum-stock`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Request Body:** `Map<String, Integer>` (Map of productId -> minimumStock)

  **Example Request Body:**
  ```json
  {
    "id1": 10,
    "id2": 20
  }
  ```
- **Response:** `200 OK` — `MessageResponse`

  **Example Response Body:**
  ```json
  {
    "message": "Operation successful"
  }
  ```
- **Error Responses:** `400 Bad Request`, `403 Forbidden`

### 5.18 Get Inventory Statistics
- **Method:** `GET`
- **Path:** `/api/inventory/statistics`
- **Access Level:** `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`)
- **Response:** `200 OK` — [`InventoryStatisticsResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/InventoryStatisticsResponse.java)

  **Example Response Body:**
  ```json
  {
    "totalItems": 120,
    "inStockItems": 105,
    "lowStockItems": 10,
    "outOfStockItems": 5,
    "totalReservedStock": 25,
    "totalStockValue": 4550.00
  }
  ```
- **Error Responses:** `403 Forbidden`

---

## 6. Storefront API
**Base Path:** `/api/storefront`  
**Controller:** `StorefrontController`

### 6.1 Get Frontpage Storefront Config
- **Method:** `GET`
- **Path:** `/api/storefront/frontpage`
- **Access Level:** `Public`
- **Response:** `200 OK` — `Storefront`

  **Example Response Body:**
  ```json
  {
    "heroSection": {
      "heroBanners": [
        {
          "imageUrl": "https://cdn.example.com/banner.jpg",
          "title": "Welcome to Bakery",
          "description": "Fresh baked goods"
        }
      ]
    },
    "aboutSection": {
      "tag": "About Us",
      "title": "Our Story",
      "description": "Since 1990...",
      "image1Url": "url1",
      "image2Url": "url2",
      "image3Url": "url3"
    },
    "howWeWorkSection": [
      {
        "title": "Bake",
        "description": "Fresh daily",
        "iconName": "CookingPot"
      }
    ],
    "specialOfferSection": {
      "offers": [
        {
          "imageUrl": "https://cdn.example.com/offer.jpg",
          "title": "Summer Sale",
          "description": "20% off",
          "couponCode": "SUMMER20",
          "discountType": "PERCENTAGE",
          "discountValue": 20.0,
          "expiryDate": "2026-12-31",
          "minCartValue": 50.0
        }
      ]
    },
    "testimonialSection": {
      "quote": "Best cakes!",
      "author": "Jane Doe",
      "rating": 5,
      "authorImageUrl": "url"
    }
  }
  ```

### 6.2 Update Frontpage Storefront Config
- **Method:** `PUT`
- **Path:** `/api/storefront/frontpage`
- **Access Level:** `Public`
- **Request Body:** `Storefront`

  **Example Request Body:**
  ```json
  {
    "heroSection": {
      "heroBanners": [
        {
          "imageUrl": "https://cdn.example.com/banner.jpg",
          "title": "Welcome to Bakery",
          "description": "Fresh baked goods"
        }
      ]
    },
    "aboutSection": {
      "tag": "About Us",
      "title": "Our Story",
      "description": "Since 1990...",
      "image1Url": "url1",
      "image2Url": "url2",
      "image3Url": "url3"
    },
    "howWeWorkSection": [
      {
        "title": "Bake",
        "description": "Fresh daily",
        "iconName": "CookingPot"
      }
    ],
    "specialOfferSection": {
      "offers": [
        {
          "imageUrl": "https://cdn.example.com/offer.jpg",
          "title": "Summer Sale",
          "description": "20% off",
          "couponCode": "SUMMER20",
          "discountType": "PERCENTAGE",
          "discountValue": 20.0,
          "expiryDate": "2026-12-31",
          "minCartValue": 50.0
        }
      ]
    },
    "testimonialSection": {
      "quote": "Best cakes!",
      "author": "Jane Doe",
      "rating": 5,
      "authorImageUrl": "url"
    }
  }
  ```
- **Response:** `200 OK` — `Storefront`

  **Example Response Body:**
  ```json
  {
    "heroSection": {
      "heroBanners": [
        {
          "imageUrl": "https://cdn.example.com/banner.jpg",
          "title": "Welcome to Bakery",
          "description": "Fresh baked goods"
        }
      ]
    },
    "aboutSection": {
      "tag": "About Us",
      "title": "Our Story",
      "description": "Since 1990...",
      "image1Url": "url1",
      "image2Url": "url2",
      "image3Url": "url3"
    },
    "howWeWorkSection": [
      {
        "title": "Bake",
        "description": "Fresh daily",
        "iconName": "CookingPot"
      }
    ],
    "specialOfferSection": {
      "offers": [
        {
          "imageUrl": "https://cdn.example.com/offer.jpg",
          "title": "Summer Sale",
          "description": "20% off",
          "couponCode": "SUMMER20",
          "discountType": "PERCENTAGE",
          "discountValue": 20.0,
          "expiryDate": "2026-12-31",
          "minCartValue": 50.0
        }
      ]
    },
    "testimonialSection": {
      "quote": "Best cakes!",
      "author": "Jane Doe",
      "rating": 5,
      "authorImageUrl": "url"
    }
  }
  ```

### 6.3 Validate Coupon Code
- **Method:** `GET`
- **Path:** `/api/storefront/validate-coupon`
- **Access Level:** `Public`
- **Query Parameters:**
  - `code` (string, required)
  - `cartTotal` (double, optional)
- **Response:** `200 OK` — Coupon details/validation result JSON
- **Error Responses:** `400 Bad Request`

---

## 7. Tax Rates API
**Base Path:** `/api/taxes`  
**Controller:** `TaxRateController`

### 7.1 Get All Tax Rates
- **Method:** `GET`
- **Path:** `/api/taxes`
- **Access Level:** `Public`
- **Query Parameters:** `page`, `size`, `sortBy` (default: `createdAt`), `sortDir` (default: `DESC`)
- **Response:** `200 OK` — `PagedModel<TaxRate>`

### 7.2 Create Tax Rate
- **Method:** `POST`
- **Path:** `/api/taxes`
- **Access Level:** `Public`
- **Request Body:** `TaxRate`

  **Example Request Body:**
  ```json
  {
    "id": "...",
    "type": "Standard",
    "rate": 0.08,
    "description": "Standard 8% tax",
    "createdAt": "2026-08-05T20:13:59",
    "updatedAt": "2026-08-05T20:13:59"
  }
  ```
- **Response:** `201 Created` — `TaxRate`

  **Example Response Body:**
  ```json
  {
    "id": "...",
    "type": "Standard",
    "rate": 0.08,
    "description": "Standard 8% tax",
    "createdAt": "2026-08-05T20:13:59",
    "updatedAt": "2026-08-05T20:13:59"
  }
  ```
- **Error Responses:** `400 Bad Request`

### 7.3 Update Tax Rate
- **Method:** `PUT`
- **Path:** `/api/taxes/{id}`
- **Access Level:** `Public`
- **Request Body:** `TaxRate`

  **Example Request Body:**
  ```json
  {
    "id": "...",
    "type": "Standard",
    "rate": 0.08,
    "description": "Standard 8% tax",
    "createdAt": "2026-08-05T20:13:59",
    "updatedAt": "2026-08-05T20:13:59"
  }
  ```
- **Response:** `200 OK` — `TaxRate`

  **Example Response Body:**
  ```json
  {
    "id": "...",
    "type": "Standard",
    "rate": 0.08,
    "description": "Standard 8% tax",
    "createdAt": "2026-08-05T20:13:59",
    "updatedAt": "2026-08-05T20:13:59"
  }
  ```
- **Error Responses:** `400 Bad Request`, `404 Not Found`

### 7.4 Delete Tax Rate
- **Method:** `DELETE`
- **Path:** `/api/taxes/{id}`
- **Access Level:** `Public`
- **Response:** `204 No Content`
- **Error Responses:** `404 Not Found`

---

## 8. Uploads API
**Base Path:** `/api/uploads`  
**Controller:** `UploadController`

### 8.1 Upload Media Files
- **Method:** `POST`
- **Path:** `/api/uploads/media`
- **Content-Type:** `multipart/form-data`
- **Access Level:** `Public`
- **Request Part:** `media` (List of `MultipartFile`)
- **Response:** `200 OK` — [`MediaUploadResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/media/MediaUploadResponse.java)
  ```json
  {
    "message": "Files uploaded successfully",
    "urls": [
      "https://cdn.example.com/media/file1.png"
    ]
  }
  ```
- **Error Responses:** `400 Bad Request` (MultipartError), `413 Payload Too Large` (MaxUploadSizeExceeded)

### 8.2 Get Uploaded Media File
- **Method:** `GET`
- **Path:** `/api/uploads/media/{fileName}`
- **Access Level:** `Public`
- **Response:** `200 OK` — Binary byte stream with dynamic `Content-Type`

---

## Data Transfer Objects (DTOs)

### [`CategoryRequest`](./src/main/java/com/blubugtech/bakery_product_service/dto/category/CategoryRequest.java)
```json
{
  "name": "Cakes",
  "description": "Delicious freshly baked cakes",
  "displayOrder": 1,
  "active": true,
  "isTopCategory": false,
  "mediaUrls": ["https://cdn.example.com/cake.jpg"],
  "iconClass": "fa-cake"
}
```

### [`CategoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/category/CategoryResponse.java)
```json
{
  "id": "64f1a2b3c4d5e6f7a8b9c0d1",
  "name": "Cakes",
  "description": "Delicious freshly baked cakes",
  "displayOrder": 1,
  "active": true,
  "isTopCategory": false,
  "mediaUrls": ["https://cdn.example.com/cake.jpg"],
  "iconClass": "fa-cake",
  "productCount": 15,
  "activeProductCount": 12,
  "createdAt": "2026-01-15T10:30:00",
  "updatedAt": "2026-02-01T14:20:00"
}
```

### [`ProductRequest`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductRequest.java)
```json
{
  "sku": "CAKE-CHOCO-001",
  "name": "Chocolate Fudge Cake",
  "description": "Rich dark chocolate layer cake",
  "shortDescription": "Rich chocolate cake",
  "categoryId": "64f1a2b3c4d5e6f7a8b9c0d1",
  "price": 25.99,
  "discountPrice": 22.99,
  "costPrice": 12.00,
  "taxClass": "STANDARD",
  "taxRate": 5.0,
  "metaTitle": "Buy Chocolate Fudge Cake",
  "metaDescription": "Order fresh chocolate fudge cake online",
  "maxOrderQuantity": 5,
  "status": "ACTIVE",
  "isFeatured": true,
  "preparationTimeMinutes": 60,
  "shelfLifeHours": 72,
  "unit": "piece",
  "calories": "450 kcal",
  "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
  "allergens": ["Dairy", "Eggs", "Gluten"],
  "tags": ["chocolate", "cake", "bestseller"],
  "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
  "initialStock": 50,
  "minimumStock": 5,
  "reorderLevel": 10
}
```

### [`ProductResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/product/ProductResponse.java)
```json
{
  "id": "64f1a2b3c4d5e6f7a8b9c0d2",
  "sku": "CAKE-CHOCO-001",
  "name": "Chocolate Fudge Cake",
  "description": "Rich dark chocolate layer cake",
  "shortDescription": "Rich chocolate cake",
  "category": {
    "id": "64f1a2b3c4d5e6f7a8b9c0d1",
    "name": "Cakes",
    "iconClass": "fa-cake"
  },
  "price": 25.99,
  "discountPrice": 22.99,
  "costPrice": 12.00,
  "effectivePrice": 22.99,
  "taxClass": "STANDARD",
  "taxRate": 5.0,
  "metaTitle": "Buy Chocolate Fudge Cake",
  "metaDescription": "Order fresh chocolate fudge cake online",
  "maxOrderQuantity": 5,
  "status": "ACTIVE",
  "isFeatured": true,
  "isActive": true,
  "preparationTimeMinutes": 60,
  "shelfLifeHours": 72,
  "unit": "piece",
  "calories": "450 kcal",
  "ingredients": ["Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"],
  "allergens": ["Dairy", "Eggs", "Gluten"],
  "tags": ["chocolate", "cake", "bestseller"],
  "inventory": {
    "currentStock": 45,
    "availableStock": 40,
    "isLowStock": false,
    "isOutOfStock": false,
    "status": "IN_STOCK"
  },
  "mediaUrls": ["https://cdn.example.com/choco-cake.jpg"],
  "primaryImageUrl": "https://cdn.example.com/choco-cake.jpg",
  "isAvailable": true,
  "isOnSale": true,
  "createdAt": "2026-01-15T10:30:00",
  "updatedAt": "2026-02-01T14:20:00",
  "averageRating": 4.8,
  "totalReviews": 24
}
```

### [`InventoryResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/InventoryResponse.java)
```json
{
  "id": "64f1a2b3c4d5e6f7a8b9c0d3",
  "productId": "64f1a2b3c4d5e6f7a8b9c0d2",
  "productName": "Chocolate Fudge Cake",
  "currentStock": 45,
  "reservedStock": 5,
  "availableStock": 40,
  "minimumStock": 5,
  "status": "IN_STOCK"
}
```

### [`StockAdjustmentRequest`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/StockAdjustmentRequest.java)
```json
{
  "quantity": 10,
  "notes": "Restock from bakery kitchen batch #42"
}
```

### [`StockOperationResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/StockOperationResponse.java)
```json
{
  "success": true,
  "productId": "64f1a2b3c4d5e6f7a8b9c0d2",
  "quantity": 5,
  "message": "Stock reserved successfully"
}
```

### [`StockAvailabilityResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/inventory/StockAvailabilityResponse.java)
```json
{
  "productId": "64f1a2b3c4d5e6f7a8b9c0d2",
  "requestedQuantity": 2,
  "availableStock": 40,
  "sufficient": true
}
```

### [`CategoryStatisticsResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/CategoryStatisticsResponse.java)
```json
{
  "totalCategories": 8,
  "activeCategories": 7,
  "inactiveCategories": 1,
  "topCategories": 3,
  "categoryStats": [
    {
      "categoryId": "64f1a2b3c4d5e6f7a8b9c0d1",
      "categoryName": "Cakes",
      "productCount": 15,
      "activeProductCount": 12
    }
  ]
}
```

### [`InventoryStatisticsResponse`](./src/main/java/com/blubugtech/bakery_product_service/dto/InventoryStatisticsResponse.java)
```json
{
  "totalItems": 120,
  "inStockItems": 105,
  "lowStockItems": 10,
  "outOfStockItems": 5,
  "totalReservedStock": 25,
  "totalStockValue": 4550.00
}
```

## Error Responses
The API uses a standardized `ErrorResponse` structure for all exceptions:

```json
{
  "code": "ERROR_CODE",
  "message": "Human-readable error description",
  "timestamp": "2026-08-05T20:13:59",
  "path": "/api/...",
  "details": {
    "key": "value"
  }
}
```

### Common Error Codes
| Code | HTTP Status | Description |
| :--- | :--- | :--- |
| `PRODUCT_SERVICE_ERROR` | 400 | Generic product service business logic error |
| `INSUFFICIENT_STOCK` | 409 | Requested stock exceeds available inventory |
| `INVALID_QUANTITY` | 400 | The requested quantity is not valid |
| `PAYLOAD_TOO_LARGE` | 413 | The uploaded file exceeds the maximum size limit |
| `MULTIPART_ERROR` | 400 | Failed to parse multipart request |
