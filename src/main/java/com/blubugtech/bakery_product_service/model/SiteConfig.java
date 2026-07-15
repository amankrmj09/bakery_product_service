package com.blubugtech.bakery_product_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "site_configs")
public class SiteConfig {
    @Id
    private String id;
    
    private HeroSection heroSection;
    private AboutSection aboutSection;
    private List<HowWeWorkStep> howWeWorkSection;
    private SpecialOfferSection specialOfferSection;
    private TestimonialSection testimonialSection;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HeroSection {
        private String tag;
        private String headline;
        private String subtitle;
        private String heroImageUrl;
        private SideCard sideCard1;
        private SideCard sideCard2;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SideCard {
        private String subtitle;
        private String title;
        private String price;
        private String imageUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AboutSection {
        private String tag;
        private String title;
        private String description;
        private String image1Url;
        private String image2Url;
        private String image3Url;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HowWeWorkStep {
        private String title;
        private String description;
        private String iconName; // e.g., 'CookingPot', 'Truck'
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SpecialOfferSection {
        private String tag;
        private String headline;
        private String description;
        private String imageUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TestimonialSection {
        private String quote;
        private String author;
        private Integer rating;
        private String authorImageUrl;
    }
}
