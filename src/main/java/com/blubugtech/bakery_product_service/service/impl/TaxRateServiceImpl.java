package com.blubugtech.bakery_product_service.service.impl;

import com.blubugtech.bakery_product_service.entity.TaxRate;
import com.blubugtech.bakery_product_service.repository.TaxRateRepository;
import com.blubugtech.bakery_product_service.service.TaxRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaxRateServiceImpl implements TaxRateService {

    @Autowired
    private TaxRateRepository taxRateRepository;

    @Override
    public List<TaxRate> getAllTaxRates() {
        return taxRateRepository.findAll();
    }

    @Override
    public TaxRate createTaxRate(TaxRate taxRate) {
        if (taxRateRepository.existsByType(taxRate.getType())) {
            throw new IllegalArgumentException("Tax type already exists");
        }
        taxRate.setCreatedAt(LocalDateTime.now());
        taxRate.setUpdatedAt(LocalDateTime.now());
        return taxRateRepository.save(taxRate);
    }

    @Override
    public Optional<TaxRate> updateTaxRate(String id, TaxRate request) {
        return taxRateRepository.findById(id).map(taxRate -> {
            if (!taxRate.getType().equals(request.getType()) && taxRateRepository.existsByType(request.getType())) {
                throw new IllegalArgumentException("Tax type already exists");
            }
            taxRate.setType(request.getType());
            taxRate.setRate(request.getRate());
            taxRate.setDescription(request.getDescription());
            taxRate.setUpdatedAt(LocalDateTime.now());
            return taxRateRepository.save(taxRate);
        });
    }

    @Override
    public boolean deleteTaxRate(String id) {
        return taxRateRepository.findById(id).map(taxRate -> {
            taxRateRepository.delete(taxRate);
            return true;
        }).orElse(false);
    }
}
