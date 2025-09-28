package com.shah_s.bakery_product_service.dto;

import com.shah_s.bakery_product_service.entity.ProductImage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class ProductImageResponse {

    // Getters and Setters
    private UUID id;
    private String imageUrl;
    private String altText;
    private Boolean isPrimary;
    private Integer displayOrder;
    private Long fileSizeBytes;
    private Integer imageWidth;
    private Integer imageHeight;
    private LocalDateTime createdAt;

    // Constructors
    public ProductImageResponse() {}

    // Static factory method
    public static ProductImageResponse from(ProductImage image) {
        ProductImageResponse response = new ProductImageResponse();
        response.id = image.getId();
        response.imageUrl = image.getImageUrl();
        response.altText = image.getAltText();
        response.isPrimary = image.getIsPrimary();
        response.displayOrder = image.getDisplayOrder();
        response.fileSizeBytes = image.getFileSizeBytes();
        response.imageWidth = image.getImageWidth();
        response.imageHeight = image.getImageHeight();
        response.createdAt = image.getCreatedAt();
        return response;
    }

}
