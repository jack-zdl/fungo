package com.fungo.games.listener.service.impl;

import com.alibaba.fastjson.JSON;
import com.game.common.ts.mq.service.MQDataReceiveService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("mQDataReceiveService")
public class MQDataReceiveServiceImpl implements MQDataReceiveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQDataReceiveServiceImpl.class);


    @Override
    public boolean onMessageWithMQDirect(String msgData) throws Exception {
        if (StringUtils.isBlank(msgData)) {
            return true;
        }
        try {
            LOGGER.info("MQDataReceiveServiceImpl-onMessageWithMQDirect-msg:{}", msgData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onMessageWithMQTopic(String msgData) {
        if (StringUtils.isBlank(msgData)) {
            return true;
        }
        try {
            System.out.println("我拿到了");
            //调用业务方处理消息
            LOGGER.info("MQDataReceiveServiceImpl-onMessageWithMQTopic-msg:{}", msgData);
            JSON.parseObject(msgData);
            System.out.println("我拿完了");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
