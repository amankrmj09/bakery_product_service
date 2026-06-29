package com.shah_s.bakery_product_service.service;

import com.shah_s.bakery_product_service.dto.ProductRequest;
import com.shah_s.bakery_product_service.dto.ProductResponse;
import com.shah_s.bakery_product_service.entity.Category;
import com.shah_s.bakery_product_service.entity.Inventory;
import com.shah_s.bakery_product_service.entity.Product;
import com.shah_s.bakery_product_service.exception.ProductServiceException;
import com.shah_s.bakery_product_service.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.shah_s.bakery_product_service.exception.*;
import org.springframework.stereotype.Service;
import org.devofblue.common.exception.DuplicateResourceException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

import org.devofblue.common.exception.DuplicateResourceException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    final private ProductRepository productRepository;

    final private CategoryService categoryService;

    final private InventoryService inventoryService;
    
    final private com.shah_s.bakery_product_service.repository.ProductSearchRepository productSearchRepository;
    
    final private ProductEventPublisher productEventPublisher;

    public ProductService(ProductRepository productRepository, CategoryService categoryService, InventoryService inventoryService, com.shah_s.bakery_product_service.repository.ProductSearchRepository productSearchRepository, ProductEventPublisher productEventPublisher) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.inventoryService = inventoryService;
        this.productSearchRepository = productSearchRepository;
        this.productEventPublisher = productEventPublisher;
    }

    // Create new product
    public ProductResponse createProduct(ProductRequest request) {
        logger.info("Creating new product: {} (SKU: {})", request.getName(), request.getSku());

        // Check if SKU already exists
        if (productRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("Product with SKU '" + request.getSku() + "' already exists");
        }

        // Validate category exists
        Category category = categoryService.getCategoryEntity(request.getCategoryId());

        // Create product
        Product product = new Product();
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
        product.setWeightGrams(request.getWeightGrams());
        product.setCaloriesPerUnit(request.getCaloriesPerUnit());
        product.setIngredients(request.getIngredients());
        product.setAllergens(request.getAllergens());
        product.setTags(request.getTags());
        product.setMediaUrls(request.getMediaUrls());

        Product savedProduct = productRepository.save(product);

        // Create initial inventory
        inventoryService.createInventoryForProduct(savedProduct, request.getInitialStock(),
                request.getMinimumStock(), request.getReorderLevel());

        syncToElasticsearch(savedProduct);
        publishProductEvent(savedProduct, "CREATED");

        logger.info("Product created successfully with ID: {}", savedProduct.getId());
        return ProductResponse.from(savedProduct);
    }

    // Get all products
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        logger.debug("Fetching all products");

        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    // Get active products
    @Transactional(readOnly = true)
    public List<ProductResponse> getActiveProducts() {
        logger.debug("Fetching active products");

        return productRepository.findByStatusOrderByNameAsc(Product.ProductStatus.ACTIVE).stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    // Get available products (active with stock)
    @Transactional(readOnly = true)
    public List<ProductResponse> getAvailableProducts() {
        logger.debug("Fetching available products");

        return productRepository.findAvailableProducts().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    // Get featured products
    @Transactional(readOnly = true)
    public List<ProductResponse> getFeaturedProducts() {
        logger.debug("Fetching featured products");

        return productRepository.findByIsFeaturedTrueAndStatusOrderByCreatedAtDesc(Product.ProductStatus.ACTIVE).stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    // Get product by ID
    @Transactional(readOnly = true)
    public ProductResponse getProductById(UUID productId) {
        logger.debug("Fetching product by ID: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));

        return ProductResponse.from(product);
    }

    // Get product by SKU
    @Transactional(readOnly = true)
    public Optional<ProductResponse> getProductBySku(String sku) {
        logger.debug("Fetching product by SKU: {}", sku);

        return productRepository.findBySku(sku)
                .map(ProductResponse::from);
    }

    // Get products by category
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(UUID categoryId) {
        logger.debug("Fetching products by category: {}", categoryId);

        return productRepository.findByCategoryIdAndStatusOrderByNameAsc(categoryId, Product.ProductStatus.ACTIVE).stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    // Get products by category with pagination
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByCategoryWithPagination(UUID categoryId, Pageable pageable) {
        logger.debug("Fetching products by category with pagination: {}", categoryId);

        return productRepository.findByCategoryIdAndStatus(categoryId, Product.ProductStatus.ACTIVE, pageable)
                .map(ProductResponse::from);
    }

    // Search products
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(String searchTerm) {
        logger.debug("Searching products with term: {}", searchTerm);

        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 50);
        Page<com.shah_s.bakery_product_service.document.ProductDocument> results = productSearchRepository.findByNameOrDescriptionOrTags(searchTerm, searchTerm, searchTerm, pageable);
        
        // We'll map the ES documents back to ProductResponse, though we miss some DB fields this way,
        // it's fine for search. Alternatively we could fetch IDs from ES and query DB.
        // Let's fetch IDs from DB to return complete ProductResponse
        List<UUID> productIds = results.getContent().stream()
            .map(doc -> UUID.fromString(doc.getId()))
            .collect(Collectors.toList());
            
        return productRepository.findAllById(productIds).stream()
                .filter(p -> p.getStatus() == Product.ProductStatus.ACTIVE)
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    // Search products with pagination
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProductsWithPagination(String searchTerm, Pageable pageable) {
        logger.debug("Searching products with pagination, term: {}", searchTerm);

        Page<com.shah_s.bakery_product_service.document.ProductDocument> results = productSearchRepository.findByNameOrDescriptionOrTags(searchTerm, searchTerm, searchTerm, pageable);
        List<UUID> productIds = results.getContent().stream()
            .map(doc -> UUID.fromString(doc.getId()))
            .collect(Collectors.toList());
            
        List<ProductResponse> productResponses = productRepository.findAllById(productIds).stream()
                .filter(p -> p.getStatus() == Product.ProductStatus.ACTIVE)
                .map(ProductResponse::from)
                .collect(Collectors.toList());
                
        return new org.springframework.data.domain.PageImpl<>(productResponses, pageable, results.getTotalElements());
    }

    // Get products by price range
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        logger.debug("Fetching products by price range: {} - {}", minPrice, maxPrice);

        return productRepository.findByPriceRange(minPrice, maxPrice, Product.ProductStatus.ACTIVE).stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    // Get products on sale
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsOnSale() {
        logger.debug("Fetching products on sale");

        return productRepository.findProductsOnSale(Product.ProductStatus.ACTIVE).stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    // Get products by tag
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByTag(String tag) {
        logger.debug("Fetching products by tag: {}", tag);

        return productRepository.findByTag(tag, Product.ProductStatus.ACTIVE).stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    // Get products without specific allergen
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsWithoutAllergen(String allergen) {
        logger.debug("Fetching products without allergen: {}", allergen);

        return productRepository.findProductsWithoutAllergen(allergen, Product.ProductStatus.ACTIVE).stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    // Advanced product search with filters
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProductsWithFilters(UUID categoryId, Product.ProductStatus status,
                                                          BigDecimal minPrice, BigDecimal maxPrice,
                                                          Boolean inStock) {
        logger.debug("Advanced product search with filters");

        return productRepository.findProductsWithFilters(categoryId, status, minPrice, maxPrice, inStock).stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    // Update product
    public ProductResponse updateProduct(UUID productId, ProductRequest request) {
        logger.info("Updating product: {}", productId);

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
        product.setWeightGrams(request.getWeightGrams());
        product.setCaloriesPerUnit(request.getCaloriesPerUnit());
        product.setIngredients(request.getIngredients());
        product.setAllergens(request.getAllergens());
        product.setTags(request.getTags());
        product.setMediaUrls(request.getMediaUrls());

        Product updatedProduct = productRepository.save(product);
        
        syncToElasticsearch(updatedProduct);
        publishProductEvent(updatedProduct, "UPDATED");
        
        logger.info("Product updated successfully: {}", productId);

        return ProductResponse.from(updatedProduct);
    }

    // Update product status
    public ProductResponse updateProductStatus(UUID productId, Product.ProductStatus status) {
        logger.info("Updating product status to {} for product: {}", status, productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));

        product.setStatus(status);
        Product updatedProduct = productRepository.save(product);
        
        syncToElasticsearch(updatedProduct);
        publishProductEvent(updatedProduct, "STATUS_UPDATED");

        logger.info("Product status updated successfully: {}", productId);
        return ProductResponse.from(updatedProduct);
    }

    // Toggle featured status
    public ProductResponse toggleFeaturedStatus(UUID productId) {
        logger.info("Toggling featured status for product: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));

        product.setIsFeatured(!product.getIsFeatured());
        Product updatedProduct = productRepository.save(product);

        logger.info("Product featured status toggled to {} for product: {}",
                   updatedProduct.getIsFeatured(), productId);

        return ProductResponse.from(updatedProduct);
    }

    // Delete product
    public void deleteProduct(UUID productId) {
        logger.info("Deleting product: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));

        // Check if product has pending orders (would require order service integration)
        // For now, we'll just delete

        productRepository.delete(product);
        deleteFromElasticsearch(productId);
        
        // publish event
        org.devofblue.common.event.ProductEvent event = org.devofblue.common.event.ProductEvent.builder()
                .productId(productId)
                .status("DELETED")
                .timestamp(LocalDateTime.now())
                .build();
        productEventPublisher.publishProductUpdated(event);
        
        logger.info("Product deleted successfully: {}", productId);
    }

    // Get recently added products
    @Transactional(readOnly = true)
    public List<ProductResponse> getRecentlyAddedProducts(int days) {
        logger.debug("Fetching products added in last {} days", days);

        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return productRepository.findByStatusAndCreatedAtAfterOrderByCreatedAtDesc(Product.ProductStatus.ACTIVE, since).stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    // Get products by preparation time range
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByPreparationTime(Integer minMinutes, Integer maxMinutes) {
        logger.debug("Fetching products by preparation time: {} - {} minutes", minMinutes, maxMinutes);

        return productRepository.findByStatusAndPreparationTimeMinutesBetweenOrderByPreparationTimeMinutesAsc(
                Product.ProductStatus.ACTIVE, minMinutes, maxMinutes).stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    // Get product statistics
    @Transactional(readOnly = true)
    public Map<String, Object> getProductStatistics() {
        logger.debug("Fetching product statistics");

        Object[] stats = productRepository.getProductStatistics();
        if (stats == null || stats.length < 6) {
            stats = new Object[]{0L, 0L, 0L, 0L, 0L, 0.0};
        }
        List<Object[]> categoryStats = productRepository.countProductsByCategory(Product.ProductStatus.ACTIVE);
        if (categoryStats == null) {
            categoryStats = List.of();
        }

        return Map.of(
                "totalProducts", stats[0] != null ? stats[0] : 0L,
                "activeProducts", stats[1] != null ? stats[1] : 0L,
                "inactiveProducts", stats[2] != null ? stats[2] : 0L,
                "discontinuedProducts", stats[3] != null ? stats[3] : 0L,
                "featuredProducts", stats[4] != null ? stats[4] : 0L,
                "averagePrice", stats[5] != null ? stats[5] : 0.0,
                "productsByCategory", categoryStats.stream().map(stat -> Map.of(
                        "categoryName", stat[0],
                        "productCount", stat[1]
                )).toList()
        );
    }

    // Check product availability
    @Transactional(readOnly = true)
    public boolean isProductAvailable(UUID productId) {
        return productRepository.findById(productId)
                .map(Product::isAvailable)
                .orElse(false);
    }

    // Get product entity (for internal use)
    @Transactional(readOnly = true)
    public Product getProductEntity(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));
    }

    private void syncToElasticsearch(Product product) {
        try {
            com.shah_s.bakery_product_service.document.ProductDocument doc = new com.shah_s.bakery_product_service.document.ProductDocument();
            doc.setId(product.getId().toString());
            doc.setSku(product.getSku());
            doc.setName(product.getName());
            doc.setDescription(product.getDescription());
            if (product.getCategory() != null) {
                doc.setCategoryName(product.getCategory().getName());
            }
            doc.setPrice(product.getPrice());
            doc.setStatus(product.getStatus().name());
            doc.setTags(product.getTags());
            doc.setAverageRating(product.getAverageRating());
            productSearchRepository.save(doc);
        } catch (Exception e) {
            logger.error("Failed to sync product {} to Elasticsearch: {}", product.getId(), e.getMessage());
        }
    }

    private void deleteFromElasticsearch(UUID productId) {
        try {
            productSearchRepository.deleteById(productId.toString());
        } catch (Exception e) {
            logger.error("Failed to delete product {} from Elasticsearch: {}", productId, e.getMessage());
        }
    }

    private void publishProductEvent(Product product, String action) {
        try {
            org.devofblue.common.event.ProductEvent event = org.devofblue.common.event.ProductEvent.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .status(action)
                    .timestamp(LocalDateTime.now())
                    .build();
            productEventPublisher.publishProductUpdated(event);
        } catch (Exception e) {
            logger.error("Failed to publish ProductEvent for {}: {}", product.getId(), e.getMessage());
        }
    }
}
