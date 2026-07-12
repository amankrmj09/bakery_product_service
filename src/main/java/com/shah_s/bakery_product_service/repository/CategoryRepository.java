package com.shah_s.bakery_product_service.repository;

import com.shah_s.bakery_product_service.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    // Find category by name
    Optional<Category> findByName(String name);

    // Check if category name exists
    boolean existsByName(String name);

    // Find active categories ordered by display order
    List<Category> findByActiveTrueOrderByDisplayOrderAsc();

    // Find categories by active status
    List<Category> findByActiveOrderByDisplayOrderAsc(Boolean active);

    // Search categories by name (case insensitive) - in Mongo, use regex
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Category> searchByName(String searchTerm);

    // Count active categories
    long countByActiveTrue();

    // Find categories by display order range
    List<Category> findByDisplayOrderBetweenOrderByDisplayOrderAsc(Integer start, Integer end);
}
