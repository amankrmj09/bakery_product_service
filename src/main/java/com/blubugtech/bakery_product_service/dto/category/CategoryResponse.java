package com.blubugtech.bakery_product_service.dto.category;

import com.blubugtech.bakery_product_service.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private String id;
    private String name;
    private String description;
    private Integer displayOrder;
    private Boolean active;
    private Boolean isTopCategory;
    @Builder.Default private List<String> mediaUrls = new ArrayList<>();
    private String iconClass;
    private Integer productCount;
    private Integer activeProductCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
