package Test;

import com.alibaba.fastjson.JSON;
import com.game.common.consts.Setting;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class JSONTest {



    @Test
    public void testJson(){
        Map<String,Object> noticeMap = new HashMap<>();
        noticeMap.put("eventType", Setting.ACTION_TYPE_COMMENT);
        noticeMap.put("memberId", "004d12e674574cf4913ab27ca6ea697b");
        noticeMap.put("target_id", "964a243b631045f984bc7561560e9179");
        noticeMap.put("target_type", Setting.RES_TYPE_COMMENT);
        noticeMap.put("information", "{\"user_avatar\":null,\"post_title\":\"这是带图的咯iOS9我是下午玉米\",\"post_content\":\"一样用肉在人做最空后哥哥\",\"post_id\":\"cc671601f1604bb99e59774e0804e916\",\"user_id\":\"f0a694da934c4c0e90aa821fa417342e\",\"user_name\":\"2019041851\",\"user_level\":2,\"comment_content\":\"不急\",\"type\":3}") ;
        noticeMap.put("appVersion", "2.4.8");
        noticeMap.put("replyToId", "");

        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();

        //消息类型
        transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_POST);

        //发送的队列
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM_USER.getName());

        //路由key
        StringBuffer routinKey = new StringBuffer(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_SYSTEM_USER.getName());
        routinKey.deleteCharAt(routinKey.length() - 1 );
        routinKey.append("cmtPostMQDoTask");

        transactionMessageDto.setRoutingKey(routinKey.toString());

        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.CommunityEnum.CMT_ACTION_MQ_TYPE_ACTION_ADD.getCode());

        mqResultDto.setBody(noticeMap);

        transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
        System.out.println("----------------: "+ JSON.toJSONString(transactionMessageDto));
    }

}
