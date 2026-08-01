package com.blubugtech.bakery_product_service.service;

import com.blubugtech.bakery_product_service.entity.TaxRate;
import java.util.List;
import java.util.Optional;

public interface TaxRateService {
    List<TaxRate> getAllTaxRates();
    TaxRate createTaxRate(TaxRate taxRate);
    Optional<TaxRate> updateTaxRate(String id, TaxRate request);
    boolean deleteTaxRate(String id);
}
