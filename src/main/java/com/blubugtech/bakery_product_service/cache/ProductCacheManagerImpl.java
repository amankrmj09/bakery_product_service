package com.blubugtech.bakery_product_service.cache;

import com.blubugtech.bakery_product_service.entity.Product;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductCacheManagerImpl implements ProductCacheManager {

    @Override
    @CachePut(value = "products", key = "#product.id")
    public void putProduct(Product product) {
        // Cached by Spring
    }

    @Override
    @Cacheable(value = "products", key = "#id", unless = "#result == null")
    public Optional<Product> getProduct(String id) {
        return Optional.empty(); // Should be intercepted by Spring Cache
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public void evictProduct(String id) {
        // Evicted by Spring
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public void clearCache() {
        // Cleared by Spring
    }
}
