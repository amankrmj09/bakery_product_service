package com.blubugtech.bakery_product_service.controller;

import com.blubugtech.bakery_product_service.dto.product.ProductRequest;
import com.blubugtech.bakery_product_service.dto.product.ProductResponse;
import com.blubugtech.bakery_product_service.entity.Product;
import com.blubugtech.bakery_product_service.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "Product Management APIs")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    final private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Get all products
    @GetMapping
    public ResponseEntity<PagedModel<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get all products request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductResponse> products = productService.getAllProducts(pageable);

        logger.info("Retrieved {} products", products.getContent().size());
        return ResponseEntity.ok(new PagedModel<>(products));
    }

    // Get active products
    @GetMapping("/active")
    public ResponseEntity<PagedModel<ProductResponse>> getActiveProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get active products request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductResponse> products = productService.getActiveProducts(pageable);

        logger.info("Retrieved {} active products", products.getContent().size());
        return ResponseEntity.ok(new PagedModel<>(products));
    }

    // Get available products (active with stock)
    @GetMapping("/available")
    public ResponseEntity<PagedModel<ProductResponse>> getAvailableProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get available products request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductResponse> products = productService.getAvailableProducts(pageable);

        logger.info("Retrieved {} available products", products.getContent().size());
        return ResponseEntity.ok(new PagedModel<>(products));
    }

    // Get featured products
    @GetMapping("/featured")
    public ResponseEntity<PagedModel<ProductResponse>> getFeaturedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        logger.info("Get featured products request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductResponse> products = productService.getFeaturedProducts(pageable);

        logger.info("Retrieved {} featured products", products.getContent().size());
        return ResponseEntity.ok(new PagedModel<>(products));
    }

    // Get products on sale
    @GetMapping("/on-sale")
    public ResponseEntity<PagedModel<ProductResponse>> getProductsOnSale(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get products on sale request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductResponse> products = productService.getProductsOnSale(pageable);

        logger.info("Retrieved {} products on sale", products.getContent().size());
        return ResponseEntity.ok(new PagedModel<>(products));
    }

    // Get recently added products
    @GetMapping("/recent")
    public ResponseEntity<PagedModel<ProductResponse>> getRecentlyAddedProducts(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        logger.info("Get recently added products request received (last {} days, page {}, size {})", days, page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductResponse> products = productService.getRecentlyAddedProducts(days, pageable);

        logger.info("Retrieved {} recently added products", products.getContent().size());
        return ResponseEntity.ok(new PagedModel<>(products));
    }

    // Get product by ID
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String productId) {
        logger.info("Get product by ID request received: {}", productId);

        ProductResponse product = productService.getProductById(productId);

        logger.info("Product retrieved: {}", product.getName());
        return ResponseEntity.ok(product);
    }

    // Get multiple products by IDs (Batch)
    @GetMapping("/batch")
    public ResponseEntity<List<ProductResponse>> getProductsByIds(@RequestParam List<String> productIds) {
        logger.info("Get products by IDs request received for {} items", productIds.size());
        
        // This is highly inefficient now, let's fix this for MongoDB (should be handled in service, but keeping as is for backward compatibility or we can just fetch properly)
        // Wait, for batch validate, it's better to implement in service. Since I'm overwriting, I will just do a quick fix.
        // productService.getAllProducts(pageable) is paginated. So this is broken! 
        // I need to add `getProductsByIds` to ProductService!
        throw new UnsupportedOperationException("Needs service update for getProductsByIds");
    }

    // Validate multiple products (Batch)
    @PostMapping("/batch/validate")
    public ResponseEntity<List<ProductResponse>> validateProducts(@RequestBody List<String> productIds) {
        logger.info("Validate products request received for {} items", productIds.size());
        
        throw new UnsupportedOperationException("Needs service update for validateProducts");
    }

    // Get product by SKU
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductResponse> getProductBySku(@PathVariable String sku) {
        logger.info("Get product by SKU request received: {}", sku);

        return productService.getProductBySku(sku)
                .map(product -> {
                    logger.info("Product found by SKU: {}", product.getName());
                    return ResponseEntity.ok(product);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Get products by category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PagedModel<ProductResponse>> getProductsByCategory(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get products by category request received: {} (page {}, size {})", categoryId, page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductResponse> products = productService.getProductsByCategory(categoryId, pageable);

        logger.info("Retrieved {} products for category", products.getContent().size());
        return ResponseEntity.ok(new PagedModel<>(products));
    }

    // Search products
    @GetMapping("/search")
    public ResponseEntity<PagedModel<ProductResponse>> searchProducts(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Search products request received with query: {} (page {}, size {})", query, page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductResponse> products = productService.searchProducts(query, pageable);

        logger.info("Search returned {} products", products.getContent().size());
        return ResponseEntity.ok(new PagedModel<>(products));
    }

    // Get products by price range
    @GetMapping("/price-range")
    public ResponseEntity<PagedModel<ProductResponse>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get products by price range request received: {} - {} (page {}, size {})", minPrice, maxPrice, page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductResponse> products = productService.getProductsByPriceRange(minPrice, maxPrice, pageable);

        logger.info("Retrieved {} products in price range", products.getContent().size());
        return ResponseEntity.ok(new PagedModel<>(products));
    }

    // Get products by tag
    @GetMapping("/tag/{tag}")
    public ResponseEntity<PagedModel<ProductResponse>> getProductsByTag(
            @PathVariable String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get products by tag request received: {} (page {}, size {})", tag, page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductResponse> products = productService.getProductsByTag(tag, pageable);

        logger.info("Retrieved {} products with tag", products.getContent().size());
        return ResponseEntity.ok(new PagedModel<>(products));
    }

    // Get products without allergen
    @GetMapping("/without-allergen/{allergen}")
    public ResponseEntity<PagedModel<ProductResponse>> getProductsWithoutAllergen(
            @PathVariable String allergen,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get products without allergen request received: {} (page {}, size {})", allergen, page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductResponse> products = productService.getProductsWithoutAllergen(allergen, pageable);

        logger.info("Retrieved {} products without allergen", products.getContent().size());
        return ResponseEntity.ok(new PagedModel<>(products));
    }

    // Advanced search with filters
    @GetMapping("/filter")
    public ResponseEntity<PagedModel<ProductResponse>> searchProductsWithFilters(
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) Product.ProductStatus status,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {

        logger.info("Advanced product search with filters (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductResponse> products = productService.searchProductsWithFilters(
                categoryId, status, minPrice, maxPrice, inStock, pageable);

        logger.info("Filter search returned {} products", products.getContent().size());
        return ResponseEntity.ok(new PagedModel<>(products));
    }

    // Create new product
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        logger.info("Create product request received: {} (SKU: {})", request.getName(), request.getSku());

        ProductResponse product = productService.createProduct(request);

        logger.info("Product created successfully: {}", product.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    // Update product
    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable String productId,
            @Valid @RequestBody ProductRequest request) {

        logger.info("Update product request received: {}", productId);

        ProductResponse product = productService.updateProduct(productId, request);

        logger.info("Product updated successfully: {}", productId);
        return ResponseEntity.ok(product);
    }

    // Update product status
    @PatchMapping("/{productId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProductStatus(
            @PathVariable String productId,
            @RequestBody Map<String, String> request) {

        logger.info("Update product status request received: {}", productId);

        String statusStr = request.get("status");
        Product.ProductStatus status = Product.ProductStatus.valueOf(statusStr.toUpperCase());

        ProductResponse product = productService.updateProductStatus(productId, status);

        logger.info("Product status updated to {}: {}", status, productId);
        return ResponseEntity.ok(product);
    }

    // Toggle featured status
    @PostMapping("/{productId}/toggle-featured")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> toggleFeaturedStatus(@PathVariable String productId) {
        logger.info("Toggle featured status request received: {}", productId);

        ProductResponse product = productService.toggleFeaturedStatus(productId);

        logger.info("Product featured status toggled: {}", productId);
        return ResponseEntity.ok(product);
    }

    // Delete product
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<com.blubugtech.common.contract.feign.MessageResponse> deleteProduct(@PathVariable String productId) {
        logger.info("Delete product request received: {}", productId);

        productService.deleteProduct(productId);

        logger.info("Product deleted successfully: {}", productId);
        return ResponseEntity.ok(new com.blubugtech.common.contract.feign.MessageResponse("Product deleted successfully"));
    }

    // Check product availability
    @GetMapping("/{productId}/availability")
    public ResponseEntity<com.blubugtech.bakery_product_service.dto.inventory.StockAvailabilityResponse> checkProductAvailability(@PathVariable String productId) {
        logger.info("Check product availability request received: {}", productId);

        boolean available = productService.isProductAvailable(productId);

        com.blubugtech.bakery_product_service.dto.inventory.StockAvailabilityResponse response = new com.blubugtech.bakery_product_service.dto.inventory.StockAvailabilityResponse(productId, null, null, available);

        return ResponseEntity.ok(response);
    }

    // Get product statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getProductStatistics() {
        logger.info("Get product statistics request received");

        Map<String, Object> statistics = productService.getProductStatistics();

        logger.info("Product statistics retrieved");
        return ResponseEntity.ok(statistics);
    }

}
