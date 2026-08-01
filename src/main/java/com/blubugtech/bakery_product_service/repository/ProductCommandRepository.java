package com.blubugtech.bakery_product_service.repository;

import com.blubugtech.bakery_product_service.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductCommandRepository extends MongoRepository<Product, String> {
    Optional<Product> findById(String id);
    boolean existsBySku(String sku);
}
