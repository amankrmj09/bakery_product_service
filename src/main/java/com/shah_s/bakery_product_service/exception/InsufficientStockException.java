package com.shah_s.bakery_product_service.exception;

import java.util.UUID;

public class InsufficientStockException extends RuntimeException {

    private UUID productId;
    private Integer requestedQuantity;
    private Integer availableQuantity;

    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(UUID productId, Integer requestedQuantity, Integer availableQuantity) {
        super(String.format("Insufficient stock for product %s. Requested: %d, Available: %d",
                           productId, requestedQuantity, availableQuantity));
        this.productId = productId;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }

    // Getters
    public UUID getProductId() { return productId; }
    public Integer getRequestedQuantity() { return requestedQuantity; }
    public Integer getAvailableQuantity() { return availableQuantity; }
}
