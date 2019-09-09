package com.fungo.system.config;



//import com.fasterxml.jackson.databind.JsonSerializer;
import org.codehaus.jackson.JsonGenerator;
        import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
        import org.codehaus.jackson.map.SerializerProvider;
        import org.codehaus.jackson.map.ser.CustomSerializerFactory;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>
 *     StringTrimmerEditor可以进行处理的是原始的HttpServletRequest里面的getParameter方法，但是如果为Body的内容，
 *     已流的形式进行转化的就不行了，最典型就是json格式。这个里面spring 默认的实现是MappingJackson2XmlHttp-MessageConverter进行实现的，
 *     而进行转化的是Jackson这个json转化的工具类型，我们只要实现自己的objectMapper来进行处理String的时候trim就行了。
 *
 * </p>
 *
 * @Author: dl.zhang
 * @Date: 2019/9/9
 */
@Component("customObjectMapper")
public class CustomObjectMapper extends ObjectMapper  {

    public CustomObjectMapper() {
        CustomSerializerFactory factory = new CustomSerializerFactory();
        factory.addGenericMapping(String.class, new JsonSerializer<String>() {
            @Override
            public void serialize(String o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

            }

//            @Override
//            public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//                jsonGenerator.writeString(s.trim());
//            }
        });
        this.setSerializerFactory(factory);
    }



}
