package com.shah_s.bakery_product_service.repository;

import com.shah_s.bakery_product_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    // Find category by name
    Optional<Category> findByName(String name);

    // Check if category name exists
    boolean existsByName(String name);

    // Find active categories ordered by display order
    List<Category> findByActiveTrueOrderByDisplayOrderAsc();

    // Find categories by active status
    List<Category> findByActiveOrderByDisplayOrderAsc(Boolean active);

    // Find categories with products
    @Query("SELECT c FROM Category c WHERE SIZE(c.products) > 0 ORDER BY c.displayOrder ASC")
    List<Category> findCategoriesWithProducts();

    // Find categories with active products
    @Query("SELECT DISTINCT c FROM Category c " +
           "JOIN c.products p " +
           "WHERE p.status = 'ACTIVE' AND c.active = true " +
           "ORDER BY c.displayOrder ASC")
    List<Category> findCategoriesWithActiveProducts();

    // Search categories by name (case insensitive)
    @Query("SELECT c FROM Category c " +
           "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY c.displayOrder ASC")
    List<Category> searchByName(@Param("searchTerm") String searchTerm);

    // Get category statistics
    @Query("SELECT " +
           "c.id as categoryId, " +
           "c.name as categoryName, " +
           "COUNT(p) as totalProducts, " +
           "COUNT(CASE WHEN p.status = 'ACTIVE' THEN 1 END) as activeProducts, " +
           "COUNT(CASE WHEN p.isFeatured = true THEN 1 END) as featuredProducts " +
           "FROM Category c " +
           "LEFT JOIN c.products p " +
           "GROUP BY c.id, c.name " +
           "ORDER BY c.displayOrder ASC")
    List<Object[]> getCategoryStatistics();

    // Count active categories
    long countByActiveTrue();

    // Find categories by display order range
    List<Category> findByDisplayOrderBetweenOrderByDisplayOrderAsc(Integer start, Integer end);

    // Get max display order
    @Query("SELECT COALESCE(MAX(c.displayOrder), 0) FROM Category c")
    Integer getMaxDisplayOrder();
}
