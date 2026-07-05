package com.cloudshop.order_service.dto;

public record OrderConfirmationResponse(
        String orderNumber,
        Integer quantityOrdered,
        ProductResponse productDetails
) {}
