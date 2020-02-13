package com.fungo.system.aop;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fungo.system.config.CuratorConfiguration;
import com.fungo.system.config.NacosFungoCircleConfig;
import com.game.common.enums.AbstractResultEnum;
import com.game.common.util.SecurityMD5;
import com.game.common.util.annotation.LogicCheck;
import com.game.common.util.annotation.MD5ParanCheck;
import com.game.common.util.date.DateTools;
import lombok.val;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

@Order(1)
@Aspect
@Component
public class MD5CheckParamAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger( MD5CheckParamAspect.class);

    @Resource
    private MappingJackson2HttpMessageConverter converter;
    @Resource
    private NacosFungoCircleConfig nacosFungoCircleConfig;


    @Pointcut("execution(* com.fungo.system.controller.*.*(..)) && @annotation(com.game.common.util.annotation.MD5ParanCheck)")
    public void before(){
    }


    @Before("before()")
    public void requestLimit(JoinPoint joinPoint) throws Exception {
        try {
            // 获取HttpRequest
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            MD5ParanCheck limit = this.getAnnotation(joinPoint);
            if(limit == null ) {
               return;
            }
            String sign = request.getHeader( "sign" );
            if(sign == null || "".equals( sign )){
                HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
                HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
                response.sendError( -1,AbstractResultEnum.CODE_CLOUD_MD5_AUTHORITY.getFailevalue() );
                converter.write(response, MediaType.APPLICATION_JSON, outputMessage);
                response.getOutputStream().close();
            }
            //重点 这里就是获取@RequestBody参数的关键  调试的情况下 可以看到o变量已经获取到了请求的参数
//            LOGGER.info("REQUEST：" + JSONObject.toJSONString(joinPoint.getArgs()[1]));
            String jsonString = JSONObject.toJSONString(joinPoint.getArgs()[1]);
            JSONObject jsonObject = JSON.parseObject( jsonString );
            String[] paramString =  limit.param();
            StringBuffer stringBuffer = new StringBuffer("");
            Arrays.stream( paramString ).forEach( s -> {
                String checkParam = jsonObject.getString( s);
                stringBuffer.append( "&"+s+"="+checkParam );
            });
            stringBuffer.append("&time="+ DateTools.getAddMinute(1));
            String result = SecurityMD5.MD5( URLEncoder.encode(stringBuffer.toString(), "utf-8"));
            if(result != null && result.equals(sign)){
//                return true;
            }else {
                HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
                HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
                response.sendError( -1,AbstractResultEnum.CODE_CLOUD_MD5_AUTHORITY.getFailevalue() );
                converter.write(response, MediaType.APPLICATION_JSON, outputMessage);
                response.getOutputStream().close();
            }
        } catch (Exception e) {
            LOGGER.error( "-----------------" );
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
    private MD5ParanCheck getAnnotation(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation( MD5ParanCheck.class);
        }
        return null;
    }



}
