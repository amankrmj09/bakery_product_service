package com.shah_s.bakery_product_service.repository;

import com.shah_s.bakery_product_service.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    // Find inventory by product ID
    Optional<Inventory> findByProductId(UUID productId);

    // Find inventory by product SKU
    @Query("SELECT i FROM Inventory i WHERE i.product.sku = :sku")
    Optional<Inventory> findByProductSku(@Param("sku") String sku);

    // Find low stock items
    @Query("SELECT i FROM Inventory i WHERE i.currentStock <= i.minimumStock ORDER BY i.currentStock ASC")
    List<Inventory> findLowStockItems();

    // Find out of stock items
    @Query("SELECT i FROM Inventory i WHERE i.currentStock <= 0 ORDER BY i.product.name ASC")
    List<Inventory> findOutOfStockItems();

    // Find items needing reorder
    @Query("SELECT i FROM Inventory i " +
           "WHERE i.reorderLevel > 0 " +
           "AND i.currentStock <= i.reorderLevel " +
           "ORDER BY i.currentStock ASC")
    List<Inventory> findItemsNeedingReorder();

    // Find items with auto reorder enabled
    List<Inventory> findByAutoReorderEnabledTrue();

    // Find expired items
    @Query("SELECT i FROM Inventory i " +
           "WHERE i.trackExpiry = true " +
           "AND i.expiryDate <= CURRENT_TIMESTAMP " +
           "ORDER BY i.expiryDate ASC")
    List<Inventory> findExpiredItems();

    // Find items expiring within specified hours
    @Query("SELECT i FROM Inventory i " +
           "WHERE i.trackExpiry = true " +
           "AND i.expiryDate BETWEEN CURRENT_TIMESTAMP AND :cutoffTime " +
           "ORDER BY i.expiryDate ASC")
    List<Inventory> findItemsExpiringSoon(@Param("cutoffTime") LocalDateTime cutoffTime);

    // Find items by status
    List<Inventory> findByStatusOrderByProductNameAsc(Inventory.InventoryStatus status);

    // Find items by storage location
    List<Inventory> findByStorageLocationContainingIgnoreCaseOrderByProductNameAsc(String location);

    // Find items by supplier
    List<Inventory> findBySupplierInfoContainingIgnoreCaseOrderByProductNameAsc(String supplier);

    // Update stock for a product
    @Modifying
    @Query("UPDATE Inventory i SET i.currentStock = :newStock, i.updatedAt = CURRENT_TIMESTAMP " +
           "WHERE i.product.id = :productId")
    int updateStockByProductId(@Param("productId") UUID productId, @Param("newStock") Integer newStock);

    // Reserve stock for an order
    @Modifying
    @Query("UPDATE Inventory i SET i.reservedStock = i.reservedStock + :quantity " +
           "WHERE i.product.id = :productId " +
           "AND (i.currentStock - i.reservedStock) >= :quantity")
    int reserveStock(@Param("productId") UUID productId, @Param("quantity") Integer quantity);

    // Release reserved stock
    @Modifying
    @Query("UPDATE Inventory i SET i.reservedStock = i.reservedStock - :quantity " +
           "WHERE i.product.id = :productId " +
           "AND i.reservedStock >= :quantity")
    int releaseReservedStock(@Param("productId") UUID productId, @Param("quantity") Integer quantity);

    // Consume stock (reduce both current and reserved)
    @Modifying
    @Query("UPDATE Inventory i SET " +
           "i.currentStock = i.currentStock - :quantity, " +
           "i.reservedStock = i.reservedStock - :quantity " +
           "WHERE i.product.id = :productId " +
           "AND i.currentStock >= :quantity " +
           "AND i.reservedStock >= :quantity")
    int consumeStock(@Param("productId") UUID productId, @Param("quantity") Integer quantity);

    // Add stock (restocking)
    @Modifying
    @Query("UPDATE Inventory i SET " +
           "i.currentStock = i.currentStock + :quantity, " +
           "i.lastRestockedAt = :restockTime, " +
           "i.lastRestockedQuantity = :quantity " +
           "WHERE i.product.id = :productId")
    int addStock(@Param("productId") UUID productId,
                 @Param("quantity") Integer quantity,
                 @Param("restockTime") LocalDateTime restockTime);

    // Update minimum stock level
    @Modifying
    @Query("UPDATE Inventory i SET i.minimumStock = :minimumStock " +
           "WHERE i.product.id = :productId")
    int updateMinimumStock(@Param("productId") UUID productId, @Param("minimumStock") Integer minimumStock);

    // Get total inventory value
    @Query("SELECT SUM(i.currentStock * p.price) " +
           "FROM Inventory i " +
           "JOIN i.product p " +
           "WHERE p.status = 'ACTIVE'")
    Double getTotalInventoryValue();

    // Get inventory statistics
    @Query("SELECT " +
           "COUNT(i) as totalItems, " +
           "SUM(i.currentStock) as totalStock, " +
           "SUM(i.reservedStock) as totalReservedStock, " +
           "COUNT(CASE WHEN i.currentStock <= i.minimumStock THEN 1 END) as lowStockItems, " +
           "COUNT(CASE WHEN i.currentStock <= 0 THEN 1 END) as outOfStockItems " +
           "FROM Inventory i")
    Object[] getInventoryStatistics();

    // Find products with stock between range
    @Query("SELECT i FROM Inventory i " +
           "WHERE i.currentStock BETWEEN :minStock AND :maxStock " +
           "ORDER BY i.currentStock ASC")
    List<Inventory> findByStockRange(@Param("minStock") Integer minStock,
                                    @Param("maxStock") Integer maxStock);

    // Get stock history (would need a separate StockMovement entity for full implementation)
    @Query("SELECT i FROM Inventory i " +
           "WHERE i.lastRestockedAt BETWEEN :startDate AND :endDate " +
           "ORDER BY i.lastRestockedAt DESC")
    List<Inventory> findRecentlyRestocked(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    // Check if sufficient stock is available
    @Query("SELECT CASE WHEN (i.currentStock - i.reservedStock) >= :requiredQuantity " +
           "THEN true ELSE false END " +
           "FROM Inventory i " +
           "WHERE i.product.id = :productId")
    Boolean checkStockAvailability(@Param("productId") UUID productId,
                                  @Param("requiredQuantity") Integer requiredQuantity);

    // Get available stock for a product
    @Query("SELECT (i.currentStock - i.reservedStock) " +
           "FROM Inventory i " +
           "WHERE i.product.id = :productId")
    Integer getAvailableStock(@Param("productId") UUID productId);
}
