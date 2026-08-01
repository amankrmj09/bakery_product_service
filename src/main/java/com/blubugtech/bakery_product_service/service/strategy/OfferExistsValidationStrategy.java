package com.blubugtech.bakery_product_service.service.strategy;

import com.blubugtech.bakery_product_service.model.Storefront;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class OfferExistsValidationStrategy implements CouponValidationStrategy {
    @Override
    public void validate(String code, Double cartTotal, Storefront config) {
        if (config.getSpecialOfferSection() == null || config.getSpecialOfferSection().getOffers() == null) {
            throw new RuntimeException("invalid_coupon");
        }
        
        config.getSpecialOfferSection().getOffers().stream()
                .filter(o -> code.equalsIgnoreCase(o.getCouponCode()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("invalid_coupon"));
    }
}
