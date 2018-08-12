package com.panthera.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.panthera")
@EnableJpaRepositories("com.panthera.repository")
@EntityScan("com.panthera.model")
public class CatalogueApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogueApplication.class, args);
    }
}
