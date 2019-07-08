package com.game.common.enums.circle;

import com.game.common.enums.BaseEnum;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/26
 */
public enum  CircleRecommendEnum implements BaseEnum<CircleTypeEnum,String> {
    COMMON("0","普通"),RECOMMEND("1","推荐");

    String key;
    String value;

    CircleRecommendEnum(String s, String s1) {
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
