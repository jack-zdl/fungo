package com.fungo.games.helper;

import com.alibaba.fastjson.JSON;
import com.fungo.games.feign.MQFeignClient;
import com.game.common.bean.advice.BasNoticeDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/4/26
 */
@Component
@SuppressWarnings("all")
public class MQProduct {
    @Autowired
    private AmqpTemplate template;

    @Autowired
    private MQFeignClient mqFeignClient;



    /**
     * use Topic Pattern
     * "topic.key1"  路由键
     * @param message
     */
   /* public void sendTopic(String topicExchange,String topicKey,Object message){
//        rabbitTemplate.setConfirmCallback((message,))
        template.convertAndSend(topicExchange,topicKey,message);  // 可以匹配到 topic.# and topic.key1
//        template.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key2",msg+"2");  // 可以匹配到 topic.#
    }*/



    public void basActionInsertAllColumn(BasActionDto basActionDto) {
//        sendTopic(MQConfig.TOPIC_EXCHANGE_BASACTION_INSERTALLCOLUMN,MQConfig.TOPIC_KEY_BASACTION_INSERTALLCOLUMN,basActionDto);
        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.GameMQDataType.GAME_DATA_TYPE_BASACTIONINSERT.getCode());
        mqResultDto.setBody(basActionDto);
        transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM_USER.getName());
        transactionMessageDto.setRoutingKey(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_SYSTEM_USER.getName());
        transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_USER);
        sendFeignMq(transactionMessageDto);
    }

    public void selectOneAndUpdateAllColumnById(String memberId, String targetId, int targetType, int type, int state) {
        //        sendTopic(MQConfig.TOPIC_EXCHANGE_BASACTION_SELECTONEANDUPDATEALLCOLUMNBYID,MQConfig.TOPIC_KEY_BASACTION_SELECTONEANDUPDATEALLCOLUMNBYID,map);
        Map<String,Object> map = new ConcurrentHashMap<>();
        map.put("memberId",memberId);
        map.put("targetId",targetId);
        map.put("targetType",targetType);
        map.put("type",type);
        map.put("state",state);
        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.GameMQDataType.GAME_DATA_TYPE_SELECTONEANDUPDATEALLCOLUMNBYID.getCode());
        mqResultDto.setBody(map);
        transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM_USER.getName());
        transactionMessageDto.setRoutingKey(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_SYSTEM_USER.getName());
        transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_USER);
        sendFeignMq(transactionMessageDto);

    }

    /**
     * 逻辑块变动,根据id修改basNotice数据
     * @param basNoticeDto
     */
    public void basNoticeUpdateById(BasNoticeDto basNoticeDto) {
//        sendTopic(MQConfig.TOPIC_EXCHANGE_BASNOTICE_BASNOTICEUPDATEBYID,MQConfig.TOPIC_KEY_BASNOTICE_BASNOTICEUPDATEBYID,basNoticeDto);
        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.GameMQDataType.GAME_DATA_TYPE_BASNOTICEUPDATEBYID.getCode());
        mqResultDto.setBody(basNoticeDto);
        transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM.getName());
        transactionMessageDto.setRoutingKey(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_SYSTEM.getName());
        transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_SYSTEM);
        sendFeignMq(transactionMessageDto);
    }

    /**
     * 添加插入BasNotice数据返回主键
     * @param basNoticeDto
     * @param gameInviteDto
     */
    public void basNoticeInsertAndGameInviteReturnId(BasNoticeDto basNoticeDto) {
        /*Map<String, String> map = new HashMap<>();
        map.put("basNotice", JSON.toJSONString(basNoticeDto));
        map.put("gameInvite", JSON.toJSONString(gameInviteDto));
        map.put("appVersion", JSON.toJSONString(appVersion));*/
//        sendTopic(MQConfig.TOPIC_EXCHANGE_BASNOTICE_INSERTANDGAMEINVITERETURNID,MQConfig.TOPIC_KEY_BASNOTICE_INSERTANDGAMEINVITERETURNID,map);
        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.GameMQDataType.GAME_DATA_TYPE_BASNOTICEINSERT.getCode());
        mqResultDto.setBody(basNoticeDto);
        transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM.getName());
        transactionMessageDto.setRoutingKey(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_SYSTEM.getName());
        transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_SYSTEM);
        sendFeignMq(transactionMessageDto);
    }

    /**
     * 插入消息记录
     * @param inviteMemberId
     * @param i
     * @param appVersion
     */
    public void push(String inviteMemberId, int i, String appVersion) {
        Map<String, String> map = new HashMap<>();
        map.put("inviteMemberId", inviteMemberId);
        map.put("code", i+"");
        map.put("appVersion", appVersion);
//        sendTopic(MQConfig.TOPIC_EXCHANGE_MEMBER_PUSH,MQConfig.TOPIC_KEY_MEMBER_PUSH,map);
        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.GameMQDataType.GAME_DATA_TYPE_PUSH.getCode());
        mqResultDto.setBody(map);
        transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM.getName());
        transactionMessageDto.setRoutingKey(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_SYSTEM.getName());
        transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_SYSTEM);
        sendFeignMq(transactionMessageDto);
    }




    private ResultDto sendFeignMq(TransactionMessageDto transactionMessageDto){
        ResultDto resultDto = mqFeignClient.saveAndSendMessage(transactionMessageDto);
        return resultDto;
    }


}
