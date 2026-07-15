package com.blubugtech.bakery_product_service.dto;

import com.blubugtech.bakery_product_service.entity.Inventory;
import com.blubugtech.bakery_product_service.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseDto {

    private String id; // This can just be the product id since 1:1
    private String productId;
    private String productName;
    private String productSku;

    private Integer currentStock;
    private Integer reservedStock;
    private Integer availableStock;
    private Integer minimumStock;
    private Integer maximumStock;
    private Integer reorderLevel;
    private Integer reorderQuantity;

    private String status;
    private Boolean isLowStock;
    private Boolean isOutOfStock;
    private Boolean needsReorder;

    private LocalDateTime lastRestockedAt;
    private Integer lastRestockedQuantity;
    
    private Boolean autoReorderEnabled;
    private Boolean trackExpiry;
    private LocalDateTime expiryDate;
    private String supplierInfo;
    private String storageLocation;
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static InventoryResponseDto from(Product product) {
        if (product == null || product.getInventory() == null) {
            return null;
        }
        
        Inventory inventory = product.getInventory();
        InventoryResponseDto response = new InventoryResponseDto();
        
        response.id = product.getId();
        response.productId = product.getId();
        response.productName = product.getName();
        response.productSku = product.getSku();
        
        response.currentStock = inventory.getCurrentStock();
        response.reservedStock = inventory.getReservedStock();
        response.availableStock = inventory.getAvailableStock();
        response.minimumStock = inventory.getMinimumStock();
        response.maximumStock = inventory.getMaximumStock();
        response.reorderLevel = inventory.getReorderLevel();
        response.reorderQuantity = inventory.getReorderQuantity();
        
        if (inventory.getStatus() != null) {
            response.status = inventory.getStatus().name();
        }
        
        response.isLowStock = inventory.getIsLowStock();
        response.isOutOfStock = inventory.getIsOutOfStock();
        response.needsReorder = inventory.getNeedsReorder();
        
        response.lastRestockedAt = inventory.getLastRestockedAt();
        response.lastRestockedQuantity = inventory.getLastRestockedQuantity();
        
        response.autoReorderEnabled = inventory.getAutoReorderEnabled();
        response.trackExpiry = inventory.getTrackExpiry();
        response.expiryDate = inventory.getExpiryDate();
        response.supplierInfo = inventory.getSupplierInfo();
        response.storageLocation = inventory.getStorageLocation();
        response.notes = inventory.getNotes();
        
        response.createdAt = product.getCreatedAt();
        response.updatedAt = product.getUpdatedAt();
        
        return response;
    }
}
