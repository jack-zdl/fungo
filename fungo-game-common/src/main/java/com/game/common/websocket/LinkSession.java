package com.game.common.websocket;

import javax.websocket.Session;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.Future;

/**
 * 代表一个连接
 * @author sam.shi
 *
 */
public class LinkSession implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sid = "";
    private String userId = "";
    private String udid;
    private Session session;
    private Date liveTime;
    private Date createTime;
    private String region = "";
    private String ipAndPort = "";

    /**
     * 客户端app版本
     */
    private String appversion;


    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public LinkSession() {

    }

    public LinkSession(String userId, Session session) {
        this.sid = session.getId();
        this.userId = userId;
        this.session = session;
        this.createTime = new Date();
        this.liveTime = new Date();
    }

    public LinkSession(String userId, Session session, String appversion) {
        this.sid = session.getId();
        this.userId = userId;
        this.session = session;
        this.createTime = new Date();
        this.liveTime = new Date();
        this.appversion = appversion;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public Future<Void> sendAsyncMessage(String message) throws IOException {
        return this.session.getAsyncRemote().sendText(message);
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getIpAndPort() {
        return ipAndPort;
    }

    public void setIpAndPort(String ipAndPort) {
        this.ipAndPort = ipAndPort;
    }


    public String getSid() {
        //return sid;
        return userId;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Date getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(Date liveTime) {
        this.liveTime = liveTime;
    }

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }
}
