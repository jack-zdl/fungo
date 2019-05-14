package com.game.common.cache;


import com.game.common.util.FunGoEHCacheUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;


/**
 * <p>
 *       删除缓存执行AOP切面
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@Aspect
@Component
public class FunGoCacheRemoveAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(FunGoCacheRemoveAspect.class);

    /**
     * 拦截标有@CacheRemove的方法
     */
    @Pointcut(value = "(execution(* *.*(..)) && @annotation(com.game.common.cache.FunGoCacheRemove))")
    private void pointcut() {
        LOGGER.info("----拦截到标有@CacheRemove的方法");
    }


    @AfterReturning(value = "pointcut()")
    private void process(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        FunGoCacheRemove cacheRemove = method.getAnnotation(FunGoCacheRemove.class);

        if (cacheRemove != null) {
            String value = cacheRemove.value();
            //需要移除的正则key
            String[] keys = cacheRemove.key();

            LOGGER.info("value:{}-----key:{}",value,keys );

            List cacheKeys = FunGoEHCacheUtils.cacheKeys(value);

            LOGGER.info("value:{}-----key:{}---cacheKeys:{}",value,keys ,cacheKeys);
            for (String key : keys) {

                Pattern pattern = Pattern.compile(key);

                for (Object cacheKey : cacheKeys) {

                    String cacheKeyStr = String.valueOf(cacheKey);

                    if (pattern.matcher(cacheKeyStr).find()) {

                        FunGoEHCacheUtils.remove(value, cacheKeyStr);

                    }
                }
            }
        }
    }


}
