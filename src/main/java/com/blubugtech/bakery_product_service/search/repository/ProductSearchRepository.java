package com.blubugtech.bakery_product_service.search.repository;

import com.blubugtech.bakery_product_service.search.document.ProductDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String> {

    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"name^3\", \"description\"], \"type\": \"phrase_prefix\"}}")
    Page<ProductDocument> searchAutocomplete(String query, Pageable pageable);
    
    Page<ProductDocument> findByCategoryName(String categoryName, Pageable pageable);
    
    Page<ProductDocument> findByStatus(String status, Pageable pageable);
}
