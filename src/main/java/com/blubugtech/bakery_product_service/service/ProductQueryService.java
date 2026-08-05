package com.blubugtech.bakery_product_service.service;

import com.blubugtech.bakery_product_service.dto.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductQueryService {
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
    Page<ProductResponse> getRecentlyAddedProducts(int days, Pageable pageable);
    Page<ProductResponse> getProductsByPreparationTime(Integer minMinutes, Integer maxMinutes, Pageable pageable);
    org.springframework.data.web.PagedModel<ProductResponse> getProductsByIds(List<String> productIds, Pageable pageable);
}
