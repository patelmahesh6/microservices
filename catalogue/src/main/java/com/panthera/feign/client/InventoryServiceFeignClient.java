/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.panthera.feign.client;

import com.panthera.model.ProductInventoryResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Administrator
 */
@FeignClient(name = "inventory-service")
public interface InventoryServiceFeignClient {

    @GetMapping("/api/inventory")
    List<ProductInventoryResponse> getInventoryLevels();

    @GetMapping("/api/inventory/{productCode}")
    List<ProductInventoryResponse> getInventoryByProductCode(String productCode);

}