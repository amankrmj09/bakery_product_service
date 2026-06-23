package com.shah_s.bakery_product_service.service;

import com.shah_s.bakery_product_service.dto.InventoryResponse;
import com.shah_s.bakery_product_service.dto.InventoryUpdateRequest;
import com.shah_s.bakery_product_service.entity.Inventory;
import com.shah_s.bakery_product_service.entity.Product;
import com.shah_s.bakery_product_service.exception.ProductServiceException;
import com.shah_s.bakery_product_service.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.shah_s.bakery_product_service.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    final private InventoryRepository inventoryRepository;

    @Value("${product.inventory.low-stock-threshold:10}")
    private Integer defaultLowStockThreshold;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    // Create inventory for new product
    public Inventory createInventoryForProduct(Product product, Integer initialStock,
                                             Integer minimumStock, Integer reorderLevel) {
        logger.info("Creating inventory for product: {} (SKU: {})", product.getName(), product.getSku());

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setCurrentStock(initialStock != null ? initialStock : 0);
        inventory.setMinimumStock(minimumStock != null ? minimumStock : defaultLowStockThreshold);
        inventory.setReorderLevel(reorderLevel != null ? reorderLevel : minimumStock);
        inventory.setReservedStock(0);
        inventory.updateStatus(); // Update status based on stock levels

        Inventory savedInventory = inventoryRepository.save(inventory);
        logger.info("Inventory created for product: {}", product.getId());

        return savedInventory;
    }

    // Get inventory by product ID
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryByProductId(UUID productId) {
        logger.debug("Fetching inventory for product: {}", productId);

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductServiceException("Inventory not found for product: " + productId));

        return InventoryResponse.from(inventory);
    }

    // Get inventory by product SKU
    @Transactional(readOnly = true)
    public Optional<InventoryResponse> getInventoryByProductSku(String sku) {
        logger.debug("Fetching inventory for product SKU: {}", sku);

        return inventoryRepository.findByProductSku(sku)
                .map(InventoryResponse::from);
    }

    // Get all inventory items
    @Transactional(readOnly = true)
    public List<InventoryResponse> getAllInventory() {
        logger.debug("Fetching all inventory items");

        return inventoryRepository.findAll().stream()
                .map(InventoryResponse::from)
                .collect(Collectors.toList());
    }

    // Get low stock items
    @Transactional(readOnly = true)
    public List<InventoryResponse> getLowStockItems() {
        logger.debug("Fetching low stock items");

        return inventoryRepository.findLowStockItems().stream()
                .map(InventoryResponse::from)
                .collect(Collectors.toList());
    }

    // Get out of stock items
    @Transactional(readOnly = true)
    public List<InventoryResponse> getOutOfStockItems() {
        logger.debug("Fetching out of stock items");

        return inventoryRepository.findOutOfStockItems().stream()
                .map(InventoryResponse::from)
                .collect(Collectors.toList());
    }

    // Get items needing reorder
    @Transactional(readOnly = true)
    public List<InventoryResponse> getItemsNeedingReorder() {
        logger.debug("Fetching items needing reorder");

        return inventoryRepository.findItemsNeedingReorder().stream()
                .map(InventoryResponse::from)
                .collect(Collectors.toList());
    }

    // Get expired items
    @Transactional(readOnly = true)
    public List<InventoryResponse> getExpiredItems() {
        logger.debug("Fetching expired items");

        return inventoryRepository.findExpiredItems().stream()
                .map(InventoryResponse::from)
                .collect(Collectors.toList());
    }

    // Get items expiring soon
    @Transactional(readOnly = true)
    public List<InventoryResponse> getItemsExpiringSoon(int hours) {
        logger.debug("Fetching items expiring within {} hours", hours);

        LocalDateTime cutoffTime = LocalDateTime.now().plusHours(hours);
        return inventoryRepository.findItemsExpiringSoon(cutoffTime).stream()
                .map(InventoryResponse::from)
                .collect(Collectors.toList());
    }

    // Update inventory
    public InventoryResponse updateInventory(UUID productId, InventoryUpdateRequest request) {
        logger.info("Updating inventory for product: {}", productId);

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductServiceException("Inventory not found for product: " + productId));

        // Update fields
        inventory.setCurrentStock(request.getCurrentStock());

        if (request.getReservedStock() != null) {
            inventory.setReservedStock(request.getReservedStock());
        }
        if (request.getMinimumStock() != null) {
            inventory.setMinimumStock(request.getMinimumStock());
        }
        if (request.getMaximumStock() != null) {
            inventory.setMaximumStock(request.getMaximumStock());
        }
        if (request.getReorderLevel() != null) {
            inventory.setReorderLevel(request.getReorderLevel());
        }
        if (request.getReorderQuantity() != null) {
            inventory.setReorderQuantity(request.getReorderQuantity());
        }
        if (request.getAutoReorderEnabled() != null) {
            inventory.setAutoReorderEnabled(request.getAutoReorderEnabled());
        }
        if (request.getTrackExpiry() != null) {
            inventory.setTrackExpiry(request.getTrackExpiry());
        }
        if (request.getExpiryDate() != null) {
            inventory.setExpiryDate(request.getExpiryDate());
        }
        if (request.getSupplierInfo() != null) {
            inventory.setSupplierInfo(request.getSupplierInfo());
        }
        if (request.getStorageLocation() != null) {
            inventory.setStorageLocation(request.getStorageLocation());
        }
        if (request.getNotes() != null) {
            inventory.setNotes(request.getNotes());
        }

        // Update status based on new stock levels
        inventory.updateStatus();

        Inventory updatedInventory = inventoryRepository.save(inventory);
        logger.info("Inventory updated for product: {}", productId);

        return InventoryResponse.from(updatedInventory);
    }

    // Add stock (restock)
    public InventoryResponse addStock(UUID productId, Integer quantity, String notes) {
        logger.info("Adding {} units to inventory for product: {}", quantity, productId);

        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be positive");
        }

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductServiceException("Inventory not found for product: " + productId));

        int newStock = inventory.getCurrentStock() + quantity;
        inventory.setCurrentStock(newStock);
        inventory.setLastRestockedAt(LocalDateTime.now());
        inventory.setLastRestockedQuantity(quantity);

        if (notes != null && !notes.trim().isEmpty()) {
            String existingNotes = inventory.getNotes();
            String timestampedNote = LocalDateTime.now() + ": Restocked +" + quantity + " units. " + notes;
            inventory.setNotes(existingNotes == null ? timestampedNote : existingNotes + "\n" + timestampedNote);
        }

        inventory.updateStatus();

        Inventory updatedInventory = inventoryRepository.save(inventory);
        logger.info("Stock added successfully. New stock level: {} for product: {}", newStock, productId);

        return InventoryResponse.from(updatedInventory);
    }

    // Reserve stock for order
    public boolean reserveStock(UUID productId, Integer quantity) {
        logger.info("Reserving {} units for product: {}", quantity, productId);

        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be positive");
        }

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductServiceException("Inventory not found for product: " + productId));

        // Check if sufficient stock is available
        if (inventory.getAvailableStock() < quantity) {
            logger.warn("Insufficient stock to reserve {} units for product: {}. Available: {}",
                       quantity, productId, inventory.getAvailableStock());
            return false;
        }

        inventory.setReservedStock(inventory.getReservedStock() + quantity);
        inventory.updateStatus();

        inventoryRepository.save(inventory);
        logger.info("Stock reserved successfully: {} units for product: {}", quantity, productId);

        return true;
    }

    // Release reserved stock
    public void releaseReservedStock(UUID productId, Integer quantity) {
        logger.info("Releasing {} reserved units for product: {}", quantity, productId);

        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be positive");
        }

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductServiceException("Inventory not found for product: " + productId));

        if (inventory.getReservedStock() < quantity) {
            throw new ProductServiceException("Cannot release more stock than reserved");
        }

        inventory.setReservedStock(inventory.getReservedStock() - quantity);
        inventory.updateStatus();

        inventoryRepository.save(inventory);
        logger.info("Reserved stock released: {} units for product: {}", quantity, productId);
    }

    // Consume stock (fulfill order)
    public void consumeStock(UUID productId, Integer quantity) {
        logger.info("Consuming {} units for product: {}", quantity, productId);

        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be positive");
        }

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductServiceException("Inventory not found for product: " + productId));

        if (inventory.getCurrentStock() < quantity) {
            throw new ProductServiceException("Insufficient stock to consume");
        }

        if (inventory.getReservedStock() < quantity) {
            throw new ProductServiceException("Insufficient reserved stock to consume");
        }

        inventory.setCurrentStock(inventory.getCurrentStock() - quantity);
        inventory.setReservedStock(inventory.getReservedStock() - quantity);
        inventory.updateStatus();

        inventoryRepository.save(inventory);
        logger.info("Stock consumed: {} units for product: {}. Remaining: {}",
                   quantity, productId, inventory.getCurrentStock());
    }

    // Check stock availability
    @Transactional(readOnly = true)
    public boolean checkStockAvailability(UUID productId, Integer requiredQuantity) {
        Boolean available = inventoryRepository.checkStockAvailability(productId, requiredQuantity);
        return available != null ? available : false;
    }

    // Get available stock
    @Transactional(readOnly = true)
    public Integer getAvailableStock(UUID productId) {
        Integer stock = inventoryRepository.getAvailableStock(productId);
        return stock != null ? stock : 0;
    }

    // Get inventory statistics
    @Transactional(readOnly = true)
    public Map<String, Object> getInventoryStatistics() {
        logger.debug("Fetching inventory statistics");

        Object[] stats = inventoryRepository.getInventoryStatistics();
        if (stats == null || stats.length < 5) {
            stats = new Object[]{0L, 0L, 0L, 0L, 0L};
        }
        Double totalValue = inventoryRepository.getTotalInventoryValue();

        return Map.of(
                "totalItems", stats[0] != null ? stats[0] : 0L,
                "totalStock", stats[1] != null ? stats[1] : 0L,
                "totalReservedStock", stats[2] != null ? stats[2] : 0L,
                "lowStockItems", stats[3] != null ? stats[3] : 0L,
                "outOfStockItems", stats[4] != null ? stats[4] : 0L,
                "totalInventoryValue", totalValue != null ? totalValue : 0.0
        );
    }

    // Bulk update minimum stock levels
    public void bulkUpdateMinimumStock(Map<UUID, Integer> productMinimumStocks) {
        logger.info("Bulk updating minimum stock levels for {} products", productMinimumStocks.size());

        for (Map.Entry<UUID, Integer> entry : productMinimumStocks.entrySet()) {
            UUID productId = entry.getKey();
            Integer minimumStock = entry.getValue();

            inventoryRepository.updateMinimumStock(productId, minimumStock);
        }

        logger.info("Bulk minimum stock update completed");
    }
}
