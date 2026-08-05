package com.blubugtech.bakery_product_service.repository;

import com.blubugtech.bakery_product_service.entity.TaxRate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxRateRepository extends MongoRepository<TaxRate, String> {
    Optional<TaxRate> findByType(String type);
    boolean existsByType(String type);
    org.springframework.data.domain.Page<TaxRate> findAll(org.springframework.data.domain.Pageable pageable);
}
