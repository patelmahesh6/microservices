package com.panthera;

import com.panthera.filters.AuthHeaderFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
//@EnableOAuth2Sso
@SpringBootApplication
public class ShoppingUIApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingUIApplication.class, args);
    }

    @Bean
    AuthHeaderFilter authHeaderFilter() {
        return new AuthHeaderFilter();
    }
}
