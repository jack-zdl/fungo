package com.game.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.common.dto.UserProfile;
import com.game.common.enums.CommonEnum;
import com.game.common.util.annotation.Anonymous;
import com.game.common.util.exception.BusinessException;
import com.game.common.util.token.Constant;
import com.game.common.util.token.TokenService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sam
 */
@Configuration
@Order(2)
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private MyThreadLocal myThreadLocal;

    //跨域
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowCredentials(true).allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600);
    }


    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        List<String> list = new ArrayList<>();
        list.add("*");
        corsConfiguration.setAllowedOrigins(list);
        /*
         请求常用的三种配置，*代表允许所有，当时你也可以自定义属性（比如header只能带什么，只能是post方式等等）
        */
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

    //-----------------------------------------------------------------------------------------------------------------



    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.ignoreAcceptHeader(true);
        configurer.favorParameter(true);
        configurer.favorPathExtension(true);
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
        Map<String, MediaType> mediaTypes = new HashMap<>(2);
        mediaTypes.put("json", MediaType.APPLICATION_JSON);
        mediaTypes.put("xml", MediaType.APPLICATION_XML);
        configurer.mediaTypes(mediaTypes);
    }

    /**
     * 设置添加方法参数支持
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new UserProfileArgumentResolver(this.tokenService));
    }

}

class UserProfileArgumentResolver implements HandlerMethodArgumentResolver {
    private TokenService tokenService;


    public UserProfileArgumentResolver(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    private static final Logger logger = LoggerFactory.getLogger(UserProfileArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clz = parameter.getParameterType();
        if (UserProfile.class.isAssignableFrom(clz)) {
            return true;
        }
        return false;
    }


    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader(Constant.requestTokenParamName);
        MyThreadLocal.setToken(token);
        if (token == null || "".equals(token.trim()) || token.split("\\.").length != 3) {
            logger.info("请求头中未包含token");
            if (!parameter.hasParameterAnnotation(Anonymous.class)) {  //针对部分接口即支持会员访问又支持匿名访问拦截处理
                throw new BusinessException(CommonEnum.LOGIN_TIMEOUT);
            } else {
                return null;
            }
        } else {
            try {
                long start = System.currentTimeMillis();
                Claims parseJWT = tokenService.parseJWT(token);
                String subject = parseJWT.getSubject();
                ObjectMapper objectMapper = new ObjectMapper();
                Object readValue = objectMapper.readValue(subject, parameter.getParameterType());

                logger.info("成功解析token，耗时：" + (System.currentTimeMillis() - start));

                HttpServletRequest nativeRequest = (HttpServletRequest) webRequest.getNativeRequest();
                nativeRequest.setAttribute("member", readValue);
                return readValue;
            } catch (Exception e) {
                logger.error("解析token异常:",e);
                throw new BusinessException(CommonEnum.LOGIN_TIMEOUT);
            }
        }
    }

}
