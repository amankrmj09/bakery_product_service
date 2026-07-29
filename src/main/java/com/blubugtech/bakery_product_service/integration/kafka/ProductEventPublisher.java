package com.blubugtech.bakery_product_service.integration.kafka;

import lombok.extern.slf4j.Slf4j;
import org.blubakery.bakery_common_libs.event.ProductEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    public ProductEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    public void publishProductUpdated(ProductEvent event) {
        log.info("Publishing ProductUpdated event for product ID: {}", event.getEventId());
        kafkaTemplate.send(org.blubakery.bakery_common_libs.constants.KafkaTopics.PRODUCT_TOPIC, event.getEventId().toString(), event);
    }
}
