package com.blubugtech.bakery_product_service.controller;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    private final MongoTemplate mongoTemplate;

    public HealthController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // Main service health check
    @GetMapping("/health")
    public ResponseEntity<com.blubugtech.common.dto.HealthResponseDto> health() {
        com.blubugtech.common.dto.HealthResponseDto response = new com.blubugtech.common.dto.HealthResponseDto("UP", "bakery-product-service");
        Map<String, Object> details = new HashMap<>();
        details.put("version", "1.0.0");

        // Check database connectivity
        try {
            Document result = mongoTemplate.getDb().runCommand(new Document("ping", 1));
            details.put("database", "UP");
            details.put("databaseName", mongoTemplate.getDb().getName());
        } catch (Exception e) {
            details.put("database", "DOWN");
            details.put("databaseError", e.getMessage());
        }

        response.setDetails(details);
        return ResponseEntity.ok(response);
    }

    // Service info
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("serviceName", "Bakery Product Service");
        response.put("description", "Product catalog and inventory management service");
        response.put("version", "1.0.0");
        response.put("features", Map.of(
            "categories", "Product category management",
            "products", "Product catalog management",
            "inventory", "Stock and inventory tracking",
            "search", "Advanced product search and filtering"
        ));
        response.put("endpoints", Map.of(
            "categories", "/api/categories",
            "products", "/api/products",
            "inventory", "/api/inventory"
        ));
        return ResponseEntity.ok(response);
    }

    // Service metrics
    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> metrics() {
        Map<String, Object> response = new HashMap<>();
        response.put("uptime", getUptime());
        response.put("timestamp", LocalDateTime.now().toString());

        // Memory info
        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> memory = new HashMap<>();
        memory.put("maxMemory", runtime.maxMemory() / 1024 / 1024 + " MB");
        memory.put("totalMemory", runtime.totalMemory() / 1024 / 1024 + " MB");
        memory.put("freeMemory", runtime.freeMemory() / 1024 / 1024 + " MB");
        memory.put("usedMemory", (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024 + " MB");
        response.put("memory", memory);

        return ResponseEntity.ok(response);
    }

    private String getUptime() {
        long uptime = java.lang.management.ManagementFactory.getRuntimeMXBean().getUptime();
        long seconds = uptime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        return String.format("%d days, %d hours, %d minutes, %d seconds",
                days, hours % 24, minutes % 60, seconds % 60);
    }
}
