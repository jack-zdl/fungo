package com.game.common.util.map;



import java.lang.annotation.*;

@Documented  
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.FIELD)
/**
 *  bean字段注解，对应map key
 */
public @interface MapKeyMapping {
    /**
     * bean属性对应的 map key 名称
     */
    String value() default "";

    /**
     * 忽略该属性的转化
     */
    boolean ignore() default false;
}