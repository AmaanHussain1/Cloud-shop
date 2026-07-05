package com.cloudshop.order_service.controller;

import com.cloudshop.order_service.service.ProductClient;
import com.cloudshop.order_service.dto.OrderConfirmationResponse;
import com.cloudshop.order_service.dto.ProductResponse;
import com.cloudshop.order_service.entity.Order;
import com.cloudshop.order_service.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final RabbitTemplate rabbitTemplate;

    public OrderController(OrderRepository orderRepository,
                           ProductClient productClient,
                           RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.rabbitTemplate = rabbitTemplate;
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

        // SEND THE MESSAGE TO RABBITMQ!
        String notificationMessage = "Order " + savedOrder.getOrderNumber() + " placed for " + product.name();
        rabbitTemplate.convertAndSend("order.notifications", notificationMessage);

        return new OrderConfirmationResponse(
                savedOrder.getOrderNumber(),
                savedOrder.getQuantity(),
                product
        );
    }
}
