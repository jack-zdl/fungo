package com.fungo.community.facede;


import com.alibaba.fastjson.JSON;
import com.fungo.community.feign.TSFeignClient;
import com.fungo.community.service.ICounterService;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.util.UniqueIdCkeckUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Map;

/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description: MQ 数据接收业务处理
 *
 * @Date: Create in 2018/5/8
 */
@Service
@Transactional
public class CommunityTSMQService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityTSMQService.class);

    @Autowired
    private ICounterService iCounterService;

    @Autowired
    private TSFeignClient feignClient;


    /**
     * 处理MQ消息
     * @param msgData
     * @return
     */
    public boolean excuteMQ(String msgData) {
        boolean isOk = true;
        String msgId = null;
        try {
            TransactionMessageDto transactionMessageDto = JSON.parseObject(msgData, TransactionMessageDto.class);
            //消息id不可为空
            if (null != transactionMessageDto&&transactionMessageDto.getMessageId() != null) {
                msgId = transactionMessageDto.getMessageId() .toString();
                switch (transactionMessageDto.getMessageDataType()) {
                    //接收到的消息，本服务是否应该处理 -- 在校验重复消费前做是否处理校验，避免redis存入数据
                    case TransactionMessageDto.MESSAGE_DATA_TYPE_SYSTEM:
                        //校验是否重复消费
                        if (!UniqueIdCkeckUtil.checkUniqueIdAndSave(msgId)) {
                            LOGGER.warn("社区服务-重复消费驳回,消息内容:"+msgData);
                            return true;
                        }
                        isOk = excSystem(transactionMessageDto.getMessageBody());
                        if (isOk) {
                            //删除消息
                            feignClient.deleteMessageByMessageId(transactionMessageDto.getMessageId());
                        }else{
                            //执行不成功，不确认消费，让定时任务重试（删除唯一请求标志）
                            UniqueIdCkeckUtil.deleteUniqueId(msgId);
                        }
                        break;
                    default:
                        break;
                }

            }

        } catch (Exception ex) {
            LOGGER.error("社区服务消费消息异常:" + msgData, ex);
            //手动开启事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            //删除唯一请求标志,使逻辑可以重试
            if (msgId != null) {
                UniqueIdCkeckUtil.deleteUniqueId(msgId);
            }
            isOk = false;
        }
        return isOk;
    }

    /**
     *
     * @param messageBody
     */
    private boolean excSystem(String messageBody) {
        if (StringUtils.isNotBlank(messageBody)) {

            MQResultDto mqResultDto = JSON.parseObject(messageBody, MQResultDto.class);
            //处理系统微服务发送的   SYSTEM_MQ_DATA_TYPE_COMMUNITY_UPDATE(7,"java.util.HashMap"), // 社区计数器
            if (MQResultDto.SystemMQDataType.SYSTEM_MQ_DATA_TYPE_COMMUNITY_UPDATE.getCode() == mqResultDto.getType()) {
                String bodyStr = (String) mqResultDto.getBody();
                /*
                 *       Map<String, String> map = new HashMap<String, String>();
                 *         map.put("tableName", getTableName(inputDto.getTarget_type()));
                 *         map.put("fieldName", getFieldName(type));
                 *         map.put("id", inputDto.getTarget_id());
                 *         map.put("type", "add");
                 */
                if (StringUtils.isNotBlank(bodyStr)) {
                    Map<String, String> bodyMap = JSON.parseObject(bodyStr, Map.class);

                    if (null != bodyMap && !bodyMap.isEmpty()) {
                        String typeAdd = bodyMap.get("type");

                        if (StringUtils.isNotBlank(typeAdd) && StringUtils.equalsAnyIgnoreCase("add", typeAdd)) {

                            String tableName = bodyMap.get("tableName");
                            String fieldName = bodyMap.get("fieldName");
                            String id = bodyMap.get("id");

                            if (StringUtils.isNotBlank(tableName) && StringUtils.isNotBlank(fieldName) && StringUtils.isNotBlank(id)) {
                                return iCounterService.addCounter(tableName, fieldName, id);
                            }
                        }

                    }
                }
            }
        }
        return false;
    }


    //---------
}
