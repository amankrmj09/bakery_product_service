package com.blubugtech.bakery_product_service.service.impl;

import lombok.extern.slf4j.Slf4j;
import com.blubugtech.bakery_product_service.service.CategoryService;
import com.blubugtech.bakery_product_service.service.InventoryService;

import com.blubugtech.bakery_product_service.service.ProductService;

import com.blubugtech.bakery_product_service.dto.product.ProductRequest;
import com.blubugtech.bakery_product_service.dto.product.ProductResponse;
import com.blubugtech.bakery_product_service.entity.Category;
import com.blubugtech.bakery_product_service.entity.Inventory;
import com.blubugtech.bakery_product_service.entity.Product;
import com.blubugtech.bakery_product_service.mapper.ProductMapper;
import com.blubugtech.bakery_product_service.search.service.ProductSearchService;
import com.blubugtech.bakery_product_service.exception.ProductServiceException;
import com.blubugtech.bakery_product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.blubugtech.bakery_product_service.exception.*;
import org.springframework.stereotype.Service;
import org.blubakery.bakery_common_libs.exception.common.DuplicateResourceException;
import org.blubakery.bakery_common_libs.exception.common.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.blubakery.bakery_common_libs.exception.common.DuplicateResourceException;
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

@Service
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {

    final private ProductRepository productRepository;

    final private CategoryService categoryService;

    final private InventoryService inventoryService;
    
    final private ProductSearchService productSearchService;
    
    final private com.blubugtech.bakery_product_service.integration.kafka.ProductEventPublisher productEventPublisher;
    final private ProductMapper productMapper;
    

    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService, InventoryService inventoryService, ProductSearchService productSearchService, com.blubugtech.bakery_product_service.integration.kafka.ProductEventPublisher productEventPublisher, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.inventoryService = inventoryService;
        this.productSearchService = productSearchService;
        this.productEventPublisher = productEventPublisher;
        this.productMapper = productMapper;
        
    }

    // Create new product
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating new product: {} (SKU: {})", request.getName(), request.getSku());

        // Check if SKU already exists
        if (productRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("Product with SKU '" + request.getSku() + "' already exists");
        }

        // Validate category exists
        Category category = categoryService.getCategoryEntity(request.getCategoryId());

        // Create product
        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setShortDescription(request.getShortDescription());
        product.setCategory(category);
        product.setPrice(request.getPrice());
        product.setDiscountPrice(request.getDiscountPrice());
        product.setStatus(request.getStatus());
        product.setIsFeatured(request.getIsFeatured());
        product.setPreparationTimeMinutes(request.getPreparationTimeMinutes());
        product.setShelfLifeHours(request.getShelfLifeHours());
        product.setUnit(request.getUnit());
        product.setCalories(request.getCalories());
        product.setIngredients(request.getIngredients());
        product.setAllergens(request.getAllergens());
        product.setTags(request.getTags());
        product.setMediaUrls(request.getMediaUrls());
        product.setCostPrice(request.getCostPrice());
        product.setTaxClass(request.getTaxClass());
        product.setMetaTitle(request.getMetaTitle());
        product.setMetaDescription(request.getMetaDescription());
        product.setMaxOrderQuantity(request.getMaxOrderQuantity());

        Product savedProduct = productRepository.save(product);

        // Create initial inventory
        inventoryService.createInventoryForProduct(savedProduct, request.getInitialStock(),
                request.getMinimumStock(), request.getReorderLevel());

        syncToElasticsearch(savedProduct);
        publishProductEvent(savedProduct, "CREATED");

        log.info("Product created successfully with ID: {}", savedProduct.getId());
        return productMapper.toResponse(savedProduct);
    }

    // Get all products
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        log.debug("Fetching all products");

        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }

    // Get active products
    @Transactional(readOnly = true)
    public Page<ProductResponse> getActiveProducts(Pageable pageable) {
        log.debug("Fetching active products");

        return productRepository.findByStatus(Product.ProductStatus.ACTIVE, pageable)
                .map(productMapper::toResponse);
    }

    // Get available products (active with stock)
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAvailableProducts(Pageable pageable) {
        log.debug("Fetching available products");

        return productRepository.findAvailableProducts(pageable)
                .map(productMapper::toResponse);
    }

    // Get featured products
    @Transactional(readOnly = true)
    public Page<ProductResponse> getFeaturedProducts(Pageable pageable) {
        log.debug("Fetching featured products");

        return productRepository.findByIsFeaturedTrueAndStatus(Product.ProductStatus.ACTIVE, pageable)
                .map(productMapper::toResponse);
    }

    // Get product by ID
    @Transactional(readOnly = true)
    public ProductResponse getProductById(String productId) {
        log.debug("Fetching product by ID: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));

        return productMapper.toResponse(product);
    }

    // Get product by SKU
    @Transactional(readOnly = true)
    public Optional<ProductResponse> getProductBySku(String sku) {
        log.debug("Fetching product by SKU: {}", sku);

        return productRepository.findBySku(sku)
                .map(productMapper::toResponse);
    }

    // Get products by category
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByCategory(String categoryId, Pageable pageable) {
        log.debug("Fetching products by category with pagination: {}", categoryId);

        return productRepository.findByCategoryIdAndStatus(categoryId, Product.ProductStatus.ACTIVE, pageable)
                .map(productMapper::toResponse);
    }

    // Search products
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(String searchTerm, Pageable pageable) {
        log.debug("Searching products with pagination, term: {}", searchTerm);

        return productRepository.searchProducts(searchTerm, Product.ProductStatus.ACTIVE, pageable)
                .map(productMapper::toResponse);
    }

    // Search products for admin (no status filter)
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchAdminProducts(String searchTerm, Pageable pageable) {
        log.debug("Admin searching products with pagination, term: {}", searchTerm);

        return productRepository.searchAdminProducts(searchTerm, pageable)
                .map(productMapper::toResponse);
    }

    // Get products by price range
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        log.debug("Fetching products by price range: {} - {}", minPrice, maxPrice);

        return productRepository.findByPriceRange(minPrice, maxPrice, Product.ProductStatus.ACTIVE, pageable)
                .map(productMapper::toResponse);
    }

    // Get products on sale
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsOnSale(Pageable pageable) {
        log.debug("Fetching products on sale");

        return productRepository.findProductsOnSale(Product.ProductStatus.ACTIVE, pageable)
                .map(productMapper::toResponse);
    }

    // Get products by tag
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByTag(String tag, Pageable pageable) {
        log.debug("Fetching products by tag: {}", tag);

        return productRepository.findByTag(tag, Product.ProductStatus.ACTIVE, pageable)
                .map(productMapper::toResponse);
    }

    // Get products without specific allergen
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsWithoutAllergen(String allergen, Pageable pageable) {
        log.debug("Fetching products without allergen: {}", allergen);

        return productRepository.findProductsWithoutAllergen(allergen, Product.ProductStatus.ACTIVE, pageable)
                .map(productMapper::toResponse);
    }

    // Advanced product search with filters
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProductsWithFilters(String categoryId, Product.ProductStatus status,
                                                          BigDecimal minPrice, BigDecimal maxPrice,
                                                          Boolean inStock, Pageable pageable) {
        log.debug("Advanced product search with filters");

        return productRepository.findProductsWithFilters(categoryId, status, minPrice, maxPrice, inStock, pageable)
                .map(productMapper::toResponse);
    }

    // Update product
    public ProductResponse updateProduct(String productId, ProductRequest request) {
        log.info("Updating product: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));

        // Check if new SKU conflicts with existing product
        if (!product.getSku().equals(request.getSku()) &&
            productRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("Product with SKU '" + request.getSku() + "' already exists");
        }

        // Validate category exists
        Category category = categoryService.getCategoryEntity(request.getCategoryId());

        // Update product fields
        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setShortDescription(request.getShortDescription());
        product.setCategory(category);
        product.setPrice(request.getPrice());
        product.setDiscountPrice(request.getDiscountPrice());
        product.setStatus(request.getStatus());
        product.setIsFeatured(request.getIsFeatured());
        product.setPreparationTimeMinutes(request.getPreparationTimeMinutes());
        product.setShelfLifeHours(request.getShelfLifeHours());
        product.setUnit(request.getUnit());
        product.setCalories(request.getCalories());
        product.setIngredients(request.getIngredients());
        product.setAllergens(request.getAllergens());
        product.setTags(request.getTags());
        product.setMediaUrls(request.getMediaUrls());
        product.setCostPrice(request.getCostPrice());
        product.setTaxClass(request.getTaxClass());
        product.setMetaTitle(request.getMetaTitle());
        product.setMetaDescription(request.getMetaDescription());
        product.setMaxOrderQuantity(request.getMaxOrderQuantity());

        Product updatedProduct = productRepository.save(product);
        
        syncToElasticsearch(updatedProduct);
        publishProductEvent(updatedProduct, "UPDATED");
        
        log.info("Product updated successfully: {}", productId);

        return productMapper.toResponse(updatedProduct);
    }

    // Update product status
    public ProductResponse updateProductStatus(String productId, Product.ProductStatus status) {
        log.info("Updating product status to {} for product: {}", status, productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));

        product.setStatus(status);
        Product updatedProduct = productRepository.save(product);
        
        syncToElasticsearch(updatedProduct);
        publishProductEvent(updatedProduct, "STATUS_UPDATED");

        log.info("Product status updated successfully: {}", productId);
        return productMapper.toResponse(updatedProduct);
    }

    // Toggle featured status
    public ProductResponse toggleFeaturedStatus(String productId) {
        log.info("Toggling featured status for product: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));

        product.setIsFeatured(!product.getIsFeatured());
        Product updatedProduct = productRepository.save(product);

        log.info("Product featured status toggled to {} for product: {}",
                   updatedProduct.getIsFeatured(), productId);

        return productMapper.toResponse(updatedProduct);
    }

    // Delete product
    public void deleteProduct(String productId) {
        log.info("Deleting product: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));

        // Check if product has pending orders (would require order service integration)
        // For now, we'll just delete

        productRepository.delete(product);
        deleteFromElasticsearch(productId);
        
        // publish event
        com.blubugtech.common.contract.messaging.ProductPayload payload = com.blubugtech.common.contract.messaging.ProductPayload.builder()
                .productId(UUID.fromString(productId))
                .action("DELETED")
                .timestamp(LocalDateTime.now())
                .build();
        com.blubugtech.common.event.ProductEvent event = new com.blubugtech.common.event.ProductEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventType("PRODUCT_DELETED");
        event.setTimestamp(java.time.Instant.now());
        event.setPayload(payload);
        productEventPublisher.publishProductUpdated(event);
        
        log.info("Product deleted successfully: {}", productId);
    }

    // Get recently added products
    @Transactional(readOnly = true)
    public Page<ProductResponse> getRecentlyAddedProducts(int days, Pageable pageable) {
        log.debug("Fetching products added in last {} days", days);

        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return productRepository.findByStatusAndCreatedAtAfter(Product.ProductStatus.ACTIVE, since, pageable)
                .map(productMapper::toResponse);
    }

    // Get products by preparation time range
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByPreparationTime(Integer minMinutes, Integer maxMinutes, Pageable pageable) {
        log.debug("Fetching products by preparation time: {} - {} minutes", minMinutes, maxMinutes);

        return productRepository.findByStatusAndPreparationTimeMinutesBetween(
                Product.ProductStatus.ACTIVE, minMinutes, maxMinutes, pageable)
                .map(productMapper::toResponse);
    }

    // Get product statistics
    public Map<String, Object> getProductStatistics() {
        log.debug("Fetching product statistics");
        long totalProducts = productRepository.count();
        long activeProducts = productRepository.countByStatus(Product.ProductStatus.ACTIVE);

        return Map.of(
                "totalProducts", totalProducts,
                "activeProducts", activeProducts,
                "inactiveProducts", 0L,
                "discontinuedProducts", 0L,
                "featuredProducts", 0L,
                "averagePrice", 0.0,
                "productsByCategory", List.of()
        );
    }

    // Check product availability
    @Transactional(readOnly = true)
    public boolean isProductAvailable(String productId) {
        return productRepository.findById(productId)
                .map(Product::isAvailable)
                .orElse(false);
    }

    // Get product entity (for internal use)
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByIds(List<String> productIds) {
        log.debug("Fetching products by IDs: {}", productIds);
        return productRepository.findAllById(productIds).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> validateProducts(List<String> productIds) {
        log.debug("Validating products for IDs: {}", productIds);
        List<Product> products = productRepository.findAllById(productIds);
        
        if (products.size() != productIds.size()) {
            List<String> foundIds = products.stream().map(Product::getId).collect(Collectors.toList());
            List<String> missingIds = productIds.stream().filter(id -> !foundIds.contains(id)).collect(Collectors.toList());
            throw new ResourceNotFoundException("Products", "ids", missingIds.toString());
        }
        
        return products.stream().map(productMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductEntity(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));
    }

    private void syncToElasticsearch(Product product) {
        try {
            
            

            productSearchService.indexProduct(product);
        } catch (Exception e) {
            log.error("Failed to sync product {} to Elasticsearch: {}", product.getId(), e.getMessage());
        }
    }

    private void deleteFromElasticsearch(String productId) {
        try {
            productSearchService.deleteProductFromIndex(productId);
        } catch (Exception e) {
            log.error("Failed to delete product {} from Elasticsearch: {}", productId, e.getMessage());
        }
    }

    private void publishProductEvent(Product product, String action) {
        try {
            com.blubugtech.common.contract.messaging.ProductPayload payload = com.blubugtech.common.contract.messaging.ProductPayload.builder()
                    .productId(java.util.UUID.fromString(product.getId()))
                    .name(product.getName())
                    .price(product.getPrice())
                    .action(action)
                    .timestamp(java.time.LocalDateTime.now())
                    .build();
            com.blubugtech.common.event.ProductEvent event = new com.blubugtech.common.event.ProductEvent();
            event.setEventId(java.util.UUID.randomUUID().toString());
            event.setEventType("PRODUCT_" + action.toUpperCase());
            event.setTimestamp(java.time.Instant.now());
            event.setPayload(payload);
            productEventPublisher.publishProductUpdated(event);
        } catch (Exception e) {
            log.error("Failed to publish ProductEvent for {}: {}", product.getId(), e.getMessage());
        }
    }

}
