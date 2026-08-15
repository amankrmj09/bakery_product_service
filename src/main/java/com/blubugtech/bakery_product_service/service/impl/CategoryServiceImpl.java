package com.blubugtech.bakery_product_service.service.impl;

import lombok.extern.slf4j.Slf4j;
import com.blubugtech.bakery_product_service.service.CategoryService;

import com.blubugtech.bakery_product_service.dto.category.CategoryRequest;
import com.blubugtech.bakery_product_service.dto.category.CategoryResponse;
import org.blubakery.common.core.dto.RestPageResponse;
import com.blubugtech.bakery_product_service.entity.Category;
import com.blubugtech.bakery_product_service.mapper.CategoryMapper;
import com.blubugtech.bakery_product_service.mapper.ProductMapper;
import com.blubugtech.bakery_product_service.exception.ProductServiceException;
import com.blubugtech.bakery_product_service.repository.CategoryRepository;
import com.blubugtech.bakery_product_service.repository.ProductQueryRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.blubakery.common.core.exception.common.DuplicateResourceException;
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
    final private ProductQueryRepository productRepository;
    final private CategoryMapper categoryMapper;
    final private ProductMapper productMapper;
    final private com.blubugtech.bakery_product_service.search.service.CategorySearchService categorySearchService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductQueryRepository productRepository, CategoryMapper categoryMapper, ProductMapper productMapper, com.blubugtech.bakery_product_service.search.service.CategorySearchService categorySearchService) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
        this.categorySearchService = categorySearchService;
    }

    @Caching(evict = {
            @CacheEvict(value = "categories", allEntries = true),
            @CacheEvict(value = "active-categories", allEntries = true)
    })
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
        CategoryResponse response = categoryMapper.toResponse(savedCategory);
        populateProductCounts(response);
        return response;
    }

    @Cacheable(value = "categories", key = "'all:' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        Page<CategoryResponse> responses = categoryRepository.findAll(pageable)
                .map(categoryMapper::toResponse);
        responses.getContent().forEach(this::populateProductCounts);
        return new RestPageResponse<>(responses);
    }

    @Cacheable(value = "active-categories", key = "#pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<CategoryResponse> getActiveCategories(Pageable pageable) {
        Page<CategoryResponse> responses = categoryRepository.findByActiveTrueOrderByDisplayOrderAsc(pageable)
                .map(categoryMapper::toResponse);
        responses.getContent().forEach(this::populateProductCounts);
        return new RestPageResponse<>(responses);
    }

    public Page<CategoryResponse> getCategoriesWithProducts(Pageable pageable) {
        return getActiveCategories(pageable);
    }

    public Page<CategoryResponse> getCategoriesWithActiveProducts(Pageable pageable) {
        return getActiveCategories(pageable);
    }

    public RestPageResponse<CategoryWithTopProductsResponse> getTopCategoriesWithTopProducts(int productLimit, Pageable pageable) {
        Page<Category> topCategoriesPage = categoryRepository.findByIsTopCategoryTrueAndActiveTrue(pageable);
        if (topCategoriesPage == null || topCategoriesPage.isEmpty()) {
            topCategoriesPage = categoryRepository.findByActiveTrueOrderByDisplayOrderAsc(pageable);
        }

        Page<CategoryWithTopProductsResponse> page = topCategoriesPage.map(category -> {
            Page<Product> productsPage = productRepository.findByCategoryIdAndStatusAndAverageRatingGreaterThanOrderByAverageRatingDesc(
                    category.getId(), Product.ProductStatus.ACTIVE, 0.0, PageRequest.of(0, productLimit));
            List<Product> products = productsPage.getContent();

            if (products == null || products.isEmpty()) {
                Page<Product> fallbackProductsPage = productRepository.findByCategoryIdAndStatus(
                        category.getId(), Product.ProductStatus.ACTIVE, PageRequest.of(0, Math.min(productLimit, 3)));
                products = fallbackProductsPage.getContent();
            }

            List<ProductResponse> productResponses = products.stream()
                    .map(productMapper::toResponse)
                    .collect(Collectors.toList());

            return new CategoryWithTopProductsResponse(categoryMapper.toResponse(category), productResponses);
        });
        
        return new RestPageResponse<>(page);
    }

    @Cacheable(value = "categories", key = "#categoryId")
    public CategoryResponse getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProductServiceException("Category not found with ID: " + categoryId));
        CategoryResponse response = categoryMapper.toResponse(category);
        populateProductCounts(response);
        return response;
    }

    public Optional<CategoryResponse> getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .map(category -> {
                    CategoryResponse response = categoryMapper.toResponse(category);
                    populateProductCounts(response);
                    return response;
                });
    }

    @Caching(evict = {
            @CacheEvict(value = "categories", key = "#categoryId"),
            @CacheEvict(value = "categories", allEntries = true),
            @CacheEvict(value = "active-categories", allEntries = true)
    })
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
        CategoryResponse response = categoryMapper.toResponse(updatedCategory);
        populateProductCounts(response);
        return response;
    }

    @Caching(evict = {
            @CacheEvict(value = "categories", key = "#categoryId"),
            @CacheEvict(value = "categories", allEntries = true),
            @CacheEvict(value = "active-categories", allEntries = true)
    })
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
        Page<CategoryResponse> responses = categoryRepository.searchByName(searchTerm, pageable)
                .map(categoryMapper::toResponse);
        responses.getContent().forEach(this::populateProductCounts);
        return responses;
    }

    @Caching(evict = {
            @CacheEvict(value = "categories", key = "#categoryId"),
            @CacheEvict(value = "active-categories", allEntries = true)
    })
    public CategoryResponse toggleCategoryStatus(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProductServiceException("Category not found with ID: " + categoryId));

        boolean currentStatus = category.getActive() != null ? category.getActive() : true;
        category.setActive(!currentStatus);
        Category updatedCategory = categoryRepository.save(category);
        syncToElasticsearch(updatedCategory);

        CategoryResponse response = categoryMapper.toResponse(updatedCategory);
        populateProductCounts(response);
        return response;
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

    public com.blubugtech.bakery_product_service.dto.CategoryStatisticsResponse getCategoryStatistics() {
        long activeCategoriesCount = categoryRepository.countByActiveTrue();

        return com.blubugtech.bakery_product_service.dto.CategoryStatisticsResponse.builder()
                .totalCategories(categoryRepository.count())
                .activeCategories(activeCategoriesCount)
                .inactiveCategories(categoryRepository.count() - activeCategoriesCount)
                .categoryStats(java.util.List.of())
        .build();
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
            log.error("Failed to sync category {} to Elasticsearch", category.getId(), e);
        }
    }

    private void deleteFromElasticsearch(String categoryId) {
        try {
            categorySearchService.deleteCategoryFromIndex(categoryId);
        } catch (Exception e) {
            log.error("Failed to delete category {} from Elasticsearch", categoryId, e);
        }
    }

    private void populateProductCounts(CategoryResponse response) {
        response.setProductCount((int) productRepository.countByCategoryId(response.getId()));
        response.setActiveProductCount((int) productRepository.countByCategoryIdAndStatus(response.getId(), Product.ProductStatus.ACTIVE));
    }
}
