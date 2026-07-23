package com.blubugtech.bakery_product_service.controller;

import com.blubugtech.bakery_product_service.entity.TaxRate;
import com.blubugtech.bakery_product_service.repository.TaxRateRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products/taxes")
public class TaxRateController {

    @Autowired
    private TaxRateRepository taxRateRepository;

    @GetMapping
    public ResponseEntity<List<TaxRate>> getAllTaxRates() {
        return ResponseEntity.ok(taxRateRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createTaxRate(@Valid @RequestBody TaxRate taxRate) {
        if (taxRateRepository.existsByType(taxRate.getType())) {
            return ResponseEntity.badRequest().body("Tax type already exists");
        }
        taxRate.setCreatedAt(LocalDateTime.now());
        taxRate.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(taxRateRepository.save(taxRate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTaxRate(@PathVariable String id, @Valid @RequestBody TaxRate request) {
        return taxRateRepository.findById(id).map(taxRate -> {
            // Check if type is being changed to an existing one
            if (!taxRate.getType().equals(request.getType()) && taxRateRepository.existsByType(request.getType())) {
                return ResponseEntity.badRequest().body("Tax type already exists");
            }
            taxRate.setType(request.getType());
            taxRate.setRate(request.getRate());
            taxRate.setUpdatedAt(LocalDateTime.now());
            return ResponseEntity.ok((Object) taxRateRepository.save(taxRate));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTaxRate(@PathVariable String id) {
        return taxRateRepository.findById(id).map(taxRate -> {
            taxRateRepository.delete(taxRate);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
