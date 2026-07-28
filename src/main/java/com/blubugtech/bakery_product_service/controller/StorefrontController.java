package com.blubugtech.bakery_product_service.controller;

import com.blubugtech.bakery_product_service.model.Storefront;
import com.blubugtech.bakery_product_service.service.StorefrontService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/storefront")
@RequiredArgsConstructor
public class StorefrontController {

    private final StorefrontService StorefrontService;

    @GetMapping("/frontpage")
    public ResponseEntity<Storefront> getStorefront() {
        return ResponseEntity.ok(StorefrontService.getStorefront());
    }

    @PutMapping("/frontpage")
    public ResponseEntity<Storefront> updateStorefront(@RequestBody Storefront config) {
        return ResponseEntity.ok(StorefrontService.updateStorefront(config));
    }

    @GetMapping("/validate-coupon")
    public ResponseEntity<?> validateCoupon(@RequestParam("code") String code, @RequestParam(value = "cartTotal", required = false) Double cartTotal) {
        try {
            return ResponseEntity.ok(StorefrontService.validateCoupon(code, cartTotal));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", e.getMessage()));
        }
    }
}

