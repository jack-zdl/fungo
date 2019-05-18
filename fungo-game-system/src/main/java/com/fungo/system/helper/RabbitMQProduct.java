package com.fungo.system.helper;

import com.alibaba.fastjson.JSON;
import com.fungo.system.ts.mq.service.ITransactionMessageService;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
     * 功能描述: 游戏服务操作
     * @param: [map]
     * @return: void
     * @auther: dl.zhang
     * @date: 2019/5/16 15:47
     */
    public void mqGames( Object o){
        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();
        transactionMessageDto.setMessageBody(JSON.toJSONString(o));
        /**
         * 功能描述:
         * 消息业务领域类型:
         * 1 系统
         * 2 用户
         * 3 社区-文章
         * 4 社区-心情
         * 5 游戏
         * 6 首页
         */
        transactionMessageDto.setMessageDataType(5); //
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_GAMES.getName());
        transactionMessageDto.setRoutingKey(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_GAMES.getName());
        iTransactionMessageService.saveAndSendMessage(transactionMessageDto);
    }


    /**
     * 功能描述: 游戏服务操作
     * @param: [map]
     * @return: void
     * @auther: dl.zhang
     * @date: 2019/5/16 15:47
     */
    public void mqCommunity( Object o){
        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();
        transactionMessageDto.setMessageBody(JSON.toJSONString(o));
        /**
         * 功能描述:
         * 消息业务领域类型:
         * 1 系统
         * 2 用户
         * 3 社区-文章
         * 4 社区-心情
         * 5 游戏
         * 6 首页
         * 7 社区
         */
        transactionMessageDto.setMessageDataType(7); //
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_GAMES.getName());
        transactionMessageDto.setRoutingKey(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_GAMES.getName());
        iTransactionMessageService.saveAndSendMessage(transactionMessageDto);
    }
}
