package com.shah_s.bakery_product_service.controller;

import com.shah_s.bakery_product_service.dto.CategoryRequest;
import com.shah_s.bakery_product_service.dto.CategoryResponse;
import com.shah_s.bakery_product_service.service.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    final private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        logger.info("Get all categories request received");

        List<CategoryResponse> categories = categoryService.getAllCategories();

        logger.info("Retrieved {} categories", categories.size());
        return ResponseEntity.ok(categories);
    }

    // Get active categories only
    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponse>> getActiveCategories() {
        logger.info("Get active categories request received");

        List<CategoryResponse> categories = categoryService.getActiveCategories();

        logger.info("Retrieved {} active categories", categories.size());
        return ResponseEntity.ok(categories);
    }

    // Get categories with products
    @GetMapping("/with-products")
    public ResponseEntity<List<CategoryResponse>> getCategoriesWithProducts() {
        logger.info("Get categories with products request received");

        List<CategoryResponse> categories = categoryService.getCategoriesWithProducts();

        logger.info("Retrieved {} categories with products", categories.size());
        return ResponseEntity.ok(categories);
    }

    // Get categories with active products
    @GetMapping("/with-active-products")
    public ResponseEntity<List<CategoryResponse>> getCategoriesWithActiveProducts() {
        logger.info("Get categories with active products request received");

        List<CategoryResponse> categories = categoryService.getCategoriesWithActiveProducts();

        logger.info("Retrieved {} categories with active products", categories.size());
        return ResponseEntity.ok(categories);
    }

    // Get category by ID
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable UUID categoryId) {
        logger.info("Get category by ID request received: {}", categoryId);

        CategoryResponse category = categoryService.getCategoryById(categoryId);

        logger.info("Category retrieved: {}", category.getName());
        return ResponseEntity.ok(category);
    }

    // Create new category
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        logger.info("Create category request received: {}", request.getName());

        CategoryResponse category = categoryService.createCategory(request);

        logger.info("Category created successfully: {}", category.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    // Update category
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable UUID categoryId,
            @Valid @RequestBody CategoryRequest request) {

        logger.info("Update category request received: {}", categoryId);

        CategoryResponse category = categoryService.updateCategory(categoryId, request);

        logger.info("Category updated successfully: {}", categoryId);
        return ResponseEntity.ok(category);
    }

    // Delete category
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable UUID categoryId) {
        logger.info("Delete category request received: {}", categoryId);

        categoryService.deleteCategory(categoryId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Category deleted successfully");
        response.put("categoryId", categoryId.toString());

        logger.info("Category deleted successfully: {}", categoryId);
        return ResponseEntity.ok(response);
    }

    // Search categories
    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponse>> searchCategories(@RequestParam String query) {
        logger.info("Search categories request received with query: {}", query);

        List<CategoryResponse> categories = categoryService.searchCategories(query);

        logger.info("Search returned {} categories", categories.size());
        return ResponseEntity.ok(categories);
    }

    // Toggle category status
    @PostMapping("/{categoryId}/toggle-status")
    public ResponseEntity<CategoryResponse> toggleCategoryStatus(@PathVariable UUID categoryId) {
        logger.info("Toggle category status request received: {}", categoryId);

        CategoryResponse category = categoryService.toggleCategoryStatus(categoryId);

        logger.info("Category status toggled to {}: {}", category.getActive(), categoryId);
        return ResponseEntity.ok(category);
    }

    // Reorder categories
    @PostMapping("/reorder")
    public ResponseEntity<Map<String, String>> reorderCategories(@RequestBody Map<UUID, Integer> categoryOrders) {
        logger.info("Reorder categories request received for {} categories", categoryOrders.size());

        categoryService.reorderCategories(categoryOrders);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Categories reordered successfully");

        logger.info("Categories reordered successfully");
        return ResponseEntity.ok(response);
    }

    // Get category statistics
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getCategoryStatistics() {
        logger.info("Get category statistics request received");

        Map<String, Object> statistics = categoryService.getCategoryStatistics();

        logger.info("Category statistics retrieved");
        return ResponseEntity.ok(statistics);
    }

    // Health check
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "product-service-categories");
        response.put("timestamp", java.time.LocalDateTime.now().toString());

        return ResponseEntity.ok(response);
    }
}
