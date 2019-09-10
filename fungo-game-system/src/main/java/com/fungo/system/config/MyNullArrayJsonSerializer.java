package com.fungo.system.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MyNullArrayJsonSerializer {  //extends JsonSerializer<Object>

//    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        if(value.getClass().equals(String.class)){
            jgen.writeObject(stringTram(value));

        }
//        if (value == null) {
//            jgen.writeStartArray();
//            jgen.writeEndArray();
//        } else {
//            jgen.writeObject(value);
//        }
    }

    private String stringTram(Object value){
        String valueString = (String) value;
        return valueString.trim();
    }
}
