package com.fungo.community.function.event;

import com.alibaba.fastjson.JSON;
import com.fungo.community.feign.TSFeignClient;
import com.fungo.community.function.cache.EhcacheActionFunction;
import com.fungo.community.function.mq.MQActionFunction;
import com.fungo.community.function.cache.RedisActionFunction;
import com.fungo.community.service.ICounterService;
import com.fungo.community.service.impl.PostServiceImpl;
import com.game.common.dto.AbstractEventDto;
import com.game.common.dto.ActionInput;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import com.game.common.util.date.DateTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.game.common.consts.FunGoGameConsts.CACHE_EH_KEY_POST;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/12/5
 */
@Component
public class EventPostListern implements ApplicationListener<AbstractEventDto> {

    private static final Logger logger = LoggerFactory.getLogger( EventPostListern.class );

    @Autowired
    private RedisActionFunction redisActionFunction;
    @Autowired
    private MQActionFunction mqActionFunction;
    @Autowired
    private ICounterService iCountService;
    @Autowired
    private EhcacheActionFunction ehcacheActionFunction;
    @Autowired
    private TSFeignClient tsFeignClient;
    @Autowired
    private PostServiceImpl postServiceImpl;

    @Override
    @Async
    public void onApplicationEvent(AbstractEventDto event) {
        if (AbstractEventDto.AbstractEventEnum.DELETE_POST.getKey() == event.getEventType()) {
            String userId = event.getUserId();
            int score = event.getScore();
            String postId = event.getPostId();
            redisActionFunction.deletePostRedisHandle( userId, postId );
            mqActionFunction.deletePostMQHandle( userId, score );
            Map<String, String> ehcacheMap = new HashMap<>( 1 );
            ehcacheMap.put( CACHE_EH_KEY_POST, "" );
            ehcacheActionFunction.deletePostEhcacheHandler( ehcacheMap );
        } else if (AbstractEventDto.AbstractEventEnum.UPDATE_POST.getKey() == event.getEventType()) {
            // mq发送消息
            Map<String,String> map = new HashMap<>();
            String memberId = event.getUserId();
            String postId = event.getPostId();
            String objectId = event.getObjectId();
            map.put( "id",memberId);
            map.put( "type","2");
            Map<String,String> dataMap = new HashMap<>();
            if( event.getType() == 0 && event.getAuth() == 1 ){
                dataMap.put("content",new  StringBuffer().append("您的帖子由于不符合圈子规范，暂时被隐藏，如有疑议，请进群联系管理员解决。QQ群：746928618" ).toString());
            }else if(event.getType() == 0 && event.getAuth() == 2){
                dataMap.put("content",new  StringBuffer().append("您的文章《").append( objectId ).append(  "》现已被管理员关闭回复功能，如有疑议，加群746928618联系管理员。" ).toString());
            }else if(event.getType() == 1 && event.getAuth() == 2){
                dataMap.put("content",new  StringBuffer().append("您被关闭回复的帖子现已恢复回复功能，点击查看" ).toString());
            }
            dataMap.put("actionType","1");
            dataMap.put("targetType","1");
            dataMap.put("targetId",postId);
            dataMap.put("userId", "0b8aba833f934452992a972b60e9ad10");
            dataMap.put("userType", "1");
            dataMap.put("userAvatar", "http://output-mingbo.oss-cn-beijing.aliyuncs.com/official.png");
            dataMap.put("userName", "FunGo大助手");
            dataMap.put("msgTime", DateTools.fmtDate(new Date()));
            map.put( "data", JSON.toJSONString( dataMap ) );
            // mq 消息通知
            postServiceImpl.sendMessage(map);
            redisActionFunction.updatePostRedisHandle();
            Map<String, String> ehcacheMap = new HashMap<>( 1 );
            ehcacheMap.put( CACHE_EH_KEY_POST, "" );
            ehcacheActionFunction.deletePostEhcacheHandler( ehcacheMap );

        } else if (AbstractEventDto.AbstractEventEnum.FOLLOW_ONE_OFFICIAL_CIRCLE.getKey() == event.getEventType()) {
            String memberId = event.getUserId();
            String circleId = event.getObjectId();
            sendTaskOfJoinOfficialCircle(memberId,circleId);
        }
    }

    private boolean subCounter(String communityId, String userId) {
        try {
            ActionInput actioninput = new ActionInput();
            actioninput.setTarget_type( 4 );
            actioninput.setTarget_id( communityId );
            iCountService.subCounter( userId, 7, actioninput ); // boolean b =
        } catch (Exception e) {
            logger.error( "删除文章监听事件减少圈子和社区文章数异常,参数communityId= {} ,userId= {} ", communityId, userId, e );
        }
        return true;
    }

    private void sendTaskOfJoinOfficialCircle(String memberId, String communityId) {
        BasActionDto basActionDtoAdd = new BasActionDto();
        basActionDtoAdd.setCreatedAt( new Date() );
        basActionDtoAdd.setUpdatedAt( new Date() );
        basActionDtoAdd.setMemberId( memberId );
        basActionDtoAdd.setType( 12 );
        basActionDtoAdd.setTargetType( 4 );
        basActionDtoAdd.setTargetId( communityId );
        basActionDtoAdd.setState( 0 );
        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();
        //消息类型
        transactionMessageDto.setMessageDataType( TransactionMessageDto.MESSAGE_DATA_TYPE_POST );
        //发送的队列
        transactionMessageDto.setConsumerQueue( RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM_USER.getName() );
        //路由key
        StringBuffer routinKey = new StringBuffer( RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_SYSTEM_USER.getName() );
        routinKey.deleteCharAt( routinKey.length() - 1 );
        routinKey.append( "ACTION_ADD" );
        transactionMessageDto.setRoutingKey( routinKey.toString() );
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType( MQResultDto.CommunityEnum.CMT_POST_MOOD_GAME_MQ_TYPE_TASK.getCode() );
        mqResultDto.setBody( basActionDtoAdd );
        transactionMessageDto.setMessageBody( JSON.toJSONString( mqResultDto ) );
        //执行MQ发送
        ResultDto<Long> messageResult = tsFeignClient.saveAndSendMessage( transactionMessageDto );
    }
}
