package com.fungo.community.function.mq;

import com.alibaba.fastjson.JSON;
import com.fungo.community.facede.TSMQFacedeService;
import com.game.common.dto.ResultDto;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import com.game.common.vo.UserFunVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * <p>mq相关操作</p>
 * @Date: 2019/12/5
 */
@Component
public class MQActionFunction {

    private static final Logger logger = LoggerFactory.getLogger( MQActionFunction.class);

    @Autowired
    private TSMQFacedeService tSMQFacedeService;

    public boolean deletePostMQHandle(String userId,int score){
        try {
            //MQ 业务数据发送给系统用户业务处理
            TransactionMessageDto transactionMessageDto = new TransactionMessageDto();

            //消息类型
            transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_POST);

            //发送的队列
            transactionMessageDto.setConsumerQueue( RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM_USER.getName());

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

            transactionMessageDto.setMessageBody( JSON.toJSONString(mqResultDto));
            //执行MQ发送
            ResultDto<Long> messageResult = tSMQFacedeService.saveAndSendMessage(transactionMessageDto);
            logger.info("--删除帖子执行扣减用户经验值和等级--MQ执行结果：messageResult:{}", JSON.toJSONString(messageResult));

            //--start 扣减10fun币
            //@todo
            TransactionMessageDto transactionMessageDto1 = new TransactionMessageDto();

            //消息类型
            transactionMessageDto1.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_POST);

            //发送的队列
            transactionMessageDto1.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM_USER.getName());

            //路由key
            StringBuffer routinKey1 = new StringBuffer(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_SYSTEM_USER.getName());
            routinKey1.deleteCharAt(routinKey1.length() - 1);
            routinKey1.append("deletePostSubtractExpLevel");

            transactionMessageDto1.setRoutingKey(routinKey1.toString());

            MQResultDto mqResultDto1 = new MQResultDto();
            mqResultDto1.setType(MQResultDto.CommunityEnum.CMT_POST_MOOD_GAME_MQ_TYPE_DELETE.getCode());

            UserFunVO userFunVO = new UserFunVO();
            userFunVO.setMemberId(userId );
            userFunVO.setDescription( "删除文章" );
            userFunVO.setNumber(10);
            mqResultDto1.setBody(userFunVO);
            transactionMessageDto1.setMessageBody(JSON.toJSONString(mqResultDto1));
            //执行MQ发送
            ResultDto<Long> Result = tSMQFacedeService.saveAndSendMessage(transactionMessageDto1);
            //--end 扣减10fun币
        }catch (Exception e){
            logger.error( "MQActionFunction.deletePostMQHandle执行异常,参数userId = {}, score = {} ",userId,score,e );
            return false;
        }
        return true;
    }
}
