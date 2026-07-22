package com.blubugtech.bakery_product_service.repository;

import com.blubugtech.bakery_product_service.model.Storefront;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorefrontRepository extends MongoRepository<Storefront, String> {
}

