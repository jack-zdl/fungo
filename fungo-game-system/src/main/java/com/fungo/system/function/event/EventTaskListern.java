package com.fungo.system.function.event;

import com.fungo.system.config.CuratorConfiguration;
import com.fungo.system.helper.zookeeper.DistributedLockByCurator;
import com.fungo.system.service.IScoreRuleService;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.AbstractEventDto;
import com.game.common.dto.AbstractTaskEventDto;
import com.game.common.dto.ActionInput;
import com.game.common.enums.NewTaskStatusEnum;
import com.game.common.repo.cache.facade.FungoCacheArticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/12/5
 */
@Component
public class EventTaskListern implements ApplicationListener<AbstractTaskEventDto> {

    private static final Logger logger = LoggerFactory.getLogger( EventTaskListern.class);

    @Autowired
    private IScoreRuleService scoreRuleServiceImpl;

    @Override
    public void onApplicationEvent(AbstractTaskEventDto event) {
        String userId = event.getUserId();
        String objectId = event.getObjectId();
       if(AbstractEventDto.AbstractEventEnum.FOLLOW_ONE_OFFICIAL_USER.getKey() == event.getEventType() ){
            scoreRuleServiceImpl.achieveMultiScoreRule( userId,NewTaskStatusEnum.FOLLOWOFFICIALUSER_EXP.getKey(), objectId);
            scoreRuleServiceImpl.achieveMultiScoreRule( userId,NewTaskStatusEnum.FOLLOWOFFICIALUSER_COIN.getKey(),objectId );
        }else if(AbstractEventDto.AbstractEventEnum.FOLLOW_ONE_OFFICIAL_CIRCLE.getKey() == event.getEventType()){
            scoreRuleServiceImpl.achieveMultiScoreRule( userId,NewTaskStatusEnum.JOINOFFICIALCIRLCE_EXP.getKey(),objectId );
            scoreRuleServiceImpl.achieveMultiScoreRule( userId,NewTaskStatusEnum.JOINOFFICIALCIRLCE_COIN.getKey(),objectId );
        }
    }


}
