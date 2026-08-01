package com.blubugtech.bakery_product_service.cache;

import com.blubugtech.bakery_product_service.entity.Product;
import java.util.Optional;

public interface ProductCacheManager {
    void putProduct(Product product);
    Optional<Product> getProduct(String id);
    void evictProduct(String id);
    void clearCache();
}
