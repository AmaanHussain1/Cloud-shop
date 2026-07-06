package com.cloudshop.inventory_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryListener {

    @KafkaListener(topics = "order-placed-topic", groupId = "inventory-group")
    public void handleOrderPlaced(String message){
        System.out.println("======================================");
        System.out.println("INVENTORY SERVICE: Event Received!");
        System.out.println("Message: " + message);
        System.out.println("Action: Deducting stock in database...");
        System.out.println("======================================");
    }
}
