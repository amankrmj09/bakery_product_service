package com.blubugtech.bakery_product_service.repository;

import com.blubugtech.bakery_product_service.model.SiteConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteConfigRepository extends MongoRepository<SiteConfig, String> {
}
