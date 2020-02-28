package com.fungo.system.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.entity.BasNotice;
import com.fungo.system.service.BasNoticeService;
import com.game.common.consts.UserMessageActionTypeConstant;
import com.game.common.consts.UserMessageTypeConstant;
import com.game.common.dto.MemberUserProfile;
import com.game.common.util.CommonUtils;
import com.game.common.util.date.DateTools;
import com.google.gson.JsonObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.game.common.consts.MessageConstants.SYSTEM_USER_GAME_UPDATE;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2020/2/28
 */
@Aspect
@Component
public class MemberNoticeAspect {

    private static Logger logger = LoggerFactory.getLogger(MemberNoticeAspect.class);

    @Autowired
    private BasNoticeService noticeService;

    @Pointcut("execution(public * com.fungo.system.controller.MemberNoticeController.bindThirdSNSWithLogged(..))" )
    public void webLog() {
    }

    @Before("webLog()")
    public void afterReturning (JoinPoint joinPoint) {
        try {
            String jsonString = JSONObject.toJSONString(joinPoint.getArgs()[1]);
            JSONObject jsonObject = JSON.parseObject( jsonString );
            JSONArray jsonArray = jsonObject.getJSONArray( "gameInfo");
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            MemberUserProfile member = (MemberUserProfile)request.getAttribute("member");
            if(member !=null){
                List<BasNotice> basNotices = noticeService.selectList(new EntityWrapper<BasNotice>().eq("is_read", 0).eq("member_id", member.getLoginId()).eq("type", 61));
                basNotices.stream().forEach( s ->{
                    String basNoticeString = s.getData();
                    JSONObject dataObject = JSON.parseObject( basNoticeString);
                    String gamePackageName = (String) dataObject.get("gamePackageName");
                    String gameVersion = (String) dataObject.get("gameVersion");
                    jsonArray.stream().forEach( x ->{
                        JSONObject jsonObj = (JSONObject) x;
                        String webGamePackageName = (String) jsonObj.get("gamePackageName");
                        String webGameVersion = (String) jsonObj.get("gameVersion");
                        String gameName = (String) jsonObj.get( "gameName" );
                        if(webGamePackageName.equals( gamePackageName)){
                            if(CommonUtils.isNewVersion(webGameVersion,gameVersion)){
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
                                dataMap.put("targetId","");
                                dataMap.put("userId", "0b8aba833f934452992a972b60e9ad10");
                                dataMap.put("userType", "1");
                                dataMap.put("userAvatar", "http://output-mingbo.oss-cn-beijing.aliyuncs.com/official.png");
                                dataMap.put("userName", "FunGo大助手");
                                dataMap.put("msgTime", DateTools.fmtDate(new Date()));
                                basNotice.setData( JSON.toJSONString( dataMap));
                                basNotice.insert();
                            }
                        }
                    });
                });
            }
        } catch (Exception e) {
            logger.error("浏览切面error______________________",e);
        }
    }
}
