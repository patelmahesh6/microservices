package com.panthera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@EnableCircuitBreaker
@SpringBootApplication
public class OrderManagmentApplication {
    
	public static void main(String[] args) {
		SpringApplication.run(OrderManagmentApplication.class, args);
	}
}
