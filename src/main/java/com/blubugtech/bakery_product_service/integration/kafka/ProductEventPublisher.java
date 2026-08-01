package com.blubugtech.bakery_product_service.integration.kafka;

import com.blubugtech.bakery_product_service.entity.Product;
import com.blubugtech.bakery_product_service.integration.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.blubakery.common.messaging.event.ProductEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class ProductEventPublisher implements EventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    public ProductEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishProductEvent(Product product, String action) {
        try {
            org.blubakery.common.messaging.contract.messaging.ProductPayload payload = org.blubakery.common.messaging.contract.messaging.ProductPayload.builder()
                    .productId(UUID.fromString(product.getId()))
                    .name(product.getName())
                    .price(product.getPrice())
                    .action(action)
                    .timestamp(LocalDateTime.now())
                    .build();
            ProductEvent event = new ProductEvent();
            event.setEventId(UUID.randomUUID().toString());
            event.setEventType("PRODUCT_" + action.toUpperCase());
            event.setTimestamp(Instant.now());
            event.setPayload(payload);
            
            publishProductUpdated(event);
        } catch (Exception e) {
            log.error("Failed to publish ProductEvent for {}", product.getId(), e);
        }
    }

    @Override
    public void publishProductDeletedEvent(String productId) {
        try {
            org.blubakery.common.messaging.contract.messaging.ProductPayload payload = org.blubakery.common.messaging.contract.messaging.ProductPayload.builder()
                    .productId(UUID.fromString(productId))
                    .action("DELETED")
                    .timestamp(LocalDateTime.now())
                    .build();
            ProductEvent event = new ProductEvent();
            event.setEventId(UUID.randomUUID().toString());
            event.setEventType("PRODUCT_DELETED");
            event.setTimestamp(Instant.now());
            event.setPayload(payload);
            
            publishProductUpdated(event);
        } catch (Exception e) {
            log.error("Failed to publish ProductEvent for {}", productId, e);
        }
    }
    
    private void publishProductUpdated(ProductEvent event) {
        log.info("Publishing ProductUpdated event for product ID: {}", event.getEventId());
        kafkaTemplate.send(org.blubakery.common.messaging.constants.KafkaTopics.PRODUCT_TOPIC, event.getEventId().toString(), event);
    }
}
