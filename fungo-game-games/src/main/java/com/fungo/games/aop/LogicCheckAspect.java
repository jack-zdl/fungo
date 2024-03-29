package com.fungo.games.aop;

import com.fungo.games.feign.SystemFeignClient;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.enums.CommonEnum;
import com.game.common.util.annotation.LogicCheck;
import com.game.common.util.exception.BusinessException;
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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private SystemFeignClient systemFeignClient;

    @Pointcut("execution(* com.fungo.games.controller.*.*.*(..)) && @annotation(com.game.common.util.annotation.LogicCheck)")
    public void before(){
    }

    @Before("before()")
    public void requestLogicCheck(JoinPoint joinPoint) throws Exception {
            LogicCheck limit = this.getAnnotation(joinPoint);
            if(limit == null) {
                return;
            }
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String[] loginc = limit.loginc();
            Arrays.stream( loginc ).forEach( s -> {
               if(LogicCheck.LogicEnum.BANNED_TEXT.getKey().equals( s )){
                    List<String> memberIds = new ArrayList<>(  );
                    MemberUserProfile member = (MemberUserProfile)request.getAttribute("member");
                    if(member !=null){
                        memberIds.add( member.getLoginId());
                    }
                    ResultDto<List<MemberDto>> resultDto = systemFeignClient.listMembersByids(memberIds,null);
                    if(resultDto != null && resultDto.getData() != null){
                        List<MemberDto> memberDtos = resultDto.getData();
                        MemberDto memberDto = memberDtos.get( 0);
                        if( 1 == memberDto.getAuth()){
                            throw new BusinessException( CommonEnum.POST_UNACCESSRULE);
                        }
                    }
                }
            });
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
