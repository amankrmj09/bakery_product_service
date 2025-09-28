package com.shah_s.bakery_product_service.dto;

import com.shah_s.bakery_product_service.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CategoryResponse {

    // Getters and Setters
    private UUID id;
    private String name;
    private String description;
    private Integer displayOrder;
    private Boolean active;
    private String imageUrl;
    private String iconClass;
    private Integer productCount;
    private Integer activeProductCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public CategoryResponse() {}

    // Static factory method
    public static CategoryResponse from(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.id = category.getId();
        response.name = category.getName();
        response.description = category.getDescription();
        response.displayOrder = category.getDisplayOrder();
        response.active = category.getActive();
        response.imageUrl = category.getImageUrl();
        response.iconClass = category.getIconClass();
        response.productCount = category.getProductCount();
        response.activeProductCount = category.getActiveProductCount();
        response.createdAt = category.getCreatedAt();
        response.updatedAt = category.getUpdatedAt();
        return response;
    }

}
