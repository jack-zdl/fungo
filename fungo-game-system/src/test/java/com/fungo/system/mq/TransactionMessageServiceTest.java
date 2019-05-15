package com.fungo.system.mq;

import com.alibaba.fastjson.JSON;
import com.fungo.system.ts.mq.service.ITransactionMessageService;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TransactionMessageServiceTest {

    @Autowired
    private ITransactionMessageService iTransactionMessageService;

    @Test
    public void testSaveAndSendMessage() {

        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();
        HashMap<String,String>  msg = new HashMap<>();
        msg.put("id", "190");
        msg.put("content", "test mq");


        transactionMessageDto.setMessageBody(JSON.toJSONString(msg));
        transactionMessageDto.setMessageDataType(1);
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_DIRECT_NAME_DEFAULT.getName());
       

        iTransactionMessageService.saveAndSendMessage(transactionMessageDto);


    }




}
