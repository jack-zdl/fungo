package com.fungo.community.helper;

import com.alibaba.fastjson.JSON;
import com.fungo.community.facede.TSMQFacedeService;
import com.fungo.community.service.impl.EvaluateServiceImpl;
import com.game.common.dto.ResultDto;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/4/26
 */
@Component
public class MQProduct {

    private static final Logger logger = LoggerFactory.getLogger( MQProduct.class);

    @Autowired
    private TSMQFacedeService tSMQFacedeService;

    @Autowired
    private  AmqpTemplate template;

    public void updateCounter(Map<String, String> map  ){
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.SystemMQDataType.SYSTEM_DATA_TYPE_COUNTER.getCode());
        mqResultDto.setBody(map);

        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();
        transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
        /**
         * 功能描述:
         * 消息业务领域类型:
         * 1 系统
         * 2 用户
         * 3 社区-文章
         * 4 社区-心情
         * 5 游戏
         * 6 首页
         * 7 管控台
         */
        transactionMessageDto.setMessageDataType(1); //
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_GAMES.getName());
        transactionMessageDto.setRoutingKey(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_GAMES.getName());
        ResultDto<Long> messageResult = tSMQFacedeService.saveAndSendMessage(transactionMessageDto);

        logger.info("--添加评论-执行发送消息--MQ执行结果：messageResult:{}", JSON.toJSONString(messageResult));
    }

}
