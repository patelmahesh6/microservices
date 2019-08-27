/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.panthera.service;

import com.panthera.feign.client.InventoryServiceClient;
import com.panthera.model.Product;
import com.panthera.model.ProductInventoryResponse;
import com.panthera.repository.ProductRepository;
import com.panthera.util.MyThreadLocalsHolder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Administrator
 */
@Service
@Transactional
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryServiceClient inventoryServiceClient;

    @Autowired
    public ProductService(ProductRepository productRepository, InventoryServiceClient inventoryServiceClient) {
        this.productRepository = productRepository;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    public List<Product> findAllProducts() {
        List<Product> products = productRepository.findAll();
        final Map<String, Integer> inventoryLevels = getInventoryLevelsWithFeignClient();
        final List<Product> availableProducts = products.stream()
                .filter(p -> inventoryLevels.get(p.getCode()) != null && inventoryLevels.get(p.getCode()) > 0)
                .collect(Collectors.toList());
        return availableProducts;
    }

    private Map<String, Integer> getInventoryLevelsWithFeignClient() {
        log.info("Fetching inventory levels using FeignClient");
        Map<String, Integer> inventoryLevels = new HashMap<>();
        List<ProductInventoryResponse> inventory = inventoryServiceClient.getProductInventoryLevels();
        for (ProductInventoryResponse item : inventory) {
            inventoryLevels.put(item.getProductCode(), item.getAvailableQuantity());
        }
        log.debug("InventoryLevels: {}", inventoryLevels);
        return inventoryLevels;
    }

    public Optional<Product> findProductByCode(String code) {
        Optional<Product> productOptional = productRepository.findByCode(code);
        if (productOptional.isPresent()) {
            
            String correlationId = UUID.randomUUID().toString();
            MyThreadLocalsHolder.setCorrelationId(correlationId);
            
            log.info("Before CorrelationID: " + MyThreadLocalsHolder.getCorrelationId());
            log.info("Fetching inventory level for product_code: " + code);
           
            Optional<ProductInventoryResponse> itemResponseEntity
                    = this.inventoryServiceClient.getProductInventoryByCode(code);
            
            if (itemResponseEntity.isPresent()) {
                Integer quantity = itemResponseEntity.get().getAvailableQuantity();
                productOptional.get().setInStock(quantity > 0);
            }
            
            log.info("After CorrelationID: " + MyThreadLocalsHolder.getCorrelationId());
        }
        return productOptional;
    }
}
