package com.shah_s.bakery_product_service.repository;

import com.shah_s.bakery_product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    // Find product by SKU
    Optional<Product> findBySku(String sku);

    // Check if SKU exists
    boolean existsBySku(String sku);

    // Find products by category
    List<Product> findByCategoryIdAndStatusOrderByNameAsc(String categoryId, Product.ProductStatus status);

    // Check if category has products
    boolean existsByCategoryId(String categoryId);

    // Find active products by category with pagination
    Page<Product> findByCategoryIdAndStatus(String categoryId, Product.ProductStatus status, Pageable pageable);

    // Find featured products
    List<Product> findByIsFeaturedTrueAndStatusOrderByCreatedAtDesc(Product.ProductStatus status);

    // Find products by status
    List<Product> findByStatusOrderByNameAsc(Product.ProductStatus status);

    // Find available products (active with stock)
    @Query("{ 'status': 'ACTIVE', $expr: { $gt: [ { $subtract: ['$inventory.current_stock', '$inventory.reserved_stock'] }, 0 ] } }")
    List<Product> findAvailableProducts();

    // Search products by name or description
    @Query("{ $and: [ " +
           "{ $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } }, { 'short_description': { $regex: ?0, $options: 'i' } } ] }, " +
           "{ 'status': ?1 } " +
           "] }")
    List<Product> searchProducts(String searchTerm, Product.ProductStatus status);

    // Find products by price range
    @Query("{ 'price': { $gte: ?0, $lte: ?1 }, 'status': ?2 }")
    List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Product.ProductStatus status);

    // Find products on sale
    @Query("{ 'isOnSale': true, 'status': ?0 }")
    List<Product> findProductsOnSale(Product.ProductStatus status);

    // Advanced search - approximate since dynamic queries need MongoTemplate
    @Query("{ 'categoryId': ?0, 'status': ?1, 'price': { $gte: ?2, $lte: ?3 } }")
    List<Product> findProductsWithFilters(String categoryId, Product.ProductStatus status,
                                          BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock);

    @Query("{ $and: [ " +
           "{ $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } }, { 'short_description': { $regex: ?0, $options: 'i' } } ] }, " +
           "{ 'status': ?1 } " +
           "] }")
    Page<Product> searchProductsWithPagination(String searchTerm, Product.ProductStatus status, Pageable pageable);

    // Find products by multiple categories
    @Query("{ 'category.id': { $in: ?0 }, 'status': ?1 }")
    List<Product> findByCategoryIdsAndStatus(List<String> categoryIds, Product.ProductStatus status);

    // Find products by tags
    @Query("{ 'tags': ?0, 'status': ?1 }")
    List<Product> findByTag(String tag, Product.ProductStatus status);

    // Find products by allergen (without)
    @Query("{ 'allergens': { $ne: ?0 }, 'status': ?1 }")
    List<Product> findProductsWithoutAllergen(String allergen, Product.ProductStatus status);

    // Find low stock products
    @Query("{ $expr: { $lte: ['$inventory.current_stock', '$inventory.minimum_stock'] } }")
    List<Product> findLowStockProducts();

    // Find out of stock products
    @Query("{ $expr: { $lte: [ { $subtract: ['$inventory.current_stock', '$inventory.reserved_stock'] }, 0 ] } }")
    List<Product> findOutOfStockProducts();

    // Find products needing reorder
    @Query("{ 'inventory.reorder_level': { $gt: 0 }, $expr: { $lte: ['$inventory.current_stock', '$inventory.reorder_level'] } }")
    List<Product> findProductsNeedingReorder();

    // Find recently added products
    List<Product> findByStatusAndCreatedAtAfterOrderByCreatedAtDesc(Product.ProductStatus status, LocalDateTime since);

    // Find products by preparation time range
    List<Product> findByStatusAndPreparationTimeMinutesBetweenOrderByPreparationTimeMinutesAsc(
            Product.ProductStatus status, Integer minTime, Integer maxTime);

    // Find products expiring soon
    @Query("{ 'inventory.track_expiry': true, 'inventory.expiry_date': { $gte: ?0, $lte: ?1 } }")
    List<Product> findProductsExpiringSoon(LocalDateTime now, LocalDateTime cutoffTime);

}
