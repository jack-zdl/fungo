package Test;

import com.alibaba.fastjson.JSON;
import com.game.common.enums.CommunityEnum;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import org.junit.Test;

import java.util.HashMap;

public class JSONTest {



    @Test
    public void testJson(){
        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();
        //消息类型
        transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_POST);
        //发送的队列
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_COMMUNITY_POST.getName());


        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(CommunityEnum.CMT_POST_MQ_TYPE_DELETE_POST_SUBTRACT_EXP_LEVEL.getCode());

        HashMap<String,Object> hashMap = new HashMap<String,Object>();
        hashMap.put("mb_id", "00167ecb60374a439431563401285e58");
        hashMap.put("score", 3);

        mqResultDto.setBody(hashMap);

        transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
        System.out.println("----------------: "+ JSON.toJSONString(transactionMessageDto));
    }

}
