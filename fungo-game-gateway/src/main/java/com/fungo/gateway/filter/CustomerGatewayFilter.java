package com.fungo.gateway.filter;

import com.fungo.gateway.common.AbstractResultEnum;
import com.fungo.gateway.config.NacosFungoCircleConfig;
import com.fungo.gateway.utils.RSAEncryptUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RefreshScope
public class CustomerGatewayFilter implements GatewayFilter, Ordered {


    private static final Logger LOGGER = LoggerFactory.getLogger(FungoGatewayFilter.class);

    private static final String COUNT_START_TIME = "countStartTime";

    @Value( value = "${fungo.cloud.rsa.publicKey}")
    public String rsaPublicKey;

    @Value( value = "${fungo.cloud.rsa.privateKey}")
    public String rsaPrivateKey;

    @Value( value = "${fungo.cloud.festival.days}")
    public int festivalDays;

    @Value( value = "${fungo.cloud.rsa.keyString}")
    public String keyString;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        try {

            String private_key =  rsaPrivateKey ;
            String rsaString = keyString ; //nacosFungoCircleConfig.getKeyString();
            List<String> ecc = request.getHeaders().get("ecc");
            if(ecc.size()> 0 && !"".equals(ecc.get(0))){
                String pravite = RSAEncryptUtils.encrypt(rsaString,rsaPublicKey);
                String newPath = RSAEncryptUtils.decrypt(ecc.get(0), private_key);
                if(newPath.equals(rsaString)){
                    return chain.filter(exchange.mutate().build());
                }else {
                    return  unusualDeal(exchange,response);
                }
            }
        }catch (Exception e){
            return  unusualDeal(exchange,response);
        }
        return chain.filter(exchange.mutate().build());
    }

    @Override
    public int getOrder() {
        return -100;
    }


    private Mono<Void> unusualDeal(ServerWebExchange exchange, ServerHttpResponse response){
        response.setStatusCode( HttpStatus.UNAUTHORIZED);
        String reponseText =  "{\"status\":\"1\",\"code\":\" "+ AbstractResultEnum.CODE_CLOUD_RSA_AUTHORITY.getKey() +" \",\"msg\":\" "+ AbstractResultEnum.CODE_CLOUD_RSA_AUTHORITY.getFailevalue()+"\"}";
        byte[] bytes =reponseText.getBytes( StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith( Flux.just(buffer));
    }

}
