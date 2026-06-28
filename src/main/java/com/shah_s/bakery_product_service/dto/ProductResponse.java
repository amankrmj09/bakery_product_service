package com.shah_s.bakery_product_service.dto;

import com.shah_s.bakery_product_service.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class ProductResponse {

    // Getters and Setters
    private UUID id;
    private String sku;
    private String name;
    private String description;
    private String shortDescription;
    private CategorySummary category;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private BigDecimal effectivePrice;
    private Product.ProductStatus status;
    private Boolean isFeatured;
    private Integer preparationTimeMinutes;
    private Integer shelfLifeHours;
    private String unit;
    private Integer weightGrams;
    private Integer caloriesPerUnit;
    private List<String> ingredients;
    private List<String> allergens;
    private List<String> tags;
    private InventorySummary inventory;
    private List<String> mediaUrls;
    private String primaryImageUrl;
    private Boolean isAvailable;
    private Boolean isOnSale;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public ProductResponse() {}

    // Static factory method
    public static ProductResponse from(Product product) {
        ProductResponse response = new ProductResponse();
        response.id = product.getId();
        response.sku = product.getSku();
        response.name = product.getName();
        response.description = product.getDescription();
        response.shortDescription = product.getShortDescription();
        response.category = CategorySummary.from(product.getCategory());
        response.price = product.getPrice();
        response.discountPrice = product.getDiscountPrice();
        response.effectivePrice = product.getEffectivePrice();
        response.status = product.getStatus();
        response.isFeatured = product.getIsFeatured();
        response.preparationTimeMinutes = product.getPreparationTimeMinutes();
        response.shelfLifeHours = product.getShelfLifeHours();
        response.unit = product.getUnit();
        response.weightGrams = product.getWeightGrams();
        response.caloriesPerUnit = product.getCaloriesPerUnit();
        response.ingredients = product.getIngredients();
        response.allergens = product.getAllergens();
        response.tags = product.getTags();
        response.inventory = product.getInventory() != null ?
            InventorySummary.from(product.getInventory()) : null;
        response.mediaUrls = product.getMediaUrls();
        response.primaryImageUrl = product.getPrimaryImageUrl();
        response.isAvailable = product.isAvailable();
        response.isOnSale = product.isOnSale();
        response.createdAt = product.getCreatedAt();
        response.updatedAt = product.getUpdatedAt();
        return response;
    }

    // Inner classes for nested objects
    @Setter
    @Getter
    public static class CategorySummary {
        // Getters and Setters
        private UUID id;
        private String name;
        private String iconClass;

        public static CategorySummary from(com.shah_s.bakery_product_service.entity.Category category) {
            CategorySummary summary = new CategorySummary();
            summary.id = category.getId();
            summary.name = category.getName();
            summary.iconClass = category.getIconClass();
            return summary;
        }

    }

    @Setter
    @Getter
    public static class InventorySummary {
        // Getters and Setters
        private Integer currentStock;
        private Integer availableStock;
        private Boolean isLowStock;
        private Boolean isOutOfStock;
        private com.shah_s.bakery_product_service.entity.Inventory.InventoryStatus status;

        public static InventorySummary from(com.shah_s.bakery_product_service.entity.Inventory inventory) {
            InventorySummary summary = new InventorySummary();
            summary.currentStock = inventory.getCurrentStock();
            summary.availableStock = inventory.getAvailableStock();
            summary.isLowStock = inventory.getIsLowStock();
            summary.isOutOfStock = inventory.getIsOutOfStock();
            summary.status = inventory.getStatus();
            return summary;
        }

    }
}
