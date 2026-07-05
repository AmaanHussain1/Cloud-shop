package com.cloudshop.notification_service;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public Queue notificationQueue() {
        // This tells Spring: "Make sure 'order.notifications' exists before listening to it!"
        return new Queue("order.notifications");
    }
}
