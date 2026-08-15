package com.blubugtech.bakery_product_service.mapper;

import com.blubugtech.bakery_product_service.dto.product.ProductRequest;
import com.blubugtech.bakery_product_service.dto.product.ProductResponse;
import com.blubugtech.bakery_product_service.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    Product toEntity(ProductRequest request);

    @Mapping(target = "isAvailable", expression = "java(product.isAvailable())")
    @Mapping(target = "isOnSale", expression = "java(product.isOnSale())")
    ProductResponse toResponse(Product product);
}
