package com.blubugtech.bakery_product_service.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategoryRequest {

    // Getters and Setters
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private Integer displayOrder = 0;

    private Boolean active = true;

    private Boolean isTopCategory = false;

    private List<String> mediaUrls = new ArrayList<>();

    private String iconClass;

    // Constructors
    public CategoryRequest() {}

    public CategoryRequest(String name, String description, Integer displayOrder) {
        this.name = name;
        this.description = description;
        this.displayOrder = displayOrder;
    }

}
