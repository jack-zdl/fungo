package com.game.common.enums;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/19
 */
public enum SystemTypeEnum implements BaseEnum<SystemTypeEnum,String> {
    ANDROID("Android","安卓"),IOS("iOS","苹果");

    String key;
    String value;

    SystemTypeEnum(String s, String s1) {
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
