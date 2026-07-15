package com.blubugtech.bakery_product_service.entity;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Setter
@Getter
public class Inventory {

    @Field("current_stock")
    @Min(value = 0, message = "Current stock cannot be negative")
    private Integer currentStock = 0;

    @Field("reserved_stock")
    @Min(value = 0, message = "Reserved stock cannot be negative")
    private Integer reservedStock = 0;

    @Field("minimum_stock")
    @Min(value = 0, message = "Minimum stock cannot be negative")
    private Integer minimumStock = 0;

    @Field("maximum_stock")
    @Min(value = 0, message = "Maximum stock cannot be negative")
    private Integer maximumStock;

    @Field("reorder_level")
    @Min(value = 0, message = "Reorder level cannot be negative")
    private Integer reorderLevel = 0;

    @Field("reorder_quantity")
    @Min(value = 0, message = "Reorder quantity cannot be negative")
    private Integer reorderQuantity = 0;

    private InventoryStatus status = InventoryStatus.IN_STOCK;

    @Field("last_restocked_at")
    private LocalDateTime lastRestockedAt;

    @Field("last_restocked_quantity")
    private Integer lastRestockedQuantity;

    @Field("auto_reorder_enabled")
    private Boolean autoReorderEnabled = false;

    @Field("track_expiry")
    private Boolean trackExpiry = false;

    @Field("expiry_date")
    private LocalDateTime expiryDate;

    @Field("supplier_info")
    private String supplierInfo;

    @Field("storage_location")
    private String storageLocation;

    private String notes;

    @Transient
    private Integer availableStock;

    @Transient
    private Boolean isLowStock;

    @Transient
    private Boolean isOutOfStock;

    @Transient
    private Boolean needsReorder;

    public Inventory() {}

    public Inventory(Integer currentStock, Integer minimumStock) {
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
    }

    public Integer getAvailableStock() {
        return Math.max(0, currentStock - reservedStock);
    }

    public Boolean getIsLowStock() {
        return currentStock <= minimumStock;
    }

    public Boolean getIsOutOfStock() {
        return getAvailableStock() <= 0;
    }

    public Boolean getNeedsReorder() {
        return reorderLevel > 0 && currentStock <= reorderLevel;
    }

    public boolean isExpired() {
        return trackExpiry && expiryDate != null && LocalDateTime.now().isAfter(expiryDate);
    }

    public boolean isExpiringSoon(int hours) {
        return trackExpiry && expiryDate != null &&
               LocalDateTime.now().plusHours(hours).isAfter(expiryDate);
    }

    public void updateStatus() {
        if (getIsOutOfStock()) {
            this.status = InventoryStatus.OUT_OF_STOCK;
        } else if (getIsLowStock()) {
            this.status = InventoryStatus.LOW_STOCK;
        } else {
            this.status = InventoryStatus.IN_STOCK;
        }
    }

    public enum InventoryStatus {
        IN_STOCK, LOW_STOCK, OUT_OF_STOCK
    }
}
