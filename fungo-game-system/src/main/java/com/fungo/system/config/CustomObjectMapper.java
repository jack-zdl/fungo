package com.fungo.system.config;



import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;


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
//@Component("customObjectMapper")
public class CustomObjectMapper   { //extends ObjectMapper

    public CustomObjectMapper() {
//        CustomSerializerFactory factory = new CustomSerializerFactory();
//        factory.addGenericMapping(String.class, new JsonSerializer<String>() {
//            @Override
//            public void serialize(String o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
//
//            }
//
////            @Override
////            public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
////                jsonGenerator.writeString(s.trim());
////            }
//        });
//        this.setSerializerFactory(factory);
        SimpleModule se = new SimpleModule();
        se.addSerializer(String.class, new ObjectIdSerializer());
//        se.addDeserializer(Document.class, new DocumentDeserializer());
//        this.registerModule(se);
    }


    private class ObjectIdSerializer extends JsonSerializer<String> {
//        @Override
//        public void serialize(ObjectId arg0, JsonGenerator arg1, SerializerProvider arg2)
//                throws IOException, JsonProcessingException {
//
//            System.out.println("序列化，进来了。");
//            System.out.println(arg1.toString());
//
//            if(arg0 == null) {
//                arg1.writeNull();
//            } else {
//                arg1.writeString(arg0.toString());
//            }
//        }

        @Override
        public void serialize(String objectId, com.fasterxml.jackson.core.JsonGenerator jsonGenerator, com.fasterxml.jackson.databind.SerializerProvider serializerProvider) throws IOException {

            System.out.println("序列化，进来了。");
            System.out.println(jsonGenerator.toString());

            if(objectId == null) {
//                objectId.();
            } else {
                jsonGenerator.writeString(objectId.toString().trim());
            }
        }
    }

//    private class DocumentDeserializer extends JsonDeserializer<Document> {
//        public Document deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
//
//            System.out.println("Document反序列化进来了");
//            JsonNode node = p.getCodec().readTree(p);
//            String docstr = node.toString();
//            String oid = node.get("_id").asText();
//
//            System.out.println("node.toString--->"+ docstr);
//
//            docstr = docstr.replace("\""+oid+"\"","{ \"$oid\" : \""+oid+"\" }");
//
//            System.out.println("转换后的node.toString--->"+docstr);
//
//            return Document.parse(docstr);
//
//        }
//
//    }


}
