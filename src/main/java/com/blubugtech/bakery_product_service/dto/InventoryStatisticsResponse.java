package com.blubugtech.bakery_product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryStatisticsResponse {
    private Long totalItems;
    private Long totalStock;
    private Long totalReservedStock;
    private Long lowStockItems;
    private Long outOfStockItems;
    private Double totalInventoryValue;
}
