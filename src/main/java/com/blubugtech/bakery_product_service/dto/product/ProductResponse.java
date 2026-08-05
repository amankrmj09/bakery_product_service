package com.blubugtech.bakery_product_service.dto.product;

import com.blubugtech.bakery_product_service.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private String id;
    private String sku;
    private String name;
    private String description;
    private String shortDescription;
    private CategorySummary category;
    private BigDecimal price;
    @Builder.Default private BigDecimal discountPrice = BigDecimal.ZERO;
    private BigDecimal costPrice;
    private BigDecimal effectivePrice;
    private String taxClass;
    private BigDecimal taxRate;
    private String metaTitle;
    private String metaDescription;
    private Integer maxOrderQuantity;
    private Product.ProductStatus status;
    @Builder.Default private boolean isFeatured = false;
    @Builder.Default private boolean isActive = true;
    private Integer preparationTimeMinutes;
    private Integer shelfLifeHours;
    private String unit;
    private String calories;
    @Builder.Default private List<String> ingredients = new ArrayList<>();
    @Builder.Default private List<String> allergens = new ArrayList<>();
    @Builder.Default private List<String> tags = new ArrayList<>();
    private InventorySummary inventory;
    @Builder.Default private List<String> mediaUrls = new ArrayList<>();
    private String primaryImageUrl;
    @Builder.Default private Boolean isAvailable = false;
    @Builder.Default private Boolean isOnSale = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double averageRating;
    private Integer totalReviews;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategorySummary {
        private String id;
        private String name;
        private String iconClass;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InventorySummary {
        private Integer currentStock;
        private Integer availableStock;
        private Boolean isLowStock;
        private Boolean isOutOfStock;
        private com.blubugtech.bakery_product_service.entity.Inventory.InventoryStatus status;
    }
}
