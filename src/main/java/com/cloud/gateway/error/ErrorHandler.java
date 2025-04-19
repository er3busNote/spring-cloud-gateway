package com.cloud.gateway.error;

import com.cloud.gateway.error.dto.ErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class ErrorHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ErrorInfo errorDto;
        HttpStatus status;

        // 예외 종류에 따라 분기 처리
        if (ex instanceof ConnectException) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
            errorDto = ErrorInfo.of(ErrorCode.CONNECTION_REFUSED);
        } else if (ex instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
            errorDto = ErrorInfo.of(ErrorCode.INVALID_REQUEST_PARAMETER);
        } else if (ex instanceof UnsupportedOperationException) {
            status = HttpStatus.NOT_IMPLEMENTED;
            errorDto = ErrorInfo.of(ErrorCode.UNSUPPORTED_OPERATION);
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            errorDto = ErrorInfo.of(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        String message = ErrorInfo.toJson(errorDto);
        return writeErrorResponse(exchange, status, message);
    }

    private Mono<Void> writeErrorResponse(ServerWebExchange exchange, HttpStatus status, String message) {
        if (exchange.getResponse().isCommitted()) {
            log.error("응답이 이미 Commit 되었습니다 : 에러코드 {}", status.value());
            //return Mono.error(new IllegalStateException("Response already committed"));
            return Mono.empty();
        }

        return Mono.defer(() -> {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(status);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            DataBuffer buffer = response.bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        });
    }
}
