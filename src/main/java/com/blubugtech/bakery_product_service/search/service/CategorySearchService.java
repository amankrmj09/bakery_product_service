package com.blubugtech.bakery_product_service.search.service;

import com.blubugtech.bakery_product_service.entity.Category;
import com.blubugtech.bakery_product_service.search.document.CategoryDocument;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategorySearchService {
    void indexCategory(Category category);
    void deleteCategoryFromIndex(String categoryId);
    Page<CategoryDocument> searchCategories(String query, Pageable pageable);
}
