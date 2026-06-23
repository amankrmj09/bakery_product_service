package com.shah_s.bakery_product_service.service;

import com.shah_s.bakery_product_service.dto.CategoryRequest;
import com.shah_s.bakery_product_service.dto.CategoryResponse;
import com.shah_s.bakery_product_service.entity.Category;
import com.shah_s.bakery_product_service.exception.ProductServiceException;
import com.shah_s.bakery_product_service.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.shah_s.bakery_product_service.exception.*;
import org.springframework.stereotype.Service;
import org.devofblue.common.exception.DuplicateResourceException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);


    final private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Create new category
    public CategoryResponse createCategory(CategoryRequest request) {
        logger.info("Creating new category: {}", request.getName());

        // Check if category name already exists
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category with name '" + request.getName() + "' already exists");
        }

        // Set display order if not provided
        if (request.getDisplayOrder() == null || request.getDisplayOrder() == 0) {
            Integer maxOrder = categoryRepository.getMaxDisplayOrder();
            request.setDisplayOrder(maxOrder + 1);
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setDisplayOrder(request.getDisplayOrder());
        category.setActive(request.getActive());
        category.setImageUrl(request.getImageUrl());
        category.setIconClass(request.getIconClass());

        Category savedCategory = categoryRepository.save(category);
        logger.info("Category created successfully with ID: {}", savedCategory.getId());

        return CategoryResponse.from(savedCategory);
    }

    // Get all categories
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        logger.debug("Fetching all categories");

        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    // Get active categories only
    @Transactional(readOnly = true)
    public List<CategoryResponse> getActiveCategories() {
        logger.debug("Fetching active categories");

        return categoryRepository.findByActiveTrueOrderByDisplayOrderAsc().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    // Get categories with products
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoriesWithProducts() {
        logger.debug("Fetching categories with products");

        return categoryRepository.findCategoriesWithProducts().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    // Get categories with active products
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoriesWithActiveProducts() {
        logger.debug("Fetching categories with active products");

        return categoryRepository.findCategoriesWithActiveProducts().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    // Get category by ID
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(UUID categoryId) {
        logger.debug("Fetching category by ID: {}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProductServiceException("Category not found with ID: " + categoryId));

        return CategoryResponse.from(category);
    }

    // Get category by name
    @Transactional(readOnly = true)
    public Optional<CategoryResponse> getCategoryByName(String name) {
        logger.debug("Fetching category by name: {}", name);

        return categoryRepository.findByName(name)
                .map(CategoryResponse::from);
    }

    // Update category
    public CategoryResponse updateCategory(UUID categoryId, CategoryRequest request) {
        logger.info("Updating category: {}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProductServiceException("Category not found with ID: " + categoryId));

        // Check if new name conflicts with existing category
        if (!category.getName().equals(request.getName()) &&
            categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category with name '" + request.getName() + "' already exists");
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setDisplayOrder(request.getDisplayOrder());
        category.setActive(request.getActive());
        category.setImageUrl(request.getImageUrl());
        category.setIconClass(request.getIconClass());

        Category updatedCategory = categoryRepository.save(category);
        logger.info("Category updated successfully: {}", categoryId);

        return CategoryResponse.from(updatedCategory);
    }

    // Delete category
    public void deleteCategory(UUID categoryId) {
        logger.info("Deleting category: {}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProductServiceException("Category not found with ID: " + categoryId));

        // Check if category has products
        if (category.getProductCount() > 0) {
            throw new ProductServiceException("Cannot delete category with existing products. " +
                    "Please move or delete products first.");
        }

        categoryRepository.delete(category);
        logger.info("Category deleted successfully: {}", categoryId);
    }

    // Search categories
    @Transactional(readOnly = true)
    public List<CategoryResponse> searchCategories(String searchTerm) {
        logger.debug("Searching categories with term: {}", searchTerm);

        return categoryRepository.searchByName(searchTerm).stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    // Activate/Deactivate category
    public CategoryResponse toggleCategoryStatus(UUID categoryId) {
        logger.info("Toggling status for category: {}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProductServiceException("Category not found with ID: " + categoryId));

        category.setActive(!category.getActive());
        Category updatedCategory = categoryRepository.save(category);

        logger.info("Category status toggled to {} for category: {}",
                   updatedCategory.getActive(), categoryId);

        return CategoryResponse.from(updatedCategory);
    }

    // Reorder categories
    public void reorderCategories(Map<UUID, Integer> categoryOrders) {
        logger.info("Reordering categories: {}", categoryOrders.size());

        for (Map.Entry<UUID, Integer> entry : categoryOrders.entrySet()) {
            UUID categoryId = entry.getKey();
            Integer newOrder = entry.getValue();

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ProductServiceException("Category not found with ID: " + categoryId));

            category.setDisplayOrder(newOrder);
            categoryRepository.save(category);
        }

        logger.info("Categories reordered successfully");
    }

    // Get category statistics
    @Transactional(readOnly = true)
    public Map<String, Object> getCategoryStatistics() {
        logger.debug("Fetching category statistics");

        List<Object[]> stats = categoryRepository.getCategoryStatistics();
        long activeCategoriesCount = categoryRepository.countByActiveTrue();

        Map<String, Object> statistics = Map.of(
                "totalCategories", categoryRepository.count(),
                "activeCategories", activeCategoriesCount,
                "inactiveCategories", categoryRepository.count() - activeCategoriesCount,
                "categoryStats", stats.stream().map(stat -> Map.of(
                        "categoryId", stat[0],
                        "categoryName", stat[1],
                        "totalProducts", stat[2],
                        "activeProducts", stat[3],
                        "featuredProducts", stat[4]
                )).collect(Collectors.toList())
        );

        return statistics;
    }

    // Check if category exists
    @Transactional(readOnly = true)
    public boolean categoryExists(UUID categoryId) {
        return categoryRepository.existsById(categoryId);
    }

    // Get category entity (for internal use)
    @Transactional(readOnly = true)
    public Category getCategoryEntity(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProductServiceException("Category not found with ID: " + categoryId));
    }
}

