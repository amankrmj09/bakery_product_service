package com.blubugtech.bakery_product_service.search.repository;

import com.blubugtech.bakery_product_service.search.document.CategoryDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorySearchRepository extends ElasticsearchRepository<CategoryDocument, String> {

    Page<CategoryDocument> findByNameOrDescription(String name, String description, Pageable pageable);
}
