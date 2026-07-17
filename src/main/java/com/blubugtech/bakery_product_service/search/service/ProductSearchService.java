package com.blubugtech.bakery_product_service.search.service;

import com.blubugtech.bakery_product_service.entity.Product;
import com.blubugtech.bakery_product_service.search.document.ProductDocument;

import java.util.List;

public interface ProductSearchService {
    void indexProduct(Product product);
    void deleteProductFromIndex(String productId);
    List<ProductDocument> searchProducts(String query);
}
