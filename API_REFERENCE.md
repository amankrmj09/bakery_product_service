# bakery_product_service API Report

## HealthController

### `GET` `/api/health`
- **API Name:** health
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
```json
{
  "status": "String - UP",
  "service": "String - bakery-product-service",
  "timestamp": "DateTime",
  "version": "String - 1.0.0",
  "database": "String - UP or DOWN",
  "databaseUrl": "String",
  "databaseError": "String"
}
```

---

### `GET` `/api/info`
- **API Name:** info
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
```json
{
  "serviceName": "String",
  "description": "String",
  "version": "String",
  "features": {},
  "endpoints": {}
}
```

---

### `GET` `/api/metrics`
- **API Name:** metrics
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
```json
{
  "uptime": "String",
  "timestamp": "DateTime",
  "memory": {
    "maxMemory": "String",
    "totalMemory": "String",
    "freeMemory": "String",
    "usedMemory": "String"
  }
}
```

---

## CategoryController

### `POST` `/api/categories`
- **API Name:** createCategory
- **Type:** REST / Synchronous

**Request:**
```json
{
  "name": "String - Required (2-100 chars)",
  "description": "String (Max 500 chars)",
  "displayOrder": "Integer - Default 0",
  "active": "Boolean - Default true",
  "imageUrl": "String",
  "iconClass": "String"
}
```

**Response:**
```json
{
  "id": "UUID",
  "name": "String",
  "description": "String",
  "displayOrder": "Integer",
  "active": "Boolean",
  "imageUrl": "String",
  "iconClass": "String",
  "productCount": "Integer",
  "activeProductCount": "Integer",
  "createdAt": "DateTime",
  "updatedAt": "DateTime"
}
```

---

### `GET` `/api/categories`
- **API Name:** getAllCategories
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
*(List of CategoryResponse)*

---

### `GET` `/api/categories/active`
- **API Name:** getActiveCategories
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
*(List of CategoryResponse)*

---

### `GET` `/api/categories/with-products`
- **API Name:** getCategoriesWithProducts
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
*(List of CategoryResponse)*

---

### `GET` `/api/categories/with-active-products`
- **API Name:** getCategoriesWithActiveProducts
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
*(List of CategoryResponse)*

---

### `GET` `/api/categories/{categoryId}`
- **API Name:** getCategoryById
- **Type:** REST / Synchronous
- **Path Variable:** `categoryId` (UUID)

**Request:**
None

**Response:**
*(CategoryResponse)*

---

### `PUT` `/api/categories/{categoryId}`
- **API Name:** updateCategory
- **Type:** REST / Synchronous
- **Path Variable:** `categoryId` (UUID)

**Request:**
*(Same as createCategory CategoryRequest)*

**Response:**
*(CategoryResponse)*

---

### `DELETE` `/api/categories/{categoryId}`
- **API Name:** deleteCategory
- **Type:** REST / Synchronous
- **Path Variable:** `categoryId` (UUID)

**Request:**
None

**Response:**
```json
{
  "message": "String - Category deleted successfully",
  "categoryId": "String"
}
```

---

### `GET` `/api/categories/search`
- **API Name:** searchCategories
- **Type:** REST / Synchronous
- **Query Parameters:** `query` (String)

**Request:**
None

**Response:**
*(List of CategoryResponse)*

---

### `POST` `/api/categories/{categoryId}/toggle-status`
- **API Name:** toggleCategoryStatus
- **Type:** REST / Synchronous
- **Path Variable:** `categoryId` (UUID)

**Request:**
None

**Response:**
*(CategoryResponse)*

---

### `POST` `/api/categories/reorder`
- **API Name:** reorderCategories
- **Type:** REST / Synchronous

**Request:**
```json
{
  "UUID (CategoryId)": "Integer (New Order)"
}
```
*(Map of UUID to Integer)*

**Response:**
```json
{
  "message": "String - Categories reordered successfully"
}
```

---

### `GET` `/api/categories/statistics`
- **API Name:** getCategoryStatistics
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
*(Map of statistics)*

---

### `GET` `/api/categories/health`
- **API Name:** health
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
```json
{
  "status": "String - UP",
  "service": "String - product-service-categories",
  "timestamp": "DateTime"
}
```

---

## InventoryController

### `GET` `/api/inventory`
- **API Name:** getAllInventory
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
```json
[
  {
    "id": "UUID",
    "productId": "UUID",
    "productName": "String",
    "productSku": "String",
    "currentStock": "Integer",
    "reservedStock": "Integer",
    "availableStock": "Integer",
    "minimumStock": "Integer",
    "maximumStock": "Integer",
    "reorderLevel": "Integer",
    "reorderQuantity": "Integer",
    "status": "String (IN_STOCK, LOW_STOCK, OUT_OF_STOCK)",
    "isLowStock": "Boolean",
    "isOutOfStock": "Boolean",
    "needsReorder": "Boolean",
    "lastRestockedAt": "DateTime",
    "lastRestockedQuantity": "Integer",
    "autoReorderEnabled": "Boolean",
    "trackExpiry": "Boolean",
    "expiryDate": "DateTime",
    "isExpired": "Boolean",
    "supplierInfo": "String",
    "storageLocation": "String",
    "notes": "String",
    "createdAt": "DateTime",
    "updatedAt": "DateTime"
  }
]
```
*(List of InventoryResponse)*

---

### `GET` `/api/inventory/product/{productId}`
- **API Name:** getInventoryByProductId
- **Type:** REST / Synchronous
- **Path Variable:** `productId` (UUID)

**Request:**
None

**Response:**
*(InventoryResponse)*

---

### `GET` `/api/inventory/sku/{sku}`
- **API Name:** getInventoryByProductSku
- **Type:** REST / Synchronous
- **Path Variable:** `sku` (String)

**Request:**
None

**Response:**
*(InventoryResponse)*

---

### `GET` `/api/inventory/low-stock`
- **API Name:** getLowStockItems
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
*(List of InventoryResponse)*

---

### `GET` `/api/inventory/out-of-stock`
- **API Name:** getOutOfStockItems
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
*(List of InventoryResponse)*

---

### `GET` `/api/inventory/needs-reorder`
- **API Name:** getItemsNeedingReorder
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
*(List of InventoryResponse)*

---

### `GET` `/api/inventory/expired`
- **API Name:** getExpiredItems
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
*(List of InventoryResponse)*

---

### `GET` `/api/inventory/expiring-soon`
- **API Name:** getItemsExpiringSoon
- **Type:** REST / Synchronous
- **Query Parameters:** `hours` (int, default 24)

**Request:**
None

**Response:**
*(List of InventoryResponse)*

---

### `PUT` `/api/inventory/product/{productId}`
- **API Name:** updateInventory
- **Type:** REST / Synchronous
- **Path Variable:** `productId` (UUID)

**Request:**
```json
{
  "currentStock": "Integer - Required (Min 0)",
  "reservedStock": "Integer (Min 0)",
  "minimumStock": "Integer (Min 0)",
  "maximumStock": "Integer (Min 0)",
  "reorderLevel": "Integer (Min 0)",
  "reorderQuantity": "Integer (Min 0)",
  "autoReorderEnabled": "Boolean",
  "trackExpiry": "Boolean",
  "expiryDate": "DateTime",
  "supplierInfo": "String",
  "storageLocation": "String",
  "notes": "String"
}
```

**Response:**
*(InventoryResponse)*

---

### `POST` `/api/inventory/product/{productId}/add-stock`
- **API Name:** addStock
- **Type:** REST / Synchronous
- **Path Variable:** `productId` (UUID)

**Request:**
```json
{
  "quantity": "Integer",
  "notes": "String"
}
```

**Response:**
*(InventoryResponse)*

---

### `POST` `/api/inventory/product/{productId}/reserve`
- **API Name:** reserveStock
- **Type:** REST / Synchronous
- **Path Variable:** `productId` (UUID)

**Request:**
```json
{
  "quantity": "Integer"
}
```

**Response:**
```json
{
  "success": "Boolean",
  "productId": "UUID",
  "quantity": "Integer",
  "message": "String"
}
```

---

### `POST` `/api/inventory/product/{productId}/release-reserved`
- **API Name:** releaseReservedStock
- **Type:** REST / Synchronous
- **Path Variable:** `productId` (UUID)

**Request:**
```json
{
  "quantity": "Integer"
}
```

**Response:**
```json
{
  "message": "String",
  "productId": "String",
  "quantity": "String"
}
```

---

### `POST` `/api/inventory/product/{productId}/consume`
- **API Name:** consumeStock
- **Type:** REST / Synchronous
- **Path Variable:** `productId` (UUID)

**Request:**
```json
{
  "quantity": "Integer"
}
```

**Response:**
```json
{
  "message": "String",
  "productId": "String",
  "quantity": "String"
}
```

---

### `GET` `/api/inventory/product/{productId}/availability`
- **API Name:** checkStockAvailability
- **Type:** REST / Synchronous
- **Path Variable:** `productId` (UUID)
- **Query Parameters:** `quantity` (Integer)

**Request:**
None

**Response:**
```json
{
  "productId": "UUID",
  "requestedQuantity": "Integer",
  "availableStock": "Integer",
  "sufficient": "Boolean"
}
```

---

### `GET` `/api/inventory/product/{productId}/available-stock`
- **API Name:** getAvailableStock
- **Type:** REST / Synchronous
- **Path Variable:** `productId` (UUID)

**Request:**
None

**Response:**
```json
{
  "productId": "UUID",
  "availableStock": "Integer"
}
```

---

### `POST` `/api/inventory/bulk-update-minimum-stock`
- **API Name:** bulkUpdateMinimumStock
- **Type:** REST / Synchronous

**Request:**
```json
{
  "UUID (ProductId)": "Integer (Minimum Stock)"
}
```

**Response:**
```json
{
  "message": "String",
  "updatedProducts": "String"
}
```

---

### `GET` `/api/inventory/statistics`
- **API Name:** getInventoryStatistics
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
*(Map of statistics)*

---

### `GET` `/api/inventory/health`
- **API Name:** health
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
```json
{
  "status": "String - UP",
  "service": "String - product-service-inventory",
  "timestamp": "DateTime"
}
```

---

## ProductController

### `POST` `/api/products`
- **API Name:** createProduct
- **Type:** REST / Synchronous

**Request:**
```json
{
  "sku": "String - Required (3-50 chars)",
  "name": "String - Required (2-200 chars)",
  "description": "String (Max 1000 chars)",
  "shortDescription": "String (Max 255 chars)",
  "categoryId": "UUID - Required",
  "price": "BigDecimal - Required (Min 0.01)",
  "discountPrice": "BigDecimal (Min 0.00)",
  "status": "String - Default ACTIVE",
  "isFeatured": "Boolean - Default false",
  "preparationTimeMinutes": "Integer (Min 0)",
  "shelfLifeHours": "Integer (Min 0)",
  "unit": "String - Default 'piece'",
  "weightGrams": "Integer (Min 0)",
  "caloriesPerUnit": "Integer (Min 0)",
  "ingredients": ["String"],
  "allergens": ["String"],
  "tags": ["String"],
  "initialStock": "Integer - Default 0",
  "minimumStock": "Integer - Default 0",
  "reorderLevel": "Integer - Default 0"
}
```

**Response:**
```json
{
  "id": "UUID",
  "sku": "String",
  "name": "String",
  "description": "String",
  "shortDescription": "String",
  "category": {
    "id": "UUID",
    "name": "String",
    "iconClass": "String"
  },
  "price": "BigDecimal",
  "discountPrice": "BigDecimal",
  "effectivePrice": "BigDecimal",
  "status": "String (ACTIVE, INACTIVE, DISCONTINUED)",
  "isFeatured": "Boolean",
  "preparationTimeMinutes": "Integer",
  "shelfLifeHours": "Integer",
  "unit": "String",
  "weightGrams": "Integer",
  "caloriesPerUnit": "Integer",
  "ingredients": ["String"],
  "allergens": ["String"],
  "tags": ["String"],
  "inventory": {
    "currentStock": "Integer",
    "availableStock": "Integer",
    "isLowStock": "Boolean",
    "isOutOfStock": "Boolean",
    "status": "String"
  },
  "images": [
    {
      "id": "UUID",
      "imageUrl": "String",
      "altText": "String",
      "isPrimary": "Boolean",
      "displayOrder": "Integer",
      "fileSizeBytes": "Long",
      "imageWidth": "Integer",
      "imageHeight": "Integer",
      "createdAt": "DateTime"
    }
  ],
  "primaryImageUrl": "String",
  "isAvailable": "Boolean",
  "isOnSale": "Boolean",
  "createdAt": "DateTime",
  "updatedAt": "DateTime"
}
```

---

### `GET` `/api/products`
- **API Name:** getAllProducts
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
*(List of ProductResponse)*

---

### `GET` `/api/products/active`
- **API Name:** getActiveProducts
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
*(List of ProductResponse)*

---

### `GET` `/api/products/available`
- **API Name:** getAvailableProducts
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
*(List of ProductResponse)*

---

### `GET` `/api/products/featured`
- **API Name:** getFeaturedProducts
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
*(List of ProductResponse)*

---

### `GET` `/api/products/on-sale`
- **API Name:** getProductsOnSale
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
*(List of ProductResponse)*

---

### `GET` `/api/products/recent`
- **API Name:** getRecentlyAddedProducts
- **Type:** REST / Synchronous
- **Query Parameters:** `days` (int, default 7)

**Request:**
None

**Response:**
*(List of ProductResponse)*

---

### `GET` `/api/products/{productId}`
- **API Name:** getProductById
- **Type:** REST / Synchronous
- **Path Variable:** `productId` (UUID)

**Request:**
None

**Response:**
*(ProductResponse)*

---

### `GET` `/api/products/batch`
- **API Name:** getProductsByIds
- **Type:** REST / Synchronous
- **Query Parameters:** `productIds` (List<UUID>)

**Request:**
None

**Response:**
*(List of ProductResponse)*

---

### `POST` `/api/products/batch/validate`
- **API Name:** validateProducts
- **Type:** REST / Synchronous

**Request:**
```json
[
  "UUID"
]
```

**Response:**
*(List of ProductResponse)*

---

### `GET` `/api/products/sku/{sku}`
- **API Name:** getProductBySku
- **Type:** REST / Synchronous
- **Path Variable:** `sku` (String)

**Request:**
None

**Response:**
*(ProductResponse)*

---

### `GET` `/api/products/category/{categoryId}`
- **API Name:** getProductsByCategory
- **Type:** REST / Synchronous
- **Path Variable:** `categoryId` (UUID)

**Request:**
None

**Response:**
*(List of ProductResponse)*

---

### `GET` `/api/products/category/{categoryId}/paginated`
- **API Name:** getProductsByCategoryWithPagination
- **Type:** REST / Synchronous
- **Path Variable:** `categoryId` (UUID)
- **Query Parameters:** `page` (int), `size` (int), `sortBy` (String), `sortDir` (String)

**Request:**
None

**Response:**
*(Page of ProductResponse)*

---

### `GET` `/api/products/search`
- **API Name:** searchProducts
- **Type:** REST / Synchronous
- **Query Parameters:** `query` (String)

**Request:**
None

**Response:**
*(List of ProductResponse)*

---

### `GET` `/api/products/search/paginated`
- **API Name:** searchProductsWithPagination
- **Type:** REST / Synchronous
- **Query Parameters:** `query` (String), `page` (int), `size` (int), `sortBy` (String), `sortDir` (String)

**Request:**
None

**Response:**
*(Page of ProductResponse)*

---

### `GET` `/api/products/price-range`
- **API Name:** getProductsByPriceRange
- **Type:** REST / Synchronous
- **Query Parameters:** `minPrice` (BigDecimal), `maxPrice` (BigDecimal)

**Request:**
None

**Response:**
*(List of ProductResponse)*

---

### `GET` `/api/products/tag/{tag}`
- **API Name:** getProductsByTag
- **Type:** REST / Synchronous
- **Path Variable:** `tag` (String)

**Request:**
None

**Response:**
*(List of ProductResponse)*

---

### `GET` `/api/products/without-allergen/{allergen}`
- **API Name:** getProductsWithoutAllergen
- **Type:** REST / Synchronous
- **Path Variable:** `allergen` (String)

**Request:**
None

**Response:**
*(List of ProductResponse)*

---

### `GET` `/api/products/filter`
- **API Name:** searchProductsWithFilters
- **Type:** REST / Synchronous
- **Query Parameters:** `categoryId` (UUID), `status` (String), `minPrice` (BigDecimal), `maxPrice` (BigDecimal), `inStock` (Boolean)

**Request:**
None

**Response:**
*(List of ProductResponse)*

---

### `PUT` `/api/products/{productId}`
- **API Name:** updateProduct
- **Type:** REST / Synchronous
- **Path Variable:** `productId` (UUID)

**Request:**
*(Same as createProduct ProductRequest)*

**Response:**
*(ProductResponse)*

---

### `PATCH` `/api/products/{productId}/status`
- **API Name:** updateProductStatus
- **Type:** REST / Synchronous
- **Path Variable:** `productId` (UUID)

**Request:**
```json
{
  "status": "String (e.g., ACTIVE, INACTIVE, DISCONTINUED)"
}
```

**Response:**
*(ProductResponse)*

---

### `POST` `/api/products/{productId}/toggle-featured`
- **API Name:** toggleFeaturedStatus
- **Type:** REST / Synchronous
- **Path Variable:** `productId` (UUID)

**Request:**
None

**Response:**
*(ProductResponse)*

---

### `DELETE` `/api/products/{productId}`
- **API Name:** deleteProduct
- **Type:** REST / Synchronous
- **Path Variable:** `productId` (UUID)

**Request:**
None

**Response:**
```json
{
  "message": "String - Product deleted successfully",
  "productId": "String"
}
```

---

### `GET` `/api/products/{productId}/availability`
- **API Name:** checkProductAvailability
- **Type:** REST / Synchronous
- **Path Variable:** `productId` (UUID)

**Request:**
None

**Response:**
```json
{
  "productId": "UUID",
  "available": "Boolean"
}
```

---

### `GET` `/api/products/statistics`
- **API Name:** getProductStatistics
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
*(Map of statistics)*

---

### `GET` `/api/products/health`
- **API Name:** health
- **Type:** REST / Synchronous

**Request:**
None

**Response:**
```json
{
  "status": "String - UP",
  "service": "String - product-service-products",
  "timestamp": "DateTime"
}
```
