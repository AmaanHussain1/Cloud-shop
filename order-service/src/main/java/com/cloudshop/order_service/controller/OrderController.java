package com.cloudshop.order_service.controller;

import com.cloudshop.order_service.config.ProductClient;
import com.cloudshop.order_service.dto.OrderConfirmationResponse;
import com.cloudshop.order_service.dto.ProductResponse;
import com.cloudshop.order_service.entity.Order;
import com.cloudshop.order_service.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public OrderController(OrderRepository orderRepository, ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @PostMapping
    public OrderConfirmationResponse placeOrder(@RequestBody Order order) {
        // Check if the product exists using Feign!
        ProductResponse product = productClient.getProductById(order.getProductId());

        // Process and save the order to the H2 database
        order.setOrderNumber(UUID.randomUUID().toString());
        Order savedOrder = orderRepository.save(order);

        return new OrderConfirmationResponse(
                savedOrder.getOrderNumber(),
                savedOrder.getQuantity(),
                product
        );
    }
}
