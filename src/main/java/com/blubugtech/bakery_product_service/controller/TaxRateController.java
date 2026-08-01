package com.blubugtech.bakery_product_service.controller;

import com.blubugtech.bakery_product_service.entity.TaxRate;
import com.blubugtech.bakery_product_service.service.TaxRateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taxes")
public class TaxRateController {

    @Autowired
    private TaxRateService taxRateService;

    @GetMapping
    public ResponseEntity<List<TaxRate>> getAllTaxRates() {
        return ResponseEntity.ok(taxRateService.getAllTaxRates());
    }

    @PostMapping
    public ResponseEntity<?> createTaxRate(@Valid @RequestBody TaxRate taxRate) {
        try {
            TaxRate created = taxRateService.createTaxRate(taxRate);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTaxRate(@PathVariable String id, @Valid @RequestBody TaxRate request) {
        try {
            return taxRateService.updateTaxRate(id, request)
                    .map(taxRate -> ResponseEntity.ok((Object) taxRate))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTaxRate(@PathVariable String id) {
        boolean deleted = taxRateService.deleteTaxRate(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
