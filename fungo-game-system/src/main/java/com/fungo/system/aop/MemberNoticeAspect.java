package com.fungo.system.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.dto.MemberNoticeInput;
import com.fungo.system.entity.BasNotice;
import com.fungo.system.feign.GamesFeignClient;
import com.fungo.system.service.BasNoticeService;
import com.game.common.consts.UserMessageActionTypeConstant;
import com.game.common.consts.UserMessageTypeConstant;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.game.BangGameDto;
import com.game.common.dto.game.GameOutBean;
import com.game.common.util.CommonUtils;
import com.game.common.util.date.DateTools;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import static com.game.common.consts.MessageConstants.SYSTEM_USER_GAME_UPDATE;

/**
 * <p></p>
 * @Author: dl.zhang
 * @Date: 2020/2/28
 */
@Order(1)
@Aspect
@Component
public class MemberNoticeAspect {

    private static Logger logger = LoggerFactory.getLogger(MemberNoticeAspect.class);

    @Autowired
    private BasNoticeService noticeService;

    @Autowired
    private GamesFeignClient gamesFeignClient;


    @Pointcut("execution(public * com.fungo.system.controller.MemberNoticeController.bindThirdSNSWithLogged(..))" )
    public void webLog() {
    }

    @Around("webLog()")
    public Object arround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        result = joinPoint.proceed();
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof MemberNoticeInput) {
                List<Map<String,String>> mapList = ((MemberNoticeInput) arg).getGameInfo();
                if(mapList == null || mapList.size() == 0) return result;
                BangGameDto bangGameDto = new BangGameDto();
                bangGameDto.setGameInfo(mapList );
                FungoPageResultDto<GameOutBean>  gameOutBeanFungoPageResultDto = gamesFeignClient.listGameByPackageName(bangGameDto);
                if(gameOutBeanFungoPageResultDto == null) return result;
                List<GameOutBean>  gameOutBeans = gameOutBeanFungoPageResultDto.getData();
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = attributes.getRequest();
                MemberUserProfile member = (MemberUserProfile)request.getAttribute("member");
                if(member !=null){
                    List<BasNotice> basNotices = noticeService.selectList(new EntityWrapper<BasNotice>().eq("is_read", 0).eq("member_id", member.getLoginId()).eq("type", 61));
                    gameOutBeans.stream().filter( c ->{
                        for(BasNotice basNotice :basNotices){
                            String basNoticeString = basNotice.getData();
                            JSONObject dataObject = JSON.parseObject( basNoticeString);
                            String gamePackageName = (String) dataObject.get("gamePackageName");
                            String gameVersion = (String) dataObject.get("gameVersion");
                            if(c.getGamePackageName().equals( gamePackageName)){
                                if(CommonUtils.isNewVersion(c.getGameVersion(),gameVersion)) {
                                    return true;
                                }else {
                                    return false;
                                }
                            }
                        }
                        return true;
                    } ).forEach( x ->{
                        String gameId =  x.getGameId();
                        String gameName =  x.getName();
                        BasNotice basNotice = new BasNotice();
                        basNotice.setType( 6 );
                        basNotice.setChannel( "");
                        basNotice.setIsRead( 0 );
                        basNotice.setIsPush( 0 );
                        basNotice.setMemberId( member.getLoginId());
                        basNotice.setCreatedAt( new Date() );
                        Map<String,String> dataMap = new HashMap<>();
                        // 3标志 不做跳转
                        dataMap.put("actionType",String.valueOf(  UserMessageActionTypeConstant.ACTIONTYPE_ONE ));
                        dataMap.put("content",SYSTEM_USER_GAME_UPDATE.replace("{", gameName) );
                        dataMap.put("targetType", String.valueOf( UserMessageTypeConstant.USER_REPLY_GAME));
                        dataMap.put("targetId",gameId);
                        dataMap.put("userId", "0b8aba833f934452992a972b60e9ad10");
                        dataMap.put("userType", "1");
                        dataMap.put("userAvatar", "http://output-mingbo.oss-cn-beijing.aliyuncs.com/official.png");
                        dataMap.put("userName", "FunGo大助手");
                        dataMap.put("msgTime", DateTools.fmtDate(new Date()));
                        basNotice.setData( JSON.toJSONString( dataMap));
                        basNotice.insert();
                    });
                }
            }
        }
        return result;
    }
}
