package com.blubugtech.bakery_product_service.controller;

import lombok.extern.slf4j.Slf4j;
import com.blubugtech.bakery_product_service.dto.product.ProductResponse;
import com.blubugtech.bakery_product_service.entity.Product;
import com.blubugtech.bakery_product_service.service.ProductQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.blubakery.common.core.dto.RestPageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Query", description = "Product Query Management APIs")
@Slf4j
public class ProductQueryController {

    private final ProductQueryService productService;

    public ProductQueryController(ProductQueryService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<RestPageResponse<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        Sort sort = getSort(sortBy, sortDir);
        Page<ProductResponse> products = productService.getAllProducts(PageRequest.of(page, size, sort));
        return ResponseEntity.ok(new RestPageResponse<>(products));
    }

    @GetMapping("/active")
    public ResponseEntity<RestPageResponse<ProductResponse>> getActiveProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        Sort sort = getSort(sortBy, sortDir);
        Page<ProductResponse> products = productService.getActiveProducts(PageRequest.of(page, size, sort));
        return ResponseEntity.ok(new RestPageResponse<>(products));
    }

    @GetMapping("/available")
    public ResponseEntity<RestPageResponse<ProductResponse>> getAvailableProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        Sort sort = getSort(sortBy, sortDir);
        Page<ProductResponse> products = productService.getAvailableProducts(PageRequest.of(page, size, sort));
        return ResponseEntity.ok(new RestPageResponse<>(products));
    }

    @GetMapping("/featured")
    public ResponseEntity<RestPageResponse<ProductResponse>> getFeaturedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        Sort sort = getSort(sortBy, sortDir);
        Page<ProductResponse> products = productService.getFeaturedProducts(PageRequest.of(page, size, sort));
        return ResponseEntity.ok(new RestPageResponse<>(products));
    }

    @GetMapping("/on-sale")
    public ResponseEntity<RestPageResponse<ProductResponse>> getProductsOnSale(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        Sort sort = getSort(sortBy, sortDir);
        Page<ProductResponse> products = productService.getProductsOnSale(PageRequest.of(page, size, sort));
        return ResponseEntity.ok(new RestPageResponse<>(products));
    }

    @GetMapping("/recent")
    public ResponseEntity<RestPageResponse<ProductResponse>> getRecentlyAddedProducts(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        Sort sort = getSort(sortBy, sortDir);
        Page<ProductResponse> products = productService.getRecentlyAddedProducts(days, PageRequest.of(page, size, sort));
        return ResponseEntity.ok(new RestPageResponse<>(products));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @PostMapping("/batch")
    public ResponseEntity<RestPageResponse<ProductResponse>> getProductsByIds(
            @RequestBody List<String> productIds,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        Sort sort = getSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(productService.getProductsByIds(productIds, pageable));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductResponse> getProductBySku(@PathVariable String sku) {
        return productService.getProductBySku(sku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<RestPageResponse<ProductResponse>> getProductsByCategory(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        Sort sort = getSort(sortBy, sortDir);
        Page<ProductResponse> products = productService.getProductsByCategory(categoryId, PageRequest.of(page, size, sort));
        return ResponseEntity.ok(new RestPageResponse<>(products));
    }

    @GetMapping("/search")
    public ResponseEntity<RestPageResponse<ProductResponse>> searchProducts(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        Sort sort = getSort(sortBy, sortDir);
        Page<ProductResponse> products = productService.searchProducts(query, PageRequest.of(page, size, sort));
        return ResponseEntity.ok(new RestPageResponse<>(products));
    }

    @GetMapping("/price-range")
    public ResponseEntity<RestPageResponse<ProductResponse>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        Sort sort = getSort(sortBy, sortDir);
        Page<ProductResponse> products = productService.getProductsByPriceRange(minPrice, maxPrice, PageRequest.of(page, size, sort));
        return ResponseEntity.ok(new RestPageResponse<>(products));
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<RestPageResponse<ProductResponse>> getProductsByTag(
            @PathVariable String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        Sort sort = getSort(sortBy, sortDir);
        Page<ProductResponse> products = productService.getProductsByTag(tag, PageRequest.of(page, size, sort));
        return ResponseEntity.ok(new RestPageResponse<>(products));
    }

    @GetMapping("/without-allergen/{allergen}")
    public ResponseEntity<RestPageResponse<ProductResponse>> getProductsWithoutAllergen(
            @PathVariable String allergen,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        Sort sort = getSort(sortBy, sortDir);
        Page<ProductResponse> products = productService.getProductsWithoutAllergen(allergen, PageRequest.of(page, size, sort));
        return ResponseEntity.ok(new RestPageResponse<>(products));
    }

    private Sort getSort(String sortBy, String sortDir) {
        if ("popular".equalsIgnoreCase(sortBy)) {
            return Sort.by(Sort.Direction.DESC, "averageRating").and(Sort.by(Sort.Direction.DESC, "createdAt"));
        }
        return Sort.by(Sort.Direction.fromString(sortDir), sortBy);
    }

}
