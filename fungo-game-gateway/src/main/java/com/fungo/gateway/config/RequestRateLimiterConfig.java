package com.fungo.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/11/26
 */
@Configuration
public class RequestRateLimiterConfig {

    @Bean
    @Primary
    KeyResolver apiKeyResolver() {
        //按URL限流,即以每秒内请求数按URL分组统计，超出限流的url请求都将返回429状态
        return exchange -> Mono.just(exchange.getRequest().getPath().toString());
    }

    @Bean
    KeyResolver userKeyResolver() {
        //按用户限流
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("user"));
    }

    /**
     *  这是根据IP限流
     * 自定义限流标志的key，多个维度可以从这里入手
     * exchange对象中获取服务ID、请求信息，用户信息等
     */
    @Bean
    KeyResolver remoteAddrKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }


//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        //生成比当前时间早一个小时的UTC时间
//        ZonedDateTime minusTime = LocalDateTime.now().minusHours(1).atZone( ZoneId.systemDefault());
//        return builder.routes()
//                .route(r ->r.path("/demo/**")
//                        //过滤器
//                        .filters(f -> f.filter(new GatewayRateLimitFilterByIP())
//                                .filter(new GatewayRateLimitFilterByIP(10,1, Duration.ofSeconds(1))))
//                        .uri("http://192.168.26.113:8001/demo").order(0).id("demo_route"))
//                .route(r ->r.path("/test")
//                        .uri("http://192.168.26.113/system/nav/login").id("jd_route")
//                ).build();
//    }

}
