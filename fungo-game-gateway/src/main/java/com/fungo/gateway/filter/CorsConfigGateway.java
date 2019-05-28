package com.fungo.gateway.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.DefaultCorsProcessor;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPatternParser;


/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/28
 */
@Configuration
public class CorsConfigGateway {
    @Bean
    public CorsResponseHeaderFilter corsResponseHeaderFilter() {
        return new CorsResponseHeaderFilter();
    }

    @Bean
    public CorsWebFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", buildCorsConfiguration());

        CorsWebFilter corsWebFilter = new CorsWebFilter(source, new DefaultCorsProcessor() {
            @Override
            protected boolean handleInternal(ServerWebExchange exchange, CorsConfiguration config,
                                             boolean preFlightRequest)
            {
                // 预留扩展点
                // if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
                return super.handleInternal(exchange, config, preFlightRequest);
                // }

                // return true;
            }
        });

        return corsWebFilter;
    }

    private CorsConfiguration buildCorsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");

        corsConfiguration.addAllowedMethod(HttpMethod.OPTIONS);
        corsConfiguration.addAllowedMethod(HttpMethod.POST);
        corsConfiguration.addAllowedMethod(HttpMethod.GET);
        corsConfiguration.addAllowedMethod(HttpMethod.PUT);
        corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
        corsConfiguration.addAllowedMethod(HttpMethod.PATCH);
        // corsConfiguration.addAllowedMethod("*");

        corsConfiguration.addAllowedHeader("origin");
        corsConfiguration.addAllowedHeader("content-type");
        corsConfiguration.addAllowedHeader("accept");
        corsConfiguration.addAllowedHeader("x-requested-with");
        corsConfiguration.addAllowedHeader("Referer");
        corsConfiguration.addAllowedHeader(RequestHeaderKeys.USER_AGENT);
        corsConfiguration.addAllowedHeader(RequestHeaderKeys.TOKEN);
        corsConfiguration.addAllowedHeader(RequestHeaderKeys.REFRESH_TOKEN);
        corsConfiguration.addAllowedHeader(RequestHeaderKeys.OS);
        corsConfiguration.addAllowedHeader(RequestHeaderKeys.X_APP_KEY);
        corsConfiguration.addAllowedHeader(RequestHeaderKeys.X_DEVICE_ID);
        corsConfiguration.addAllowedHeader(RequestHeaderKeys.X_TOKEN);
        corsConfiguration.addAllowedHeader(RequestHeaderKeys.UDID);
        corsConfiguration.addAllowedHeader(RequestHeaderKeys.APPVERSION);
        corsConfiguration.addAllowedHeader(RequestHeaderKeys.BRAND);
        corsConfiguration.addAllowedHeader(RequestHeaderKeys.HEIGHT);
        corsConfiguration.addAllowedHeader(RequestHeaderKeys.WIDTH);
        corsConfiguration.addAllowedHeader(RequestHeaderKeys.VERSION);
        corsConfiguration.addAllowedHeader(RequestHeaderKeys.IOSCHANNEL);
        corsConfiguration.addAllowedHeader(RequestHeaderKeys.APP_CHANNEL);
        // corsConfiguration.addAllowedHeader("*");

        corsConfiguration.setMaxAge(7200L);
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }


}

class RequestHeaderKeys{
    public static final String USER_AGENT = "USER_AGENT";
    public static final String TOKEN = "token";
    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    public static final String OS = "os";
    public static final String X_APP_KEY = "X_APP_KEY";
    public static final String X_DEVICE_ID = "X_DEVICE_ID";
    public static final String X_TOKEN = "X_TOKEN";
    public static final String UDID = "udid";
    public static final String APPVERSION = "appVersion";
    public static final String BRAND = "brand";
    public static final String HEIGHT ="height";
    public static final String WIDTH = "width";
    public static final String VERSION = "version";
    public static final String IOSCHANNEL = "iosChannel";
    public static final String  APP_CHANNEL = "app_channel";
}
