package com.blubugtech.bakery_product_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

import org.springframework.context.annotation.Import;
import com.blubugtech.common.security.MethodSecurityConfig;

@SpringBootApplication
@EnableDiscoveryClient
@Import(MethodSecurityConfig.class)
public class BakeryProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BakeryProductServiceApplication.class, args);
	}

}
