package com.fungo.system.function.event;

import com.fungo.system.config.CuratorConfiguration;
import com.fungo.system.dto.MemberLevelBean;
import com.fungo.system.helper.zookeeper.DistributedLockByCurator;
import com.fungo.system.service.IMemberService;
import com.fungo.system.service.IScoreRuleService;
import com.fungo.system.service.ITaskService;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.consts.MessageConstants;
import com.game.common.dto.AbstractEventDto;
import com.game.common.dto.ActionInput;
import com.game.common.enums.NewTaskStatusEnum;
import com.game.common.repo.cache.facade.FungoCacheArticle;
import com.game.common.repo.cache.facade.FungoCacheTask;
import com.game.common.util.CommonUtil;
import org.apache.curator.framework.CuratorFramework;
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
public class EventPostListern implements ApplicationListener<AbstractEventDto> {

    private static final Logger logger = LoggerFactory.getLogger( EventPostListern.class);

    @Autowired
    private DistributedLockByCurator distributedLockByCurator;
    @Autowired
    private CuratorConfiguration curatorConfiguration;
    @Autowired
    private FungoCacheArticle fungoCacheArticle;
    @Autowired
    private IScoreRuleService scoreRuleServiceImpl;
    @Autowired
    private ITaskService taskServiceImpl;
    @Autowired
    private FungoCacheTask fungoCacheTask;
    @Autowired
    private IMemberService memberServiceImpl;

    @Override
    @Async
    public void onApplicationEvent(AbstractEventDto event) {
        String lockPath = curatorConfiguration.getCounterLock();
        String loginPath = curatorConfiguration.getLoginNum();
        String actionPath = curatorConfiguration.getActionLock();
        String memberId = null;
        if (AbstractEventDto.AbstractEventEnum.USER_LOGIN.getKey() == event.getEventType()) {
            try {
                distributedLockByCurator.acquireMyDistributedLock( lockPath,loginPath);
                distributedLockByCurator.updateZKNode( lockPath,1);
            }catch (Exception e){
                logger.error( "修改登陆人数分布式计数器失败",e);
            }finally {
                distributedLockByCurator.releaseMyDistributedLock( lockPath,loginPath);
            }
        }else if(AbstractEventDto.AbstractEventEnum.USER_LOGOUT.getKey() == event.getEventType()){
            try {
                distributedLockByCurator.acquireMyDistributedLock( lockPath,loginPath);
                distributedLockByCurator.updateZKNode( lockPath,2);
            }catch (Exception e){
                logger.error( "修改登陆人数分布式计数器失败",e);
            }finally {
                distributedLockByCurator.releaseMyDistributedLock( lockPath,loginPath);
            }
        }else if(AbstractEventDto.AbstractEventEnum.USER_FOLLOW.getKey() == event.getEventType() || AbstractEventDto.AbstractEventEnum.USER_UNFOLLOW.getKey() == event.getEventType()){
            try {
                distributedLockByCurator.acquireMyDistributedLock( actionPath,loginPath);
                if( ActionInput.circleList.contains(event.getFollowType())){
                    fungoCacheArticle.removeIndexDecodeCache(false, FungoCoreApiConstant.PUB_CIRCLE);
                    distributedLockByCurator.updateZKNode( actionPath,"CIRCLE");
                }else if(ActionInput.postList.contains( event.getFollowType() )){
                    fungoCacheArticle.removeIndexDecodeCache(false, FungoCoreApiConstant.PUB_POST);
                    distributedLockByCurator.updateZKNode( actionPath,"POST");
                }
            }catch (Exception e){
                logger.error( "修改登陆人数分布式计数器失败",e);
            }finally {
                distributedLockByCurator.releaseMyDistributedLock( actionPath,loginPath);
            }
        }else if(AbstractEventDto.AbstractEventEnum.ADD_LIKE.getKey() == event.getEventType() || AbstractEventDto.AbstractEventEnum.DELETE_LIKE.getKey() == event.getEventType()){
            try {
                distributedLockByCurator.acquireMyDistributedLock( actionPath,loginPath);
                if( ActionInput.circleList.contains(event.getFollowType())){
                    fungoCacheArticle.removeIndexDecodeCache(false, FungoCoreApiConstant.PUB_CIRCLE);
                    distributedLockByCurator.updateZKNode( actionPath,"CIRCLE");
                }else if(ActionInput.postList.contains( event.getFollowType() )){
                    fungoCacheArticle.removeIndexDecodeCache(false, FungoCoreApiConstant.PUB_POST);
                    distributedLockByCurator.updateZKNode( actionPath,"POST");
                }
            }catch (Exception e){
                logger.error( "修改登陆人数分布式计数器失败",e);
            }finally {
                distributedLockByCurator.releaseMyDistributedLock( actionPath,loginPath);
            }
        }else if(AbstractEventDto.AbstractEventEnum.EDIT_USER.getKey() == event.getEventType()){
            memberId = event.getUserId();
            scoreRuleServiceImpl.achieveScoreRule( memberId, NewTaskStatusEnum.EDITUSER_EXP.getKey());
            scoreRuleServiceImpl.achieveCoinRule( memberId, NewTaskStatusEnum.EDITUSER_COIN.getKey());
        }else if(AbstractEventDto.AbstractEventEnum.FOLLOW_OFFICIAL_USER.getKey() == event.getEventType()){
            memberId = event.getUserId();
            scoreRuleServiceImpl.achieveScoreRule( memberId, NewTaskStatusEnum.FOLLOWOFFICIALUSER_EXP.getKey());
            scoreRuleServiceImpl.achieveCoinRule( memberId, NewTaskStatusEnum.FOLLOWOFFICIALUSER_COIN.getKey());
        }else if(AbstractEventDto.AbstractEventEnum.BROWSE_SHOP.getKey() == event.getEventType()){
            memberId = event.getUserId();
            scoreRuleServiceImpl.achieveScoreRule( memberId, NewTaskStatusEnum.BROWSESHOP_EXP.getKey());
            scoreRuleServiceImpl.achieveCoinRule( memberId, NewTaskStatusEnum.BROWSESHOP_COIN.getKey());
        }else if(AbstractEventDto.AbstractEventEnum.BINDQQ_USER.getKey() == event.getEventType()){
            memberId = event.getUserId();
            scoreRuleServiceImpl.achieveScoreRule( memberId, NewTaskStatusEnum.BINDQQ_EXP.getKey());
            scoreRuleServiceImpl.achieveCoinRule( memberId, NewTaskStatusEnum.BINDQQ_COIN.getKey());
        }else if(AbstractEventDto.AbstractEventEnum.BINDWECHAT_USER.getKey() == event.getEventType()){
            memberId = event.getUserId();
            scoreRuleServiceImpl.achieveScoreRule( memberId, NewTaskStatusEnum.BINDWECHAT_Exp.getKey());
            scoreRuleServiceImpl.achieveCoinRule( memberId, NewTaskStatusEnum.BINDWECHAT_COIN.getKey());
        }else if(AbstractEventDto.AbstractEventEnum.BINDWEIBO_USER.getKey() == event.getEventType()){
            memberId = event.getUserId();
            scoreRuleServiceImpl.achieveScoreRule( memberId, NewTaskStatusEnum.BINDWEIBO_EXP.getKey());
            scoreRuleServiceImpl.achieveCoinRule( memberId, NewTaskStatusEnum.BINDWEIBO_COIN.getKey());
        }else if(AbstractEventDto.AbstractEventEnum.OPENPUSH_USER.getKey() == event.getEventType()){
            String userId = event.getUserId();
            scoreRuleServiceImpl.achieveScoreRule( userId, NewTaskStatusEnum.OPENPUSH_EXP.getKey());
            scoreRuleServiceImpl.achieveCoinRule( userId, NewTaskStatusEnum.OPENPUSH_COIN.getKey());
        }else if(AbstractEventDto.AbstractEventEnum.VPN_GAME.getKey() == event.getEventType()){
            memberId = event.getUserId();
            scoreRuleServiceImpl.achieveScoreRule( memberId, NewTaskStatusEnum.VPN_EXP.getKey());
            scoreRuleServiceImpl.achieveCoinRule( memberId, NewTaskStatusEnum.VPN_COIN.getKey());
        }else if(AbstractEventDto.AbstractEventEnum.TASK_USER_CHECK.getKey() == event.getEventType()){
            memberId = event.getUserId();
            taskServiceImpl.taskCheckUserFollowOfficialUser(memberId);
            taskServiceImpl.taskCheckUserFollowOfficialCircle( memberId );
            taskServiceImpl.taskCheckUserBindQQWeiboWechat( memberId);
        }else if(AbstractEventDto.AbstractEventEnum.EDIT_USER_MESSAGE.getKey() == event.getEventType()){
            memberId = event.getUserId();
            Integer type = event.getScore();
            if(0 == type){
                memberServiceImpl.addActionTypeNotice(memberId,  MessageConstants.SYSTEM_NOTICE_USER_ON,"3");
            }else if(1 == type){
                memberServiceImpl.addActionTypeNotice(memberId,  MessageConstants.SYSTEM_NOTICE_USER_OFF,"3");
            }

        }
        if(!CommonUtil.isNull( memberId )){
            String keyPreffix = FungoCoreApiConstant.FUNGO_CORE_API_TASK_USER_TASK_PROGRESS + "-" + memberId;
            fungoCacheTask.excIndexCache(false,keyPreffix,null,null  );
            fungoCacheTask.excIndexCache(false,FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + memberId,null,null  );
            fungoCacheTask.excIndexCache(false,FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_RANKS_LEVEL + memberId,null,null  );
            fungoCacheArticle.removeIndexDecodeCache(false,"*"  );
        }

    }


}
