package com.blubugtech.bakery_product_service.repository;

import com.blubugtech.bakery_product_service.entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByProductId(String productId);
    Optional<Review> findByProductIdAndOrderId(String productId, String orderId);
}
