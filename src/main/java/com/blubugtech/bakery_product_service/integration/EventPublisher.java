package com.blubugtech.bakery_product_service.integration;

import com.blubugtech.bakery_product_service.entity.Product;

public interface EventPublisher {
    void publishProductEvent(Product product, String action);
    void publishProductDeletedEvent(String productId);
}
