package com.blubugtech.bakery_product_service.service.impl;

import com.blubugtech.bakery_product_service.service.StorefrontService;
import com.blubugtech.bakery_product_service.model.Storefront;
import com.blubugtech.bakery_product_service.repository.StorefrontRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorefrontServiceImpl implements StorefrontService {

    private final StorefrontRepository storefrontRepository;
    private static final String DEFAULT_ID = "home-page";

    public Storefront getStorefront() {
        return storefrontRepository.findById(DEFAULT_ID).orElseGet(this::createDefaultConfig);
    }

    @Transactional
    public Storefront updateStorefront(Storefront config) {
        config.setId(DEFAULT_ID); // Ensure we only update the home-page config
        return storefrontRepository.save(config);
    }

    public org.blubakery.common.feign.contract.feign.CouponValidationResponse validateCoupon(String code, Double cartTotal) {
        Storefront config = getStorefront();
        if (config.getSpecialOfferSection() == null || config.getSpecialOfferSection().getOffers() == null) {
            throw new RuntimeException("invalid_coupon");
        }
        
        Storefront.SpecialOffer offer = config.getSpecialOfferSection().getOffers().stream()
                .filter(o -> code.equalsIgnoreCase(o.getCouponCode()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("invalid_coupon"));
                
        if (offer.getExpiryDate() != null && !offer.getExpiryDate().isEmpty()) {
            try {
                java.time.LocalDate expiry = java.time.LocalDate.parse(offer.getExpiryDate());
                if (java.time.LocalDate.now().isAfter(expiry)) {
                    throw new RuntimeException("coupon code expired and not valid");
                }
            } catch (java.time.format.DateTimeParseException e) {
                // If it's an ISO date time string
                try {
                    java.time.Instant expiry = java.time.Instant.parse(offer.getExpiryDate());
                    if (java.time.Instant.now().isAfter(expiry)) {
                        throw new RuntimeException("coupon code expired and not valid");
                    }
                } catch(Exception ex) {
                    log.error("Failed to parse expiry date for coupon {}", code, ex);
                }
            }
        }
        
        if (offer.getMinCartValue() != null && cartTotal != null && cartTotal < offer.getMinCartValue()) {
            throw new RuntimeException("doesn't apply on this cart");
        }
        
        return org.blubakery.common.feign.contract.feign.CouponValidationResponse.builder()
                .couponCode(offer.getCouponCode())
                .discountType(offer.getDiscountType())
                .discountValue(offer.getDiscountValue())
                .message("Valid")
                .build();
    }

    private Storefront createDefaultConfig() {
        Storefront config = Storefront.builder()
                .id(DEFAULT_ID)
                .heroSection(Storefront.HeroSection.builder()
                        .heroBanners(List.of())
                        .build())
                .aboutSection(Storefront.AboutSection.builder()
                        .tag(null)
                        .title(null)
                        .description(null)
                        .image1Url(null)
                        .image2Url(null)
                        .image3Url(null)
                        .build())
                .howWeWorkSection(List.of())
                .specialOfferSection(Storefront.SpecialOfferSection.builder()
                        .offers(List.of())
                        .build())
                .testimonialSection(Storefront.TestimonialSection.builder()
                        .quote(null)
                        .author(null)
                        .rating(null)
                        .authorImageUrl(null)
                        .build())
                .build();
        return storefrontRepository.save(config);
    }
}
