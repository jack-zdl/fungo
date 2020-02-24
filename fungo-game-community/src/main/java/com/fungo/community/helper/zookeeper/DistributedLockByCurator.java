package com.fungo.community.helper.zookeeper;

import com.fungo.community.config.CuratorConfiguration;
import com.fungo.community.helper.RedisActionHelper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
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
    @Autowired
    private RedisActionHelper redisActionHelper;


    /**
     * 获取分布式锁
     */
    public void acquireDistributedLock(String path) {
        String keyPath = "/" + curatorConfiguration.getNoticeLock() + "/" + curatorConfiguration.getNoticeChildrenLock()+path;
        while (true) {
            try {
                curatorFramework
                        .create()
                        .creatingParentsIfNeeded()
                        .withMode( CreateMode.EPHEMERAL)
                        .withACL( ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(keyPath);
                break;
            } catch (Exception e) {
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
            String keyPath = "/" + curatorConfiguration.getNoticeLock() + "/" + curatorConfiguration.getNoticeChildrenLock()+path;
            if (curatorFramework.checkExists().forPath(keyPath) != null) {
                curatorFramework.delete().forPath(keyPath);
            }
        } catch (Exception e) {
            logger.error("community releaseDistributedLock function  failed to release lock");
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
        } catch (Exception e) {
            logger.error("community afterPropertiesSet function   connect zookeeper fail，please check the log >> {}", e.getMessage(), e);
        }

        // 建立永久节点
        try {
            String loginNumPath = "/" + curatorConfiguration.getActionLock();
            if (curatorFramework.checkExists().forPath(loginNumPath) == null) {
                curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(loginNumPath);
            }
            addMyWatcher(curatorConfiguration.getActionLock());
        }catch (Exception e){
            logger.error("systyem  create distributed counter file，please check the log >> {}", e.getMessage(), e);
        }
    }

    /**
     * 创建 watcher 事件  监听事件
     */
    private void addMyWatcher(String path) throws Exception {
        String keyPath = "/" + path;
        NodeCache nodeCache = new NodeCache(curatorFramework, keyPath, false);
        nodeCache.start();
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                String key = new String(nodeCache.getCurrentData().getData());
                System.out.println("重新获得节点内容为：" + key);
                redisActionHelper.removeEhcacheKey(key);
            }
        });
    }
}
