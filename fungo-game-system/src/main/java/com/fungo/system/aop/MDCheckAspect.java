package com.fungo.system.aop;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.entity.IncentRanked;
import com.fungo.system.entity.IncentRuleRank;
import com.fungo.system.entity.IncentRuleRankGroup;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.MemberUserProfile;
import com.game.common.enums.CommonEnum;
import com.game.common.util.annotation.LogicCheck;
import com.game.common.util.annotation.MD5;
import com.game.common.util.exception.CommonException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

@Order(1)
@Aspect
@Component
public class MDCheckAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger( MDCheckAspect.class);

    @Pointcut("execution(* com.fungo.system.controller..*.*(..)) && @annotation(com.game.common.util.annotation.MD5)")
    public void before(){
    }

    @Before("before()")
    public void requestLogicCheck(JoinPoint joinPoint) throws Exception {
        MD5 limit = this.getAnnotation(joinPoint);
        if(limit == null) {
            return;
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
    }

    /**
     *
     * @Description: 获得注解
     * @param joinPoint
     * @return
     * @throws Exception
     *
     * @author leechenxiang
     * @date 2016年12月14日 下午9:55:32
     */
    private MD5 getAnnotation(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation( MD5.class);
        }
        return null;
    }

}
