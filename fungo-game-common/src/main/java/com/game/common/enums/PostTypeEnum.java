package com.game.common.enums;

import com.game.common.enums.circle.CircleTypeEnum;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/7/2
 */
public enum  PostTypeEnum implements BaseEnum<PostTypeEnum,String> {
    COMMON("1","普通"),CREAM("2","精华"),TOP("3","置顶");

    String key;
    String value;

    PostTypeEnum(String s, String s1) {
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
