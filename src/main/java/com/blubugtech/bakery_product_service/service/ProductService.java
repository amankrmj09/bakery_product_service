package com.blubugtech.bakery_product_service.service;

import com.blubugtech.bakery_product_service.service.ProductService;
import com.blubugtech.bakery_product_service.dto.product.ProductRequest;
import com.blubugtech.bakery_product_service.dto.product.ProductResponse;
import com.blubugtech.bakery_product_service.entity.Category;
import com.blubugtech.bakery_product_service.entity.Inventory;
import com.blubugtech.bakery_product_service.entity.Product;
import com.blubugtech.bakery_product_service.exception.ProductServiceException;
import com.blubugtech.bakery_product_service.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.blubugtech.bakery_product_service.exception.*;
import org.springframework.stereotype.Service;
import com.blubugtech.common.exception.common.DuplicateResourceException;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.blubugtech.common.exception.common.DuplicateResourceException;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    Page<ProductResponse> getAllProducts(Pageable pageable);
    Page<ProductResponse> getActiveProducts(Pageable pageable);
    Page<ProductResponse> getAvailableProducts(Pageable pageable);
    Page<ProductResponse> getFeaturedProducts(Pageable pageable);
    ProductResponse getProductById(String productId);
    Optional<ProductResponse> getProductBySku(String sku);
    Page<ProductResponse> getProductsByCategory(String categoryId, Pageable pageable);
    Page<ProductResponse> searchProducts(String searchTerm, Pageable pageable);
    Page<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    Page<ProductResponse> getProductsOnSale(Pageable pageable);
    Page<ProductResponse> getProductsByTag(String tag, Pageable pageable);
    Page<ProductResponse> getProductsWithoutAllergen(String allergen, Pageable pageable);
    Page<ProductResponse> searchProductsWithFilters(String categoryId, Product.ProductStatus status,
                                                          BigDecimal minPrice, BigDecimal maxPrice,
                                                          Boolean inStock, Pageable pageable);
    ProductResponse updateProduct(String productId, ProductRequest request);
    ProductResponse updateProductStatus(String productId, Product.ProductStatus status);
    ProductResponse toggleFeaturedStatus(String productId);
    void deleteProduct(String productId);
    Page<ProductResponse> getRecentlyAddedProducts(int days, Pageable pageable);
    Page<ProductResponse> getProductsByPreparationTime(Integer minMinutes, Integer maxMinutes, Pageable pageable);
    Map<String, Object> getProductStatistics();
    boolean isProductAvailable(String productId);
    List<ProductResponse> getProductsByIds(List<String> productIds);
    List<ProductResponse> validateProducts(List<String> productIds);
    Product getProductEntity(String productId);
    com.blubugtech.bakery_product_service.dto.review.ReviewResponse addReview(String productId, com.blubugtech.bakery_product_service.dto.review.ReviewRequest request);
    List<com.blubugtech.bakery_product_service.dto.review.ReviewResponse> getProductReviews(String productId);
    void deleteReview(String productId, String reviewId, String userId);
    void reportReview(String productId, String reviewId, String reason);
    Page<com.blubugtech.bakery_product_service.dto.review.ReviewResponse> getReportedReviews(Pageable pageable);
    void dismissReviewReport(String reviewId);
}
