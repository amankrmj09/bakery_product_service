package com.blubugtech.bakery_product_service.search.service.impl;

import com.blubugtech.bakery_product_service.entity.Product;
import com.blubugtech.bakery_product_service.search.document.ProductDocument;
import com.blubugtech.bakery_product_service.search.repository.ProductSearchRepository;
import com.blubugtech.bakery_product_service.search.service.ProductSearchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    private final ProductSearchRepository searchRepository;

    public ProductSearchServiceImpl(ProductSearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @Override
    public void indexProduct(Product product) {
        ProductDocument doc = new ProductDocument();
        doc.setId(product.getId().toString());
        doc.setName(product.getName());
        doc.setDescription(product.getDescription());
        if (product.getCategory() != null) {
            
            doc.setCategoryName(product.getCategory().getName());
        }
        doc.setPrice(product.getPrice());
        doc.setStatus(product.getStatus().name());
        searchRepository.save(doc);
    }

    @Override
    public void deleteProductFromIndex(String productId) {
        searchRepository.deleteById(productId);
    }

    @Override
    public List<ProductDocument> searchProducts(String query) {
        return searchRepository.findByNameOrDescriptionOrTags(query, query, query, org.springframework.data.domain.Pageable.unpaged()).getContent();
    }
}
