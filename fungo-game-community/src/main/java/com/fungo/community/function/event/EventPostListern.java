package com.fungo.community.function.event;

import com.fungo.community.function.cache.EhcacheActionFunction;
import com.fungo.community.function.mq.MQActionFunction;
import com.fungo.community.function.cache.RedisActionFunction;
import com.fungo.community.service.ICounterService;
import com.game.common.dto.AbstractEventDto;
import com.game.common.dto.ActionInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/12/5
 */
@Component
public class EventPostListern implements ApplicationListener<AbstractEventDto> {

    private static final Logger logger = LoggerFactory.getLogger( EventPostListern.class);

    @Autowired
    private RedisActionFunction redisActionFunction;
    @Autowired
    private MQActionFunction mqActionFunction;
    @Autowired
    private ICounterService iCountService;
    @Autowired
    private EhcacheActionFunction ehcacheActionFunction;

    @Override
    @Async
    public void onApplicationEvent(AbstractEventDto event) {
        if (AbstractEventDto.AbstractEventEnum.DELETE_POST.getKey() == event.getEventType()) {
            String userId = event.getUserId();
            int score = event.getScore();
            String postId = event.getPostId();
            redisActionFunction.deletePostRedisHandle(userId,postId);
            mqActionFunction.deletePostMQHandle( userId,score);
            Map<String,String> ehcacheMap = new HashMap<>(2);
            ehcacheMap.put( "CACHE_EH_KEY_POST","");
            ehcacheActionFunction.deletePostEhcacheHandler(ehcacheMap);
        }
    }

    private boolean subCounter(String communityId,String userId){
        try {
            ActionInput actioninput = new ActionInput();
            actioninput.setTarget_type(4);
            actioninput.setTarget_id(communityId);
            iCountService.subCounter(userId, 7, actioninput); // boolean b =
        }catch (Exception e){
            logger.error( "删除文章监听事件减少圈子和社区文章数异常,参数communityId= {} ,userId= {} ",communityId,userId,e);
        }
        return true;
    }
}
