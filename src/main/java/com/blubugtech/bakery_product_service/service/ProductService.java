package com.blubugtech.bakery_product_service.service;

import com.blubugtech.bakery_product_service.dto.ProductRequestDto;
import com.blubugtech.bakery_product_service.dto.ProductResponseDto;
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
import com.blubugtech.common.exception.DuplicateResourceException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.blubugtech.common.exception.DuplicateResourceException;
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
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    final private ProductRepository productRepository;

    final private CategoryService categoryService;

    final private InventoryService inventoryService;
    
    final private com.blubugtech.bakery_product_service.repository.ProductSearchRepository productSearchRepository;
    
    final private ProductEventPublisher productEventPublisher;

    public ProductService(ProductRepository productRepository, CategoryService categoryService, InventoryService inventoryService, com.blubugtech.bakery_product_service.repository.ProductSearchRepository productSearchRepository, ProductEventPublisher productEventPublisher) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.inventoryService = inventoryService;
        this.productSearchRepository = productSearchRepository;
        this.productEventPublisher = productEventPublisher;
    }

    // Create new product
    public ProductResponseDto createProduct(ProductRequestDto request) {
        logger.info("Creating new product: {} (SKU: {})", request.getName(), request.getSku());

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
        return ProductResponseDto.from(savedProduct);
    }

    // Get all products
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        logger.debug("Fetching all products");

        return productRepository.findAll(pageable)
                .map(ProductResponseDto::from);
    }

    // Get active products
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getActiveProducts(Pageable pageable) {
        logger.debug("Fetching active products");

        return productRepository.findByStatus(Product.ProductStatus.ACTIVE, pageable)
                .map(ProductResponseDto::from);
    }

    // Get available products (active with stock)
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAvailableProducts(Pageable pageable) {
        logger.debug("Fetching available products");

        return productRepository.findAvailableProducts(pageable)
                .map(ProductResponseDto::from);
    }

    // Get featured products
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getFeaturedProducts(Pageable pageable) {
        logger.debug("Fetching featured products");

        return productRepository.findByIsFeaturedTrueAndStatus(Product.ProductStatus.ACTIVE, pageable)
                .map(ProductResponseDto::from);
    }

    // Get product by ID
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(String productId) {
        logger.debug("Fetching product by ID: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));

        return ProductResponseDto.from(product);
    }

    // Get product by SKU
    @Transactional(readOnly = true)
    public Optional<ProductResponseDto> getProductBySku(String sku) {
        logger.debug("Fetching product by SKU: {}", sku);

        return productRepository.findBySku(sku)
                .map(ProductResponseDto::from);
    }

    // Get products by category
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProductsByCategory(String categoryId, Pageable pageable) {
        logger.debug("Fetching products by category with pagination: {}", categoryId);

        return productRepository.findByCategoryIdAndStatus(categoryId, Product.ProductStatus.ACTIVE, pageable)
                .map(ProductResponseDto::from);
    }

    // Search products
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> searchProducts(String searchTerm, Pageable pageable) {
        logger.debug("Searching products with pagination, term: {}", searchTerm);

        Page<com.blubugtech.bakery_product_service.document.ProductDocument> results = productSearchRepository.findByNameOrDescriptionOrTags(searchTerm, searchTerm, searchTerm, pageable);
        List<String> productIds = results.getContent().stream()
            .map(doc -> doc.getId())
            .collect(Collectors.toList());
            
        List<ProductResponseDto> productResponses = productRepository.findAllById(productIds).stream()
                .filter(p -> p.getStatus() == Product.ProductStatus.ACTIVE)
                .map(ProductResponseDto::from)
                .collect(Collectors.toList());
                
        return new org.springframework.data.domain.PageImpl<>(productResponses, pageable, results.getTotalElements());
    }

    // Get products by price range
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        logger.debug("Fetching products by price range: {} - {}", minPrice, maxPrice);

        return productRepository.findByPriceRange(minPrice, maxPrice, Product.ProductStatus.ACTIVE, pageable)
                .map(ProductResponseDto::from);
    }

    // Get products on sale
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProductsOnSale(Pageable pageable) {
        logger.debug("Fetching products on sale");

        return productRepository.findProductsOnSale(Product.ProductStatus.ACTIVE, pageable)
                .map(ProductResponseDto::from);
    }

    // Get products by tag
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProductsByTag(String tag, Pageable pageable) {
        logger.debug("Fetching products by tag: {}", tag);

        return productRepository.findByTag(tag, Product.ProductStatus.ACTIVE, pageable)
                .map(ProductResponseDto::from);
    }

    // Get products without specific allergen
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProductsWithoutAllergen(String allergen, Pageable pageable) {
        logger.debug("Fetching products without allergen: {}", allergen);

        return productRepository.findProductsWithoutAllergen(allergen, Product.ProductStatus.ACTIVE, pageable)
                .map(ProductResponseDto::from);
    }

    // Advanced product search with filters
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> searchProductsWithFilters(String categoryId, Product.ProductStatus status,
                                                          BigDecimal minPrice, BigDecimal maxPrice,
                                                          Boolean inStock, Pageable pageable) {
        logger.debug("Advanced product search with filters");

        return productRepository.findProductsWithFilters(categoryId, status, minPrice, maxPrice, inStock, pageable)
                .map(ProductResponseDto::from);
    }

    // Update product
    public ProductResponseDto updateProduct(String productId, ProductRequestDto request) {
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

        return ProductResponseDto.from(updatedProduct);
    }

    // Update product status
    public ProductResponseDto updateProductStatus(String productId, Product.ProductStatus status) {
        logger.info("Updating product status to {} for product: {}", status, productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));

        product.setStatus(status);
        Product updatedProduct = productRepository.save(product);
        
        syncToElasticsearch(updatedProduct);
        publishProductEvent(updatedProduct, "STATUS_UPDATED");

        logger.info("Product status updated successfully: {}", productId);
        return ProductResponseDto.from(updatedProduct);
    }

    // Toggle featured status
    public ProductResponseDto toggleFeaturedStatus(String productId) {
        logger.info("Toggling featured status for product: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));

        product.setIsFeatured(!product.getIsFeatured());
        Product updatedProduct = productRepository.save(product);

        logger.info("Product featured status toggled to {} for product: {}",
                   updatedProduct.getIsFeatured(), productId);

        return ProductResponseDto.from(updatedProduct);
    }

    // Delete product
    public void deleteProduct(String productId) {
        logger.info("Deleting product: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));

        // Check if product has pending orders (would require order service integration)
        // For now, we'll just delete

        productRepository.delete(product);
        deleteFromElasticsearch(productId);
        
        // publish event
        com.blubugtech.common.event.ProductEvent event = com.blubugtech.common.event.ProductEvent.builder()
                .productId(UUID.fromString(productId))
                .status("DELETED")
                .timestamp(LocalDateTime.now())
                .build();
        productEventPublisher.publishProductUpdated(event);
        
        logger.info("Product deleted successfully: {}", productId);
    }

    // Get recently added products
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getRecentlyAddedProducts(int days, Pageable pageable) {
        logger.debug("Fetching products added in last {} days", days);

        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return productRepository.findByStatusAndCreatedAtAfter(Product.ProductStatus.ACTIVE, since, pageable)
                .map(ProductResponseDto::from);
    }

    // Get products by preparation time range
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProductsByPreparationTime(Integer minMinutes, Integer maxMinutes, Pageable pageable) {
        logger.debug("Fetching products by preparation time: {} - {} minutes", minMinutes, maxMinutes);

        return productRepository.findByStatusAndPreparationTimeMinutesBetween(
                Product.ProductStatus.ACTIVE, minMinutes, maxMinutes, pageable)
                .map(ProductResponseDto::from);
    }

    // Get product statistics
    public Map<String, Object> getProductStatistics() {
        logger.debug("Fetching product statistics");
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
    @Transactional(readOnly = true)
    public Product getProductEntity(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));
    }

    private void syncToElasticsearch(Product product) {
        try {
            com.blubugtech.bakery_product_service.document.ProductDocument doc = new com.blubugtech.bakery_product_service.document.ProductDocument();
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

            productSearchRepository.save(doc);
        } catch (Exception e) {
            logger.error("Failed to sync product {} to Elasticsearch: {}", product.getId(), e.getMessage());
        }
    }

    private void deleteFromElasticsearch(String productId) {
        try {
            productSearchRepository.deleteById(productId.toString());
        } catch (Exception e) {
            logger.error("Failed to delete product {} from Elasticsearch: {}", productId, e.getMessage());
        }
    }

    private void publishProductEvent(Product product, String action) {
        try {
            com.blubugtech.common.event.ProductEvent event = com.blubugtech.common.event.ProductEvent.builder()
                    .productId(UUID.fromString(product.getId()))
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
