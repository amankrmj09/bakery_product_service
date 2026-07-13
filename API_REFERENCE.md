# Bakery Product Service - API Reference

This document provides a comprehensive reference to the Bakery Product Service REST API, reflecting the current Java controllers and their associated request/response DTOs.

## Table of Contents
1. [Health & System](#1-health--system)
2. [Categories](#2-categories)
3. [Products](#3-products)
4. [Inventory](#4-inventory)
5. [Site Configuration](#5-site-configuration)
6. [Uploads](#6-uploads)

---

## 1. Health & System

Endpoints for monitoring the health and status of the service.

### `GET /api/health`
Main service and database health check.
**Response:**
```json
{
  "status": "UP",
  "service": "bakery-product-service",
  "timestamp": "2023-10-10T12:00:00",
  "version": "1.0.0",
  "database": "UP",
  "databaseName": "bakery_products"
}
```

### `GET /api/info`
Service information and available features/endpoints.
**Response:**
```json
{
  "serviceName": "Bakery Product Service",
  "description": "Product catalog and inventory management service",
  "version": "1.0.0",
  "features": {
    "categories": "Product category management",
    "products": "Product catalog management",
    "inventory": "Stock and inventory tracking",
    "search": "Advanced product search and filtering"
  },
  "endpoints": {
    "categories": "/api/categories",
    "products": "/api/products",
    "inventory": "/api/inventory"
  }
}
```

### `GET /api/metrics`
Service metrics including uptime and memory usage.
**Response:**
```json
{
  "uptime": "1 days, 2 hours, 30 minutes, 15 seconds",
  "timestamp": "2023-10-10T12:00:00",
  "memory": {
    "maxMemory": "512 MB",
    "totalMemory": "256 MB",
    "freeMemory": "128 MB",
    "usedMemory": "128 MB"
  }
}
```

---

## 2. Categories

Base Path: `/api/categories`

### Models

**CategoryRequestDto**
```json
{
  "name": "string",
  "description": "string",
  "displayOrder": 0,
  "active": true,
  "mediaUrls": ["string"],
  "iconClass": "string"
}
```

**CategoryResponseDto**
```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "displayOrder": 0,
  "active": true,
  "mediaUrls": ["string"],
  "iconClass": "string",
  "productCount": 0,
  "activeProductCount": 0,
  "createdAt": "2023-10-10T12:00:00",
  "updatedAt": "2023-10-10T12:00:00"
}
```

### Endpoints

- **`GET /api/categories`**: Get all categories (paginated).
  - *Params:* `page` (default 0), `size` (default 20), `sortBy` (default "displayOrder"), `sortDir` (default "ASC")
  - *Response:* `Page<CategoryResponseDto>`

- **`GET /api/categories/active`**: Get active categories only.
  - *Params:* `page`, `size`, `sortBy`, `sortDir`
  - *Response:* `Page<CategoryResponseDto>`

- **`GET /api/categories/with-products`**: Get categories with products.
  - *Params:* `page`, `size`, `sortBy`, `sortDir`
  - *Response:* `Page<CategoryResponseDto>`

- **`GET /api/categories/with-active-products`**: Get categories with active products.
  - *Params:* `page`, `size`, `sortBy`, `sortDir`
  - *Response:* `Page<CategoryResponseDto>`

- **`GET /api/categories/{categoryId}`**: Get category by ID.
  - *Response:* `CategoryResponseDto`

- **`POST /api/categories`**: Create a new category (Requires `ADMIN` role).
  - *Request Body:* `CategoryRequestDto`
  - *Response:* `CategoryResponseDto`

- **`PUT /api/categories/{categoryId}`**: Update a category.
  - *Request Body:* `CategoryRequestDto`
  - *Response:* `CategoryResponseDto`

- **`DELETE /api/categories/{categoryId}`**: Delete a category.
  - *Response:* `{"message": "Category deleted successfully", "categoryId": "string"}`

- **`GET /api/categories/search`**: Search categories by query.
  - *Params:* `query`, `page`, `size`, `sortBy`, `sortDir`
  - *Response:* `Page<CategoryResponseDto>`

- **`POST /api/categories/{categoryId}/toggle-status`**: Toggle a category's active status.
  - *Response:* `CategoryResponseDto`

- **`POST /api/categories/reorder`**: Reorder multiple categories.
  - *Request Body:* `{"categoryId1": 1, "categoryId2": 2}`
  - *Response:* `{"message": "Categories reordered successfully"}`

- **`GET /api/categories/statistics`**: Get category statistics.
  - *Response:* `Map<String, Object>`

- **`GET /api/categories/health`**: Category controller health check.
  - *Response:* `{"status": "UP", "service": "product-service-categories", "timestamp": "..."}`

---

## 3. Products

Base Path: `/api/products`

### Models

**ProductRequestDto**
```json
{
  "sku": "string",
  "name": "string",
  "description": "string",
  "shortDescription": "string",
  "categoryId": "string",
  "price": 0.00,
  "discountPrice": 0.00,
  "status": "ACTIVE",
  "isFeatured": false,
  "preparationTimeMinutes": 0,
  "shelfLifeHours": 0,
  "unit": "piece",
  "weightGrams": 0,
  "caloriesPerUnit": 0,
  "ingredients": ["string"],
  "allergens": ["string"],
  "tags": ["string"],
  "mediaUrls": ["string"],
  "initialStock": 0,
  "minimumStock": 0,
  "reorderLevel": 0
}
```
*Note: Status can be `ACTIVE`, `INACTIVE`, `DRAFT`, or `ARCHIVED`.*

**ProductResponseDto**
```json
{
  "id": "string",
  "sku": "string",
  "name": "string",
  "description": "string",
  "shortDescription": "string",
  "category": {
    "id": "string",
    "name": "string",
    "iconClass": "string"
  },
  "price": 0.00,
  "discountPrice": 0.00,
  "effectivePrice": 0.00,
  "status": "ACTIVE",
  "isFeatured": false,
  "preparationTimeMinutes": 0,
  "shelfLifeHours": 0,
  "unit": "piece",
  "weightGrams": 0,
  "caloriesPerUnit": 0,
  "ingredients": ["string"],
  "allergens": ["string"],
  "tags": ["string"],
  "inventory": {
    "currentStock": 0,
    "availableStock": 0,
    "isLowStock": false,
    "isOutOfStock": false,
    "status": "IN_STOCK"
  },
  "mediaUrls": ["string"],
  "primaryImageUrl": "string",
  "isAvailable": true,
  "isOnSale": false,
  "createdAt": "2023-10-10T12:00:00",
  "updatedAt": "2023-10-10T12:00:00"
}
```

### Endpoints

- **`GET /api/products`**: Get all products (paginated).
  - *Params:* `page` (default 0), `size` (default 20), `sortBy` (default "name"), `sortDir` (default "ASC")
  - *Response:* `Page<ProductResponseDto>`

- **`GET /api/products/active`**: Get active products.
  - *Response:* `Page<ProductResponseDto>`

- **`GET /api/products/available`**: Get available products (active with stock).
  - *Response:* `Page<ProductResponseDto>`

- **`GET /api/products/featured`**: Get featured products.
  - *Response:* `Page<ProductResponseDto>`

- **`GET /api/products/on-sale`**: Get products on sale.
  - *Response:* `Page<ProductResponseDto>`

- **`GET /api/products/recent`**: Get recently added products.
  - *Params:* `days` (default 7)
  - *Response:* `Page<ProductResponseDto>`

- **`GET /api/products/{productId}`**: Get product by ID.
  - *Response:* `ProductResponseDto`

- **`GET /api/products/batch`**: Get multiple products by IDs.
  - *Params:* `productIds` (List)
  - *Response:* `List<ProductResponseDto>`

- **`POST /api/products/batch/validate`**: Validate multiple products.
  - *Request Body:* `["productId1", "productId2"]`
  - *Response:* `List<ProductResponseDto>`

- **`GET /api/products/sku/{sku}`**: Get product by SKU.
  - *Response:* `ProductResponseDto`

- **`GET /api/products/category/{categoryId}`**: Get products by category.
  - *Response:* `Page<ProductResponseDto>`

- **`GET /api/products/search`**: Search products by query.
  - *Params:* `query`
  - *Response:* `Page<ProductResponseDto>`

- **`GET /api/products/price-range`**: Get products by price range.
  - *Params:* `minPrice`, `maxPrice`
  - *Response:* `Page<ProductResponseDto>`

- **`GET /api/products/tag/{tag}`**: Get products by tag.
  - *Response:* `Page<ProductResponseDto>`

- **`GET /api/products/without-allergen/{allergen}`**: Get products without specific allergen.
  - *Response:* `Page<ProductResponseDto>`

- **`GET /api/products/filter`**: Advanced search with filters.
  - *Params:* `categoryId`, `status`, `minPrice`, `maxPrice`, `inStock`
  - *Response:* `Page<ProductResponseDto>`

- **`POST /api/products`**: Create a new product.
  - *Request Body:* `ProductRequestDto`
  - *Response:* `ProductResponseDto`

- **`PUT /api/products/{productId}`**: Update a product.
  - *Request Body:* `ProductRequestDto`
  - *Response:* `ProductResponseDto`

- **`PATCH /api/products/{productId}/status`**: Update a product's status.
  - *Request Body:* `{"status": "INACTIVE"}`
  - *Response:* `ProductResponseDto`

- **`POST /api/products/{productId}/toggle-featured`**: Toggle product's featured status.
  - *Response:* `ProductResponseDto`

- **`DELETE /api/products/{productId}`**: Delete a product.
  - *Response:* `{"message": "Product deleted successfully", "productId": "string"}`

- **`GET /api/products/{productId}/availability`**: Check if product is available.
  - *Response:* `{"productId": "string", "available": true}`

- **`GET /api/products/statistics`**: Get product statistics.
  - *Response:* `Map<String, Object>`

- **`GET /api/products/health`**: Product controller health check.
  - *Response:* `{"status": "UP", "service": "product-service-products", "timestamp": "..."}`

---

## 4. Inventory

Base Path: `/api/inventory`

### Models

**InventoryUpdateRequestDto**
```json
{
  "currentStock": 0,
  "reservedStock": 0,
  "minimumStock": 0,
  "maximumStock": 0,
  "reorderLevel": 0,
  "reorderQuantity": 0,
  "autoReorderEnabled": false,
  "trackExpiry": false,
  "expiryDate": "2023-10-10T12:00:00",
  "supplierInfo": "string",
  "storageLocation": "string",
  "notes": "string"
}
```

**InventoryResponseDto**
```json
{
  "id": "string",
  "productId": "string",
  "productName": "string",
  "productSku": "string",
  "currentStock": 0,
  "reservedStock": 0,
  "availableStock": 0,
  "minimumStock": 0,
  "maximumStock": 0,
  "reorderLevel": 0,
  "reorderQuantity": 0,
  "status": "IN_STOCK",
  "isLowStock": false,
  "isOutOfStock": false,
  "needsReorder": false,
  "lastRestockedAt": "2023-10-10T12:00:00",
  "lastRestockedQuantity": 0,
  "autoReorderEnabled": false,
  "trackExpiry": false,
  "expiryDate": "2023-10-10T12:00:00",
  "supplierInfo": "string",
  "storageLocation": "string",
  "notes": "string",
  "createdAt": "2023-10-10T12:00:00",
  "updatedAt": "2023-10-10T12:00:00"
}
```

### Endpoints

- **`GET /api/inventory`**: Get all inventory items (paginated).
  - *Response:* `Page<InventoryResponseDto>`

- **`GET /api/inventory/product/{productId}`**: Get inventory for a specific product ID.
  - *Response:* `InventoryResponseDto`

- **`GET /api/inventory/sku/{sku}`**: Get inventory by product SKU.
  - *Response:* `InventoryResponseDto`

- **`GET /api/inventory/low-stock`**: Get low stock inventory items.
  - *Response:* `Page<InventoryResponseDto>`

- **`GET /api/inventory/out-of-stock`**: Get out of stock inventory items.
  - *Response:* `Page<InventoryResponseDto>`

- **`GET /api/inventory/needs-reorder`**: Get items needing reorder.
  - *Response:* `Page<InventoryResponseDto>`

- **`GET /api/inventory/expired`**: Get expired inventory items.
  - *Response:* `Page<InventoryResponseDto>`

- **`GET /api/inventory/expiring-soon`**: Get items expiring soon.
  - *Params:* `hours` (default 24)
  - *Response:* `Page<InventoryResponseDto>`

- **`PUT /api/inventory/product/{productId}`**: Update inventory details.
  - *Request Body:* `InventoryUpdateRequestDto`
  - *Response:* `InventoryResponseDto`

- **`POST /api/inventory/product/{productId}/add-stock`**: Add stock (restock).
  - *Request Body:* `{"quantity": 10, "notes": "string"}`
  - *Response:* `InventoryResponseDto`

- **`POST /api/inventory/product/{productId}/reserve`**: Reserve stock for orders.
  - *Request Body:* `{"quantity": 5}`
  - *Response:* `{"success": true, "productId": "string", "quantity": 5, "message": "Stock reserved successfully"}`

- **`POST /api/inventory/product/{productId}/release-reserved`**: Release reserved stock back to available.
  - *Request Body:* `{"quantity": 5}`
  - *Response:* `{"message": "Reserved stock released successfully", "productId": "string", "quantity": "5"}`

- **`POST /api/inventory/product/{productId}/consume`**: Consume stock (reduce current and reserved).
  - *Request Body:* `{"quantity": 5}`
  - *Response:* `{"message": "Stock consumed successfully", "productId": "string", "quantity": "5"}`

- **`GET /api/inventory/product/{productId}/availability`**: Check if requested quantity is available.
  - *Params:* `quantity`
  - *Response:* `{"productId": "string", "requestedQuantity": 5, "availableStock": 10, "sufficient": true}`

- **`GET /api/inventory/product/{productId}/available-stock`**: Get just the available stock number.
  - *Response:* `{"productId": "string", "availableStock": 10}`

- **`POST /api/inventory/bulk-update-minimum-stock`**: Update minimum stock levels in bulk.
  - *Request Body:* `{"productId1": 10, "productId2": 20}` (Map of ID to min stock)
  - *Response:* `{"message": "Minimum stock levels updated successfully", "updatedProducts": "2"}`

- **`GET /api/inventory/statistics`**: Get inventory statistics.
  - *Response:* `Map<String, Object>`

- **`GET /api/inventory/health`**: Inventory controller health check.
  - *Response:* `{"status": "UP", "service": "product-service-inventory", "timestamp": "..."}`

---

## 5. Site Configuration

Base Path: `/api/site-config`

### Models

**SiteConfig**
```json
{
  "id": "string",
  "heroSection": {
    "tag": "string",
    "headline": "string",
    "subtitle": "string",
    "heroImageUrl": "string",
    "sideCard1": {
      "subtitle": "string",
      "title": "string",
      "price": "string",
      "imageUrl": "string"
    },
    "sideCard2": {
      "subtitle": "string",
      "title": "string",
      "price": "string",
      "imageUrl": "string"
    }
  },
  "aboutSection": {
    "tag": "string",
    "title": "string",
    "description": "string",
    "image1Url": "string",
    "image2Url": "string",
    "image3Url": "string"
  },
  "howWeWorkSection": [
    {
      "title": "string",
      "description": "string",
      "iconName": "string"
    }
  ],
  "specialOfferSection": {
    "tag": "string",
    "headline": "string",
    "description": "string",
    "imageUrl": "string"
  },
  "testimonialSection": {
    "quote": "string",
    "author": "string",
    "rating": 5,
    "authorImageUrl": "string"
  }
}
```

### Endpoints

- **`GET /api/site-config/frontpage`**: Fetch the current site configuration.
  - *Response:* `SiteConfig`

- **`PUT /api/site-config/frontpage`**: Update the site configuration.
  - *Request Body:* `SiteConfig`
  - *Response:* `SiteConfig`

---

## 6. Uploads

Base Path: `/api/uploads`

### Endpoints

- **`POST /api/uploads/media`**: Upload one or more media files.
  - *Consumes:* `multipart/form-data`
  - *Params:* `media` (List of MultipartFile)
  - *Response:*
    ```json
    {
      "message": "Files uploaded successfully",
      "urls": ["string", "string"]
    }
    ```

- **`GET /api/uploads/media/{fileName}`**: Get an uploaded media file.
  - *Response:* Binary file content (`byte[]` with correct content type)
