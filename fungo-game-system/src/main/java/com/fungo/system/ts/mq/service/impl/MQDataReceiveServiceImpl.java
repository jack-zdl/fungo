package com.fungo.system.ts.mq.service.impl;

import com.game.common.ts.mq.service.MQDataReceiveService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("mQDataReceiveService")
public class MQDataReceiveServiceImpl implements MQDataReceiveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQDataReceiveServiceImpl.class);


    @Override
    public void onMessageWithMQDirect(String msgData) throws Exception {
        if (StringUtils.isBlank(msgData)) {
            return;
        }
        try {
            LOGGER.info("MQDataReceiveServiceImpl-onMessageWithMQDirect-msg:{}", msgData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onMessageWithMQTopic(String msgData) {
        if (StringUtils.isBlank(msgData)) {
            return;
        }
        try {
            //调用业务方处理消息
            LOGGER.info("MQDataReceiveServiceImpl-onMessageWithMQTopic-msg:{}", msgData);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
