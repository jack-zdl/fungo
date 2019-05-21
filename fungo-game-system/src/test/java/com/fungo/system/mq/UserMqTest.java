package com.fungo.system.mq;

import com.alibaba.fastjson.JSON;
import com.fungo.system.ts.mq.service.ITransactionMessageService;
import com.game.common.dto.action.BasActionDto;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;

/**
 *  用户mq相关测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserMqTest {

    @Autowired
    private ITransactionMessageService iTransactionMessageService;

    /**
     *  扣减用户经验值和等级
     */
    @Test
    public void testprocessExpChange() {
        //参数部分
        String userId = "00167ecb60374a439431563401285e58";
        Integer score = 10;

        //MQ 业务数据发送给系统用户业务处理
        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();

        //消息类型
        transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_POST);

        //发送的队列
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM_USER.getName());

        //路由key
        StringBuffer routinKey = new StringBuffer(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_SYSTEM_USER.getName());
        routinKey.deleteCharAt(routinKey.length() - 1);
        routinKey.append("deletePostSubtractExpLevel");

        transactionMessageDto.setRoutingKey(routinKey.toString());

        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.CommunityEnum.CMT_POST_MQ_TYPE_DELETE_POST_SUBTRACT_EXP_LEVEL.getCode());

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("mb_id", userId);
        hashMap.put("score", score);

        mqResultDto.setBody(hashMap);

        transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));

        iTransactionMessageService.saveAndSendMessage(transactionMessageDto);

    }

    /**
     *  扣减用户经验值和等级
     */
    @Test
    public void processAddAction() {
        //参数
        BasActionDto basActionDto = new BasActionDto();
        basActionDto.setCreatedAt(new Date());
        basActionDto.setTargetId("11111");
        basActionDto.setTargetType(6);
        basActionDto.setType(8);
        basActionDto.setMemberId("xxxxx");
        basActionDto.setState(0);
        basActionDto.setUpdatedAt(new Date());

        //消息发送
        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.GameMQDataType.GAME_DATA_TYPE_BASACTIONINSERT.getCode());
        mqResultDto.setBody(basActionDto);
        transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM_USER.getName());
        transactionMessageDto.setRoutingKey(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_SYSTEM_USER.getName());
        transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_USER);
        iTransactionMessageService.saveAndSendMessage(transactionMessageDto);
    }
}
