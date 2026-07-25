package com.blubugtech.bakery_product_service.service;

import com.blubugtech.bakery_product_service.service.CategoryService;
import com.blubugtech.bakery_product_service.dto.category.CategoryRequest;
import com.blubugtech.bakery_product_service.dto.category.CategoryResponse;
import com.blubugtech.bakery_product_service.entity.Category;
import com.blubugtech.bakery_product_service.exception.ProductServiceException;
import com.blubugtech.bakery_product_service.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.blubugtech.common.exception.common.DuplicateResourceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    Page<CategoryResponse> getAllCategories(Pageable pageable);
    Page<CategoryResponse> getActiveCategories(Pageable pageable);
    List<com.blubugtech.bakery_product_service.dto.category.CategoryWithTopProductsResponse> getTopCategoriesWithTopProducts(int productLimit);
    Page<CategoryResponse> getCategoriesWithProducts(Pageable pageable);
    Page<CategoryResponse> getCategoriesWithActiveProducts(Pageable pageable);
    CategoryResponse getCategoryById(String categoryId);
    Optional<CategoryResponse> getCategoryByName(String name);
    CategoryResponse updateCategory(String categoryId, CategoryRequest request);
    void deleteCategory(String categoryId);
    Page<CategoryResponse> searchCategories(String searchTerm, Pageable pageable);
    CategoryResponse toggleCategoryStatus(String categoryId);
    void reorderCategories(Map<String, Integer> categoryOrders);
    Map<String, Object> getCategoryStatistics();
    boolean categoryExists(String categoryId);
    Category getCategoryEntity(String categoryId);
}
