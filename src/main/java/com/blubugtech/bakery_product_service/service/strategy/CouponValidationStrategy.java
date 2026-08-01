package com.blubugtech.bakery_product_service.service.strategy;

import com.blubugtech.bakery_product_service.model.Storefront;

public interface CouponValidationStrategy {
    void validate(String code, Double cartTotal, Storefront config);
}
