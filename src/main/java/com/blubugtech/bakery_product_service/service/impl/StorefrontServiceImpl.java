package com.blubugtech.bakery_product_service.service.impl;

import com.blubugtech.bakery_product_service.service.StorefrontService;
import com.blubugtech.bakery_product_service.model.Storefront;
import com.blubugtech.bakery_product_service.repository.StorefrontRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
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

    private Storefront createDefaultConfig() {
        Storefront config = Storefront.builder()
                .id(DEFAULT_ID)
                .heroSection(Storefront.HeroSection.builder()
                        .campaigns(List.of())
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
