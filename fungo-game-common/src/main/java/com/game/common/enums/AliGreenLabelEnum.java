package com.game.common.enums;

/**
 * <p>阿里云内容安全您指定的场景对应的label分类参数</p>
 * @Date: 2019/11/8
 */
public enum AliGreenLabelEnum implements BaseEnum<AliGreenLabelEnum,String>{

    NORMAL("normal","正常文本"),
    SPAM("spam","含垃圾信息"),
    AD("ad","广告"),
    POLITICS("politics","涉政"),
    TERRORISM("terrorism","暴恐"),
    ABUSE("abuse","辱骂"),
    PORN("porn","色情"),
    FLOOD("flood","灌水"),
    CONTRABAND("contraband","违禁"),
    MEANINGLESS("meaningless","无意义"),
    CUSTOMIZED("customized","自定义");

    String key;
    String value;

    AliGreenLabelEnum(String s, String s1) {
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
