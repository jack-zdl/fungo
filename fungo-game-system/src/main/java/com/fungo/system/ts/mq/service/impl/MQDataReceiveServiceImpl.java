package com.fungo.system.ts.mq.service.impl;

import com.alibaba.fastjson.JSON;
import com.fungo.system.ts.mq.service.ITransactionMessageService;
import com.fungo.system.ts.mq.service.SystemMqConsumeService;
import com.fungo.system.ts.mq.service.UserMqConsumeService;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.service.MQDataReceiveService;
import com.game.common.util.StringUtil;
import com.game.common.util.UniqueIdCkeckUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


@Service("mQDataReceiveService")
@Transactional
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
        LOGGER.info("MQDataReceiveServiceImpl-onMessageWithMQTopic-msg:{}", msgData);
        if (StringUtils.isBlank(msgData)) {
            return true;
        }
        String msgId = null;
        try {
            //解析消息体，确认是哪个队列的消息
            TransactionMessageDto messageDto = JSON.parseObject(msgData, TransactionMessageDto.class);

            //消息id不可为空
            if(messageDto == null || messageDto.getMessageId() == null){
                return true;
            }
            //接收到的消息，系统服务是否应该处理 -- 在校验重复消费前做是否处理校验，避免redis存入数据
            if(!whetherProcess(messageDto.getMessageDataType())){
                return false;
            }

            msgId = messageDto.getMessageId() .toString();
            //校验是否重复消费
            if (!UniqueIdCkeckUtil.checkUniqueIdAndSave(msgId)) {
                LOGGER.warn("系统服务-重复消费驳回,消息内容:"+msgData);
                return true;
            }

            boolean result;
            if (TransactionMessageDto.MESSAGE_DATA_TYPE_SYSTEM == messageDto.getMessageDataType()) {
                result = systemMqConsumeService.processMsg(messageDto.getMessageBody());
            } else if (TransactionMessageDto.MESSAGE_DATA_TYPE_USER == messageDto.getMessageDataType()) {
                result = userMqConsumeService.processMsg(messageDto.getMessageBody());
            } else {
                //不是系统和用户应该处理的消息  走不到这里，因为上面逻辑校验了是否应该处理
                UniqueIdCkeckUtil.deleteUniqueId(msgId);
                return false;
            }
            if (result) {
                //删除消息
                transactionMessageService.deleteMessageByMessageId(messageDto.getMessageId());
            }else{
                //执行不成功，不确认消费，让定时任务重试（删除唯一请求标志）
                UniqueIdCkeckUtil.deleteUniqueId(msgId);
            }
            return result;
        } catch (Exception ex) {
            LOGGER.error("系统服务消费消息异常:" + msgData, ex);
            //手动开启事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            //删除唯一请求标志,使逻辑可以重试
            if (msgId != null) {
                UniqueIdCkeckUtil.deleteUniqueId(msgId);
            }
        }
        return false;
    }


    /**
     *  判断接收到的消息，系统服务是否应该处理
     * @param type 消息大类型
     * @return 是否应该处理
     */
    private boolean whetherProcess(Integer type){
        if(type==null){
            return false;
        }
        return TransactionMessageDto.MESSAGE_DATA_TYPE_SYSTEM == type||TransactionMessageDto.MESSAGE_DATA_TYPE_USER == type;
    }
}
