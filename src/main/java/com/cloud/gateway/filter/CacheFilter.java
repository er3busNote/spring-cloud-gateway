package com.cloud.gateway.filter;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CacheFilter implements GlobalFilter, Ordered {

    @Autowired
    private CacheManager cacheManager;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!"GET".equalsIgnoreCase(exchange.getRequest().getMethod().name())) {
            return chain.filter(exchange);
        }

        String cacheKey = getCacheKey(exchange);
        Cache cache = cacheManager.getCache("gateway-cache");

        if (cache == null) return chain.filter(exchange);

        byte[] cachedResponse = cache.get(cacheKey, byte[].class);
        if (cachedResponse != null) {
            exchange.getResponse().getHeaders().add("X-Cache", "Hit from spring-cache");
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(cachedResponse);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        }

        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();

        ServerHttpResponseDecorator decorated = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                Flux<? extends DataBuffer> flux = Flux.from(body);

                return super.writeWith(flux.map(dataBuffer -> {
                    byte[] content = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(content);
                    cache.put(cacheKey, content);
                    originalResponse.getHeaders().add("X-Cache", "Miss from spring-cache");
                    return bufferFactory.wrap(content);
                }));
            }
        };

        return chain.filter(exchange.mutate().response(decorated).build());
    }

    private String getCacheKey(ServerWebExchange exchange) {
        String raw = exchange.getRequest().getURI().toString();
        return DigestUtils.md5DigestAsHex(raw.getBytes());
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
