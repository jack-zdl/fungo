package com.game.common.buriedpoint.model;

import java.util.List;

/**
 *  埋点 - 评论 回复实体
 */
public class BuriedPointReplyModel extends BuriedPointModel{
    /**
     * 回复类型 回复作者/回复评论者
     */
    private String type;

    /**
     *  被回复用户昵称
     */
    private  String nickname;

    /**
     * 圈子名集合
     */
    private List<String> channel;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<String> getChannel() {
        return channel;
    }

    public void setChannel(List<String> channel) {
        this.channel = channel;
    }

}
