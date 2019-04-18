package com.fungo.system.function;

import com.fungo.websocket.api.LinkSession;
import com.fungo.websocket.api.LocalLinkSessionPoolsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            //add
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //--------
}
