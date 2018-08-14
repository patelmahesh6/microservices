/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.panthera.controller;

import com.panthera.dao.InventoryItemRepository;
import com.panthera.model.InventoryItem;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Administrator
 */
@RestController
@Slf4j
public class InventoryController {

    private final InventoryItemRepository inventoryItemRepository;

    @Autowired
    public InventoryController(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @GetMapping("/api/inventory/{productCode}")
    public ResponseEntity<InventoryItem> findInventoryByProductCode(@PathVariable("productCode") String productCode) {
        log.info("Finding inventory for product code :" + productCode);
        Optional<InventoryItem> inventoryItem = inventoryItemRepository.findByProductCode(productCode);
        if (inventoryItem.isPresent()) {
            return new ResponseEntity(inventoryItem, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/inventory")
    public List<InventoryItem> getInventory() {
        log.info("Finding inventory for all products ");
        return inventoryItemRepository.findAll();
    }
}
