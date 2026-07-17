package com.blubugtech.bakery_product_service.mapper;

import com.blubugtech.bakery_product_service.dto.inventory.InventoryResponse;
import com.blubugtech.bakery_product_service.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InventoryMapper {
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "id", target = "productId")
    @Mapping(source = "name", target = "productName")
    @Mapping(source = "sku", target = "productSku")
    @Mapping(source = "inventory.currentStock", target = "currentStock")
    @Mapping(source = "inventory.reservedStock", target = "reservedStock")
    @Mapping(source = "inventory.availableStock", target = "availableStock")
    @Mapping(source = "inventory.minimumStock", target = "minimumStock")
    @Mapping(source = "inventory.maximumStock", target = "maximumStock")
    @Mapping(source = "inventory.reorderLevel", target = "reorderLevel")
    @Mapping(source = "inventory.reorderQuantity", target = "reorderQuantity")
    @Mapping(source = "inventory.status", target = "status")
    @Mapping(source = "inventory.isLowStock", target = "isLowStock")
    @Mapping(source = "inventory.isOutOfStock", target = "isOutOfStock")
    @Mapping(source = "inventory.needsReorder", target = "needsReorder")
    @Mapping(source = "inventory.lastRestockedAt", target = "lastRestockedAt")
    @Mapping(source = "inventory.lastRestockedQuantity", target = "lastRestockedQuantity")
    @Mapping(source = "inventory.autoReorderEnabled", target = "autoReorderEnabled")
    @Mapping(source = "inventory.trackExpiry", target = "trackExpiry")
    @Mapping(source = "inventory.expiryDate", target = "expiryDate")
    @Mapping(source = "inventory.supplierInfo", target = "supplierInfo")
    @Mapping(source = "inventory.storageLocation", target = "storageLocation")
    @Mapping(source = "inventory.notes", target = "notes")
    InventoryResponse toResponse(Product product);
}
