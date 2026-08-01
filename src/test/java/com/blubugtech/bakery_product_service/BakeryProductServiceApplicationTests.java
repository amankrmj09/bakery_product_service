package com.blubugtech.bakery_product_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;

import org.springframework.boot.test.mock.mockito.MockBean;
import com.blubugtech.bakery_product_service.search.repository.CategorySearchRepository;
import com.blubugtech.bakery_product_service.search.repository.ProductSearchRepository;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
@ActiveProfiles("test")
class BakeryProductServiceApplicationTests {

    @MockBean
    private CategorySearchRepository categorySearchRepository;

    @MockBean
    private ProductSearchRepository productSearchRepository;

    @MockBean
    private KafkaTemplate<String, Object> kafkaTemplate;

	@Test
	void contextLoads() {
	}

}
