package com.fungo.system;

import com.fungo.system.config.CuratorConfiguration;
import com.fungo.system.helper.zookeeper.DistributedLockByCurator;
import com.game.common.dto.AbstractEventDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/12/6
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ZKTest {

    @Autowired
    private DistributedLockByCurator distributedLockByCurator;

    @Autowired
    private CuratorConfiguration curatorConfiguration;

    @Test
    public void testZKupdateTest(){
        String lockPath = curatorConfiguration.getCounterLock();
        String loginPath = curatorConfiguration.getCounterLock();
            try {
                distributedLockByCurator.acquireMyDistributedLock( lockPath,loginPath);
                distributedLockByCurator.getZKNode(loginPath);
                distributedLockByCurator.updateZKNode( loginPath,1);
            }catch (Exception e){
                System.out.println( "修改登陆人数分布式计数器失败");
            }finally {
                distributedLockByCurator.releaseMyDistributedLock( lockPath,loginPath);
            }
    }
}
