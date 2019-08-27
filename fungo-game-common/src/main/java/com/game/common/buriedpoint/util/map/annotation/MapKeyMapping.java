package com.game.common.buriedpoint.util.map.annotation;



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

}