package com.blubugtech.bakery_product_service.service.impl;

import lombok.extern.slf4j.Slf4j;
import com.blubugtech.bakery_product_service.service.CategoryService;

import com.blubugtech.bakery_product_service.dto.category.CategoryRequest;
import com.blubugtech.bakery_product_service.dto.category.CategoryResponse;
import com.blubugtech.bakery_product_service.entity.Category;
import com.blubugtech.bakery_product_service.mapper.CategoryMapper;
import com.blubugtech.bakery_product_service.exception.ProductServiceException;
import com.blubugtech.bakery_product_service.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.blubakery.bakery_common_libs.exception.common.DuplicateResourceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import com.blubugtech.bakery_product_service.dto.category.CategoryWithTopProductsResponse;
import com.blubugtech.bakery_product_service.dto.product.ProductResponse;
import com.blubugtech.bakery_product_service.entity.Product;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    final private CategoryRepository categoryRepository;
    final private com.blubugtech.bakery_product_service.repository.ProductRepository productRepository;
    final private CategoryMapper categoryMapper;
    final private com.blubugtech.bakery_product_service.search.service.CategorySearchService categorySearchService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, com.blubugtech.bakery_product_service.repository.ProductRepository productRepository, CategoryMapper categoryMapper, com.blubugtech.bakery_product_service.search.service.CategorySearchService categorySearchService) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.categoryMapper = categoryMapper;
        this.categorySearchService = categorySearchService;
    }

    public CategoryResponse createCategory(CategoryRequest request) {
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
        category.setIsTopCategory(request.getIsTopCategory());
        category.setMediaUrls(request.getMediaUrls());
        category.setIconClass(request.getIconClass());

        Category savedCategory = categoryRepository.save(category);
        syncToElasticsearch(savedCategory);
        return categoryMapper.toResponse(savedCategory);
    }

    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toResponse);
    }

    public Page<CategoryResponse> getActiveCategories(Pageable pageable) {
        return categoryRepository.findByActiveTrueOrderByDisplayOrderAsc(pageable)
                .map(categoryMapper::toResponse);
    }

    public Page<CategoryResponse> getCategoriesWithProducts(Pageable pageable) {
        return getActiveCategories(pageable);
    }

    public Page<CategoryResponse> getCategoriesWithActiveProducts(Pageable pageable) {
        return getActiveCategories(pageable);
    }

    public List<CategoryWithTopProductsResponse> getTopCategoriesWithTopProducts(int productLimit) {
        List<Category> topCategories = categoryRepository.findByIsTopCategoryTrueAndActiveTrueOrderByDisplayOrderAsc();
        if (topCategories == null || topCategories.isEmpty()) {
            Page<Category> fallbackCategories = categoryRepository.findByActiveTrueOrderByDisplayOrderAsc(PageRequest.of(0, 3));
            topCategories = fallbackCategories.getContent();
        }

        return topCategories.stream().map(category -> {
            Page<Product> productsPage = productRepository.findByCategoryIdAndStatusAndAverageRatingGreaterThanOrderByAverageRatingDesc(
                    category.getId(), Product.ProductStatus.ACTIVE, 0.0, PageRequest.of(0, productLimit)
            );
            List<Product> products = productsPage.getContent();

            if (products == null || products.isEmpty()) {
                Page<Product> fallbackProductsPage = productRepository.findByCategoryIdAndStatus(
                        category.getId(), Product.ProductStatus.ACTIVE, PageRequest.of(0, Math.min(productLimit, 3))
                );
                products = fallbackProductsPage.getContent();
            }

            List<ProductResponse> productResponses = products.stream()
                    .map(ProductResponse::from)
                    .collect(Collectors.toList());

            return new CategoryWithTopProductsResponse(categoryMapper.toResponse(category), productResponses);
        }).collect(Collectors.toList());
    }

    public CategoryResponse getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProductServiceException("Category not found with ID: " + categoryId));
        return categoryMapper.toResponse(category);
    }

    public Optional<CategoryResponse> getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .map(categoryMapper::toResponse);
    }

    public CategoryResponse updateCategory(String categoryId, CategoryRequest request) {
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
        category.setIsTopCategory(request.getIsTopCategory());
        category.setMediaUrls(request.getMediaUrls());
        category.setIconClass(request.getIconClass());

        Category updatedCategory = categoryRepository.save(category);
        syncToElasticsearch(updatedCategory);
        return categoryMapper.toResponse(updatedCategory);
    }

    public void deleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProductServiceException("Category not found with ID: " + categoryId));

        if (productRepository.existsByCategoryId(categoryId)) {
            throw new ProductServiceException("Cannot delete category with existing products. Please move or delete products first.");
        }

        categoryRepository.delete(category);
        deleteFromElasticsearch(categoryId);
    }

    public Page<CategoryResponse> searchCategories(String searchTerm, Pageable pageable) {
        return categoryRepository.searchByName(searchTerm, pageable)
                .map(categoryMapper::toResponse);
    }

    public CategoryResponse toggleCategoryStatus(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProductServiceException("Category not found with ID: " + categoryId));

        category.setActive(!category.getActive());
        Category updatedCategory = categoryRepository.save(category);
        syncToElasticsearch(updatedCategory);

        return categoryMapper.toResponse(updatedCategory);
    }

    public void reorderCategories(Map<String, Integer> categoryOrders) {
        for (Map.Entry<String, Integer> entry : categoryOrders.entrySet()) {
            String categoryId = entry.getKey();
            Integer newOrder = entry.getValue();

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ProductServiceException("Category not found with ID: " + categoryId));

            category.setDisplayOrder(newOrder);
            Category saved = categoryRepository.save(category);
            syncToElasticsearch(saved);
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

    private void syncToElasticsearch(Category category) {
        try {
            categorySearchService.indexCategory(category);
        } catch (Exception e) {
            log.error("Failed to sync category {} to Elasticsearch: {}", category.getId(), e.getMessage());
        }
    }

    private void deleteFromElasticsearch(String categoryId) {
        try {
            categorySearchService.deleteCategoryFromIndex(categoryId);
        } catch (Exception e) {
            log.error("Failed to delete category {} from Elasticsearch: {}", categoryId, e.getMessage());
        }
    }
}
