package com.fungo.system.config;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.support.WebBindingInitializer;

/**
 * <p>填写注册方法，来帮助处理前端传递进来的String类型的前后空格</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/9/9
 */
@ControllerAdvice
public class MyStringTrimmerEditor implements WebBindingInitializer {
    @Override
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(String.class,new StringTrimmerEditor(true));
    }
}
