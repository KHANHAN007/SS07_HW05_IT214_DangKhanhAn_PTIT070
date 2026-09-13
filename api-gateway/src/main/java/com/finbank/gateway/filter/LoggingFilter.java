package com.finbank.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long start = System.currentTimeMillis();
        String method = exchange.getRequest().getMethod().name();
        String path = exchange.getRequest().getURI().getPath();

        log.info("Gateway request start: {} {}", method, path);

        exchange.getResponse().beforeCommit(() -> {
            long responseTime = System.currentTimeMillis() - start;
            exchange.getResponse().getHeaders().add("X-Response-Time", responseTime + "ms");
            return Mono.empty();
        });

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long responseTime = System.currentTimeMillis() - start;
            log.info("Gateway request end: {} {} - {}ms", method, path, responseTime);
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
