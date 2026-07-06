package com.cloudshop.order_service.controller;

import com.cloudshop.order_service.service.ProductClient;
import com.cloudshop.order_service.dto.OrderConfirmationResponse;
import com.cloudshop.order_service.dto.ProductResponse;
import com.cloudshop.order_service.entity.Order;
import com.cloudshop.order_service.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final RabbitTemplate rabbitTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderController(OrderRepository orderRepository,
                           ProductClient productClient,
                           RabbitTemplate rabbitTemplate,
                           KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.rabbitTemplate = rabbitTemplate;
        this.kafkaTemplate = kafkaTemplate;
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

        // Send RabbitMQ Message (Task: Send Email)
        String notificationMessage = "Order " + savedOrder.getOrderNumber() + " placed for " + product.name();
        rabbitTemplate.convertAndSend("order.notifications", notificationMessage);

        // Send Kafka Event (Domain Event: System-wide announcement)
        String kafkaEvent = "OrderPlaced: ID=" + savedOrder.getOrderNumber() + ", ProductID=" + product.id() + ", Qty=" + order.getQuantity();
        kafkaTemplate.send("order-placed-topic", kafkaEvent);

        System.out.println("ORDER SERVICE: Successfully pushed message to Kafka!");


        return new OrderConfirmationResponse(
                savedOrder.getOrderNumber(),
                savedOrder.getQuantity(),
                product
        );
    }
}
