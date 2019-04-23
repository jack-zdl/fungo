package com.game.common.util;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ZhangGang on 2017/9/26.
 */
public class ReflectUtils {

    public static Set<Field> getFieldsByClass(Class cls) {
        Set<Field> fieldSet = new HashSet<>();
        for (Class<?> clazz = cls; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            if (CommonUtil.isNull(fields)) {
                continue;
            }
            for (Field field : fields) {
                if (!"class".equals( field.getName() ) && !"serialVersionUID".equals( field.getName() )) {
                    fieldSet.add(field);
                }
            }
        }
        return fieldSet;
    }
}
