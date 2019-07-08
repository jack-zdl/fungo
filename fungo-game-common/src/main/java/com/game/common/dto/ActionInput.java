package com.game.common.dto;

import com.game.common.enums.BaseEnum;

public class ActionInput {
    /**
     * 关注目标id
     */
    private String target_id;

    /**
     * 关注目标类型：
     *  用户 | 0
     *  帖子 | 1
     *  心情 | 2
     *  游戏 | 3
     *  社区 | 4
     *  帖子评论 | 5
     *  游戏评价 | 6
     *  回复 | 7
     *  心情评论 | 8
     *  意见反馈 | 9
     *  游戏标签 | 10
     *  圈子  / 11   @zdl 2019/6/18
     */
    private int target_type;

    /**
     * 备注信息
     */
    private String information;

    /**
     * 被关注用户id
     */
    private String user_id;

    public String getTarget_id() {
        return target_id;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }

    public int getTarget_type() {
        return target_type;
    }

    public void setTarget_type(int target_type) {
        this.target_type = target_type;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public enum ActionEnum implements BaseEnum<ActionEnum,String> {
        CIRCLE("11","circle");

        String key;
        String value;

        ActionEnum(String s, String s1) {
            this.key = s;
            this.value = s1;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }
    }
}


