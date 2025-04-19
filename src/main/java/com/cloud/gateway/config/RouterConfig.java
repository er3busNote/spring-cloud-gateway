package com.cloud.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class RouterConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("accounts_route", r -> r.path("/accounts/**")
                        .filters(f -> f
                                .rewritePath("/accounts(?<segment>/.*)", "${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://ACCOUNTS"))
                .route("info_route", r -> r.path("/info/**")
                        .filters(f -> f
                                .rewritePath("/info(?<segment>/.*)", "${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://INFO"))
                .build();
    }
}
