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

    public void pushControllerNotice() {

        if (executeState == 1) {
            return;
        }
        setExecuteState(1);
        //获取所有连接
        //获取未推送消息
        List<HashMap<String, Object>> notices = noticeDao.getUnpushNotices();
        if (notices != null && notices.size() > 0) {
            for (HashMap<String, Object> map : notices) {//查找用户连接
                String memberId = (String) map.get("member_id");
                List<LinkSession> linkList = LocalLinkSessionPoolsImpl.getInstance().queryByUserId(memberId);
                for (LinkSession link : linkList) {//有连接 推送
                    String appVersion = link.getAppversion();
                    try {
                        pushService.push(memberId, -1, appVersion);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        setExecuteState(2);
    }

    public static void setExecuteState(int executeState) {
        PushFunction.executeState = executeState;
    }


}
