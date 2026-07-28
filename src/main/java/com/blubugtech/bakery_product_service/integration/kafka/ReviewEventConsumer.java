package com.blubugtech.bakery_product_service.integration.kafka;

import com.blubugtech.bakery_product_service.entity.Product;
import com.blubugtech.bakery_product_service.repository.ProductRepository;
import org.blubakery.bakery_common_libs.constants.KafkaTopics;
import org.blubakery.bakery_common_libs.contract.messaging.ReviewPayload;
import org.blubakery.bakery_common_libs.event.ReviewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewEventConsumer {

    private final ProductRepository productRepository;

    @KafkaListener(topics = KafkaTopics.REVIEWS_TOPIC, groupId = "product-service-review-group")
    public void consumeReviewEvent(ReviewEvent event) {
        log.info("Received ReviewEvent: {}", event.getEventId());

        try {
            ReviewPayload payload = event.getPayload();
            String productId = payload.getProductId();
            
            if (productId == null) {
                log.warn("Received ReviewEvent without a valid productId. Skipping...");
                return;
            }

            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                
                // Update stats based on payload from Engagement Service
                if (payload.getAverageRating() != null) {
                    product.setAverageRating(payload.getAverageRating());
                }
                if (payload.getTotalReviews() != null) {
                    product.setTotalReviews(payload.getTotalReviews());
                }
                
                productRepository.save(product);
                log.info("Successfully updated averageRating for product {}", productId);
            } else {
                log.warn("Product {} not found. Could not update rating from ReviewEvent", productId);
            }

        } catch (Exception e) {
            log.error("Error processing ReviewEvent: {}", event.getEventId(), e);
        }
    }
}
