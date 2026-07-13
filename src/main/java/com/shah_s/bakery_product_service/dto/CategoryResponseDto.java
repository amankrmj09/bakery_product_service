package com.shah_s.bakery_product_service.dto;

import com.shah_s.bakery_product_service.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
public class CategoryResponseDto {

    // Getters and Setters
    private String id;
    private String name;
    private String description;
    private Integer displayOrder;
    private Boolean active;
    private List<String> mediaUrls;
    private String iconClass;
    private Integer productCount;
    private Integer activeProductCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public CategoryResponseDto() {}

    // Static factory method
    public static CategoryResponseDto from(Category category) {
        CategoryResponseDto response = new CategoryResponseDto();
        response.id = category.getId();
        response.name = category.getName();
        response.description = category.getDescription();
        response.displayOrder = category.getDisplayOrder();
        response.active = category.getActive();
        response.mediaUrls = category.getMediaUrls();
        response.iconClass = category.getIconClass();
        response.productCount = 0;
        response.activeProductCount = 0;
        response.createdAt = category.getCreatedAt();
        response.updatedAt = category.getUpdatedAt();
        return response;
    }

}
