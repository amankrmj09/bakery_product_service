package com.blubugtech.bakery_product_service.dto.category;

import com.blubugtech.bakery_product_service.dto.product.ProductResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryWithTopProductsResponse {
    private CategoryResponse category;
    private List<ProductResponse> topProducts;
    
    public CategoryWithTopProductsResponse() {}
    
    public CategoryWithTopProductsResponse(CategoryResponse category, List<ProductResponse> topProducts) {
        this.category = category;
        this.topProducts = topProducts;
    }
}
