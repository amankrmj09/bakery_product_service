package com.shah_s.bakery_product_service.service;

import com.shah_s.bakery_product_service.dto.CategoryRequestDto;
import com.shah_s.bakery_product_service.dto.CategoryResponseDto;
import com.shah_s.bakery_product_service.entity.Category;
import com.shah_s.bakery_product_service.exception.ProductServiceException;
import com.shah_s.bakery_product_service.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.devofblue.common.exception.DuplicateResourceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    final private CategoryRepository categoryRepository;
    final private com.shah_s.bakery_product_service.repository.ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, com.shah_s.bakery_product_service.repository.ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public CategoryResponseDto createCategory(CategoryRequestDto request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category with name '" + request.getName() + "' already exists");
        }

        if (request.getDisplayOrder() == null || request.getDisplayOrder() == 0) {
            request.setDisplayOrder(0);
        }

        Category category = new Category();
        category.setId(UUID.randomUUID().toString());
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setDisplayOrder(request.getDisplayOrder());
        category.setActive(request.getActive());
        category.setMediaUrls(request.getMediaUrls());
        category.setIconClass(request.getIconClass());

        Category savedCategory = categoryRepository.save(category);
        return CategoryResponseDto.from(savedCategory);
    }

    public Page<CategoryResponseDto> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(CategoryResponseDto::from);
    }

    public Page<CategoryResponseDto> getActiveCategories(Pageable pageable) {
        return categoryRepository.findByActiveTrueOrderByDisplayOrderAsc(pageable)
                .map(CategoryResponseDto::from);
    }

    public Page<CategoryResponseDto> getCategoriesWithProducts(Pageable pageable) {
        return getActiveCategories(pageable);
    }

    public Page<CategoryResponseDto> getCategoriesWithActiveProducts(Pageable pageable) {
        return getActiveCategories(pageable);
    }

    public CategoryResponseDto getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProductServiceException("Category not found with ID: " + categoryId));
        return CategoryResponseDto.from(category);
    }

    public Optional<CategoryResponseDto> getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .map(CategoryResponseDto::from);
    }

    public CategoryResponseDto updateCategory(String categoryId, CategoryRequestDto request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProductServiceException("Category not found with ID: " + categoryId));

        if (!category.getName().equals(request.getName()) &&
            categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category with name '" + request.getName() + "' already exists");
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setDisplayOrder(request.getDisplayOrder());
        category.setActive(request.getActive());
        category.setMediaUrls(request.getMediaUrls());
        category.setIconClass(request.getIconClass());

        Category updatedCategory = categoryRepository.save(category);
        return CategoryResponseDto.from(updatedCategory);
    }

    public void deleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProductServiceException("Category not found with ID: " + categoryId));

        if (productRepository.existsByCategoryId(categoryId)) {
            throw new ProductServiceException("Cannot delete category with existing products. Please move or delete products first.");
        }

        categoryRepository.delete(category);
    }

    public Page<CategoryResponseDto> searchCategories(String searchTerm, Pageable pageable) {
        return categoryRepository.searchByName(searchTerm, pageable)
                .map(CategoryResponseDto::from);
    }

    public CategoryResponseDto toggleCategoryStatus(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProductServiceException("Category not found with ID: " + categoryId));

        category.setActive(!category.getActive());
        Category updatedCategory = categoryRepository.save(category);

        return CategoryResponseDto.from(updatedCategory);
    }

    public void reorderCategories(Map<String, Integer> categoryOrders) {
        for (Map.Entry<String, Integer> entry : categoryOrders.entrySet()) {
            String categoryId = entry.getKey();
            Integer newOrder = entry.getValue();

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ProductServiceException("Category not found with ID: " + categoryId));

            category.setDisplayOrder(newOrder);
            categoryRepository.save(category);
        }
    }

    public Map<String, Object> getCategoryStatistics() {
        long activeCategoriesCount = categoryRepository.countByActiveTrue();

        return Map.of(
                "totalCategories", categoryRepository.count(),
                "activeCategories", activeCategoriesCount,
                "inactiveCategories", categoryRepository.count() - activeCategoriesCount,
                "categoryStats", List.of()
        );
    }

    public boolean categoryExists(String categoryId) {
        return categoryRepository.existsById(categoryId);
    }

    public Category getCategoryEntity(String categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProductServiceException("Category not found with ID: " + categoryId));
    }
}
