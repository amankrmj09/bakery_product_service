package com.blubugtech.bakery_product_service.service;

import com.blubugtech.bakery_product_service.service.InventoryService;
import com.blubugtech.bakery_product_service.dto.inventory.InventoryResponse;
import com.blubugtech.bakery_product_service.dto.inventory.InventoryUpdateRequest;
import com.blubugtech.bakery_product_service.entity.Inventory;
import com.blubugtech.bakery_product_service.entity.Product;
import com.blubugtech.bakery_product_service.exception.ProductServiceException;
import com.blubugtech.bakery_product_service.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import com.blubugtech.bakery_product_service.exception.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryService {
    Inventory createInventoryForProduct(Product product, Integer initialStock,
                                             Integer minimumStock, Integer reorderLevel);
    InventoryResponse getInventoryByProductId(String productId);
    Optional<InventoryResponse> getInventoryByProductSku(String sku);
    Page<InventoryResponse> searchInventory(String searchTerm, Pageable pageable);
    Page<InventoryResponse> getAllInventory(Pageable pageable);
    Page<InventoryResponse> getLowStockItems(Pageable pageable);
    Page<InventoryResponse> getOutOfStockItems(Pageable pageable);
    Page<InventoryResponse> getItemsNeedingReorder(Pageable pageable);
    Page<InventoryResponse> getExpiredItems(Pageable pageable);
    Page<InventoryResponse> getItemsExpiringSoon(int hours, Pageable pageable);
    InventoryResponse updateInventory(String productId, InventoryUpdateRequest request);
    InventoryResponse addStock(String productId, Integer quantity, String notes);
    boolean reserveStock(String productId, Integer quantity);
    void releaseReservedStock(String productId, Integer quantity);
    void consumeStock(String productId, Integer quantity);
    boolean checkStockAvailability(String productId, Integer requiredQuantity);
    Integer getAvailableStock(String productId);
    Map<String, Object> getInventoryStatistics();
    void bulkUpdateMinimumStock(Map<String, Integer> productMinimumStocks);
}
