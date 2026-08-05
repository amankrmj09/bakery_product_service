package com.blubugtech.bakery_product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryStatisticsResponse {
    private Long totalCategories;
    private Long activeCategories;
    private Long inactiveCategories;
    private List<CategoryStatResponse> categoryStats;
}
