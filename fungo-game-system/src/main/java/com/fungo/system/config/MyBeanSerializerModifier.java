package com.fungo.system.config;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.List;

public class MyBeanSerializerModifier  { //extends BeanSerializerModifier

//    private JsonSerializer<Object> _nullArrayJsonSerializer = new MyNullArrayJsonSerializer();

//    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        // 循环所有的beanPropertyWriter
        for (int i = 0; i < beanProperties.size(); i++) {
            BeanPropertyWriter writer = beanProperties.get(i);

            // 判断字段的类型，如果是array，list，set则注册nullSerializer
            if (isArrayType(writer)) {
                //给writer注册一个自己的nullSerializer
                writer.assignSerializer(this.defaultNullArrayJsonSerializer());

            }
        }
        return beanProperties;
    }



    // 判断是什么类型
    protected boolean isArrayType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getPropertyType();
        return clazz.equals(String.class) ; //clazz.isArray() || clazz.equals(List.class) || clazz.equals(Set.class)

    }

    protected JsonSerializer<Object> defaultNullArrayJsonSerializer() {
        return  null; //_nullArrayJsonSerializer;
    }
}
