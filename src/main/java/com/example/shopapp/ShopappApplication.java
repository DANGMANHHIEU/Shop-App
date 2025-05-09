package com.example.shopapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.shopapp.repositories")
@ComponentScan(basePackages = {
		"com.example.shopapp",
//		"com.project.shopapp.services",
//		"com.project.shopapp.components",
//		"com.project.shopapp.configurations",
//		"com.project.shopapp.filters"
})
public class ShopappApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopappApplication.class, args);
	}

}
