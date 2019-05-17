package com.fungo.system.helper;

import com.alibaba.fastjson.JSON;
import com.fungo.system.ts.mq.service.ITransactionMessageService;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>rabbitmqd的生产者</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/16
 */
@Component
public class RabbitMQProduct {

    @Autowired
    private ITransactionMessageService iTransactionMessageService;

    /**
     * 功能描述: 根据map的值发送更新操作
     * @param: [map]
     * @return: void
     * @auther: dl.zhang
     * @date: 2019/5/16 15:47
     */
    public void updateCounter( Map<String, String> map){
        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();
        transactionMessageDto.setMessageBody(JSON.toJSONString(map));
        transactionMessageDto.setMessageDataType(2);
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_UPDATECOUNTER.getName());
        transactionMessageDto.setRoutingKey(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_DEFAULT_UPDATECOUNTER.getName());
        iTransactionMessageService.saveAndSendMessage(transactionMessageDto);
    }
}
