package com.shah_s.bakery_product_service.service;

import com.shah_s.bakery_product_service.dto.InventoryResponse;
import com.shah_s.bakery_product_service.dto.InventoryUpdateRequest;
import com.shah_s.bakery_product_service.entity.Inventory;
import com.shah_s.bakery_product_service.entity.Product;
import com.shah_s.bakery_product_service.exception.ProductServiceException;
import com.shah_s.bakery_product_service.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import com.shah_s.bakery_product_service.exception.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    final private ProductRepository productRepository;

    @Value("${product.inventory.low-stock-threshold:10}")
    private Integer defaultLowStockThreshold;

    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Inventory createInventoryForProduct(Product product, Integer initialStock,
                                             Integer minimumStock, Integer reorderLevel) {
        logger.info("Creating inventory for product: {} (SKU: {})", product.getName(), product.getSku());

        Inventory inventory = new Inventory();
        inventory.setCurrentStock(initialStock != null ? initialStock : 0);
        inventory.setMinimumStock(minimumStock != null ? minimumStock : defaultLowStockThreshold);
        inventory.setReorderLevel(reorderLevel != null ? reorderLevel : minimumStock);
        inventory.setReservedStock(0);
        inventory.updateStatus();

        product.setInventory(inventory);
        productRepository.save(product);
        logger.info("Inventory created for product: {}", product.getId());

        return inventory;
    }

    public InventoryResponse getInventoryByProductId(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found: " + productId));
        if (product.getInventory() == null) {
            throw new ProductServiceException("Inventory not found for product: " + productId);
        }
        return InventoryResponse.from(product);
    }

    public Optional<InventoryResponse> getInventoryByProductSku(String sku) {
        return productRepository.findBySku(sku)
                .filter(p -> p.getInventory() != null)
                .map(InventoryResponse::from);
    }

    public List<InventoryResponse> getAllInventory() {
        return productRepository.findAll().stream()
                .filter(p -> p.getInventory() != null)
                .map(InventoryResponse::from)
                .collect(Collectors.toList());
    }

    public List<InventoryResponse> getLowStockItems() {
        return productRepository.findLowStockProducts().stream()
                .filter(p -> p.getInventory() != null)
                .map(InventoryResponse::from)
                .collect(Collectors.toList());
    }

    public List<InventoryResponse> getOutOfStockItems() {
        return productRepository.findOutOfStockProducts().stream()
                .filter(p -> p.getInventory() != null)
                .map(InventoryResponse::from)
                .collect(Collectors.toList());
    }

    public List<InventoryResponse> getItemsNeedingReorder() {
        return productRepository.findProductsNeedingReorder().stream()
                .filter(p -> p.getInventory() != null)
                .map(InventoryResponse::from)
                .collect(Collectors.toList());
    }

    public List<InventoryResponse> getExpiredItems() {
        return productRepository.findProductsExpiringSoon(LocalDateTime.MIN, LocalDateTime.now()).stream()
                .filter(p -> p.getInventory() != null)
                .map(InventoryResponse::from)
                .collect(Collectors.toList());
    }

    public List<InventoryResponse> getItemsExpiringSoon(int hours) {
        return productRepository.findProductsExpiringSoon(LocalDateTime.now(), LocalDateTime.now().plusHours(hours)).stream()
                .filter(p -> p.getInventory() != null)
                .map(InventoryResponse::from)
                .collect(Collectors.toList());
    }

    public InventoryResponse updateInventory(String productId, InventoryUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found: " + productId));
        
        Inventory inventory = product.getInventory();
        if (inventory == null) {
            throw new ProductServiceException("Inventory not found for product: " + productId);
        }

        inventory.setCurrentStock(request.getCurrentStock());

        if (request.getReservedStock() != null) inventory.setReservedStock(request.getReservedStock());
        if (request.getMinimumStock() != null) inventory.setMinimumStock(request.getMinimumStock());
        if (request.getMaximumStock() != null) inventory.setMaximumStock(request.getMaximumStock());
        if (request.getReorderLevel() != null) inventory.setReorderLevel(request.getReorderLevel());
        if (request.getReorderQuantity() != null) inventory.setReorderQuantity(request.getReorderQuantity());
        if (request.getAutoReorderEnabled() != null) inventory.setAutoReorderEnabled(request.getAutoReorderEnabled());
        if (request.getTrackExpiry() != null) inventory.setTrackExpiry(request.getTrackExpiry());
        if (request.getExpiryDate() != null) inventory.setExpiryDate(request.getExpiryDate());
        if (request.getSupplierInfo() != null) inventory.setSupplierInfo(request.getSupplierInfo());
        if (request.getStorageLocation() != null) inventory.setStorageLocation(request.getStorageLocation());
        if (request.getNotes() != null) inventory.setNotes(request.getNotes());

        inventory.updateStatus();
        productRepository.save(product);

        return InventoryResponse.from(product);
    }

    public InventoryResponse addStock(String productId, Integer quantity, String notes) {
        if (quantity <= 0) throw new InvalidQuantityException("Quantity must be positive");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found: " + productId));
        
        Inventory inventory = product.getInventory();
        if (inventory == null) throw new ProductServiceException("Inventory not found for product: " + productId);

        inventory.setCurrentStock(inventory.getCurrentStock() + quantity);
        inventory.setLastRestockedAt(LocalDateTime.now());
        inventory.setLastRestockedQuantity(quantity);

        if (notes != null && !notes.trim().isEmpty()) {
            String timestampedNote = LocalDateTime.now() + ": Restocked +" + quantity + " units. " + notes;
            inventory.setNotes(inventory.getNotes() == null ? timestampedNote : inventory.getNotes() + "\n" + timestampedNote);
        }

        inventory.updateStatus();
        productRepository.save(product);

        return InventoryResponse.from(product);
    }

    public boolean reserveStock(String productId, Integer quantity) {
        if (quantity <= 0) throw new InvalidQuantityException("Quantity must be positive");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found: " + productId));
        
        Inventory inventory = product.getInventory();
        if (inventory == null || inventory.getAvailableStock() < quantity) {
            return false;
        }

        inventory.setReservedStock(inventory.getReservedStock() + quantity);
        inventory.updateStatus();
        productRepository.save(product);

        return true;
    }

    public void releaseReservedStock(String productId, Integer quantity) {
        if (quantity <= 0) throw new InvalidQuantityException("Quantity must be positive");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found: " + productId));
        
        Inventory inventory = product.getInventory();
        if (inventory == null || inventory.getReservedStock() < quantity) {
            throw new ProductServiceException("Cannot release more stock than reserved");
        }

        inventory.setReservedStock(inventory.getReservedStock() - quantity);
        inventory.updateStatus();
        productRepository.save(product);
    }

    public void consumeStock(String productId, Integer quantity) {
        if (quantity <= 0) throw new InvalidQuantityException("Quantity must be positive");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found: " + productId));
        
        Inventory inventory = product.getInventory();
        if (inventory == null || inventory.getCurrentStock() < quantity || inventory.getReservedStock() < quantity) {
            throw new ProductServiceException("Insufficient stock to consume");
        }

        inventory.setCurrentStock(inventory.getCurrentStock() - quantity);
        inventory.setReservedStock(inventory.getReservedStock() - quantity);
        inventory.updateStatus();
        productRepository.save(product);
    }

    public boolean checkStockAvailability(String productId, Integer requiredQuantity) {
        return productRepository.findById(productId)
                .map(p -> p.getInventory())
                .map(i -> i.getAvailableStock() >= requiredQuantity)
                .orElse(false);
    }

    public Integer getAvailableStock(String productId) {
        return productRepository.findById(productId)
                .map(p -> p.getInventory())
                .map(Inventory::getAvailableStock)
                .orElse(0);
    }

    public Map<String, Object> getInventoryStatistics() {
        return Map.of(
                "totalItems", 0L,
                "totalStock", 0L,
                "totalReservedStock", 0L,
                "lowStockItems", 0L,
                "outOfStockItems", 0L,
                "totalInventoryValue", 0.0
        );
    }

    public void bulkUpdateMinimumStock(Map<String, Integer> productMinimumStocks) {
        for (Map.Entry<String, Integer> entry : productMinimumStocks.entrySet()) {
            productRepository.findById(entry.getKey()).ifPresent(product -> {
                if (product.getInventory() != null) {
                    product.getInventory().setMinimumStock(entry.getValue());
                    productRepository.save(product);
                }
            });
        }
    }
}
