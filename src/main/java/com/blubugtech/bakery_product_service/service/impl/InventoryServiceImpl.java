package com.blubugtech.bakery_product_service.service.impl;

import lombok.extern.slf4j.Slf4j;
import com.blubugtech.bakery_product_service.service.InventoryService;

import com.blubugtech.bakery_product_service.dto.inventory.InventoryResponse;
import com.blubugtech.bakery_product_service.dto.inventory.InventoryUpdateRequest;
import com.blubugtech.bakery_product_service.entity.Inventory;
import com.blubugtech.bakery_product_service.mapper.InventoryMapper;
import com.blubugtech.bakery_product_service.entity.Product;
import com.blubugtech.bakery_product_service.exception.ProductServiceException;
import com.blubugtech.bakery_product_service.repository.ProductQueryRepository;
import com.blubugtech.bakery_product_service.repository.ProductCommandRepository;
import org.springframework.beans.factory.annotation.Value;
import com.blubugtech.bakery_product_service.exception.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    final private ProductQueryRepository productQueryRepository;
    final private ProductCommandRepository productCommandRepository;
    final private InventoryMapper inventoryMapper;

    @Value("${product.inventory.low-stock-threshold:10}")
    private Integer defaultLowStockThreshold;

    public InventoryServiceImpl(ProductQueryRepository productQueryRepository, ProductCommandRepository productCommandRepository, InventoryMapper inventoryMapper) {
        this.productQueryRepository = productQueryRepository;
        this.productCommandRepository = productCommandRepository;
        this.inventoryMapper = inventoryMapper;
    }

    public Inventory createInventoryForProduct(Product product, Integer initialStock,
                                             Integer minimumStock, Integer reorderLevel) {
        log.info("Creating inventory for product: {} (SKU: {})", product.getName(), product.getSku());

        Inventory inventory = new Inventory();
        inventory.setCurrentStock(initialStock != null ? initialStock : 0);
        inventory.setMinimumStock(minimumStock != null ? minimumStock : defaultLowStockThreshold);
        inventory.setReorderLevel(reorderLevel != null ? reorderLevel : minimumStock);
        inventory.setReservedStock(0);
        inventory.updateStatus();

        product.setInventory(inventory);
        productCommandRepository.save(product);
        log.info("Inventory created for product: {}", product.getId());

        return inventory;
    }

    public InventoryResponse getInventoryByProductId(String productId) {
        Product product = productCommandRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found: " + productId));
        if (product.getInventory() == null) {
            throw new ProductServiceException("Inventory not found for product: " + productId);
        }
        return inventoryMapper.toResponse(product);
    }

    public Optional<InventoryResponse> getInventoryByProductSku(String sku) {
        return productQueryRepository.findBySku(sku)
                .filter(p -> p.getInventory() != null)
                .map(inventoryMapper::toResponse);
    }

    public Page<InventoryResponse> searchInventory(String searchTerm, Pageable pageable) {
        return productQueryRepository.searchAdminProducts(searchTerm, pageable)
                .map(inventoryMapper::toResponse);
    }

    public Page<InventoryResponse> getAllInventory(Pageable pageable) {
        return productQueryRepository.findAll(pageable)
                .map(inventoryMapper::toResponse);
    }

    public Page<InventoryResponse> getLowStockItems(Pageable pageable) {
        return productQueryRepository.findLowStockProducts(pageable)
                .map(inventoryMapper::toResponse);
    }

    public Page<InventoryResponse> getOutOfStockItems(Pageable pageable) {
        return productQueryRepository.findOutOfStockProducts(pageable)
                .map(inventoryMapper::toResponse);
    }

    public Page<InventoryResponse> getItemsNeedingReorder(Pageable pageable) {
        return productQueryRepository.findProductsNeedingReorder(pageable)
                .map(inventoryMapper::toResponse);
    }

    public Page<InventoryResponse> getExpiredItems(Pageable pageable) {
        return productQueryRepository.findProductsExpiringSoon(LocalDateTime.MIN, LocalDateTime.now(), pageable)
                .map(inventoryMapper::toResponse);
    }

    public Page<InventoryResponse> getItemsExpiringSoon(int hours, Pageable pageable) {
        return productQueryRepository.findProductsExpiringSoon(LocalDateTime.now(), LocalDateTime.now().plusHours(hours), pageable)
                .map(inventoryMapper::toResponse);
    }

    public InventoryResponse updateInventory(String productId, InventoryUpdateRequest request) {
        Product product = productCommandRepository.findById(productId)
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
        productCommandRepository.save(product);

        return inventoryMapper.toResponse(product);
    }

    public InventoryResponse addStock(String productId, Integer quantity, String notes) {
        if (quantity <= 0) throw new InvalidQuantityException("Quantity must be positive");

        Product product = productCommandRepository.findById(productId)
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
        productCommandRepository.save(product);

        return inventoryMapper.toResponse(product);
    }

    public boolean reserveStock(String productId, Integer quantity) {
        if (quantity <= 0) throw new InvalidQuantityException("Quantity must be positive");

        Product product = productCommandRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found: " + productId));
        
        Inventory inventory = product.getInventory();
        if (inventory == null || inventory.getAvailableStock() < quantity) {
            return false;
        }

        inventory.setReservedStock(inventory.getReservedStock() + quantity);
        inventory.updateStatus();
        productCommandRepository.save(product);

        return true;
    }

    public void releaseReservedStock(String productId, Integer quantity) {
        if (quantity <= 0) throw new InvalidQuantityException("Quantity must be positive");

        Product product = productCommandRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found: " + productId));
        
        Inventory inventory = product.getInventory();
        if (inventory == null || inventory.getReservedStock() < quantity) {
            throw new ProductServiceException("Cannot release more stock than reserved");
        }

        inventory.setReservedStock(inventory.getReservedStock() - quantity);
        inventory.updateStatus();
        productCommandRepository.save(product);
    }

    public void consumeStock(String productId, Integer quantity) {
        if (quantity <= 0) throw new InvalidQuantityException("Quantity must be positive");

        Product product = productCommandRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found: " + productId));
        
        Inventory inventory = product.getInventory();
        if (inventory == null || inventory.getCurrentStock() < quantity || inventory.getReservedStock() < quantity) {
            throw new ProductServiceException("Insufficient stock to consume");
        }

        inventory.setCurrentStock(inventory.getCurrentStock() - quantity);
        inventory.setReservedStock(inventory.getReservedStock() - quantity);
        inventory.updateStatus();
        productCommandRepository.save(product);
    }

    public boolean checkStockAvailability(String productId, Integer requiredQuantity) {
        return productCommandRepository.findById(productId)
                .map(p -> p.getInventory())
                .map(i -> i.getAvailableStock() >= requiredQuantity)
                .orElse(false);
    }

    public Integer getAvailableStock(String productId) {
        return productCommandRepository.findById(productId)
                .map(p -> p.getInventory())
                .map(Inventory::getAvailableStock)
                .orElse(0);
    }

    public com.blubugtech.bakery_product_service.dto.InventoryStatisticsResponse getInventoryStatistics() {
        return com.blubugtech.bakery_product_service.dto.InventoryStatisticsResponse.builder()
                .totalItems(0L)
                .totalStock(0L)
                .totalReservedStock(0L)
                .lowStockItems(0L)
                .outOfStockItems(0L)
                .totalInventoryValue(0.0)
        .build();
    }

    public void bulkUpdateMinimumStock(Map<String, Integer> productMinimumStocks) {
        for (Map.Entry<String, Integer> entry : productMinimumStocks.entrySet()) {
            productCommandRepository.findById(entry.getKey()).ifPresent(product -> {
                if (product.getInventory() != null) {
                    product.getInventory().setMinimumStock(entry.getValue());
                    productCommandRepository.save(product);
                }
            });
        }
    }
}
