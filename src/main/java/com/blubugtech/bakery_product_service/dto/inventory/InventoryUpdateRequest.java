package com.blubugtech.bakery_product_service.dto.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InventoryUpdateRequest {

    // Getters and Setters
    @NotNull(message = "Current stock is required")
    @Min(value = 0, message = "Current stock cannot be negative")
    private Integer currentStock;

    @Min(value = 0, message = "Reserved stock cannot be negative")
    private Integer reservedStock;

    @Min(value = 0, message = "Minimum stock cannot be negative")
    private Integer minimumStock;

    @Min(value = 0, message = "Maximum stock cannot be negative")
    private Integer maximumStock;

    @Min(value = 0, message = "Reorder level cannot be negative")
    private Integer reorderLevel;

    @Min(value = 0, message = "Reorder quantity cannot be negative")
    private Integer reorderQuantity;

    private Boolean autoReorderEnabled;

    private Boolean trackExpiry;

    private LocalDateTime expiryDate;

    private String supplierInfo;

    private String storageLocation;

    private String notes;

    // Constructors
    public InventoryUpdateRequest() {}

}
