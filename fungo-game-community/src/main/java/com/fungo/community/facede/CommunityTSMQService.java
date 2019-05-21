package com.fungo.community.facede;


import com.alibaba.fastjson.JSON;
import com.fungo.community.service.ICounterService;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description: MQ 数据接收业务处理
 *
 * @Date: Create in 2018/5/8
 */
@Component
public class CommunityTSMQService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityTSMQService.class);

    @Autowired
    private ICounterService iCounterService;


    /**
     * 处理MQ消息
     * @param msgData
     * @return
     */
    public boolean excuteMQ(String msgData) {

        boolean isOk = true;

        try {

            TransactionMessageDto transactionMessageDto = JSON.parseObject(msgData, TransactionMessageDto.class);
            if (null != transactionMessageDto) {
                switch (transactionMessageDto.getMessageDataType()) {
                    //系统业务
                    case TransactionMessageDto.MESSAGE_DATA_TYPE_SYSTEM:
                        isOk = excSystem(transactionMessageDto.getMessageBody());
                        break;
                    default:
                        break;
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
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
