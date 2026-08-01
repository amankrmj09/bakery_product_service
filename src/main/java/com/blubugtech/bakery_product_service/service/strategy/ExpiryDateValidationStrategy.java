package com.blubugtech.bakery_product_service.service.strategy;

import com.blubugtech.bakery_product_service.model.Storefront;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Slf4j
@Component
@Order(2)
public class ExpiryDateValidationStrategy implements CouponValidationStrategy {
    @Override
    public void validate(String code, Double cartTotal, Storefront config) {
        if (config.getSpecialOfferSection() == null || config.getSpecialOfferSection().getOffers() == null) {
            return;
        }

        Storefront.SpecialOffer offer = config.getSpecialOfferSection().getOffers().stream()
                .filter(o -> code.equalsIgnoreCase(o.getCouponCode()))
                .findFirst()
                .orElse(null);

        if (offer == null || offer.getExpiryDate() == null || offer.getExpiryDate().isEmpty()) {
            return;
        }

        try {
            LocalDate expiry = LocalDate.parse(offer.getExpiryDate());
            if (LocalDate.now().isAfter(expiry)) {
                throw new RuntimeException("coupon code expired and not valid");
            }
        } catch (DateTimeParseException e) {
            // If it's an ISO date time string
            try {
                Instant expiry = Instant.parse(offer.getExpiryDate());
                if (Instant.now().isAfter(expiry)) {
                    throw new RuntimeException("coupon code expired and not valid");
                }
            } catch (Exception ex) {
                log.error("Failed to parse expiry date for coupon {}", code, ex);
            }
        }
    }
}
