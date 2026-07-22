package com.blubugtech.bakery_product_service.service;

import com.blubugtech.bakery_product_service.service.StorefrontService;
import com.blubugtech.bakery_product_service.model.Storefront;
import com.blubugtech.bakery_product_service.repository.StorefrontRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface StorefrontService {
    Storefront getStorefront();
    Storefront updateStorefront(Storefront config);
}

