/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.panthera.feign.client;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.panthera.model.ProductInventoryResponse;
import com.panthera.util.MyThreadLocalsHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Administrator
 */
@Service
@Slf4j
public class InventoryServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private InventoryServiceFeignClient inventoryServiceFeignClient;

    //TODO; move this to config file
    private static final String INVENTORY_API_PATH = "http://inventory-service/api/";

    @HystrixCommand(fallbackMethod = "getDefaultProductInventoryLevels",
            commandProperties = {
                @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
            }
    )
    public List<ProductInventoryResponse> getProductInventoryLevels() {
        return this.inventoryServiceFeignClient.getInventoryLevels();
    }

    @SuppressWarnings("unused")
    List<ProductInventoryResponse> getDefaultProductInventoryLevels() {
        log.info("Returning default product inventory levels");
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "getDefaultProductInventoryByCode")
    public Optional<ProductInventoryResponse> getProductInventoryByCode(String productCode) {
        log.info("CorrelationID: " + MyThreadLocalsHolder.getCorrelationId());
        ResponseEntity<ProductInventoryResponse> itemResponseEntity
                = restTemplate.getForEntity(INVENTORY_API_PATH + "inventory/{code}",
                        ProductInventoryResponse.class,
                        productCode);

        if (itemResponseEntity.getStatusCode() == HttpStatus.OK) {
            Integer quantity = itemResponseEntity.getBody().getAvailableQuantity();
            log.info("Available quantity: " + quantity);
            return Optional.ofNullable(itemResponseEntity.getBody());
        } else {
            log.error("Unable to get inventory level for product_code: " + productCode + ", StatusCode: " + itemResponseEntity.getStatusCode());
            return Optional.empty();
        }
    }

    @SuppressWarnings("unused")
    Optional<ProductInventoryResponse> getDefaultProductInventoryByCode(String productCode) {
        log.info("Returning default ProductInventoryByCode for productCode: " + productCode);
        log.info("CorrelationID: " + MyThreadLocalsHolder.getCorrelationId());
        ProductInventoryResponse response = new ProductInventoryResponse();
        response.setProductCode(productCode);
        response.setAvailableQuantity(50);
        return Optional.ofNullable(response);
    }

}
