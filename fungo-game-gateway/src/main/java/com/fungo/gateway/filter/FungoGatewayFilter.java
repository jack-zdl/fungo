package com.fungo.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 过滤器
 */
public class FungoGatewayFilter implements GatewayFilter, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(FungoGatewayFilter.class);
    private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                            LOGGER.info("------------------过滤器:{}", exchange.getRequest().getURI().getRawPath());
                            exchange.getAttributes().put(REQUEST_TIME_BEGIN, System.currentTimeMillis());
                        }
                ));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
