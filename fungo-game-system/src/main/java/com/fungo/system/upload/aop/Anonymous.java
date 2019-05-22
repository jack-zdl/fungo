package com.fungo.system.upload.aop;

import java.lang.annotation.*;

/**
 * 匿名处理
 * @author scm
 *
 */
@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到  
@Target({ElementType.PARAMETER})//定义注解的作用目标**作用范围字段、枚举的常量/方法  
@Documented
public @interface Anonymous {

}
