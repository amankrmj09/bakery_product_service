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
    public ResponseEntity<org.springframework.data.web.PagedModel<TaxRate>> getAllTaxRates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.fromString(sortDir), sortBy);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        return ResponseEntity.ok(taxRateService.getAllTaxRates(pageable));
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
