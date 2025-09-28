package com.shah_s.bakery_product_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "inventory", indexes = {
    @Index(name = "idx_inventory_product", columnList = "product_id"),
    @Index(name = "idx_inventory_status", columnList = "status"),
    @Index(name = "idx_inventory_stock", columnList = "current_stock")
})
public class Inventory {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    @NotNull(message = "Product is required for inventory")
    private Product product;

    @Column(name = "current_stock", nullable = false)
    @Min(value = 0, message = "Current stock cannot be negative")
    private Integer currentStock = 0;

    @Column(name = "reserved_stock", nullable = false)
    @Min(value = 0, message = "Reserved stock cannot be negative")
    private Integer reservedStock = 0; // Stock reserved for pending orders

    @Column(name = "minimum_stock", nullable = false)
    @Min(value = 0, message = "Minimum stock cannot be negative")
    private Integer minimumStock = 0; // Low stock threshold

    @Column(name = "maximum_stock")
    @Min(value = 0, message = "Maximum stock cannot be negative")
    private Integer maximumStock;

    @Column(name = "reorder_level")
    @Min(value = 0, message = "Reorder level cannot be negative")
    private Integer reorderLevel = 0; // When to reorder

    @Column(name = "reorder_quantity")
    @Min(value = 0, message = "Reorder quantity cannot be negative")
    private Integer reorderQuantity = 0; // How much to reorder

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryStatus status = InventoryStatus.IN_STOCK;

    @Column(name = "last_restocked_at")
    private LocalDateTime lastRestockedAt;

    @Column(name = "last_restocked_quantity")
    private Integer lastRestockedQuantity;

    @Column(name = "auto_reorder_enabled")
    private Boolean autoReorderEnabled = false;

    @Column(name = "track_expiry")
    private Boolean trackExpiry = false;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "supplier_info")
    private String supplierInfo;

    @Column(name = "storage_location")
    private String storageLocation;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Transient
    private Integer availableStock;

    @Transient
    private Boolean isLowStock;

    @Transient
    private Boolean isOutOfStock;

    @Transient
    private Boolean needsReorder;

    // Constructors
    public Inventory() {}

    public Inventory(Product product, Integer currentStock, Integer minimumStock) {
        this.product = product;
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
    }

    // Utility Methods
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

    // Update inventory status based on stock levels
    public void updateStatus() {
        if (getIsOutOfStock()) {
            this.status = InventoryStatus.OUT_OF_STOCK;
        } else if (getIsLowStock()) {
            this.status = InventoryStatus.LOW_STOCK;
        } else {
            this.status = InventoryStatus.IN_STOCK;
        }
    }

    // Enums
    public enum InventoryStatus {
        IN_STOCK, LOW_STOCK, OUT_OF_STOCK
    }
}
