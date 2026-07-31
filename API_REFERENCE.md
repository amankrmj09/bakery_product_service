# Bakery Product Service API Reference

This document provides a comprehensive reference to the Bakery Product Service REST API, reflecting the current Java controllers and their associated request/response DTOs.

---

## 1. System & Monitoring (Actuator)
**Base Path:** `/actuator`

Standard Spring Boot Actuator endpoints are used for monitoring and metrics.

### 1.1 Health Check
- **Method:** `GET`
- **Path:** `/actuator/health`
- **Type of API:** `Public`
- **Response Body:** `200 OK` (Standard Actuator Health JSON)

### 1.2 Service Info
- **Method:** `GET`
- **Path:** `/actuator/info`
- **Type of API:** `Public`
- **Response Body:** `200 OK` (Standard Actuator Info JSON)

### 1.3 Prometheus Metrics
- **Method:** `GET`
- **Path:** `/actuator/prometheus`
- **Type of API:** `Public`
- **Response Body:** `200 OK` (Prometheus Text Format)

---

## 2. Categories
**Base Path:** `/api/categories`

### 2.1 Get all categories
- **Method:** `GET`
- **Path:** `/api/categories`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<CategoryResponse>`

### 2.2 Get active categories
- **Method:** `GET`
- **Path:** `/api/categories/active`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<CategoryResponse>`

### 2.3 Get categories with products
- **Method:** `GET`
- **Path:** `/api/categories/with-products`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<CategoryResponse>`

### 2.4 Get categories with active products
- **Method:** `GET`
- **Path:** `/api/categories/with-active-products`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<CategoryResponse>`

### 2.5 Get category by ID
- **Method:** `GET`
- **Path:** `/api/categories/{categoryId}`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `CategoryResponse`

### 2.6 Create new category
- **Method:** `POST`
- **Path:** `/api/categories`
- **Type of API:** `Admin`
- **Request Body:**
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
- **Response Body:** `201 Created`
  `CategoryResponse`

### 2.7 Update category
- **Method:** `PUT`
- **Path:** `/api/categories/{categoryId}`
- **Type of API:** `Admin`
- **Request Body:**
  *(Same as Create new category)*
- **Response Body:** `200 OK`
  `CategoryResponse`

### 2.8 Delete category
- **Method:** `DELETE`
- **Path:** `/api/categories/{categoryId}`
- **Type of API:** `Admin`
- **Request Body:** None
- **Response Body:** `200 OK`
  ```json
  {
    "message": "Category deleted successfully",
    "categoryId": "string"
  }
  ```

### 2.9 Search categories
- **Method:** `GET`
- **Path:** `/api/categories/search`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<CategoryResponse>`

### 2.10 Toggle category status
- **Method:** `POST`
- **Path:** `/api/categories/{categoryId}/toggle-status`
- **Type of API:** `Admin`
- **Request Body:** None
- **Response Body:** `200 OK`
  `CategoryResponse`

### 2.11 Reorder categories
- **Method:** `POST`
- **Path:** `/api/categories/reorder`
- **Type of API:** `Admin`
- **Request Body:**
  ```json
  {
    "categoryId1": 1,
    "categoryId2": 2
  }
  ```
- **Response Body:** `200 OK`
  ```json
  {
    "message": "Categories reordered successfully"
  }
  ```

### 2.12 Category statistics
- **Method:** `GET`
- **Path:** `/api/categories/statistics`
- **Type of API:** `Admin`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Map<String, Object>`


---

## 3. Products
**Base Path:** `/api/products`

### 3.1 Get all products
- **Method:** `GET`
- **Path:** `/api/products`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<ProductResponse>`

### 3.2 Get active products
- **Method:** `GET`
- **Path:** `/api/products/active`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<ProductResponse>`

### 3.3 Get available products
- **Method:** `GET`
- **Path:** `/api/products/available`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<ProductResponse>`

### 3.4 Get featured products
- **Method:** `GET`
- **Path:** `/api/products/featured`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<ProductResponse>`

### 3.5 Get products on sale
- **Method:** `GET`
- **Path:** `/api/products/on-sale`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<ProductResponse>`

### 3.6 Get recently added products
- **Method:** `GET`
- **Path:** `/api/products/recent`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<ProductResponse>`

### 3.7 Get product by ID
- **Method:** `GET`
- **Path:** `/api/products/{productId}`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `ProductResponse`

### 3.8 Get multiple products by IDs
- **Method:** `GET`
- **Path:** `/api/products/batch`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `List<ProductResponse>`

### 3.9 Validate multiple products
- **Method:** `POST`
- **Path:** `/api/products/batch/validate`
- **Type of API:** `Public`
- **Request Body:**
  ```json
  [
    "productId1",
    "productId2"
  ]
  ```
- **Response Body:** `200 OK`
  `List<ProductResponse>`

### 3.10 Get product by SKU
- **Method:** `GET`
- **Path:** `/api/products/sku/{sku}`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `ProductResponse`

### 3.11 Get products by category
- **Method:** `GET`
- **Path:** `/api/products/category/{categoryId}`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<ProductResponse>`

### 3.12 Search products by query
- **Method:** `GET`
- **Path:** `/api/products/search`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<ProductResponse>`

### 3.13 Get products by price range
- **Method:** `GET`
- **Path:** `/api/products/price-range`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<ProductResponse>`

### 3.14 Get products by tag
- **Method:** `GET`
- **Path:** `/api/products/tag/{tag}`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<ProductResponse>`

### 3.15 Get products without allergen
- **Method:** `GET`
- **Path:** `/api/products/without-allergen/{allergen}`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<ProductResponse>`

### 3.16 Advanced search with filters
- **Method:** `GET`
- **Path:** `/api/products/filter`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<ProductResponse>`

### 3.17 Create new product
- **Method:** `POST`
- **Path:** `/api/products`
- **Type of API:** `Admin`
- **Request Body:**
  ```json
  {
    "sku": "string",
    "name": "string",
    "description": "string",
    "categoryId": "string",
    "price": 0.00
  }
  ```
- **Response Body:** `201 Created`
  `ProductResponse`

### 3.18 Update product
- **Method:** `PUT`
- **Path:** `/api/products/{productId}`
- **Type of API:** `Admin`
- **Request Body:**
  *(Same as Create new product)*
- **Response Body:** `200 OK`
  `ProductResponse`

### 3.19 Update product status
- **Method:** `PATCH`
- **Path:** `/api/products/{productId}/status`
- **Type of API:** `Admin`
- **Request Body:**
  ```json
  {
    "status": "INACTIVE"
  }
  ```
- **Response Body:** `200 OK`
  `ProductResponse`

### 3.20 Toggle featured status
- **Method:** `POST`
- **Path:** `/api/products/{productId}/toggle-featured`
- **Type of API:** `Admin`
- **Request Body:** None
- **Response Body:** `200 OK`
  `ProductResponse`

### 3.21 Delete product
- **Method:** `DELETE`
- **Path:** `/api/products/{productId}`
- **Type of API:** `Admin`
- **Request Body:** None
- **Response Body:** `200 OK`
  ```json
  {
    "message": "Product deleted successfully",
    "productId": "string"
  }
  ```

### 3.22 Check product availability
- **Method:** `GET`
- **Path:** `/api/products/{productId}/availability`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  ```json
  {
    "productId": "string",
    "available": true
  }
  ```

### 3.23 Get product statistics
- **Method:** `GET`
- **Path:** `/api/products/statistics`
- **Type of API:** `Admin`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Map<String, Object>`


---

## 4. Inventory
**Base Path:** `/api/inventory`

### 4.1 Get all inventory items
- **Method:** `GET`
- **Path:** `/api/inventory`
- **Type of API:** `Admin`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<InventoryResponse>`

### 4.2 Get inventory for specific product
- **Method:** `GET`
- **Path:** `/api/inventory/product/{productId}`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `InventoryResponse`

### 4.3 Get inventory by SKU
- **Method:** `GET`
- **Path:** `/api/inventory/sku/{sku}`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `InventoryResponse`

### 4.4 Get low stock
- **Method:** `GET`
- **Path:** `/api/inventory/low-stock`
- **Type of API:** `Admin`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<InventoryResponse>`

### 4.5 Get out of stock
- **Method:** `GET`
- **Path:** `/api/inventory/out-of-stock`
- **Type of API:** `Admin`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<InventoryResponse>`

### 4.6 Get items needing reorder
- **Method:** `GET`
- **Path:** `/api/inventory/needs-reorder`
- **Type of API:** `Admin`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<InventoryResponse>`

### 4.7 Get expired
- **Method:** `GET`
- **Path:** `/api/inventory/expired`
- **Type of API:** `Admin`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<InventoryResponse>`

### 4.8 Get expiring soon
- **Method:** `GET`
- **Path:** `/api/inventory/expiring-soon`
- **Type of API:** `Admin`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Page<InventoryResponse>`

### 4.9 Update inventory details
- **Method:** `PUT`
- **Path:** `/api/inventory/product/{productId}`
- **Type of API:** `Admin`
- **Request Body:**
  ```json
  {
    "currentStock": 0,
    "reservedStock": 0,
    "minimumStock": 0
  }
  ```
- **Response Body:** `200 OK`
  `InventoryResponse`

### 4.10 Add stock
- **Method:** `POST`
- **Path:** `/api/inventory/product/{productId}/add-stock`
- **Type of API:** `Admin`
- **Request Body:**
  ```json
  {
    "quantity": 10,
    "notes": "Restock supply"
  }
  ```
- **Response Body:** `200 OK`
  `InventoryResponse`

### 4.11 Reserve stock
- **Method:** `POST`
- **Path:** `/api/inventory/product/{productId}/reserve`
- **Type of API:** `Internal/Admin`
- **Request Body:**
  ```json
  {
    "quantity": 5
  }
  ```
- **Response Body:** `200 OK`
  ```json
  {
    "success": true,
    "productId": "string",
    "quantity": 5,
    "message": "Stock reserved successfully"
  }
  ```

### 4.12 Release reserved stock
- **Method:** `POST`
- **Path:** `/api/inventory/product/{productId}/release-reserved`
- **Type of API:** `Internal/Admin`
- **Request Body:**
  ```json
  {
    "quantity": 5
  }
  ```
- **Response Body:** `200 OK`
  ```json
  {
    "message": "Reserved stock released successfully",
    "productId": "string",
    "quantity": "5"
  }
  ```

### 4.13 Consume stock
- **Method:** `POST`
- **Path:** `/api/inventory/product/{productId}/consume`
- **Type of API:** `Internal/Admin`
- **Request Body:**
  ```json
  {
    "quantity": 5
  }
  ```
- **Response Body:** `200 OK`
  ```json
  {
    "message": "Stock consumed successfully",
    "productId": "string",
    "quantity": "5"
  }
  ```

### 4.14 Check available quantity
- **Method:** `GET`
- **Path:** `/api/inventory/product/{productId}/availability`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  ```json
  {
    "productId": "string",
    "requestedQuantity": 5,
    "availableStock": 10,
    "sufficient": true
  }
  ```

### 4.15 Get available stock number
- **Method:** `GET`
- **Path:** `/api/inventory/product/{productId}/available-stock`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  ```json
  {
    "productId": "string",
    "availableStock": 10
  }
  ```

### 4.16 Bulk update minimum stock
- **Method:** `POST`
- **Path:** `/api/inventory/bulk-update-minimum-stock`
- **Type of API:** `Admin`
- **Request Body:**
  ```json
  {
    "productId1": 10,
    "productId2": 20
  }
  ```
- **Response Body:** `200 OK`
  ```json
  {
    "message": "Minimum stock levels updated successfully",
    "updatedProducts": "2"
  }
  ```

### 4.17 Inventory statistics
- **Method:** `GET`
- **Path:** `/api/inventory/statistics`
- **Type of API:** `Admin`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Map<String, Object>`


---

## 5. Storefront
**Base Path:** `/api/storefront`

### 5.1 Fetch storefront config
- **Method:** `GET`
- **Path:** `/api/storefront/frontpage`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `Storefront`

### 5.2 Update storefront config
- **Method:** `PUT`
- **Path:** `/api/storefront/frontpage`
- **Type of API:** `Public`
- **Request Body:** `Storefront`
- **Response Body:** `200 OK`
  `Storefront`

### 5.3 Validate coupon
- **Method:** `GET`
- **Path:** `/api/storefront/validate-coupon?code={string}&cartTotal={double}`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK` (Validation result JSON)

---

## 6. Tax Rates
**Base Path:** `/api/taxes`

### 6.1 Get all tax rates
- **Method:** `GET`
- **Path:** `/api/taxes`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  `List<TaxRate>`

### 6.2 Create tax rate
- **Method:** `POST`
- **Path:** `/api/taxes`
- **Type of API:** `Admin`
- **Request Body:** `TaxRate`
- **Response Body:** `201 Created`
  `TaxRate`

### 6.3 Update tax rate
- **Method:** `PUT`
- **Path:** `/api/taxes/{id}`
- **Type of API:** `Admin`
- **Request Body:** `TaxRate`
- **Response Body:** `200 OK`
  `TaxRate`

### 6.4 Delete tax rate
- **Method:** `DELETE`
- **Path:** `/api/taxes/{id}`
- **Type of API:** `Admin`
- **Request Body:** None
- **Response Body:** `204 No Content`

---

## 7. Uploads
**Base Path:** `/api/uploads`

### 7.1 Upload media
- **Method:** `POST`
- **Path:** `/api/uploads/media`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  ```json
  {
    "message": "Files uploaded successfully",
    "urls": ["string", "string"]
  }
  ```

### 7.2 Get uploaded media
- **Method:** `GET`
- **Path:** `/api/uploads/media/{fileName}`
- **Type of API:** `Public`
- **Request Body:** None
- **Response Body:** `200 OK`
  *(Binary file content, byte[] with correct content type)*

---

## Common DTOs

### CategoryResponse
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

### ProductResponse
```json
{
  "id": "string",
  "sku": "string",
  "name": "string",
  "description": "string",
  "category": {
    "id": "string",
    "name": "string"
  },
  "price": 0.00,
  "status": "ACTIVE",
  "inventory": {
    "currentStock": 0,
    "availableStock": 0,
    "status": "IN_STOCK"
  },
  "mediaUrls": ["string"],
  "primaryImageUrl": "string"
}
```

### InventoryResponse
```json
{
  "id": "string",
  "productId": "string",
  "productName": "string",
  "currentStock": 0,
  "reservedStock": 0,
  "availableStock": 0,
  "minimumStock": 0,
  "status": "IN_STOCK"
}
```

### Storefront
```json
{
  "id": "string",
  "heroSection": {
    "campaigns": [
      {
        "imageUrl": "string",
        "title": "string",
        "description": "string"
      }
    ]
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
    "offers": [
      {
        "imageUrl": "string",
        "title": "string",
        "description": "string",
        "couponCode": "string",
        "discountType": "string",
        "discountValue": 0.0,
        "expiryDate": "string",
        "minCartValue": 0.0
      }
    ]
  },
  "testimonialSection": {
    "quote": "string",
    "author": "string",
    "rating": 0,
    "authorImageUrl": "string"
  }
}
```

### TaxRate
```json
{
  "id": "string",
  "type": "string",
  "rate": 0.0,
  "description": "string",
  "createdAt": "2023-10-10T12:00:00",
  "updatedAt": "2023-10-10T12:00:00"
}
```
