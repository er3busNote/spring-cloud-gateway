package com.cloud.gateway.config;

import com.cloud.gateway.error.ErrorHandler;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class ErrorHandlerConfig {

    @Bean
    @Order(-2)
    public ErrorWebExceptionHandler globalErrorHandler() {
        return new ErrorHandler();
    }
}
