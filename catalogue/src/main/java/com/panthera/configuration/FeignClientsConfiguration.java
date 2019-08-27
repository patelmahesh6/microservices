/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.panthera.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author INTERACTIVE
 */
@Configuration
@EnableFeignClients(basePackages = {"com.panthera.feign.client"})
public class FeignClientsConfiguration {
    
}
