package Test;

import com.alibaba.fastjson.JSON;
import com.game.common.consts.MemberIncentTaskConsts;
import com.game.common.dto.system.TaskDto;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import org.junit.Test;

public class JSONTest {



    @Test
    public void testJson(){
        TaskDto taskDtoExp = new TaskDto();
        taskDtoExp.setRequestId("06f2f38999204b48a6b24429b8014455");
        taskDtoExp.setMbId("01d1c44a2eda4fd3a1fe82e81abab9cf");
        taskDtoExp.setTaskGroupFlag(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code());
        taskDtoExp.setTaskType(MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT);
        taskDtoExp.setTypeCodeIdt(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_ARTICLE_EXP.code());

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

        mqResultDto.setBody(taskDtoExp);

        transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
        System.out.println("----------------: "+ JSON.toJSONString(transactionMessageDto));
    }

}
