package com.shah_s.bakery_product_service.controller;

import com.shah_s.bakery_product_service.dto.InventoryResponseDto;
import com.shah_s.bakery_product_service.dto.InventoryUpdateRequestDto;
import com.shah_s.bakery_product_service.service.InventoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/inventory")

public class InventoryController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    final private InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // Get all inventory items
    @GetMapping
    public ResponseEntity<Page<InventoryResponseDto>> getAllInventory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get all inventory request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryResponseDto> inventory = inventoryService.getAllInventory(pageable);

        logger.info("Retrieved {} inventory items", inventory.getContent().size());
        return ResponseEntity.ok(inventory);
    }

    // Get inventory by product ID
    @GetMapping("/product/{productId}")
    public ResponseEntity<InventoryResponseDto> getInventoryByProductId(@PathVariable String productId) {
        logger.info("Get inventory by product ID request received: {}", productId);

        InventoryResponseDto inventory = inventoryService.getInventoryByProductId(productId);

        logger.info("Inventory retrieved for product: {}", productId);
        return ResponseEntity.ok(inventory);
    }

    // Get inventory by product SKU
    @GetMapping("/sku/{sku}")
    public ResponseEntity<InventoryResponseDto> getInventoryByProductSku(@PathVariable String sku) {
        logger.info("Get inventory by product SKU request received: {}", sku);

        return inventoryService.getInventoryByProductSku(sku)
                .map(inventory -> {
                    logger.info("Inventory found for SKU: {}", sku);
                    return ResponseEntity.ok(inventory);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Get low stock items
    @GetMapping("/low-stock")
    public ResponseEntity<Page<InventoryResponseDto>> getLowStockItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get low stock items request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryResponseDto> lowStockItems = inventoryService.getLowStockItems(pageable);

        logger.info("Retrieved {} low stock items", lowStockItems.getContent().size());
        return ResponseEntity.ok(lowStockItems);
    }

    // Get out of stock items
    @GetMapping("/out-of-stock")
    public ResponseEntity<Page<InventoryResponseDto>> getOutOfStockItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get out of stock items request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryResponseDto> outOfStockItems = inventoryService.getOutOfStockItems(pageable);

        logger.info("Retrieved {} out of stock items", outOfStockItems.getContent().size());
        return ResponseEntity.ok(outOfStockItems);
    }

    // Get items needing reorder
    @GetMapping("/needs-reorder")
    public ResponseEntity<Page<InventoryResponseDto>> getItemsNeedingReorder(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get items needing reorder request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryResponseDto> itemsNeedingReorder = inventoryService.getItemsNeedingReorder(pageable);

        logger.info("Retrieved {} items needing reorder", itemsNeedingReorder.getContent().size());
        return ResponseEntity.ok(itemsNeedingReorder);
    }

    // Get expired items
    @GetMapping("/expired")
    public ResponseEntity<Page<InventoryResponseDto>> getExpiredItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get expired items request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryResponseDto> expiredItems = inventoryService.getExpiredItems(pageable);

        logger.info("Retrieved {} expired items", expiredItems.getContent().size());
        return ResponseEntity.ok(expiredItems);
    }

    // Get items expiring soon
    @GetMapping("/expiring-soon")
    public ResponseEntity<Page<InventoryResponseDto>> getItemsExpiringSoon(
            @RequestParam(defaultValue = "24") int hours,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get items expiring soon request received (within {} hours, page {}, size {})", hours, page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryResponseDto> expiringSoonItems = inventoryService.getItemsExpiringSoon(hours, pageable);

        logger.info("Retrieved {} items expiring soon", expiringSoonItems.getContent().size());
        return ResponseEntity.ok(expiringSoonItems);
    }

    // Update inventory
    @PutMapping("/product/{productId}")
    public ResponseEntity<InventoryResponseDto> updateInventory(
            @PathVariable String productId,
            @Valid @RequestBody InventoryUpdateRequestDto request) {

        logger.info("Update inventory request received for product: {}", productId);

        InventoryResponseDto inventory = inventoryService.updateInventory(productId, request);

        logger.info("Inventory updated successfully for product: {}", productId);
        return ResponseEntity.ok(inventory);
    }

    // Add stock (restock)
    @PostMapping("/product/{productId}/add-stock")
    public ResponseEntity<InventoryResponseDto> addStock(
            @PathVariable String productId,
            @RequestBody Map<String, Object> request) {

        logger.info("Add stock request received for product: {}", productId);

        Integer quantity = (Integer) request.get("quantity");
        String notes = (String) request.get("notes");

        InventoryResponseDto inventory = inventoryService.addStock(productId, quantity, notes);

        logger.info("Stock added successfully: {} units for product: {}", quantity, productId);
        return ResponseEntity.ok(inventory);
    }

    // Reserve stock
    @PostMapping("/product/{productId}/reserve")
    public ResponseEntity<Map<String, Object>> reserveStock(
            @PathVariable String productId,
            @RequestBody Map<String, Integer> request) {

        logger.info("Reserve stock request received for product: {}", productId);

        Integer quantity = request.get("quantity");
        boolean success = inventoryService.reserveStock(productId, quantity);

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("productId", productId);
        response.put("quantity", quantity);

        if (success) {
            response.put("message", "Stock reserved successfully");
            logger.info("Stock reserved successfully: {} units for product: {}", quantity, productId);
        } else {
            response.put("message", "Insufficient stock to reserve");
            logger.warn("Failed to reserve stock: {} units for product: {}", quantity, productId);
        }

        return ResponseEntity.ok(response);
    }

    // Release reserved stock
    @PostMapping("/product/{productId}/release-reserved")
    public ResponseEntity<Map<String, String>> releaseReservedStock(
            @PathVariable String productId,
            @RequestBody Map<String, Integer> request) {

        logger.info("Release reserved stock request received for product: {}", productId);

        Integer quantity = request.get("quantity");
        inventoryService.releaseReservedStock(productId, quantity);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Reserved stock released successfully");
        response.put("productId", productId.toString());
        response.put("quantity", quantity.toString());

        logger.info("Reserved stock released: {} units for product: {}", quantity, productId);
        return ResponseEntity.ok(response);
    }

    // Consume stock
    @PostMapping("/product/{productId}/consume")
    public ResponseEntity<Map<String, String>> consumeStock(
            @PathVariable String productId,
            @RequestBody Map<String, Integer> request) {

        logger.info("Consume stock request received for product: {}", productId);

        Integer quantity = request.get("quantity");
        inventoryService.consumeStock(productId, quantity);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Stock consumed successfully");
        response.put("productId", productId.toString());
        response.put("quantity", quantity.toString());

        logger.info("Stock consumed: {} units for product: {}", quantity, productId);
        return ResponseEntity.ok(response);
    }

    // Check stock availability
    @GetMapping("/product/{productId}/availability")
    public ResponseEntity<Map<String, Object>> checkStockAvailability(
            @PathVariable String productId,
            @RequestParam Integer quantity) {

        logger.info("Check stock availability request received for product: {} (quantity: {})",
                productId, quantity);

        boolean available = inventoryService.checkStockAvailability(productId, quantity);
        Integer availableStock = inventoryService.getAvailableStock(productId);

        Map<String, Object> response = new HashMap<>();
        response.put("productId", productId);
        response.put("requestedQuantity", quantity);
        response.put("availableStock", availableStock);
        response.put("sufficient", available);

        return ResponseEntity.ok(response);
    }

    // Get available stock for a product
    @GetMapping("/product/{productId}/available-stock")
    public ResponseEntity<Map<String, Object>> getAvailableStock(@PathVariable String productId) {
        logger.info("Get available stock request received for product: {}", productId);

        Integer availableStock = inventoryService.getAvailableStock(productId);

        Map<String, Object> response = new HashMap<>();
        response.put("productId", productId);
        response.put("availableStock", availableStock);

        return ResponseEntity.ok(response);
    }

    // Bulk update minimum stock levels
    @PostMapping("/bulk-update-minimum-stock")
    public ResponseEntity<Map<String, String>> bulkUpdateMinimumStock(
            @RequestBody Map<String, Integer> productMinimumStocks) {

        logger.info("Bulk update minimum stock request received for {} products",
                productMinimumStocks.size());

        inventoryService.bulkUpdateMinimumStock(productMinimumStocks);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Minimum stock levels updated successfully");
        response.put("updatedProducts", String.valueOf(productMinimumStocks.size()));

        logger.info("Bulk minimum stock update completed for {} products", productMinimumStocks.size());
        return ResponseEntity.ok(response);
    }

    // Get inventory statistics
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getInventoryStatistics() {
        logger.info("Get inventory statistics request received");

        Map<String, Object> statistics = inventoryService.getInventoryStatistics();

        logger.info("Inventory statistics retrieved");
        return ResponseEntity.ok(statistics);
    }

    // Health check
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "product-service-inventory");
        response.put("timestamp", java.time.LocalDateTime.now().toString());

        return ResponseEntity.ok(response);
    }
}
