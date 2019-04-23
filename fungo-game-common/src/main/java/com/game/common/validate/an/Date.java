package com.game.common.validate.an;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Date {
    String format();

    String msg() default "";
}
