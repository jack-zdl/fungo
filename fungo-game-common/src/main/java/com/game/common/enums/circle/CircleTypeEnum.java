package com.game.common.enums.circle;

import com.game.common.enums.BaseEnum;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/19
 */
public enum CircleTypeEnum implements BaseEnum<CircleTypeEnum,String> {
    OFFICIAL("1","官方"),GAME("2","游戏"),INTEREST("3","兴趣");

    String key;
    String value;

    CircleTypeEnum(String s, String s1) {
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
