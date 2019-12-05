package com.fungo.community.aop;

import com.fungo.community.config.LogicCheck;
import com.fungo.community.dao.mapper.CmmPostDao;
import com.game.common.enums.AbstractResultEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * <p>逻辑检查</p>
 * @Date: 2019/12/5
 */
@Order(1)
@Aspect
@Component
public class LogicCheckAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger( LogicCheckAspect.class);

    @Autowired
    private CmmPostDao cmmPostDao;

    @Pointcut("execution(* com.fungo.community.controller.*.*(..)) && @annotation(com.fungo.community.config.LogicCheck)")
    public void before(){
    }

    @Before("before()")
    public void requestLogicCheck(JoinPoint joinPoint) throws Exception {
        try {
            LogicCheck limit = this.getAnnotation(joinPoint);
            if(limit == null) {
                return;
            }
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String[] loginc = limit.loginc();
            Arrays.stream( loginc ).forEach( s -> {
                if(LogicCheck.LogicEnum.DELETE_POST.getKey().equals(s)){
                    String uri = request.getRequestURI();
                    String[] uris = uri.split( "/" );
                    String postId = uris[uris.length-1];
                    int postNum = cmmPostDao.getPostNumById( postId);
                    if(postNum == 0){
                        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
                        try {
                            response.sendError( -1, AbstractResultEnum.CODE_CLOUD_NOT_FOUND.getFailevalue() );
                            response.getOutputStream().close();
                        } catch (IOException e) {
                            LOGGER.error( "返回response异常",e );
                        }
                    }
                }
            });
        }catch (Exception e){
            LOGGER.error( "逻辑检查异常",e );
        }
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
    private LogicCheck getAnnotation(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation( LogicCheck.class);
        }
        return null;
    }
}
