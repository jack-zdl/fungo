package com.fungo.system.config;

import cn.yueshutong.springbootstartercurrentlimiting.handler.CurrentAspectHandler;
import cn.yueshutong.springbootstartercurrentlimiting.method.annotation.CurrentLimiter;
import com.game.common.dto.FungoPageResultDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/8/27
 */
@Component
public class MyAspectHandler implements CurrentAspectHandler {
    @Override
    public Object around(ProceedingJoinPoint pjp, CurrentLimiter rateLimiter) throws Throwable {
        //被注解修饰的方法返回值，慎用！
        //可以结合Controller返回自定义视图
        return FungoPageResultDto.error("-1", "抽取中秋节日礼品失败");
    }
}
