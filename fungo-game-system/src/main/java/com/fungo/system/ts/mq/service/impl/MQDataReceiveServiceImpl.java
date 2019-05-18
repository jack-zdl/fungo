package com.fungo.system.ts.mq.service.impl;

import com.alibaba.fastjson.JSON;
import com.fungo.system.ts.mq.service.ITransactionMessageService;
import com.fungo.system.ts.mq.service.SystemMqConsumeService;
import com.fungo.system.ts.mq.service.UserMqConsumeService;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.service.MQDataReceiveService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("mQDataReceiveService")
public class MQDataReceiveServiceImpl implements MQDataReceiveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQDataReceiveServiceImpl.class);

    @Autowired
    private SystemMqConsumeService systemMqConsumeService;

    @Autowired
    private UserMqConsumeService userMqConsumeService;

    @Autowired
    private ITransactionMessageService transactionMessageService;

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
            //解析消息体，确认是哪个队列的消息
            TransactionMessageDto messageDto = JSON.parseObject(msgData, TransactionMessageDto.class);
            boolean result;
            if(TransactionMessageDto.MESSAGE_DATA_TYPE_SYSTEM == messageDto.getMessageDataType()){
                result = systemMqConsumeService.processMsg(messageDto.getMessageBody());
            }else if(TransactionMessageDto.MESSAGE_DATA_TYPE_USER == messageDto.getMessageDataType()){
                result = userMqConsumeService.processMsg(messageDto.getMessageBody());
            }else{
                //不是系统和用户应该处理的消息
                return false;
            }
            LOGGER.info("MQDataReceiveServiceImpl-onMessageWithMQTopic-msg:{}", msgData);

            if(result){
                //删除消息
                transactionMessageService.deleteMessageByMessageId(messageDto.getMessageId());
            }
            //执行不成功，不确认消费，让定时任务重试,
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
