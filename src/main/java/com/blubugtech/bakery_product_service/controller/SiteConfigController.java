package com.blubugtech.bakery_product_service.controller;

import com.blubugtech.bakery_product_service.model.SiteConfig;
import com.blubugtech.bakery_product_service.service.SiteConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/site-config")
@RequiredArgsConstructor
public class SiteConfigController {

    private final SiteConfigService siteConfigService;

    @GetMapping("/frontpage")
    public ResponseEntity<SiteConfig> getSiteConfig() {
        return ResponseEntity.ok(siteConfigService.getSiteConfig());
    }

    @PutMapping("/frontpage")
    public ResponseEntity<SiteConfig> updateSiteConfig(@RequestBody SiteConfig config) {
        return ResponseEntity.ok(siteConfigService.updateSiteConfig(config));
    }
}
