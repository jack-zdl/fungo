package com.game.common.utils.annotation;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})//此注解作用于属性域上
public @interface EnumConver {

    Class enumType();//定义使用的枚举类型
}
