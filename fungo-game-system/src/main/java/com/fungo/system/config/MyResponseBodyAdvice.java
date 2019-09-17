package com.fungo.system.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/9/17
 */
@Slf4j
@ControllerAdvice
public class MyResponseBodyAdvice implements ResponseBodyAdvice {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object object, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (null != object) {
            try {
                Map map = objectMapper.readValue(objectMapper.writeValueAsString(object), Map.class);
                Set<Map.Entry<String, Object>> entrySet = map.entrySet();
                for (Map.Entry<String, Object> entry : entrySet) {
                    String key = entry.getKey();
                    Object objValue = entry.getValue();
                    if (objValue instanceof String) {
                        String value = objValue.toString();
                        map.put( key, value.trim() ); //filterDangerString(value)
                    } else {
                        map.put( key, objValue ); //filterDangerString(value)
                    }
                }
                return  map;
            } catch (IOException e) {
                log.error("加密数据失败.", e);
            }
        }
        return object;
    }
}
