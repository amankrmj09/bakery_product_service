package com.blubugtech.bakery_product_service.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockOperationResponse {
    private Boolean success;
    private String productId;
    private Integer quantity;
    private String message;
}
