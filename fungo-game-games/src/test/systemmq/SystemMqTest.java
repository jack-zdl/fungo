package systemmq;

import com.alibaba.fastjson.JSON;
import com.fungo.games.FungoGamesApplication;
import com.fungo.games.feign.MQFeignClient;
import com.game.common.dto.ResultDto;
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


/**
 *  ysx
 *  用户mq相关测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FungoGamesApplication.class)
public class SystemMqTest {
    @Autowired
    private MQFeignClient mqFeignClient;


    /**
     *  添加行为记录
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
        ResultDto resultDto = mqFeignClient.saveAndSendMessage(transactionMessageDto);

    }
}
