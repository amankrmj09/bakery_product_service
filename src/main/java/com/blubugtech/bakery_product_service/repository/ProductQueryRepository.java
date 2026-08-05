package com.blubugtech.bakery_product_service.repository;

import com.blubugtech.bakery_product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface ProductQueryRepository extends Repository<Product, String> {
    Optional<Product> findById(String id);
    boolean existsById(String id);
    Page<Product> findByIdIn(List<String> ids, Pageable pageable);
    List<Product> findAllById(Iterable<String> ids);
    Page<Product> findAll(Pageable pageable);
    Optional<Product> findBySku(String sku);
    boolean existsBySku(String sku);
    Page<Product> findByCategoryIdAndStatus(String categoryId, Product.ProductStatus status, Pageable pageable);
    Page<Product> findByCategoryIdAndStatusOrderByAverageRatingDesc(String categoryId, Product.ProductStatus status, Pageable pageable);
    Page<Product> findByCategoryIdAndStatusAndAverageRatingGreaterThanOrderByAverageRatingDesc(String categoryId, Product.ProductStatus status, Double minRating, Pageable pageable);
    boolean existsByCategoryId(String categoryId);
    Page<Product> findByIsFeaturedTrueAndStatus(Product.ProductStatus status, Pageable pageable);
    Page<Product> findByStatus(Product.ProductStatus status, Pageable pageable);
    long countByStatus(Product.ProductStatus status);
    long count();
    @Query("{ 'status': 'ACTIVE', $expr: { $gt: [ { $subtract: ['$inventory.current_stock', '$inventory.reserved_stock'] }, 0 ] } }")
    Page<Product> findAvailableProducts(Pageable pageable);
    @Query("{ $and: [ " +
           "{ $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'sku': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } }, { 'short_description': { $regex: ?0, $options: 'i' } } ] }, " +
           "{ 'status': ?1 } " +
           "] }")
    Page<Product> searchProducts(String searchTerm, Product.ProductStatus status, Pageable pageable);
    @Query("{ $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'sku': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } }, { 'short_description': { $regex: ?0, $options: 'i' } } ] }")
    Page<Product> searchAdminProducts(String searchTerm, Pageable pageable);
    @Query("{ 'price': { $gte: ?0, $lte: ?1 }, 'status': ?2 }")
    Page<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Product.ProductStatus status, Pageable pageable);
    @Query("{ 'isOnSale': true, 'status': ?0 }")
    Page<Product> findProductsOnSale(Product.ProductStatus status, Pageable pageable);
    @Query("{ 'categoryId': ?0, 'status': ?1, 'price': { $gte: ?2, $lte: ?3 } }")
    Page<Product> findProductsWithFilters(String categoryId, Product.ProductStatus status,
                                          BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock, Pageable pageable);
    @Query("{ 'category.id': { $in: ?0 }, 'status': ?1 }")
    Page<Product> findByCategoryIdsAndStatus(List<String> categoryIds, Product.ProductStatus status, Pageable pageable);
    @Query("{ 'tags': ?0, 'status': ?1 }")
    Page<Product> findByTag(String tag, Product.ProductStatus status, Pageable pageable);
    @Query("{ 'allergens': { $ne: ?0 }, 'status': ?1 }")
    Page<Product> findProductsWithoutAllergen(String allergen, Product.ProductStatus status, Pageable pageable);
    @Query("{ $expr: { $lte: ['$inventory.current_stock', '$inventory.minimum_stock'] } }")
    Page<Product> findLowStockProducts(Pageable pageable);
    @Query("{ $expr: { $lte: [ { $subtract: ['$inventory.current_stock', '$inventory.reserved_stock'] }, 0 ] } }")
    Page<Product> findOutOfStockProducts(Pageable pageable);
    @Query("{ 'inventory.reorder_level': { $gt: 0 }, $expr: { $lte: ['$inventory.current_stock', '$inventory.reorder_level'] } }")
    Page<Product> findProductsNeedingReorder(Pageable pageable);
    Page<Product> findByStatusAndCreatedAtAfter(Product.ProductStatus status, LocalDateTime since, Pageable pageable);
    Page<Product> findByStatusAndPreparationTimeMinutesBetween(
            Product.ProductStatus status, Integer minTime, Integer maxTime, Pageable pageable);
    @Query("{ 'inventory.track_expiry': true, 'inventory.expiry_date': { $gte: ?0, $lte: ?1 } }")
    Page<Product> findProductsExpiringSoon(LocalDateTime now, LocalDateTime cutoffTime, Pageable pageable);
}
