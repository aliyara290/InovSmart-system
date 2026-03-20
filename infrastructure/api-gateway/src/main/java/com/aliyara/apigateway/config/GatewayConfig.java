package com.aliyara.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("company-service", r -> r.path("/company/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://COMPANY-SERVICE"))
                .route("inventory-service", r -> r.path("/inventory/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://INVENTORY-SERVICE"))
                .route("billing-service", r -> r.path("/billing/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://BILLING-SERVICE"))
                .route("supply-service", r -> r.path("/supply/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://SUPPLY-SERVICE"))
                .build();
    }
}