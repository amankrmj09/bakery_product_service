package com.blubugtech.bakery_product_service.controller;

import com.blubugtech.bakery_product_service.dto.review.ReviewResponse;
import com.blubugtech.bakery_product_service.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/reviews")
@Tag(name = "Admin Reviews", description = "Admin Review Management APIs")
public class AdminReviewController {

    private static final Logger logger = LoggerFactory.getLogger(AdminReviewController.class);

    private final ProductService productService;

    public AdminReviewController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/reported")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get reported reviews")
    public ResponseEntity<PagedModel<ReviewResponse>> getReportedReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "reportedAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        logger.info("Get reported reviews request received (page {}, size {})", page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ReviewResponse> reviews = productService.getReportedReviews(pageable);

        return ResponseEntity.ok(new PagedModel<>(reviews));
    }

    @PostMapping("/{reviewId}/dismiss")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Dismiss a report on a review")
    public ResponseEntity<Void> dismissReviewReport(@PathVariable String reviewId) {
        logger.info("Dismiss review report request received for review ID: {}", reviewId);
        
        productService.dismissReviewReport(reviewId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reviewId}/product/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a reported review")
    public ResponseEntity<Void> deleteReportedReview(
            @PathVariable String reviewId,
            @PathVariable String productId) {
        logger.info("Delete reported review request received for review ID: {}, product ID: {}", reviewId, productId);
        
        productService.deleteReview(productId, reviewId, null); // passing null as userId to signify admin override
        return ResponseEntity.noContent().build();
    }
}
