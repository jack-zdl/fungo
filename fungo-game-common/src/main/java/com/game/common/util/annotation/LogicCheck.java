package com.game.common.util.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface LogicCheck {

    String[] loginc() default {};

    public static enum LogicEnum{

        DELETE_POST("DELETE_POST"),
        BANNED_TEXT("BANNED_TEXT"),
        BANNED_AUTH("BANNED_AUTH");

        String key;

        LogicEnum(String k){
            this.key = k;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
