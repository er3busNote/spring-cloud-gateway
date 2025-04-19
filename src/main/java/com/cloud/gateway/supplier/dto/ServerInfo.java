package com.cloud.gateway.supplier.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerInfo {
    private String host;
    private int port;

    public static ServerInfo of(String host, int port) {
        return ServerInfo.builder()
                .host(host)
                .port(port)
                .build();
    }
}
