package com.blubugtech.bakery_product_service.service.strategy;

import com.blubugtech.bakery_product_service.model.Storefront;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class MinCartValueValidationStrategy implements CouponValidationStrategy {
    @Override
    public void validate(String code, Double cartTotal, Storefront config) {
        if (config.getSpecialOfferSection() == null || config.getSpecialOfferSection().getOffers() == null) {
            return;
        }

        Storefront.SpecialOffer offer = config.getSpecialOfferSection().getOffers().stream()
                .filter(o -> code.equalsIgnoreCase(o.getCouponCode()))
                .findFirst()
                .orElse(null);

        if (offer != null && offer.getMinCartValue() != null && cartTotal != null && cartTotal < offer.getMinCartValue()) {
            throw new RuntimeException("doesn't apply on this cart");
        }
    }
}
