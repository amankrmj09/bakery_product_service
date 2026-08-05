package com.blubugtech.bakery_product_service.dto.category;

import com.blubugtech.bakery_product_service.dto.product.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryWithTopProductsResponse {
    private CategoryResponse category;
    @Builder.Default private List<ProductResponse> topProducts = new ArrayList<>();
}
