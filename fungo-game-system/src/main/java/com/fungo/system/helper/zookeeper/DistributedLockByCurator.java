package com.fungo.system.helper.zookeeper;

import com.fungo.system.config.CuratorConfiguration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * <p>zk客户端基础服务类</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/7/29
 */
@Component
public class DistributedLockByCurator implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(DistributedLockByCurator.class);

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @Autowired
    private CuratorFramework curatorFramework;
    @Autowired
    private CuratorConfiguration curatorConfiguration;


    /**
     * 获取分布式锁
     */
    public void acquireDistributedLock(String path) {
        String keyPath = "/" + curatorConfiguration.getNoticeLock() + "/"+curatorConfiguration.getNoticeChildrenLock() + path;
        while (true) {
            try {
                curatorFramework
                        .create()
                        .creatingParentsIfNeeded()
                        .withMode( CreateMode.EPHEMERAL)
                        .withACL( ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(keyPath);
                logger.info("systyem success to acquire lock for path:{}", keyPath);
                break;
            } catch (Exception e) {
                logger.info("systyem failed to acquire lock for path:{}", keyPath);
                logger.info("systyem while try again .......");
                try {
                    if (countDownLatch.getCount() <= 0) {
                        countDownLatch = new CountDownLatch(1);
                    }
                    countDownLatch.await();
                } catch (InterruptedException e1) {
                    logger.error( "acquireDistributedLock 转换异常",e1 );
                }
            }
        }
    }

    /**
     * 释放分布式锁
     */
    public boolean releaseDistributedLock(String path) {
        try {
            String keyPath = "/" + curatorConfiguration.getNoticeLock() + "/"+curatorConfiguration.getNoticeChildrenLock() + path;
            if (curatorFramework.checkExists().forPath(keyPath) != null) {
                curatorFramework.delete().forPath(keyPath);
            }
        } catch (Exception e) {
            logger.error("systyem failed to release lock");
            return false;
        }
        return true;
    }

    /**
     * 创建 watcher 事件  监听事件
     */
    private void addWatcher(String path) throws Exception {
        String keyPath;
        if (path.equals(curatorConfiguration.getNoticeLock())) {
            keyPath = "/" + path;
        } else {
            keyPath = "/" + curatorConfiguration.getNoticeLock() + "/" + path;
        }
        final PathChildrenCache cache = new PathChildrenCache(curatorFramework, keyPath, false);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener((client, event) -> {
            if (event.getType().equals( PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                String oldPath = event.getData().getPath();
                logger.info("监听事件 systyem success to release lock for path:{}", oldPath);
                if (oldPath.contains(path)) {
                    //释放计数器，让当前的请求获取锁
                    countDownLatch.countDown();
                }
            }
        });
    }

    //创建父节点，并创建永久节点
    @Override
    public void afterPropertiesSet() {
        curatorFramework = curatorFramework.usingNamespace(curatorConfiguration.getNamespaceLock());
        String path = "/" + curatorConfiguration.getNoticeLock();
        try {
            if (curatorFramework.checkExists().forPath(path) == null) {
                curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(path);
            }
            addWatcher(curatorConfiguration.getNoticeLock());
            logger.info("systyem root path 的 watcher 事件创建成功");
        } catch (Exception e) {
            logger.error("systyem connect zookeeper fail，please check the log >> {}", e.getMessage(), e);
        }
    }
}
