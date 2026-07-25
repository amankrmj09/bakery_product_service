package com.blubugtech.bakery_product_service.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Document(collection = "categories")
public class Category {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Field("display_order")
    @Indexed
    private Integer displayOrder = 0;

    @Indexed
    private Boolean active = true;

    @Field("is_top_category")
    @Indexed
    private Boolean isTopCategory = false;

    @Field("media_urls")
    private List<String> mediaUrls = new ArrayList<>();

    @Field("icon_class")
    private String iconClass;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;

    public Category() {}

    public Category(String name, String description, Integer displayOrder) {
        this.name = name;
        this.description = description;
        this.displayOrder = displayOrder;
    }
}
