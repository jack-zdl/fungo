package com.fungo.community.aop;

import com.fungo.community.dao.mapper.CmmCircleMapper;
import com.fungo.community.dao.mapper.CmmPostDao;
import com.fungo.community.entity.CmmCircle;
import com.fungo.community.feign.SystemFeignClient;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.PostInput;
import com.game.common.dto.user.MemberDto;
import com.game.common.enums.AbstractResultEnum;
import com.game.common.enums.CommonEnum;
import com.game.common.util.CommonUtil;
import com.game.common.util.annotation.LogicCheck;
import com.game.common.util.exception.BusinessException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
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
import java.util.*;

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
    @Autowired
    private SystemFeignClient systemFeignClient;
    @Autowired
    private CmmCircleMapper cmmCircleMapper;

    @Pointcut("execution(*  com.fungo.community.controller..*.*(..)) && @annotation(com.game.common.util.annotation.LogicCheck)")
    public void before(){
    }

    @Before("before()")
    public void requestLogicCheck(JoinPoint joinPoint) throws Exception {
            LogicCheck limit = this.getAnnotation(joinPoint);
            if(limit == null) {
                return;
            }
        Map<String, Object> param = new HashMap<>();

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
                }else if(LogicCheck.LogicEnum.BANNED_TEXT.getKey().equals( s )){
                    List<String> memberIds = new ArrayList<>(  );
                    MemberUserProfile member = (MemberUserProfile)request.getAttribute("member");
                    if(member !=null){
                        memberIds.add( member.getLoginId());
                    }
                    ResultDto<List<MemberDto>> resultDto = systemFeignClient.listMembersByids(memberIds,null);
                    if(resultDto != null && resultDto.getData() != null){
                        List<MemberDto> memberDtos = resultDto.getData();
                        MemberDto memberDto = memberDtos.get( 0);
                        if( 2 == memberDto.getAuth()){
                            throw new BusinessException( CommonEnum.UNACCESSRULE);
                        }
                    }
                }else if(LogicCheck.LogicEnum.BANNED_AUTH.getKey().equals( s)){
                    MemberUserProfile member = (MemberUserProfile)request.getAttribute("member");
                    Object[] paramValues = joinPoint.getArgs();
                    String[] paramNames = ((CodeSignature)joinPoint.getSignature()).getParameterNames();
                    for (int i = 0; i < paramNames.length; i++) {
                        param.put(paramNames[i], paramValues[i]);
                    }
                    PostInput postInput = (PostInput) param.get( "postInput" );
                    String postId = postInput.getPostId();
                    if(member ==null){
                        throw new BusinessException( CommonEnum.UNACCESSRULE);
                    }
                    ResultDto<List<MemberDto>> resultDto = systemFeignClient.listMembersByids( Collections.singletonList( member.getLoginId() ),null);
                    if(resultDto != null && resultDto.getData() != null){
                        List<MemberDto> memberDtos = resultDto.getData();
                        MemberDto memberDto = memberDtos.get( 0);
                        if(!CommonUtil.isNull( memberDto.getCircleId() )){
                            List<CmmCircle>  cmmCircles  =  cmmCircleMapper.selectCircleByPostId( postId);
                            if(!(cmmCircles != null && cmmCircles.size()>0 && memberDto.getCircleId().equals(cmmCircles.get( 0 ).getId()))){
                                throw new BusinessException( CommonEnum.UNACCESSRULE);
                            }
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
