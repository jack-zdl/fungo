package com.fungo.system.job;

import com.fungo.system.dao.BasNoticeDao;
import com.fungo.system.service.BasNoticeService;
import com.fungo.system.service.IPushService;
import com.game.common.websocket.LinkSession;
import com.game.common.websocket.LocalLinkSessionPoolsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class PushFunction {

    @Autowired
    private BasNoticeService basNoticeService;
    @Autowired
    private IPushService pushService;
    @Autowired
    private BasNoticeDao noticeDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(PushFunction.class);

    //执行状态 0 未执行 1执行中 2已执行
    private static int executeState = 0;

//    @Scheduled(cron = "0 0/2 * * * ?")//每日隔2分钟执行
    public void pushControllerNotice() {

        if (executeState == 1) {
            return;
        }

        setExecuteState(1);
        //获取所有连接
        LOGGER.info("管理台消息推送执行。。。。");

        //获取未推送消息
        List<HashMap<String, Object>> notices = noticeDao.getUnpushNotices();

        if (notices != null && notices.size() > 0) {

            for (HashMap<String, Object> map : notices) {//查找用户连接
                String memberId = (String) map.get("member_id");
                List<LinkSession> linkList = LocalLinkSessionPoolsImpl.getInstance().queryByUserId(memberId);
                for (LinkSession link : linkList) {//有连接 推送
                    String appVersion = link.getAppversion();
                    try {
                        LOGGER.info("检测到有需要推送的消息,开始推送,用户id : {},当前app版本 : {}", memberId, appVersion);
                        pushService.push(memberId, -1, appVersion);
                    } catch (Exception e) {
                        LOGGER.info("推送失败,用户id : {},当前app版本 : {}", memberId, appVersion);
                        e.printStackTrace();
                    }
                }

            }
        }
//		List<LinkSession> linkList = LocalLinkSessionPoolsImpl.getInstance().queryAllLinkSession();
//		
//		setExecuteState(1);
//		for(LinkSession link : linkList) {
//			
//			
//			String userId = link.getUserId();
//			if(!CommonUtil.isNull(userId)) {//获取需要推送的消息,并且推送
//				int count = basNoticeService.selectCount(new EntityWrapper<BasNotice>().eq("member_id",userId).eq("is_push", 0).eq("type", 6));
//				if(count > 0) {
//					Session session = link.getSession();
//					Map headers = (Map) session.getUserProperties().get("headers");
//					String appVersion = "";
//					Object o = headers.get("appversion");
//					try {
//						if(o != null) {
//							List<String> app = (List<String>) headers.get("appversion");
//							appVersion = app.get(0);
//						}
//						LOGGER.info("检测到有需要推送的消息,开始推送,用户id : {},当前app版本 : {}",userId,appVersion);
//						pushService.push(userId, -1, appVersion);
//					} catch (Exception e) {
//						LOGGER.info("推送失败,用户id : {},当前app版本 : {}",userId,appVersion);
//						e.printStackTrace();
//					}
//				}
//			}
//		}
        setExecuteState(2);

    }

    public static int getExecuteState() {
        return executeState;
    }

    public static void setExecuteState(int executeState) {
        PushFunction.executeState = executeState;
    }


}
