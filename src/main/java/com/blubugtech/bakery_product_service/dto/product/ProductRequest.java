package com.blubugtech.bakery_product_service.dto.product;

import com.blubugtech.bakery_product_service.entity.Product;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class ProductRequest {

    // Getters and Setters
    @NotBlank(message = "SKU is required")
    @Size(min = 3, max = 50, message = "SKU must be between 3 and 50 characters")
    private String sku;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 200, message = "Product name must be between 2 and 200 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Size(max = 255, message = "Short description must not exceed 255 characters")
    private String shortDescription;

    @NotNull(message = "Category ID is required")
    private String categoryId;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Invalid price format")
    private BigDecimal price;

    @DecimalMin(value = "0.00", message = "Discount price cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Invalid discount price format")
    private BigDecimal discountPrice;

    @DecimalMin(value = "0.00", message = "Cost price cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Invalid cost price format")
    private BigDecimal costPrice;

    @Size(max = 50, message = "Tax class must not exceed 50 characters")
    private String taxClass = "STANDARD";

    @DecimalMin(value = "0.00", message = "Tax rate cannot be negative")
    private BigDecimal taxRate;

    @Size(max = 100, message = "Meta title must not exceed 100 characters")
    private String metaTitle;

    @Size(max = 255, message = "Meta description must not exceed 255 characters")
    private String metaDescription;

    @Min(value = 1, message = "Max order quantity must be at least 1")
    private Integer maxOrderQuantity;

    private Product.ProductStatus status = Product.ProductStatus.ACTIVE;

    private Boolean isFeatured = false;

    @Min(value = 0, message = "Preparation time cannot be negative")
    private Integer preparationTimeMinutes;

    @Min(value = 0, message = "Shelf life cannot be negative")
    private Integer shelfLifeHours;

    @Size(max = 255, message = "Unit description must not exceed 255 characters")
    private String unit = "piece";

    @Size(max = 255, message = "Calories description must not exceed 255 characters")
    private String calories;

    private List<String> ingredients = new ArrayList<>();

    private List<String> allergens = new ArrayList<>();

    private List<String> tags = new ArrayList<>();

    private List<String> mediaUrls = new ArrayList<>();

    // Inventory fields
    @Min(value = 0, message = "Initial stock cannot be negative")
    private Integer initialStock = 0;

    @Min(value = 0, message = "Minimum stock cannot be negative")
    private Integer minimumStock = 0;

    @Min(value = 0, message = "Reorder level cannot be negative")
    private Integer reorderLevel = 0;

    // Constructors
    public ProductRequest() {}

}
