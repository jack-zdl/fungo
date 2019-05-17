package Test;

import com.alibaba.fastjson.JSON;
import com.game.common.dto.action.BasActionDto;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import org.junit.Test;

import java.util.Date;

public class JSONTest {



    @Test
    public void testJson(){
        BasActionDto basActionDtoAdd = new BasActionDto();
        basActionDtoAdd.setCreatedAt(new Date());
        basActionDtoAdd.setUpdatedAt(new Date());
        basActionDtoAdd.setMemberId("01d1c44a2eda4fd3a1fe82e81abab9cf");
        basActionDtoAdd.setType(11);
        basActionDtoAdd.setTargetType(1);
        basActionDtoAdd.setTargetId("01d1c44a2eda4fd3a1fe82e81abab9cf");
        basActionDtoAdd.setState(0);

        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();

        //消息类型
        transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_POST);

        //发送的队列
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM_USER.getName());

        //路由key
        StringBuffer routinKey = new StringBuffer(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_SYSTEM_USER.getName());
        routinKey.deleteCharAt(routinKey.length() - 1 );
        routinKey.append("ACTION_ADD");

        transactionMessageDto.setRoutingKey(routinKey.toString());

        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.CommunityEnum.CMT_ACTION_MQ_TYPE_ACTION_ADD.getCode());

        mqResultDto.setBody(basActionDtoAdd);

        transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
        System.out.println("----------------: "+ JSON.toJSONString(transactionMessageDto));
    }

}
