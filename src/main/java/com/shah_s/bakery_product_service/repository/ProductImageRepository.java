package com.shah_s.bakery_product_service.repository;

import com.shah_s.bakery_product_service.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {

    // Find images by product ID
    List<ProductImage> findByProductIdOrderByDisplayOrderAsc(UUID productId);

    // Find primary image for a product
    Optional<ProductImage> findByProductIdAndIsPrimaryTrue(UUID productId);

    // Find images by product ID and primary status
    List<ProductImage> findByProductIdAndIsPrimary(UUID productId, Boolean isPrimary);

    // Count images for a product
    long countByProductId(UUID productId);

    // Find images by display order range for a product
    List<ProductImage> findByProductIdAndDisplayOrderBetweenOrderByDisplayOrderAsc(
            UUID productId, Integer startOrder, Integer endOrder);

    // Set all images as non-primary for a product (before setting a new primary)
    @Modifying
    @Query("UPDATE ProductImage pi SET pi.isPrimary = false WHERE pi.product.id = :productId")
    int clearPrimaryImages(@Param("productId") UUID productId);

    // Set specific image as primary
    @Modifying
    @Query("UPDATE ProductImage pi SET pi.isPrimary = true WHERE pi.id = :imageId")
    int setPrimaryImage(@Param("imageId") UUID imageId);

    // Update display order for an image
    @Modifying
    @Query("UPDATE ProductImage pi SET pi.displayOrder = :displayOrder WHERE pi.id = :imageId")
    int updateDisplayOrder(@Param("imageId") UUID imageId, @Param("displayOrder") Integer displayOrder);

    // Get max display order for a product
    @Query("SELECT COALESCE(MAX(pi.displayOrder), 0) FROM ProductImage pi WHERE pi.product.id = :productId")
    Integer getMaxDisplayOrderForProduct(@Param("productId") UUID productId);

    // Delete images by product ID
    void deleteByProductId(UUID productId);

    // Find images by file size range
    List<ProductImage> findByFileSizeBytesBetweenOrderByFileSizeBytesDesc(Long minSize, Long maxSize);

    // Find images by dimensions
    List<ProductImage> findByImageWidthAndImageHeight(Integer width, Integer height);
}
