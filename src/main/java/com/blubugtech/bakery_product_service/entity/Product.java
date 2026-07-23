package com.blubugtech.bakery_product_service.entity;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "SKU is required")
    @Size(min = 3, max = 50, message = "SKU must be between 3 and 50 characters")
    private String sku;

    @Indexed
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 200, message = "Product name must be between 2 and 200 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Field("short_description")
    @Size(max = 255, message = "Short description must not exceed 255 characters")
    private String shortDescription;

    @DocumentReference(lazy = true)
    @NotNull(message = "Product category is required")
    private Category category;

    @Indexed
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Invalid price format")
    private BigDecimal price;

    @Field("discount_price")
    @DecimalMin(value = "0.00", message = "Discount price cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Invalid discount price format")
    private BigDecimal discountPrice;

    @Field("cost_price")
    @DecimalMin(value = "0.00", message = "Cost price cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Invalid cost price format")
    private BigDecimal costPrice;

    @Field("tax_class")
    @Size(max = 50, message = "Tax class must not exceed 50 characters")
    private String taxClass = "STANDARD";

    @Field("tax_rate")
    @DecimalMin(value = "0.00", message = "Tax rate cannot be negative")
    private BigDecimal taxRate = new BigDecimal("0.08");

    @Field("meta_title")
    @Size(max = 100, message = "Meta title must not exceed 100 characters")
    private String metaTitle;

    @Field("meta_description")
    @Size(max = 255, message = "Meta description must not exceed 255 characters")
    private String metaDescription;

    @Field("max_order_quantity")
    @Min(value = 1, message = "Max order quantity must be at least 1")
    private Integer maxOrderQuantity;

    @Indexed
    private ProductStatus status = ProductStatus.ACTIVE;

    @Field("is_featured")
    private Boolean isFeatured = false;

    @Field("preparation_time")
    @Min(value = 0, message = "Preparation time cannot be negative")
    private Integer preparationTimeMinutes;

    @Field("shelf_life_hours")
    @Min(value = 0, message = "Shelf life cannot be negative")
    private Integer shelfLifeHours;

    @Size(max = 100, message = "Unit must not exceed 100 characters")
    private String unit = "piece";

    @Field("weight_grams")
    @Min(value = 0, message = "Weight cannot be negative")
    private Integer weightGrams;

    @Field("calories_per_unit")
    @Min(value = 0, message = "Calories cannot be negative")
    private Integer caloriesPerUnit;

    private List<String> ingredients = new ArrayList<>();

    private List<String> allergens = new ArrayList<>();

    private List<String> tags = new ArrayList<>();

    private Inventory inventory;

    @Field("media_urls")
    private List<String> mediaUrls = new ArrayList<>();

    @Field("average_rating")
    private Double averageRating = 0.0;

    @Field("total_reviews")
    private Integer totalReviews = 0;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;

    public Product() {}

    public Product(String sku, String name, String description, Category category, BigDecimal price) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
    }

    public BigDecimal getEffectivePrice() {
        return discountPrice != null && discountPrice.compareTo(BigDecimal.ZERO) > 0 ? discountPrice : price;
    }

    public boolean isOnSale() {
        return discountPrice != null && discountPrice.compareTo(price) < 0;
    }

    public boolean isAvailable() {
        return status == ProductStatus.ACTIVE &&
               inventory != null &&
               inventory.getAvailableStock() > 0;
    }

    public String getPrimaryImageUrl() {
        return mediaUrls != null && !mediaUrls.isEmpty() ? mediaUrls.get(0) : null;
    }

    public enum ProductStatus {
        ACTIVE, INACTIVE, DISCONTINUED
    }
}
