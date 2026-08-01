package com.blubugtech.bakery_product_service.service;

import com.blubugtech.bakery_product_service.dto.product.ProductResponse;
import com.blubugtech.bakery_product_service.entity.Product;

import java.util.List;

public interface ProductValidationService {
    boolean isProductAvailable(String productId);
    List<ProductResponse> validateProducts(List<String> productIds);
    Product getProductEntity(String productId);
}
