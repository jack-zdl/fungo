package com.fungo.system.config;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

/**
 * <p>
 *     入參解析
 * </p>
 *
 * @Author: dl.zhang
 * @Date: 2019/9/10
 */
//@ControllerAdvice
public class RequestBodyDecryptAdvice  { //extends RequestBodyAdviceAdapter

    /**
     * 前置拦截匹配操作(定义自己业务相关的拦截匹配规则)
     * 满足为true的才会执行下面的方法
     *
     * @date 2018/10/10 11:43
     */
//    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> converterType) {
        return  StringHttpMessageConverter.class.isAssignableFrom(converterType);
    }

    /**
     * 对加密的请求参数，解密
     *
     * @date 2018/10/10 12:55
     */
//    @Override
//    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
//                                Class<? extends HttpMessageConverter<?>> converterType) {
//        //对加密的请求参数，解密
//        String jsonStrDecrypt = AESUtil.AES_Decrypt(BaseConstant.AES_KEY, String.valueOf(body));
//        System.out.println("对加密的请求参数，解密："+ jsonStrDecrypt);
//        return jsonStrDecrypt;
//    }


    /**
     * 对加密的请求参数，解密
     *
     * @date 2018/10/10 12:55
     */
//    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        //对加密的请求参数，解密
        return String.valueOf(body).trim();
    }
}
