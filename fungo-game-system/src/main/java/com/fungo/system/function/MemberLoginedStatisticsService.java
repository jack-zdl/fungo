package com.fungo.system.function;

import com.game.common.dto.AbstractEventDto;
import com.game.common.websocket.LinkSession;
import com.game.common.websocket.LocalLinkSessionPoolsImpl;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * <p>
 * <p>
 *      已登录用户统计业务类
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@Component
public class MemberLoginedStatisticsService {

    private static final Logger logger = LoggerFactory.getLogger(MemberLoginedStatisticsService.class);

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    /**
     * 添加登录用户到在线人数统计
     * @param mb_id
     * @param appVersion
     */
    public void addLoginToBucket(String mb_id, String appVersion) {

        try {


            LinkSession link = new LinkSession();
            link.setUserId(mb_id);
            link.setAppversion(appVersion);
//            //add
            LocalLinkSessionPoolsImpl.getInstance().addChannel(link);
            int mbCount = LocalLinkSessionPoolsImpl.getInstance().size();
            logger.info("****************************有新用户登录mb_id:{}----当前在线人数：{}", mb_id, mbCount);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 删除登出用户从在线人数统计
     * @param mb_id
     */
    public void removeLogoutFromBucket(String mb_id) {
        try {
            LocalLinkSessionPoolsImpl.getInstance().removeChannel(mb_id);
            int mbCount = LocalLinkSessionPoolsImpl.getInstance().size();
            logger.info("****************************有用户登出mb_id:{}----当前在线人数：{}", mb_id, mbCount);
            AbstractEventDto abstractEventDto = new AbstractEventDto(this);
            abstractEventDto.setEventType( AbstractEventDto.AbstractEventEnum.USER_LOGOUT.getKey());
            applicationEventPublisher.publishEvent(abstractEventDto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //--------
}
