package com.shah_s.bakery_product_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Setter
@Getter
@Entity
@Table(name = "categories", indexes = {
    @Index(name = "idx_category_name", columnList = "name"),
    @Index(name = "idx_category_active", columnList = "active"),
    @Index(name = "idx_category_display_order", columnList = "display_order")
})
public class Category {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 100)
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    private String name;

    @Column(columnDefinition = "TEXT")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Column(nullable = false)
    private Boolean active = true;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "media_urls", columnDefinition = "text[]")
    private List<String> mediaUrls = new ArrayList<>();

    @Column(name = "icon_class")
    private String iconClass; // For FontAwesome or similar icons

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @BatchSize(size = 50)
    private List<Product> products = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Category() {}

    public Category(String name, String description, Integer displayOrder) {
        this.name = name;
        this.description = description;
        this.displayOrder = displayOrder;
    }

    // Utility methods
    public int getProductCount() {
        return products != null ? products.size() : 0;
    }

    public int getActiveProductCount() {
        return products != null ?
            (int) products.stream().filter(p -> p.getStatus() == Product.ProductStatus.ACTIVE).count() : 0;
    }
}
