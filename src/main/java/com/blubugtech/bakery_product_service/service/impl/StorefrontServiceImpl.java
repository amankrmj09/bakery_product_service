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
                        .campaigns(java.util.stream.Stream.generate(() -> 
                                Storefront.Campaign.builder().largeImageUrl(null).smallImageUrl(null).build()
                        ).limit(5).toList())
                        .build())
                .aboutSection(Storefront.AboutSection.builder()
                        .tag("Savor the Flavor, Anytime, Anywhere")
                        .title("FRESH. DELICIOUS. DELIVERED!")
                        .description("Welcome to your ultimate destination for mouthwatering meals and snacks delivered straight to your doorstep.")
                        .image1Url("/images/bakery_chef.png")
                        .image2Url("/images/hero_croissant.png")
                        .image3Url("/images/hero_cupcakes.png")
                        .build())
                .howWeWorkSection(List.of(
                        Storefront.HowWeWorkStep.builder().title("Gathering").description("Making Fresh and Tastiest Food.").iconName("CookingPot").build(),
                        Storefront.HowWeWorkStep.builder().title("Transportation").description("Select the best and transport it.").iconName("Truck").build(),
                        Storefront.HowWeWorkStep.builder().title("Packaging").description("Carefully pack your order.").iconName("PackageCheck").build(),
                        Storefront.HowWeWorkStep.builder().title("Delivery").description("We can drive any products.").iconName("Delivery").build()
                ))
                .specialOfferSection(Storefront.SpecialOfferSection.builder()
                        .tag("Limited Time")
                        .headline("Get 20% Off Your First Custom Cake!")
                        .description("Order any of our signature cakes today.")
                        .imageUrl("/images/hero_cake.png")
                        .build())
                .testimonialSection(Storefront.TestimonialSection.builder()
                        .quote("This site has transformed the way I enjoy my meals.")
                        .author("Rohit Sharma")
                        .rating(5)
                        .authorImageUrl("https://ui-avatars.com/api/?name=Rohit+Sharma")
                        .build())
                .build();
        return storefrontRepository.save(config);
    }
}
