package com.fungo.games.helper.zookeeper;

import com.fungo.games.config.CuratorConfiguration;
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
     *   String keyPath = "/" + curatorConfiguration.getNoticeLock() + "/" + curatorConfiguration.getNoticeChildrenLock()+path;
     *   "/" + curatorConfiguration.getNoticeLock()  父节点
     *   "/" + curatorConfiguration.getNoticeChildrenLock()+path; 临时子节点
     */
    public void acquireDistributedLock(String path) {
        String keyPath = "/" + curatorConfiguration.getNoticeLock() + "/" + curatorConfiguration.getNoticeChildrenLock()+path;
        while (true) {
            try {
                curatorFramework
                        .create()
                        .creatingParentsIfNeeded()
                        .withMode( CreateMode.EPHEMERAL)
                        .withACL( ZooDefs.Ids.OPEN_ACL_UNSAFE) // 子节点
                        .forPath(keyPath);
                logger.info("games success to acquire lock for path:{}", keyPath);
                break;
            } catch (Exception e) {
                logger.info(" games failed to acquire lock for path:{}", keyPath);
                logger.info("games  while try again .......");
                try {
                    if (countDownLatch.getCount() <= 0) {
                        countDownLatch = new CountDownLatch(1);
                    }
                    countDownLatch.await();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
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
            logger.error("games failed to release lock");
            return false;
        }
        return true;
    }

    /**
     * 创建 watcher 事件
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
                logger.info("games  success to release lock for path:{}", oldPath);
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
                        .withMode(CreateMode.PERSISTENT)    // PERSISTENT
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)   // 父节点
                        .forPath(path);
            }
            addWatcher(curatorConfiguration.getNoticeLock());
            logger.info("games root path 的 watcher 事件创建成功");
        } catch (Exception e) {
            logger.error("games connect zookeeper fail，please check the log >> {}", e.getMessage(), e);
        }
    }

    /**
     * 检查该节点是否存在
     * @param path
     * @return
     */
    //检查临时节点
    public boolean checkTemporaryPropertiesSet(String path) {
        String keyPath = "/" + curatorConfiguration.getNoticeLock() + "/" + curatorConfiguration.getNoticeChildrenLock()+path;
        try {
            if (curatorFramework.checkExists().forPath(keyPath) != null) {
               return false;
            }
            logger.info("games root path 的 watcher 事件创建成功");
        } catch (Exception e) {
            logger.error("games temporaryPropertiesSet {}", e.getMessage(), e);
        }
        return true;
    }
}
