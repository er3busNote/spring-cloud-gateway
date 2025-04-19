package com.cloud.gateway.error.dto;

import com.cloud.gateway.error.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorInfo {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String message;
    private int status;
    private String timestamp;

    public static ErrorInfo of(ErrorCode errorCode) {
        return ErrorInfo.builder()
                .message(errorCode.getMessage())
                .status(errorCode.getStatus())
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    public static String toJson(ErrorInfo dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            return serializeFallbackError(); // fallback JSON
        }
    }

    public static String serializeFallbackError() {
        return "{\"message\":\"Error serializing fallback response\",\"status\":500}";
    }
}
