package com.game.common.validate.an;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Regex {
    String value();

    String msg() default "";
}
