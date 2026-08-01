package com.blubugtech.bakery_product_service.service.impl;

import lombok.extern.slf4j.Slf4j;
import com.blubugtech.bakery_product_service.service.ProductValidationService;
import com.blubugtech.bakery_product_service.dto.product.ProductResponse;
import com.blubugtech.bakery_product_service.entity.Product;
import com.blubugtech.bakery_product_service.mapper.ProductMapper;
import com.blubugtech.bakery_product_service.exception.ProductServiceException;
import com.blubugtech.bakery_product_service.repository.ProductQueryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.blubakery.common.core.exception.common.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class ProductValidationServiceImpl implements ProductValidationService {

    private final ProductQueryRepository productRepository;
    private final ProductMapper productMapper;

    public ProductValidationServiceImpl(ProductQueryRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public boolean isProductAvailable(String productId) {
        return productRepository.findById(productId)
                .map(Product::isAvailable)
                .orElse(false);
    }

    @Override
    public List<ProductResponse> validateProducts(List<String> productIds) {
        log.debug("Validating products for IDs: {}", productIds);
        List<Product> products = productRepository.findAllById(productIds);
        
        if (products.size() != productIds.size()) {
            List<String> foundIds = products.stream().map(Product::getId).collect(Collectors.toList());
            List<String> missingIds = productIds.stream().filter(id -> !foundIds.contains(id)).collect(Collectors.toList());
            throw new ResourceNotFoundException("Products", "ids", missingIds.toString());
        }
        
        return products.stream().map(productMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Product getProductEntity(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product not found with ID: " + productId));
    }
}
