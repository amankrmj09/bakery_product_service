package com.blubugtech.bakery_product_service.service;

import com.blubugtech.common.event.ProductEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductEventPublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductEventPublisher.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    public ProductEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    public void publishProductUpdated(ProductEvent event) {
        logger.info("Publishing ProductUpdated event for product ID: {}", event.getProductId());
        kafkaTemplate.send("product-events", event.getProductId().toString(), event);
    }
}
