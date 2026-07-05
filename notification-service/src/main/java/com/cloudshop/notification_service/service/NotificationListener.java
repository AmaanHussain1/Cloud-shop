package com.cloudshop.notification_service.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    // This method will automatically trigger whenever a message hits the queue!
    @RabbitListener(queues = "order.notifications")
    public void handleOrderNotification(String message) {
        System.out.println("======================================");
        System.out.println("📧 EMAIL SENT TO CUSTOMER!");
        System.out.println("Message received: " + message);
        System.out.println("======================================");
    }
}
