package com.blubugtech.bakery_product_service.service.impl;

import lombok.extern.slf4j.Slf4j;
import com.blubugtech.bakery_product_service.service.CategoryService;
import com.blubugtech.bakery_product_service.service.InventoryService;
import com.blubugtech.bakery_product_service.service.ProductCommandService;
import com.blubugtech.bakery_product_service.dto.product.ProductRequest;
import com.blubugtech.bakery_product_service.dto.product.ProductResponse;
import com.blubugtech.bakery_product_service.entity.Category;
import com.blubugtech.bakery_product_service.entity.Product;
import com.blubugtech.bakery_product_service.mapper.ProductMapper;
import com.blubugtech.bakery_product_service.search.service.ProductSearchService;
import com.blubugtech.bakery_product_service.exception.ProductServiceException;
import com.blubugtech.bakery_product_service.repository.ProductCommandRepository;
import com.blubugtech.bakery_product_service.cache.ProductCacheManager;
import org.springframework.stereotype.Service;
import org.blubakery.common.core.exception.common.DuplicateResourceException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class ProductCommandServiceImpl implements ProductCommandService {

    private final ProductCommandRepository productRepository;
    private final CategoryService categoryService;
    private final InventoryService inventoryService;
    private final ProductSearchService productSearchService;
    private final com.blubugtech.bakery_product_service.integration.EventPublisher productEventPublisher;
    private final ProductMapper productMapper;
    private final ProductCacheManager productCacheManager;

    public ProductCommandServiceImpl(ProductCommandRepository productRepository, CategoryService categoryService,
                                     InventoryService inventoryService, ProductSearchService productSearchService,
                                     com.blubugtech.bakery_product_service.integration.EventPublisher productEventPublisher,
                                     ProductMapper productMapper, ProductCacheManager productCacheManager) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.inventoryService = inventoryService;
        this.productSearchService = productSearchService;
        this.productEventPublisher = productEventPublisher;
        this.productMapper = productMapper;
        this.productCacheManager = productCacheManager;
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating new product: {} (SKU: {})", request.getName(), request.getSku());

        if (productRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("Product with SKU '" + request.getSku() + "' already exists");
        }

        Category category = categoryService.getCategoryEntity(request.getCategoryId());

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

        inventoryService.createInventoryForProduct(savedProduct, request.getInitialStock(),
                request.getMinimumStock(), request.getReorderLevel());

        syncToElasticsearch(savedProduct);
        publishProductEvent(savedProduct, "CREATED");
        productCacheManager.putProduct(savedProduct);

        log.info("Product created successfully with ID: {}", savedProduct.getId());
        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(String productId, ProductRequest request) {
        log.info("Updating product: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));

        if (!product.getSku().equals(request.getSku()) &&
            productRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("Product with SKU '" + request.getSku() + "' already exists");
        }

        Category category = categoryService.getCategoryEntity(request.getCategoryId());

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
        productCacheManager.putProduct(updatedProduct);
        
        log.info("Product updated successfully: {}", productId);

        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public ProductResponse updateProductStatus(String productId, Product.ProductStatus status) {
        log.info("Updating product status to {} for product: {}", status, productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));

        product.setStatus(status);
        Product updatedProduct = productRepository.save(product);
        
        syncToElasticsearch(updatedProduct);
        publishProductEvent(updatedProduct, "STATUS_UPDATED");
        productCacheManager.putProduct(updatedProduct);

        log.info("Product status updated successfully: {}", productId);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public ProductResponse toggleFeaturedStatus(String productId) {
        log.info("Toggling featured status for product: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));

        product.setIsFeatured(!product.getIsFeatured());
        Product updatedProduct = productRepository.save(product);
        productCacheManager.putProduct(updatedProduct);

        log.info("Product featured status toggled to {} for product: {}",
                   updatedProduct.getIsFeatured(), productId);

        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(String productId) {
        log.info("Deleting product: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));

        productRepository.delete(product);
        deleteFromElasticsearch(productId);
        
        productEventPublisher.publishProductDeletedEvent(productId);
        productCacheManager.evictProduct(productId);
        
        log.info("Product deleted successfully: {}", productId);
    }

    private void syncToElasticsearch(Product product) {
        try {
            productSearchService.indexProduct(product);
        } catch (Exception e) {
            log.error("Failed to sync product {} to Elasticsearch", product.getId(), e);
        }
    }

    private void deleteFromElasticsearch(String productId) {
        try {
            productSearchService.deleteProductFromIndex(productId);
        } catch (Exception e) {
            log.error("Failed to delete product {} from Elasticsearch", productId, e);
        }
    }

    private void publishProductEvent(Product product, String action) {
        productEventPublisher.publishProductEvent(product, action);
    }
}
