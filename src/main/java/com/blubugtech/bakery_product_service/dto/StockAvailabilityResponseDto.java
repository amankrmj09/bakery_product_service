package com.blubugtech.bakery_product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAvailabilityResponseDto {
    private String productId;
    private Integer requestedQuantity;
    private Integer availableStock;
    private Boolean sufficient;
}
