package com.blubugtech.bakery_product_service.service;

import com.blubugtech.bakery_product_service.service.SiteConfigService;
import com.blubugtech.bakery_product_service.model.SiteConfig;
import com.blubugtech.bakery_product_service.repository.SiteConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface SiteConfigService {
    SiteConfig getSiteConfig();
    SiteConfig updateSiteConfig(SiteConfig config);
}
