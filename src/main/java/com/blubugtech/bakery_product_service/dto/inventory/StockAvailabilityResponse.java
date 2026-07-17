package com.blubugtech.bakery_product_service.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAvailabilityResponse {
    private String productId;
    private Integer requestedQuantity;
    private Integer availableStock;
    private Boolean sufficient;
}
