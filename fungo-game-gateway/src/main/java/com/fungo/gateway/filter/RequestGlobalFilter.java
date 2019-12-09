package com.fungo.gateway.filter;

import com.fungo.gateway.common.AbstractResultEnum;
import com.fungo.gateway.config.NacosFungoCircleConfig;
import com.fungo.gateway.utils.RSAEncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
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

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/11/20
 */
@Component
public class RequestGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    NacosFungoCircleConfig nacosFungoCircleConfig;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String private_key = nacosFungoCircleConfig.getRsaPrivateKey(); // "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKm339wxgrja8pOtNBnFeVonjVIR99h2bwxx1pzTITgwoSihwKG7MJamjyAZSyZovrbSt+aAQ6zGpvyy5hxPE4aW3BamGLnAFAmgghbywnHt1mlTZqfG24Owyw1AWWwnJBm8Dx+s2t8Pp7PJO/2SGspsEWYgCT2hX26bZ19d+fxvAgMBAAECgYBZYchY/A4/crf8hGSDChg+HcR0q5fe/YjAghen4h2FrT7C2mVMB/yv6qpIGmoUoWeHe3R0xapejQSW41UDJbY+9iL5Dsj1bmQikRW2RcCji8Z5mqy612QMtVpgGFu1kMypNhXa5yGHxJRMgvXNbVLDCs+fQf419VgKGfzYSIOvkQJBAPWXj2H8U+TeDr1QM1geoBlMoBzDKDRp/vJH30V/Cx1k7UMYhgBT8nvYn39un0i9Zybe/dgzVcwtLeF2M2W51XcCQQCw6Sf3StHWuCZbdeS8RQFNxR6kXyVwYK5Z1wibdq/VTIzho8vX0xzrxllD29u+zDuse2XuqB7TgzyJFhWgei7JAkEAqSYz+RbjaVSP7k/YrS6ashIWjPjVLhR7Jb/exWzU6O/kgjvXv04SSr5OTGSSjHT3IMyhJVFmHHRZj1nr2Hp/zQJAUAjbRNw7fImJCN2YIriRM27XQwOse1+x4QEQaszSFYxxO++PW8+tiNkOL0365yGcaZbV8ZdrgsMfkuFD3XsJSQJARrieh2TwBwjdw2dJz1hpU/miSSfcmm1JXivaJgDl9Xq9f4QmIkBc4UEnRVv9yi6W/6/mWZdiVdQcY+LCBTF01Q==";

                //  request.getHeaders().get( "public_key" );
        // 获取请求路径前缀（api-dev/api-prod）
        String url = request.getPath().value();
        String newPath = "";
        if(url.contains( "/api/encrypt" )){
            String[] pathList = url.split( "encrypt/" );
            try {
               String key =  private_key ;
                newPath = RSAEncryptUtils.decrypt(pathList[1], key);
                String[] newpathList = newPath.split( "/encrypt" );
                long newDate = System.currentTimeMillis();
                if(newDate < Long.valueOf( newpathList[0] )){
                    newPath = newpathList[1];
                }else {
                    return  unusualDeal(exchange,response);
                }
                newPath = "/api/"+newPath;
                ServerHttpRequest newRequest = request.mutate().path(newPath).build();
                return chain.filter(exchange.mutate().request(newRequest).build());  //.request(newRequest)
            } catch (Exception e) {
                return  unusualDeal(exchange,response);
            }
        } else {
            return chain.filter(exchange.mutate().build());
        }
    }

    private Mono<Void> unusualDeal(ServerWebExchange exchange, ServerHttpResponse response){
        response.setStatusCode( HttpStatus.UNAUTHORIZED);
        String reponseText =  "{\"status\":\"1\",\"code\":\" "+ AbstractResultEnum.CODE_CLOUD_RSA_AUTHORITY.getKey() +" \",\"msg\":\" "+ AbstractResultEnum.CODE_CLOUD_RSA_AUTHORITY.getFailevalue()+"\"}";
        byte[] bytes =reponseText.getBytes( StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith( Flux.just(buffer));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
