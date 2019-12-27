package com.fungo.community.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
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

    @Value("${curator.retryCount}")
    private int retryCount;

    @Value("${curator.elapsedTimeMs}")
    private int elapsedTimeMs;

    @Value("${curator.connectString}")
    private String connectString;

    @Value("${curator.sessionTimeoutMs}")
    private int sessionTimeoutMs;

    @Value("${curator.connectionTimeoutMs}")
    private int connectionTimeoutMs;

    @Value( value = "${zk.notice.lock}")
    private String  noticeLock;

    @Value(value = "${zk.notice.children.lock}")
    private String noticeChildrenLock;
    @Value( value = "${zk.namespace.notice.lock}")
    private String namespaceLock;
    @Value( value = "${zk.notice.action}")
    private String  actionLock;

    @Value(value = "zk.notice.login")
    private String loginNum;

    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework() {
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

    public String getActionLock() {
        return actionLock;
    }

    public void setActionLock(String actionLock) {
        this.actionLock = actionLock;
    }

    public String getLoginNum() {
        return loginNum;
    }

    public void setLoginNum(String loginNum) {
        this.loginNum = loginNum;
    }
}
