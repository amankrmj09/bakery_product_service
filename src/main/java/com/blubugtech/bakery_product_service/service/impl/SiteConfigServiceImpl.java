package com.blubugtech.bakery_product_service.service.impl;

import com.blubugtech.bakery_product_service.service.SiteConfigService;

import com.blubugtech.bakery_product_service.model.SiteConfig;
import com.blubugtech.bakery_product_service.repository.SiteConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SiteConfigServiceImpl implements SiteConfigService {

    private final SiteConfigRepository siteConfigRepository;
    private static final String DEFAULT_ID = "home-page";

    public SiteConfig getSiteConfig() {
        return siteConfigRepository.findById(DEFAULT_ID).orElseGet(this::createDefaultConfig);
    }

    @Transactional
    public SiteConfig updateSiteConfig(SiteConfig config) {
        config.setId(DEFAULT_ID); // Ensure we only update the home-page config
        return siteConfigRepository.save(config);
    }

    private SiteConfig createDefaultConfig() {
        SiteConfig config = SiteConfig.builder()
                .id(DEFAULT_ID)
                .heroSection(SiteConfig.HeroSection.builder()
                        .tag("Artisanal Bakery")
                        .headline("FRESH, SWEET &\nTASTY")
                        .subtitle("Sale 20% every Monday")
                        .heroImageUrl("/images/hero_burger.png")
                        .sideCard1(SiteConfig.SideCard.builder()
                                .subtitle("Freshly Baked")
                                .title("Signature Cake")
                                .price("$24.50")
                                .imageUrl("/images/hero_cake.png")
                                .build())
                        .sideCard2(SiteConfig.SideCard.builder()
                                .subtitle("Sweet Treat")
                                .title("Vanilla Cupcakes")
                                .imageUrl("/images/hero_cupcakes.png")
                                .build())
                        .build())
                .aboutSection(SiteConfig.AboutSection.builder()
                        .tag("Savor the Flavor, Anytime, Anywhere")
                        .title("FRESH. DELICIOUS. DELIVERED!")
                        .description("Welcome to your ultimate destination for mouthwatering meals and snacks delivered straight to your doorstep.")
                        .image1Url("/images/bakery_chef.png")
                        .image2Url("/images/hero_croissant.png")
                        .image3Url("/images/hero_cupcakes.png")
                        .build())
                .howWeWorkSection(List.of(
                        SiteConfig.HowWeWorkStep.builder().title("Gathering").description("Making Fresh and Tastiest Food.").iconName("CookingPot").build(),
                        SiteConfig.HowWeWorkStep.builder().title("Transportation").description("Select the best and transport it.").iconName("Truck").build(),
                        SiteConfig.HowWeWorkStep.builder().title("Packaging").description("Carefully pack your order.").iconName("PackageCheck").build(),
                        SiteConfig.HowWeWorkStep.builder().title("Delivery").description("We can drive any products.").iconName("Delivery").build()
                ))
                .specialOfferSection(SiteConfig.SpecialOfferSection.builder()
                        .tag("Limited Time")
                        .headline("Get 20% Off Your First Custom Cake!")
                        .description("Order any of our signature cakes today.")
                        .imageUrl("/images/hero_cake.png")
                        .build())
                .testimonialSection(SiteConfig.TestimonialSection.builder()
                        .quote("This site has transformed the way I enjoy my meals.")
                        .author("Rohit Sharma")
                        .rating(5)
                        .authorImageUrl("https://ui-avatars.com/api/?name=Rohit+Sharma")
                        .build())
                .build();
        return siteConfigRepository.save(config);
    }
}
