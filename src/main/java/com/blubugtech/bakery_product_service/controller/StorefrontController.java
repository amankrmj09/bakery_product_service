package com.blubugtech.bakery_product_service.controller;

import com.blubugtech.bakery_product_service.model.Storefront;
import com.blubugtech.bakery_product_service.service.StorefrontService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/site-config")
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
}

