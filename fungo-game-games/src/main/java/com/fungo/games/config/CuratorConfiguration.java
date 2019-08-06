package com.fungo.games.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>zk客戶端Curator使用</p>
 * @Author: dl.zhang
 * @Date: 2019/7/29
 */
@Configuration
@RefreshScope
public class CuratorConfiguration {
    //重试次数
    @Value("${curator.retryCount}")
    private int retryCount;
    //重试间隔时间
    @Value("${curator.elapsedTimeMs}")
    private int elapsedTimeMs;
    //zookeeper 地址
    @Value("${curator.connectString}")
    private String connectString;
    //session超时时间
    @Value("${curator.sessionTimeoutMs}")
    private int sessionTimeoutMs;
    //连接超时时间
    @Value("${curator.connectionTimeoutMs}")
    private int connectionTimeoutMs;
    //zk永久父节点
    @Value( value = "${zk.notice.lock}")
    private String  noticeLock;
    //notice-memberId子节点
    @Value( value = "${zk.notice.children.lock}")
    private String noticeChildrenLock;

    @Value( value = "${zk.namespace.notice.lock}")
    private String namespaceLock;

    // 创建Curator客户端
    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework() {
//        RetryPolicy retryPolicy = new ExponentialBackoffRetry(elapsedTimeMs, retryCount);
        return CuratorFrameworkFactory.newClient(
                connectString,
                sessionTimeoutMs,
                connectionTimeoutMs,
                new RetryNTimes(retryCount, elapsedTimeMs));
    }

    public String getNoticeLock() {
        return noticeLock;
    }

    public void setNoticeLock(String noticeLock) {
        this.noticeLock = noticeLock;
    }

    public String getNoticeChildrenLock() {
        return noticeChildrenLock;
    }

    public void setNoticeChildrenLock(String noticeChildrenLock) {
        this.noticeChildrenLock = noticeChildrenLock;
    }

    public String getNamespaceLock() {
        return namespaceLock;
    }

    public void setNamespaceLock(String namespaceLock) {
        this.namespaceLock = namespaceLock;
    }
}
