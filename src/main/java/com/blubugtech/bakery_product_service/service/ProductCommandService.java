package com.blubugtech.bakery_product_service.service;

import com.blubugtech.bakery_product_service.dto.product.ProductRequest;
import com.blubugtech.bakery_product_service.dto.product.ProductResponse;
import com.blubugtech.bakery_product_service.entity.Product;

public interface ProductCommandService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(String productId, ProductRequest request);
    ProductResponse updateProductStatus(String productId, Product.ProductStatus status);
    ProductResponse toggleFeaturedStatus(String productId);
    void deleteProduct(String productId);
}
