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
import com.game.common.repo.cache.facade.FungoCacheTask;
import com.game.common.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;


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
    @Autowired
    private FungoCacheTask fungoCacheTask;
    @Autowired
    private FungoCacheArticle fungoCacheArticle;

    @Override
    public void onApplicationEvent(AbstractTaskEventDto event) {
        String userId = event.getUserId();
        String objectId = event.getObjectId();
       if(AbstractEventDto.AbstractEventEnum.FOLLOW_ONE_OFFICIAL_USER.getKey() == event.getEventType() ){
            scoreRuleServiceImpl.achieveMultiScoreRule( userId,NewTaskStatusEnum.FOLLOWOFFICIALUSER_EXP.getKey(), objectId);
            scoreRuleServiceImpl.achieveMultiCoinRule( userId,NewTaskStatusEnum.FOLLOWOFFICIALUSER_COIN.getKey(),objectId );
        }else if(AbstractEventDto.AbstractEventEnum.FOLLOW_ONE_OFFICIAL_CIRCLE.getKey() == event.getEventType()){
            scoreRuleServiceImpl.achieveMultiScoreRule( userId,NewTaskStatusEnum.JOINOFFICIALCIRLCE_EXP.getKey(),objectId );
            scoreRuleServiceImpl.achieveMultiCoinRule( userId,NewTaskStatusEnum.JOINOFFICIALCIRLCE_COIN.getKey(),objectId );
        }else if(AbstractEventDto.AbstractEventEnum.TASK_USER_CHECK_OFFICIAL_USER.getKey() == event.getEventType()){
           List<String> objectList = event.getObjectIdList();
           objectList.stream().forEach( s ->{
               scoreRuleServiceImpl.achieveMultiScoreRule( userId,NewTaskStatusEnum.FOLLOWOFFICIALUSER_EXP.getKey(),s);
               scoreRuleServiceImpl.achieveMultiCoinRule( userId,NewTaskStatusEnum.FOLLOWOFFICIALUSER_COIN.getKey(),s );
           } );
       }else if(AbstractEventDto.AbstractEventEnum.TASK_USER_CHECK_OFFICIAL_CIRCLE.getKey() == event.getEventType()){
           List<String> objectList = event.getObjectIdList();
           objectList.stream().forEach( s ->{
               scoreRuleServiceImpl.achieveMultiScoreRule( userId,NewTaskStatusEnum.JOINOFFICIALCIRLCE_EXP.getKey(),s);
               scoreRuleServiceImpl.achieveMultiCoinRule( userId,NewTaskStatusEnum.JOINOFFICIALCIRLCE_COIN.getKey(),s );
           } );
       }else if(AbstractEventDto.AbstractEventEnum.TASK_USER_CHECK_BIND_QQ_WEIBO_WECHAT.getKey() == event.getEventType()){
           List<Integer> list = event.getEventTypeList();
           if(list.contains( AbstractEventDto.AbstractEventEnum.TASK_USER_CHECK_BIND_QQ.getKey() ) ){
               scoreRuleServiceImpl.achieveScoreRule(userId,NewTaskStatusEnum.BINDQQ_EXP.getKey());
               scoreRuleServiceImpl.achieveCoinRule( userId,NewTaskStatusEnum.BINDQQ_COIN.getKey());
           }
           if( list.contains( AbstractEventDto.AbstractEventEnum.TASK_USER_CHECK_BIND_WEIBO.getKey()  )){
               scoreRuleServiceImpl.achieveScoreRule(userId,NewTaskStatusEnum.BINDWEIBO_EXP.getKey());
               scoreRuleServiceImpl.achieveCoinRule( userId,NewTaskStatusEnum.BINDWEIBO_COIN.getKey());
           }
           if( list.contains(  AbstractEventDto.AbstractEventEnum.TASK_USER_CHECK_BIND_WECHAT.getKey() ) ){
               scoreRuleServiceImpl.achieveScoreRule(userId,NewTaskStatusEnum.BINDWECHAT_Exp.getKey());
               scoreRuleServiceImpl.achieveCoinRule( userId,NewTaskStatusEnum.BINDWECHAT_Exp.getKey());
           }
       }
        if(!CommonUtil.isNull( userId )){
            String keyPreffix = FungoCoreApiConstant.FUNGO_CORE_API_TASK_USER_TASK_PROGRESS + "-" + userId;
            fungoCacheTask.excIndexCache(false,keyPreffix,null,null  );
            fungoCacheTask.excIndexCache(false,FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + userId,null,null  );
            fungoCacheTask.excIndexCache(false,FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_RANKS_LEVEL + userId,null,null  );
            fungoCacheArticle.removeIndexDecodeCache(false,"*"  );
        }
    }




}
