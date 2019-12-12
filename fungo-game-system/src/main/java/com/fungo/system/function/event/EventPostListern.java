package com.fungo.system.function.event;

import com.fungo.system.config.CuratorConfiguration;
import com.fungo.system.helper.zookeeper.DistributedLockByCurator;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.AbstractEventDto;
import com.game.common.dto.ActionInput;
import com.game.common.repo.cache.facade.FungoCacheArticle;
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
    @Override

    @Async
    public void onApplicationEvent(AbstractEventDto event) {
        String lockPath = curatorConfiguration.getCounterLock();
        String loginPath = curatorConfiguration.getLoginNum();
        String actionPath = curatorConfiguration.getActionLock();
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
        }
    }


}
