package com.blubugtech.bakery_product_service.controller;

import com.blubugtech.bakery_product_service.dto.inventory.InventoryResponse;
import com.blubugtech.bakery_product_service.dto.inventory.InventoryUpdateRequest;
import com.blubugtech.bakery_product_service.service.InventoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<InventoryResponse>> getAllInventory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get all inventory request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryResponse> inventory = inventoryService.getAllInventory(pageable);

        logger.info("Retrieved {} inventory items", inventory.getContent().size());
        return ResponseEntity.ok(inventory);
    }

    // Get inventory by product ID
    @GetMapping("/product/{productId}")
    public ResponseEntity<InventoryResponse> getInventoryByProductId(@PathVariable String productId) {
        logger.info("Get inventory by product ID request received: {}", productId);

        InventoryResponse inventory = inventoryService.getInventoryByProductId(productId);

        logger.info("Inventory retrieved for product: {}", productId);
        return ResponseEntity.ok(inventory);
    }

    // Get inventory by product SKU
    @GetMapping("/sku/{sku}")
    public ResponseEntity<InventoryResponse> getInventoryByProductSku(@PathVariable String sku) {
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<InventoryResponse>> getLowStockItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get low stock items request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryResponse> lowStockItems = inventoryService.getLowStockItems(pageable);

        logger.info("Retrieved {} low stock items", lowStockItems.getContent().size());
        return ResponseEntity.ok(lowStockItems);
    }

    // Get out of stock items
    @GetMapping("/out-of-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<InventoryResponse>> getOutOfStockItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get out of stock items request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryResponse> outOfStockItems = inventoryService.getOutOfStockItems(pageable);

        logger.info("Retrieved {} out of stock items", outOfStockItems.getContent().size());
        return ResponseEntity.ok(outOfStockItems);
    }

    // Get items needing reorder
    @GetMapping("/needs-reorder")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<InventoryResponse>> getItemsNeedingReorder(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get items needing reorder request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryResponse> itemsNeedingReorder = inventoryService.getItemsNeedingReorder(pageable);

        logger.info("Retrieved {} items needing reorder", itemsNeedingReorder.getContent().size());
        return ResponseEntity.ok(itemsNeedingReorder);
    }

    // Get expired items
    @GetMapping("/expired")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<InventoryResponse>> getExpiredItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get expired items request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryResponse> expiredItems = inventoryService.getExpiredItems(pageable);

        logger.info("Retrieved {} expired items", expiredItems.getContent().size());
        return ResponseEntity.ok(expiredItems);
    }

    // Get items expiring soon
    @GetMapping("/expiring-soon")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<InventoryResponse>> getItemsExpiringSoon(
            @RequestParam(defaultValue = "24") int hours,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get items expiring soon request received (within {} hours, page {}, size {})", hours, page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryResponse> expiringSoonItems = inventoryService.getItemsExpiringSoon(hours, pageable);

        logger.info("Retrieved {} items expiring soon", expiringSoonItems.getContent().size());
        return ResponseEntity.ok(expiringSoonItems);
    }

    // Update inventory
    @PutMapping("/product/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventoryResponse> updateInventory(
            @PathVariable String productId,
            @Valid @RequestBody InventoryUpdateRequest request) {

        logger.info("Update inventory request received for product: {}", productId);

        InventoryResponse inventory = inventoryService.updateInventory(productId, request);

        logger.info("Inventory updated successfully for product: {}", productId);
        return ResponseEntity.ok(inventory);
    }

    // Add stock (restock)
    @PostMapping("/product/{productId}/add-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventoryResponse> addStock(
            @PathVariable String productId,
            @RequestBody Map<String, Object> request) {

        logger.info("Add stock request received for product: {}", productId);

        Integer quantity = (Integer) request.get("quantity");
        String notes = (String) request.get("notes");

        InventoryResponse inventory = inventoryService.addStock(productId, quantity, notes);

        logger.info("Stock added successfully: {} units for product: {}", quantity, productId);
        return ResponseEntity.ok(inventory);
    }

    // Reserve stock
    @PostMapping("/product/{productId}/reserve")
    public ResponseEntity<com.blubugtech.bakery_product_service.dto.inventory.StockOperationResponse> reserveStock(
            @PathVariable String productId,
            @RequestBody Map<String, Integer> request) {

        logger.info("Reserve stock request received for product: {}", productId);

        Integer quantity = request.get("quantity");
        boolean success = inventoryService.reserveStock(productId, quantity);

        com.blubugtech.bakery_product_service.dto.inventory.StockOperationResponse response = new com.blubugtech.bakery_product_service.dto.inventory.StockOperationResponse();
        response.setSuccess(success);
        response.setProductId(productId);
        response.setQuantity(quantity);

        if (success) {
            response.setMessage("Stock reserved successfully");
            logger.info("Stock reserved successfully: {} units for product: {}", quantity, productId);
        } else {
            response.setMessage("Insufficient stock to reserve");
            logger.warn("Failed to reserve stock: {} units for product: {}", quantity, productId);
        }

        return ResponseEntity.ok(response);
    }

    // Release reserved stock
    @PostMapping("/product/{productId}/release-reserved")
    public ResponseEntity<com.blubugtech.bakery_product_service.dto.inventory.StockOperationResponse> releaseReservedStock(
            @PathVariable String productId,
            @RequestBody Map<String, Integer> request) {

        logger.info("Release reserved stock request received for product: {}", productId);

        Integer quantity = request.get("quantity");
        inventoryService.releaseReservedStock(productId, quantity);

        com.blubugtech.bakery_product_service.dto.inventory.StockOperationResponse response = new com.blubugtech.bakery_product_service.dto.inventory.StockOperationResponse(true, productId, quantity, "Reserved stock released successfully");

        logger.info("Reserved stock released: {} units for product: {}", quantity, productId);
        return ResponseEntity.ok(response);
    }

    // Consume stock
    @PostMapping("/product/{productId}/consume")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<com.blubugtech.bakery_product_service.dto.inventory.StockOperationResponse> consumeStock(
            @PathVariable String productId,
            @RequestBody Map<String, Integer> request) {

        logger.info("Consume stock request received for product: {}", productId);

        Integer quantity = request.get("quantity");
        inventoryService.consumeStock(productId, quantity);

        com.blubugtech.bakery_product_service.dto.inventory.StockOperationResponse response = new com.blubugtech.bakery_product_service.dto.inventory.StockOperationResponse(true, productId, quantity, "Stock consumed successfully");

        logger.info("Stock consumed: {} units for product: {}", quantity, productId);
        return ResponseEntity.ok(response);
    }

    // Check stock availability
    @GetMapping("/product/{productId}/availability")
    public ResponseEntity<com.blubugtech.bakery_product_service.dto.inventory.StockAvailabilityResponse> checkStockAvailability(
            @PathVariable String productId,
            @RequestParam Integer quantity) {

        logger.info("Check stock availability request received for product: {} (quantity: {})",
                productId, quantity);

        boolean available = inventoryService.checkStockAvailability(productId, quantity);
        Integer availableStock = inventoryService.getAvailableStock(productId);

        com.blubugtech.bakery_product_service.dto.inventory.StockAvailabilityResponse response = new com.blubugtech.bakery_product_service.dto.inventory.StockAvailabilityResponse(productId, quantity, availableStock, available);

        return ResponseEntity.ok(response);
    }

    // Get available stock for a product
    @GetMapping("/product/{productId}/available-stock")
    public ResponseEntity<com.blubugtech.bakery_product_service.dto.inventory.StockAvailabilityResponse> getAvailableStock(@PathVariable String productId) {
        logger.info("Get available stock request received for product: {}", productId);

        Integer availableStock = inventoryService.getAvailableStock(productId);

        com.blubugtech.bakery_product_service.dto.inventory.StockAvailabilityResponse response = new com.blubugtech.bakery_product_service.dto.inventory.StockAvailabilityResponse(productId, null, availableStock, null);

        return ResponseEntity.ok(response);
    }

    // Bulk update minimum stock levels
    @PostMapping("/bulk-update-minimum-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<com.blubugtech.common.contract.feign.MessageResponse> bulkUpdateMinimumStock(
            @RequestBody Map<String, Integer> productMinimumStocks) {

        logger.info("Bulk update minimum stock request received for {} products",
                productMinimumStocks.size());

        inventoryService.bulkUpdateMinimumStock(productMinimumStocks);

        logger.info("Bulk minimum stock update completed for {} products", productMinimumStocks.size());
        return ResponseEntity.ok(new com.blubugtech.common.contract.feign.MessageResponse("Minimum stock levels updated successfully. Updated products: " + productMinimumStocks.size()));
    }

    // Get inventory statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getInventoryStatistics() {
        logger.info("Get inventory statistics request received");

        Map<String, Object> statistics = inventoryService.getInventoryStatistics();

        logger.info("Inventory statistics retrieved");
        return ResponseEntity.ok(statistics);
    }

    // Health check
    @GetMapping("/health")
    public ResponseEntity<com.blubugtech.common.contract.feign.HealthResponse> health() {
        return ResponseEntity.ok(new com.blubugtech.common.contract.feign.HealthResponse("UP", "product-service-inventory"));
    }
}
