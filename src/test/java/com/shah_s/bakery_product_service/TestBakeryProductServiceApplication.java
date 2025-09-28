package com.shah_s.bakery_product_service;

import org.springframework.boot.SpringApplication;

public class TestBakeryProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(BakeryProductServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
