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
@Document(collection = "storefront")
public class Storefront {
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
        private List<Campaign> campaigns;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Campaign { 
        private String imageUrl;
        private String title;
        private String description;
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
    public static class SpecialOffer {
        private String imageUrl;
        private String title;
        private String description;
        private String couponCode;
        private String discountType; // PERCENTAGE or FLAT
        private Double discountValue;
        private String expiryDate; // e.g. ISO-8601 string or local date string
        private Double minCartValue;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SpecialOfferSection {
        private List<SpecialOffer> offers;
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

