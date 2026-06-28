package com.shah_s.bakery_product_service.repository;

import com.shah_s.bakery_product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @EntityGraph(attributePaths = {"category", "inventory"})
    List<Product> findAll();

    @EntityGraph(attributePaths = {"category", "inventory"})
    Page<Product> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"category", "inventory"})
    Optional<Product> findById(UUID id);

    // Find product by SKU
    @EntityGraph(attributePaths = {"category", "inventory"})
    Optional<Product> findBySku(String sku);

    // Check if SKU exists
    boolean existsBySku(String sku);

    // Find products by category
    @EntityGraph(attributePaths = {"category", "inventory"})
    List<Product> findByCategoryIdAndStatusOrderByNameAsc(UUID categoryId, Product.ProductStatus status);

    // Find active products by category
    @EntityGraph(attributePaths = {"category", "inventory"})
    List<Product> findByCategoryIdAndStatus(UUID categoryId, Product.ProductStatus status);

    // Find featured products
    @EntityGraph(attributePaths = {"category", "inventory"})
    List<Product> findByIsFeaturedTrueAndStatusOrderByCreatedAtDesc(Product.ProductStatus status);

    // Find products by status
    @EntityGraph(attributePaths = {"category", "inventory"})
    List<Product> findByStatusOrderByNameAsc(Product.ProductStatus status);

    // Find available products (active with stock)
    @EntityGraph(attributePaths = {"category", "inventory"})
    @Query("SELECT p FROM Product p " +
           "JOIN p.inventory i " +
           "WHERE p.status = 'ACTIVE' " +
           "AND (i.currentStock - i.reservedStock) > 0 " +
           "ORDER BY p.name ASC")
    List<Product> findAvailableProducts();

    // Search products by name or description
    @EntityGraph(attributePaths = {"category", "inventory"})
    @Query("SELECT p FROM Product p " +
           "WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND p.status = :status " +
           "ORDER BY p.name ASC")
    List<Product> searchProducts(@Param("searchTerm") String searchTerm,
                                @Param("status") Product.ProductStatus status);

    // Search products with pagination
    @EntityGraph(attributePaths = {"category", "inventory"})
    @Query("SELECT p FROM Product p " +
           "WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND p.status = :status")
    Page<Product> searchProductsWithPagination(@Param("searchTerm") String searchTerm,
                                              @Param("status") Product.ProductStatus status,
                                              Pageable pageable);

    // Find products by price range
    @EntityGraph(attributePaths = {"category", "inventory"})
    @Query("SELECT p FROM Product p " +
           "WHERE p.status = :status " +
           "AND ((p.discountPrice IS NOT NULL AND p.discountPrice BETWEEN :minPrice AND :maxPrice) " +
           "OR (p.discountPrice IS NULL AND p.price BETWEEN :minPrice AND :maxPrice)) " +
           "ORDER BY p.price ASC")
    List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                                   @Param("maxPrice") BigDecimal maxPrice,
                                   @Param("status") Product.ProductStatus status);

    // Find products on sale
    @EntityGraph(attributePaths = {"category", "inventory"})
    @Query("SELECT p FROM Product p " +
           "WHERE p.status = :status " +
           "AND p.discountPrice IS NOT NULL " +
           "AND p.discountPrice < p.price " +
           "ORDER BY ((p.price - p.discountPrice) / p.price) DESC")
    List<Product> findProductsOnSale(@Param("status") Product.ProductStatus status);

    // Find products by category with pagination
    @EntityGraph(attributePaths = {"category", "inventory"})
    Page<Product> findByCategoryIdAndStatus(UUID categoryId, Product.ProductStatus status, Pageable pageable);

    // Find products by multiple categories
    @EntityGraph(attributePaths = {"category", "inventory"})
    @Query("SELECT p FROM Product p " +
           "WHERE p.category.id IN :categoryIds " +
           "AND p.status = :status " +
           "ORDER BY p.name ASC")
    List<Product> findByCategoryIdsAndStatus(@Param("categoryIds") List<UUID> categoryIds,
                                            @Param("status") Product.ProductStatus status);

    // Find products by tags
    @EntityGraph(attributePaths = {"category", "inventory"})
    @Query("SELECT DISTINCT p FROM Product p " +
           "WHERE :tag MEMBER OF p.tags " +
           "AND p.status = :status " +
           "ORDER BY p.name ASC")
    List<Product> findByTag(@Param("tag") String tag, @Param("status") Product.ProductStatus status);

    // Find products by allergen
    @EntityGraph(attributePaths = {"category", "inventory"})
    @Query("SELECT DISTINCT p FROM Product p " +
           "WHERE :allergen NOT MEMBER OF p.allergens " +
           "AND p.status = :status " +
           "ORDER BY p.name ASC")
    List<Product> findProductsWithoutAllergen(@Param("allergen") String allergen,
                                             @Param("status") Product.ProductStatus status);

    // Find low stock products
    @EntityGraph(attributePaths = {"category", "inventory"})
    @Query("SELECT p FROM Product p " +
           "JOIN p.inventory i " +
           "WHERE i.currentStock <= i.minimumStock " +
           "ORDER BY i.currentStock ASC")
    List<Product> findLowStockProducts();

    // Find out of stock products
    @EntityGraph(attributePaths = {"category", "inventory"})
    @Query("SELECT p FROM Product p " +
           "JOIN p.inventory i " +
           "WHERE (i.currentStock - i.reservedStock) <= 0 " +
           "ORDER BY p.name ASC")
    List<Product> findOutOfStockProducts();

    // Find products needing reorder
    @EntityGraph(attributePaths = {"category", "inventory"})
    @Query("SELECT p FROM Product p " +
           "JOIN p.inventory i " +
           "WHERE i.reorderLevel > 0 AND i.currentStock <= i.reorderLevel " +
           "ORDER BY i.currentStock ASC")
    List<Product> findProductsNeedingReorder();

    // Find recently added products
    @EntityGraph(attributePaths = {"category", "inventory"})
    List<Product> findByStatusAndCreatedAtAfterOrderByCreatedAtDesc(Product.ProductStatus status,
                                                                   LocalDateTime since);

    // Find products by preparation time range
    @EntityGraph(attributePaths = {"category", "inventory"})
    List<Product> findByStatusAndPreparationTimeMinutesBetweenOrderByPreparationTimeMinutesAsc(
            Product.ProductStatus status, Integer minTime, Integer maxTime);

    // Find products expiring soon
    @EntityGraph(attributePaths = {"category", "inventory"})
    @Query("SELECT p FROM Product p " +
           "JOIN p.inventory i " +
           "WHERE i.trackExpiry = true " +
           "AND i.expiryDate BETWEEN :now AND :cutoffTime " +
           "ORDER BY i.expiryDate ASC")
    List<Product> findProductsExpiringSoon(@Param("now") LocalDateTime now,
                                          @Param("cutoffTime") LocalDateTime cutoffTime);

    // Get product statistics
    @Query("SELECT " +
           "COUNT(p) as totalProducts, " +
           "COUNT(CASE WHEN p.status = 'ACTIVE' THEN 1 END) as activeProducts, " +
           "COUNT(CASE WHEN p.status = 'INACTIVE' THEN 1 END) as inactiveProducts, " +
           "COUNT(CASE WHEN p.status = 'DISCONTINUED' THEN 1 END) as discontinuedProducts, " +
           "COUNT(CASE WHEN p.isFeatured = true THEN 1 END) as featuredProducts, " +
           "AVG(p.price) as averagePrice " +
           "FROM Product p")
    Object[] getProductStatistics();

    // Count products by category
    @Query("SELECT c.name, COUNT(p) " +
           "FROM Product p " +
           "JOIN p.category c " +
           "WHERE p.status = :status " +
           "GROUP BY c.id, c.name " +
           "ORDER BY COUNT(p) DESC")
    List<Object[]> countProductsByCategory(@Param("status") Product.ProductStatus status);

    // Find top selling products (this would require order integration)
    @EntityGraph(attributePaths = {"category", "inventory"})
    @Query("SELECT p FROM Product p " +
           "WHERE p.status = :status " +
           "ORDER BY p.createdAt DESC")
    List<Product> findTopProducts(@Param("status") Product.ProductStatus status, Pageable pageable);

    // Advanced search with multiple filters
    @EntityGraph(attributePaths = {"category", "inventory"})
    @Query("SELECT p FROM Product p " +
           "JOIN p.category c " +
           "LEFT JOIN p.inventory i " +
           "WHERE (:categoryId IS NULL OR c.id = :categoryId) " +
           "AND (:status IS NULL OR p.status = :status) " +
           "AND (:minPrice IS NULL OR " +
           "    (p.discountPrice IS NOT NULL AND p.discountPrice >= :minPrice) OR " +
           "    (p.discountPrice IS NULL AND p.price >= :minPrice)) " +
           "AND (:maxPrice IS NULL OR " +
           "    (p.discountPrice IS NOT NULL AND p.discountPrice <= :maxPrice) OR " +
           "    (p.discountPrice IS NULL AND p.price <= :maxPrice)) " +
           "AND (:inStock IS NULL OR " +
           "    (:inStock = true AND (i.currentStock - i.reservedStock) > 0) OR " +
           "    (:inStock = false)) " +
           "ORDER BY p.name ASC")
    List<Product> findProductsWithFilters(@Param("categoryId") UUID categoryId,
                                         @Param("status") Product.ProductStatus status,
                                         @Param("minPrice") BigDecimal minPrice,
                                         @Param("maxPrice") BigDecimal maxPrice,
                                         @Param("inStock") Boolean inStock);
}
