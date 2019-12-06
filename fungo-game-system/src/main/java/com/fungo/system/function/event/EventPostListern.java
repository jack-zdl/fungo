package com.fungo.system.function.event;

import com.fungo.system.config.CuratorConfiguration;
import com.fungo.system.helper.zookeeper.DistributedLockByCurator;
import com.game.common.dto.AbstractEventDto;
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
    @Override
    @Async
    public void onApplicationEvent(AbstractEventDto event) {
        String lockPath = curatorConfiguration.getCounterLock();
        String loginPath = curatorConfiguration.getLoginNum();
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
        }
    }


}
