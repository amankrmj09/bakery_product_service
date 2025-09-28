package com.shah_s.bakery_product_service.dto;

import com.shah_s.bakery_product_service.entity.Inventory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class InventoryResponse {

    // Getters and Setters (abbreviated for space)
    private UUID id;
    private UUID productId;
    private String productName;
    private String productSku;
    private Integer currentStock;
    private Integer reservedStock;
    private Integer availableStock;
    private Integer minimumStock;
    private Integer maximumStock;
    private Integer reorderLevel;
    private Integer reorderQuantity;
    private Inventory.InventoryStatus status;
    private Boolean isLowStock;
    private Boolean isOutOfStock;
    private Boolean needsReorder;
    private LocalDateTime lastRestockedAt;
    private Integer lastRestockedQuantity;
    private Boolean autoReorderEnabled;
    private Boolean trackExpiry;
    private LocalDateTime expiryDate;
    private Boolean isExpired;
    private String supplierInfo;
    private String storageLocation;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public InventoryResponse() {}

    // Static factory method
    public static InventoryResponse from(Inventory inventory) {
        InventoryResponse response = new InventoryResponse();
        response.id = inventory.getId();
        response.productId = inventory.getProduct().getId();
        response.productName = inventory.getProduct().getName();
        response.productSku = inventory.getProduct().getSku();
        response.currentStock = inventory.getCurrentStock();
        response.reservedStock = inventory.getReservedStock();
        response.availableStock = inventory.getAvailableStock();
        response.minimumStock = inventory.getMinimumStock();
        response.maximumStock = inventory.getMaximumStock();
        response.reorderLevel = inventory.getReorderLevel();
        response.reorderQuantity = inventory.getReorderQuantity();
        response.status = inventory.getStatus();
        response.isLowStock = inventory.getIsLowStock();
        response.isOutOfStock = inventory.getIsOutOfStock();
        response.needsReorder = inventory.getNeedsReorder();
        response.lastRestockedAt = inventory.getLastRestockedAt();
        response.lastRestockedQuantity = inventory.getLastRestockedQuantity();
        response.autoReorderEnabled = inventory.getAutoReorderEnabled();
        response.trackExpiry = inventory.getTrackExpiry();
        response.expiryDate = inventory.getExpiryDate();
        response.isExpired = inventory.isExpired();
        response.supplierInfo = inventory.getSupplierInfo();
        response.storageLocation = inventory.getStorageLocation();
        response.notes = inventory.getNotes();
        response.createdAt = inventory.getCreatedAt();
        response.updatedAt = inventory.getUpdatedAt();
        return response;
    }

}
