package com.cloud.gateway.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_REQUEST_PARAMETER("파라미터가 올바르지 않습니다.", 400),
    INTERNAL_SERVER_ERROR("서버 에러", 500),
    UNSUPPORTED_OPERATION("지원하지 않는 기능입니다.", 501),
    CONNECTION_REFUSED("해당 서비스를 이용할수 없습니다.", 503);

    private final String message;
    private final int status;

    ErrorCode(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
