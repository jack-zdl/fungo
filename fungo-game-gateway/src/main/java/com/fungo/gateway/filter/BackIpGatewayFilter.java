package com.fungo.gateway.filter;

import com.fungo.gateway.config.AuthSkipUrlsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/12/6
 */
@Component
public class BackIpGatewayFilter  { //implements GlobalFilter, Ordered

//    @Autowired
//    private AuthSkipUrlsProperties authSkipUrlsProperties;

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        return null;
//    }
//
//    @Override
//    public int getOrder() {
//        return 0;
//    }

//    /**
//     * 白名单
//     *
//     * @param context
//     * @param exchange
//     * @return
//     */
//    private boolean whiteListCheck(GatewayContext context, ServerWebExchange exchange) {
//        String url = exchange.getRequest().getURI().getPath();
//        boolean white = authSkipUrlsProperties.getUrlPatterns().stream()
//                .map(pattern -> pattern.matcher(url))
//                .anyMatch(Matcher::find);
//        if (white) {
//            context.setPath(url);
//            return true;
//        }
//        return false;
//    }
//
//
//    /**
//     * 黑名单
//     *
//     * @param context
//     * @param exchange
//     * @return
//     */
//    private boolean blackServersCheck(GatewayContext context, ServerWebExchange exchange) {
//        String instanceId = exchange.getRequest().getURI().getPath().substring(1, exchange.getRequest().getURI().getPath().indexOf('/', 1));
//        if (!CollectionUtils.isEmpty(authSkipUrlsProperties.getInstanceServers())) {
//            boolean black = authSkipUrlsProperties.getServerPatterns().stream()
//                    .map(pattern -> pattern.matcher(instanceId))
//                    .anyMatch( Matcher::find);
//            if (black) {
//                context.setBlack(true);
//                return true;
//            }
//        }
//        return false;
//    }


    /**
     * @param request
     * @return
     */
    private String getSsoUrl(ServerHttpRequest request) {
        return request.getPath().value();
    }

}
