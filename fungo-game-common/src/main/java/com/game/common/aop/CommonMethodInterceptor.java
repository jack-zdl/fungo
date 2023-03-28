package com.game.common.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author zhangdelei
 * @description
 * @date 2023/3/28 11:09
 */
public class CommonMethodInterceptor implements MethodInterceptor {

    /**
     * @Description: 针对方法界别的拦截器,一般我自己使用入参检查,出参自动映射
     * @Author: zhangdelei  2023/3/28 11:09
     **/
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        return null;
    }
}
