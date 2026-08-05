package com.blubugtech.bakery_product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryStatResponse {
    private String categoryId;
    private String categoryName;
    private Long productCount;
}
