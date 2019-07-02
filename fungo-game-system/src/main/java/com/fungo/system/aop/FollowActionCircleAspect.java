package com.fungo.system.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 关注圈子的行为切面
 */
@SuppressWarnings("all")
@Aspect
@Component
public class FollowActionCircleAspect {

    private static Logger logger = LoggerFactory.getLogger(FollowActionCircleAspect.class);

    @Pointcut("execution(public * com.fungo.system.service.impl.ActionServiceImpl.follow(..))" )
    public void webLog() {
    }

    @AfterReturning("webLog()") //成功后执行
    public void afterReturning (JoinPoint joinPoint) {
        try {
//
        } catch (Exception e) {
            logger.error("浏览切面error______________________");
            e.printStackTrace();
        }

    }


}
