package com.blubugtech.bakery_product_service.search.service.impl;

import com.blubugtech.bakery_product_service.entity.Category;
import com.blubugtech.bakery_product_service.search.document.CategoryDocument;
import com.blubugtech.bakery_product_service.search.repository.CategorySearchRepository;
import com.blubugtech.bakery_product_service.search.service.CategorySearchService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class CategorySearchServiceImpl implements CategorySearchService {

    private final CategorySearchRepository searchRepository;

    public CategorySearchServiceImpl(CategorySearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @Override
    public void indexCategory(Category category) {
        CategoryDocument doc = new CategoryDocument();
        doc.setId(category.getId());
        doc.setName(category.getName());
        doc.setDescription(category.getDescription());
        doc.setDisplayOrder(category.getDisplayOrder());
        doc.setActive(category.getActive());
        doc.setIconClass(category.getIconClass());
        searchRepository.save(doc);
    }

    @Override
    public void deleteCategoryFromIndex(String categoryId) {
        searchRepository.deleteById(categoryId);
    }

    @Override
    public Page<CategoryDocument> searchCategories(String query, Pageable pageable) {
        return searchRepository.findByNameOrDescription(query, query, pageable);
    }
}
