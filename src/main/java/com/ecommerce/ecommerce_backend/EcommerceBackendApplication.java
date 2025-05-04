package com.ecommerce.ecommerce_backend;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EcommerceBackendApplication {
	@Value("${server.port}")
	private String serverPort;

	@Value("${springdoc.swagger-ui.path}")
	private String swaggerPath;

	public static void main(String[] args) {
		SpringApplication.run(EcommerceBackendApplication.class, args);
	}
	@PostConstruct
	public void printSwaggerUrl() {
		System.out.println("Swagger UI está disponible en: http://localhost:" + serverPort + swaggerPath);
	}
}
