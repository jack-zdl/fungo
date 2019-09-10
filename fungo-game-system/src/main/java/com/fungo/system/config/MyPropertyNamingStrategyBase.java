package com.fungo.system.config;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import org.apache.commons.lang3.StringUtils;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/9/10
 */
public class MyPropertyNamingStrategyBase{ // extends PropertyNamingStrategy

//    @Override
    public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
        return underscoreName(defaultName);
    }

//    @Override
    public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
        return underscoreName(defaultName);
    }

    private String underscoreName(String name) {
        if (StringUtils.isEmpty(name)) {
            return "";
        }
        return name.trim();
    }
}
