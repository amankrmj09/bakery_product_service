package com.blubugtech.bakery_product_service.controller;

import com.blubugtech.bakery_product_service.dto.category.CategoryRequest;
import com.blubugtech.bakery_product_service.dto.category.CategoryResponse;
import com.blubugtech.bakery_product_service.service.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/categories")

public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    final private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<PagedModel<CategoryResponse>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "displayOrder") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get all categories request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CategoryResponse> categories = categoryService.getAllCategories(pageable);

        logger.info("Retrieved {} categories", categories.getContent().size());
        return ResponseEntity.ok(new PagedModel<>(categories));
    }

    // Get active categories only
    @GetMapping("/active")
    public ResponseEntity<PagedModel<CategoryResponse>> getActiveCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "displayOrder") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get active categories request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CategoryResponse> categories = categoryService.getActiveCategories(pageable);

        logger.info("Retrieved {} active categories", categories.getContent().size());
        return ResponseEntity.ok(new PagedModel<>(categories));
    }

    // Get categories with products
    @GetMapping("/with-products")
    public ResponseEntity<PagedModel<CategoryResponse>> getCategoriesWithProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "displayOrder") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get categories with products request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CategoryResponse> categories = categoryService.getCategoriesWithProducts(pageable);

        logger.info("Retrieved {} categories with products", categories.getContent().size());
        return ResponseEntity.ok(new PagedModel<>(categories));
    }

    // Get categories with active products
    @GetMapping("/with-active-products")
    public ResponseEntity<PagedModel<CategoryResponse>> getCategoriesWithActiveProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "displayOrder") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Get categories with active products request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CategoryResponse> categories = categoryService.getCategoriesWithActiveProducts(pageable);

        logger.info("Retrieved {} categories with active products", categories.getContent().size());
        return ResponseEntity.ok(new PagedModel<>(categories));
    }

    // Get top categories with top products
    @GetMapping("/top-with-products")
    public ResponseEntity<List<com.blubugtech.bakery_product_service.dto.category.CategoryWithTopProductsResponse>> getTopCategoriesWithTopProducts(
            @RequestParam(defaultValue = "5") int productLimit) {
        logger.info("Get top categories with top products request received (productLimit {})", productLimit);

        List<com.blubugtech.bakery_product_service.dto.category.CategoryWithTopProductsResponse> categories = categoryService.getTopCategoriesWithTopProducts(productLimit);

        logger.info("Retrieved {} top categories with products", categories.size());
        return ResponseEntity.ok(categories);
    }

    // Get category by ID
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable String categoryId) {
        logger.info("Get category by ID request received: {}", categoryId);

        CategoryResponse category = categoryService.getCategoryById(categoryId);

        logger.info("Category retrieved: {}", category.getName());
        return ResponseEntity.ok(category);
    }

    // Create new category
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        logger.info("Create category request received: {}", request.getName());

        CategoryResponse category = categoryService.createCategory(request);

        logger.info("Category created successfully: {}", category.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    // Update category
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable String categoryId,
            @Valid @RequestBody CategoryRequest request) {

        logger.info("Update category request received: {}", categoryId);

        CategoryResponse category = categoryService.updateCategory(categoryId, request);

        logger.info("Category updated successfully: {}", categoryId);
        return ResponseEntity.ok(category);
    }

    // Delete category
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<com.blubugtech.common.contract.feign.MessageResponse> deleteCategory(@PathVariable String categoryId) {
        logger.info("Delete category request received: {}", categoryId);

        categoryService.deleteCategory(categoryId);

        logger.info("Category deleted successfully: {}", categoryId);
        return ResponseEntity.ok(new com.blubugtech.common.contract.feign.MessageResponse("Category deleted successfully"));
    }

    // Search categories
    @GetMapping("/search")
    public ResponseEntity<PagedModel<CategoryResponse>> searchCategories(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "displayOrder") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        logger.info("Search categories request received with query: {} (page {}, size {})", query, page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CategoryResponse> categories = categoryService.searchCategories(query, pageable);

        logger.info("Search returned {} categories", categories.getContent().size());
        return ResponseEntity.ok(new PagedModel<>(categories));
    }

    // Toggle category status
    @PostMapping("/{categoryId}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> toggleCategoryStatus(@PathVariable String categoryId) {
        logger.info("Toggle category status request received: {}", categoryId);

        CategoryResponse category = categoryService.toggleCategoryStatus(categoryId);

        logger.info("Category status toggled to {}: {}", category.getActive(), categoryId);
        return ResponseEntity.ok(category);
    }

    // Reorder categories
    @PostMapping("/reorder")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<com.blubugtech.common.contract.feign.MessageResponse> reorderCategories(@RequestBody Map<String, Integer> categoryOrders) {
        logger.info("Reorder categories request received for {} categories", categoryOrders.size());

        categoryService.reorderCategories(categoryOrders);

        logger.info("Categories reordered successfully");
        return ResponseEntity.ok(new com.blubugtech.common.contract.feign.MessageResponse("Categories reordered successfully"));
    }

    // Get category statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getCategoryStatistics() {
        logger.info("Get category statistics request received");

        Map<String, Object> statistics = categoryService.getCategoryStatistics();

        logger.info("Category statistics retrieved");
        return ResponseEntity.ok(statistics);
    }

}
