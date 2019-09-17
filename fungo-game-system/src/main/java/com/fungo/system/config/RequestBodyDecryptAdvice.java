package com.fungo.system.config;

import com.alibaba.fastjson.JSON;
import com.fungo.system.dto.UserBean;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *     入參解析
 * </p>
 *
 * @Author: dl.zhang
 * @Date: 2019/9/10
 */
@ControllerAdvice
public class RequestBodyDecryptAdvice  extends RequestBodyAdviceAdapter{

    /**
     * 前置拦截匹配操作(定义自己业务相关的拦截匹配规则)
     * 满足为true的才会执行下面的方法
     *
     * @date 2018/10/10 11:43
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;  // type.getTypeName().equals( UserBean.class.getName());
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


    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                  Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        try {
            return new MyHttpInputMessage(inputMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return inputMessage;
        }
    }

    /**
     * 对加密的请求参数，解密
     *
     * @date 2018/10/10 12:55
     */
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        //对加密的请求参数，解密
        return body;
    }
}

class MyHttpInputMessage  implements HttpInputMessage{
    private HttpHeaders headers;
    private InputStream body;

    @SuppressWarnings("unchecked")
    public MyHttpInputMessage(HttpInputMessage inputMessage) throws Exception {
        String string = IOUtils.toString(inputMessage.getBody(), "UTF-8");
        Map<String, Object> mapJson = (Map<String, Object>) JSON.parseObject(string, Map.class);
        Map<String, Object> map = new HashMap<>();
        Set<Map.Entry<String, Object>> entrySet = mapJson.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            String key = entry.getKey();
            Object objValue = entry.getValue();
            if (objValue instanceof String) {
                String value = objValue.toString();
                map.put(key,value.trim() ); //filterDangerString(value)
            }else{
                map.put(key,objValue ); //filterDangerString(value)
            }
//            else { // 针对结合的处理
//                @SuppressWarnings("rawtypes")
//                List<HashMap> parseArray = JSONArray.parseArray(objValue.toString(), HashMap.class);
//                List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
//                for (Map<String, Object> innerMap : parseArray) {
//                    Map<String, Object> childrenMap = new HashMap<String, Object>();
//                    Set<Map.Entry<String, Object>> elseEntrySet = innerMap.entrySet();
//                    for (Map.Entry<String, Object> en : elseEntrySet) {
//                        String innerKey = en.getKey();
//                        Object innerObj = en.getValue();
//                        if (innerObj instanceof String) {
//                            String value = innerObj.toString();
//                            childrenMap.put(innerKey, filterDangerString(value));
//                        }
//                    }
//                    listMap.add(childrenMap);
//                }
//                map.put(key, listMap);
//            }
        }
        this.headers = inputMessage.getHeaders();
        this.body = IOUtils.toInputStream(JSON.toJSONString(map), "UTF-8");
    }

    @Override
    public InputStream getBody() throws IOException {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }

    private String filterDangerString(String value) {
        if (value == null) {
        return null;
        }
        value = value.replaceAll("\\|", "");
        value = value.replaceAll("&", "");
        value = value.replaceAll(";", "");
        value = value.replaceAll("@", "");
        value = value.replaceAll("'", "");
        value = value.replaceAll("\\'", "");
        value = value.replaceAll("<", "");
        value = value.replaceAll("-", "");
        value = value.replaceAll(">", "");
        value = value.replaceAll("\\(", "");
        value = value.replaceAll("\\)", "");
        value = value.replaceAll("\\+", "");
        value = value.replaceAll("\r", "");
        value = value.replaceAll("\n", "");
        value = value.replaceAll("script", "");
        value = value.replaceAll("select", "");
        value = value.replaceAll("\"", "");
        value = value.replaceAll(">", "");
        value = value.replaceAll("<", "");
        value = value.replaceAll("=", "");
        value = value.replaceAll("/", "");
        return value;
        }
}
