package com.game.common.enums;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2020/1/2
 */
public enum UserAuthLevelEnum  {

    NONE(0,"普通"),
    GENERAL(1,"普通"),
    CIRCLE(2,"圈主"),
    OFFICIAL(4,"官方");

    private int key;

    private String value;

    UserAuthLevelEnum(int key,String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
