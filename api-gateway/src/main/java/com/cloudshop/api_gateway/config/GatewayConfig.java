package com.cloudshop.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator cloudShopRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                // Route for Product Service
                .route("product-service-route", r -> r
                        .path("/api/products/**")
                        .uri("lb://PRODUCT-SERVICE"))

                // Route for Order Service
                .route("order-service-route", r -> r
                        .path("/api/orders/**")
                        .uri("lb://ORDER-SERVICE"))
                .build();
    }
}
