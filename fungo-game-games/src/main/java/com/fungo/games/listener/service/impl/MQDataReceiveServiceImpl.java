package com.fungo.games.listener.service.impl;

import com.alibaba.fastjson.JSON;
import com.game.common.dto.GameDto;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.service.MQDataReceiveService;
import com.game.common.ts.mq.service.MQDataSendService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
            TransactionMessageDto transactionMessageDto = JSON.parseObject(msgData, TransactionMessageDto.class);
            String messageBody = transactionMessageDto.getMessageBody();
            MQResultDto mqResultDto = JSON.parseObject(messageBody, MQResultDto.class);
//            if (mqResultDto.getType()==)
            Object body = mqResultDto.getBody();
            GameDto gameDto1 = JSON.parseObject(body.toString(), GameDto.class);
            System.out.println(gameDto1.toString());
            System.out.println("我拿完了");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
