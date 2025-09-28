package com.shah_s.bakery_product_service.controller;

import com.shah_s.bakery_product_service.dto.ProductRequest;
import com.shah_s.bakery_product_service.dto.ProductResponse;
import com.shah_s.bakery_product_service.entity.Product;
import com.shah_s.bakery_product_service.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    final private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Get all products
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        logger.info("Get all products request received");

        List<ProductResponse> products = productService.getAllProducts();

        logger.info("Retrieved {} products", products.size());
        return ResponseEntity.ok(products);
    }

    // Get active products
    @GetMapping("/active")
    public ResponseEntity<List<ProductResponse>> getActiveProducts() {
        logger.info("Get active products request received");

        List<ProductResponse> products = productService.getActiveProducts();

        logger.info("Retrieved {} active products", products.size());
        return ResponseEntity.ok(products);
    }

    // Get available products (active with stock)
    @GetMapping("/available")
    public ResponseEntity<List<ProductResponse>> getAvailableProducts() {
        logger.info("Get available products request received");

        List<ProductResponse> products = productService.getAvailableProducts();

        logger.info("Retrieved {} available products", products.size());
        return ResponseEntity.ok(products);
    }

    // Get featured products
    @GetMapping("/featured")
    public ResponseEntity<List<ProductResponse>> getFeaturedProducts() {
        logger.info("Get featured products request received");

        List<ProductResponse> products = productService.getFeaturedProducts();

        logger.info("Retrieved {} featured products", products.size());
        return ResponseEntity.ok(products);
    }

    // Get products on sale
    @GetMapping("/on-sale")
    public ResponseEntity<List<ProductResponse>> getProductsOnSale() {
        logger.info("Get products on sale request received");

        List<ProductResponse> products = productService.getProductsOnSale();

        logger.info("Retrieved {} products on sale", products.size());
        return ResponseEntity.ok(products);
    }

    // Get recently added products
    @GetMapping("/recent")
    public ResponseEntity<List<ProductResponse>> getRecentlyAddedProducts(
            @RequestParam(defaultValue = "7") int days) {
        logger.info("Get recently added products request received (last {} days)", days);

        List<ProductResponse> products = productService.getRecentlyAddedProducts(days);

        logger.info("Retrieved {} recently added products", products.size());
        return ResponseEntity.ok(products);
    }

    // Get product by ID
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID productId) {
        logger.info("Get product by ID request received: {}", productId);

        ProductResponse product = productService.getProductById(productId);

        logger.info("Product retrieved: {}", product.getName());
        return ResponseEntity.ok(product);
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
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable UUID categoryId) {
        logger.info("Get products by category request received: {}", categoryId);

        List<ProductResponse> products = productService.getProductsByCategory(categoryId);

        logger.info("Retrieved {} products for category", products.size());
        return ResponseEntity.ok(products);
    }

    // Get products by category with pagination
    @GetMapping("/category/{categoryId}/paginated")
    public ResponseEntity<Page<ProductResponse>> getProductsByCategoryWithPagination(
            @PathVariable UUID categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {

        logger.info("Get products by category with pagination: {}, page: {}, size: {}", categoryId, page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProductResponse> products = productService.getProductsByCategoryWithPagination(categoryId, pageable);

        logger.info("Retrieved {} products (page {} of {})", products.getContent().size(),
                   page + 1, products.getTotalPages());
        return ResponseEntity.ok(products);
    }

    // Search products
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String query) {
        logger.info("Search products request received with query: {}", query);

        List<ProductResponse> products = productService.searchProducts(query);

        logger.info("Search returned {} products", products.size());
        return ResponseEntity.ok(products);
    }

    // Search products with pagination
    @GetMapping("/search/paginated")
    public ResponseEntity<Page<ProductResponse>> searchProductsWithPagination(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {

        logger.info("Search products with pagination: {}, page: {}, size: {}", query, page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProductResponse> products = productService.searchProductsWithPagination(query, pageable);

        logger.info("Search returned {} products (page {} of {})", products.getContent().size(),
                   page + 1, products.getTotalPages());
        return ResponseEntity.ok(products);
    }

    // Get products by price range
    @GetMapping("/price-range")
    public ResponseEntity<List<ProductResponse>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        logger.info("Get products by price range request received: {} - {}", minPrice, maxPrice);

        List<ProductResponse> products = productService.getProductsByPriceRange(minPrice, maxPrice);

        logger.info("Retrieved {} products in price range", products.size());
        return ResponseEntity.ok(products);
    }

    // Get products by tag
    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<ProductResponse>> getProductsByTag(@PathVariable String tag) {
        logger.info("Get products by tag request received: {}", tag);

        List<ProductResponse> products = productService.getProductsByTag(tag);

        logger.info("Retrieved {} products with tag", products.size());
        return ResponseEntity.ok(products);
    }

    // Get products without allergen
    @GetMapping("/without-allergen/{allergen}")
    public ResponseEntity<List<ProductResponse>> getProductsWithoutAllergen(@PathVariable String allergen) {
        logger.info("Get products without allergen request received: {}", allergen);

        List<ProductResponse> products = productService.getProductsWithoutAllergen(allergen);

        logger.info("Retrieved {} products without allergen", products.size());
        return ResponseEntity.ok(products);
    }

    // Advanced search with filters
    @GetMapping("/filter")
    public ResponseEntity<List<ProductResponse>> searchProductsWithFilters(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) Product.ProductStatus status,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean inStock) {

        logger.info("Advanced product search with filters");

        List<ProductResponse> products = productService.searchProductsWithFilters(
                categoryId, status, minPrice, maxPrice, inStock);

        logger.info("Filter search returned {} products", products.size());
        return ResponseEntity.ok(products);
    }

    // Create new product
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        logger.info("Create product request received: {} (SKU: {})", request.getName(), request.getSku());

        ProductResponse product = productService.createProduct(request);

        logger.info("Product created successfully: {}", product.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    // Update product
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductRequest request) {

        logger.info("Update product request received: {}", productId);

        ProductResponse product = productService.updateProduct(productId, request);

        logger.info("Product updated successfully: {}", productId);
        return ResponseEntity.ok(product);
    }

    // Update product status
    @PatchMapping("/{productId}/status")
    public ResponseEntity<ProductResponse> updateProductStatus(
            @PathVariable UUID productId,
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
    public ResponseEntity<ProductResponse> toggleFeaturedStatus(@PathVariable UUID productId) {
        logger.info("Toggle featured status request received: {}", productId);

        ProductResponse product = productService.toggleFeaturedStatus(productId);

        logger.info("Product featured status toggled: {}", productId);
        return ResponseEntity.ok(product);
    }

    // Delete product
    @DeleteMapping("/{productId}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable UUID productId) {
        logger.info("Delete product request received: {}", productId);

        productService.deleteProduct(productId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Product deleted successfully");
        response.put("productId", productId.toString());

        logger.info("Product deleted successfully: {}", productId);
        return ResponseEntity.ok(response);
    }

    // Check product availability
    @GetMapping("/{productId}/availability")
    public ResponseEntity<Map<String, Object>> checkProductAvailability(@PathVariable UUID productId) {
        logger.info("Check product availability request received: {}", productId);

        boolean available = productService.isProductAvailable(productId);

        Map<String, Object> response = new HashMap<>();
        response.put("productId", productId);
        response.put("available", available);

        return ResponseEntity.ok(response);
    }

    // Get product statistics
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getProductStatistics() {
        logger.info("Get product statistics request received");

        Map<String, Object> statistics = productService.getProductStatistics();

        logger.info("Product statistics retrieved");
        return ResponseEntity.ok(statistics);
    }

    // Health check
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "product-service-products");
        response.put("timestamp", java.time.LocalDateTime.now().toString());

        return ResponseEntity.ok(response);
    }
}
