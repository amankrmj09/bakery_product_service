package com.blubugtech.bakery_product_service.service.impl;

import lombok.extern.slf4j.Slf4j;
import com.blubugtech.bakery_product_service.service.ProductQueryService;
import com.blubugtech.bakery_product_service.dto.product.ProductResponse;
import com.blubugtech.bakery_product_service.entity.Product;
import com.blubugtech.bakery_product_service.mapper.ProductMapper;
import com.blubugtech.bakery_product_service.exception.ProductServiceException;
import com.blubugtech.bakery_product_service.repository.ProductQueryRepository;
import com.blubugtech.bakery_product_service.cache.ProductCacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductQueryRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductCacheManager productCacheManager;

    public ProductQueryServiceImpl(ProductQueryRepository productRepository, ProductMapper productMapper, ProductCacheManager productCacheManager) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productCacheManager = productCacheManager;
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        log.debug("Fetching all products");
        return productRepository.findAll(pageable).map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getActiveProducts(Pageable pageable) {
        log.debug("Fetching active products");
        return productRepository.findByStatus(Product.ProductStatus.ACTIVE, pageable).map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getAvailableProducts(Pageable pageable) {
        log.debug("Fetching available products");
        return productRepository.findAvailableProducts(pageable).map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getFeaturedProducts(Pageable pageable) {
        log.debug("Fetching featured products");
        return productRepository.findByIsFeaturedTrueAndStatus(Product.ProductStatus.ACTIVE, pageable).map(productMapper::toResponse);
    }

    @Override
    public ProductResponse getProductById(String productId) {
        log.debug("Fetching product by ID: {}", productId);
        Product product = productCacheManager.getProduct(productId).orElseGet(() -> {
            Product p = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));
            productCacheManager.putProduct(p);
            return p;
        });
        return productMapper.toResponse(product);
    }

    @Override
    public Optional<ProductResponse> getProductBySku(String sku) {
        log.debug("Fetching product by SKU: {}", sku);
        return productRepository.findBySku(sku).map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByCategory(String categoryId, Pageable pageable) {
        log.debug("Fetching products by category with pagination: {}", categoryId);
        return productRepository.findByCategoryIdAndStatus(categoryId, Product.ProductStatus.ACTIVE, pageable).map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> searchProducts(String searchTerm, Pageable pageable) {
        log.debug("Searching products with pagination, term: {}", searchTerm);
        return productRepository.searchProducts(searchTerm, Product.ProductStatus.ACTIVE, pageable).map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        log.debug("Fetching products by price range: {} - {}", minPrice, maxPrice);
        return productRepository.findByPriceRange(minPrice, maxPrice, Product.ProductStatus.ACTIVE, pageable).map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getProductsOnSale(Pageable pageable) {
        log.debug("Fetching products on sale");
        return productRepository.findProductsOnSale(Product.ProductStatus.ACTIVE, pageable).map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByTag(String tag, Pageable pageable) {
        log.debug("Fetching products by tag: {}", tag);
        return productRepository.findByTag(tag, Product.ProductStatus.ACTIVE, pageable).map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getProductsWithoutAllergen(String allergen, Pageable pageable) {
        log.debug("Fetching products without allergen: {}", allergen);
        return productRepository.findProductsWithoutAllergen(allergen, Product.ProductStatus.ACTIVE, pageable).map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getRecentlyAddedProducts(int days, Pageable pageable) {
        log.debug("Fetching products added in last {} days", days);
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return productRepository.findByStatusAndCreatedAtAfter(Product.ProductStatus.ACTIVE, since, pageable).map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByPreparationTime(Integer minMinutes, Integer maxMinutes, Pageable pageable) {
        log.debug("Fetching products by preparation time: {} - {} minutes", minMinutes, maxMinutes);
        return productRepository.findByStatusAndPreparationTimeMinutesBetween(Product.ProductStatus.ACTIVE, minMinutes, maxMinutes, pageable).map(productMapper::toResponse);
    }

    @Override
    public List<ProductResponse> getProductsByIds(List<String> productIds) {
        log.debug("Fetching products by IDs: {}", productIds);
        return productRepository.findAllById(productIds).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }
}
