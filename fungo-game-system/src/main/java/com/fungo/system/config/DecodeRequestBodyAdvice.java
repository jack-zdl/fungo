package com.fungo.system.config;


import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.game.common.util.annotation.SecurityParameter;
import com.game.common.util.validate.AesEncryptUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

@ControllerAdvice(basePackages = "com.xxx.springboot.demo.controller")
public class DecodeRequestBodyAdvice implements RequestBodyAdvice {

    private static final Logger logger = LoggerFactory.getLogger(DecodeRequestBodyAdvice.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return  false;//methodParameter.hasParameterAnnotation(RequestBody.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        try {
            boolean encode = false;
            if (methodParameter.getMethod().isAnnotationPresent(SecurityParameter.class)) {
                //获取注解配置的包含和去除字段
                SecurityParameter serializedField = methodParameter.getMethodAnnotation(SecurityParameter.class);
                //入参是否需要解密
                encode = serializedField.inDecode();

            }
            if (encode) {
                logger.info("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密");
                return new MyHttpInputMessage(httpInputMessage);
            }else{
                return httpInputMessage;
            }
        } catch (Exception e) {
            logger.error("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密出现异常："+e.getMessage());
            return httpInputMessage;
        }
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }




    class MyHttpInputMessage implements HttpInputMessage {
        private HttpHeaders headers;

        private InputStream body;

        public MyHttpInputMessage(HttpInputMessage inputMessage) throws Exception {
            this.headers = inputMessage.getHeaders();
            this.body = IOUtils.toInputStream(AesEncryptUtils.decrypt(easpString(IOUtils.toString(inputMessage.getBody(), "UTF-8"))), "UTF-8");
        }

        @Override
        public InputStream getBody() throws IOException {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

        /**
         *
         * @param requestData
         * @return
         */
        public String easpString(String requestData){
            if(requestData != null && !requestData.equals("")){
                String s = "{\"requestData\":";
                if(!requestData.startsWith(s)){
                    throw new RuntimeException("参数【requestData】缺失异常！");
                }else{
                    int closeLen = requestData.length()-1;
                    int openLen = "{\"requestData\":".length();
                    String substring = StringUtils.substring(requestData, openLen, closeLen);
                    return substring;
                }
            }
            return "";
        }
    }



}
