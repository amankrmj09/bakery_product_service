package com.blubugtech.bakery_product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockOperationResponseDto {
    private Boolean success;
    private String productId;
    private Integer quantity;
    private String message;
}
