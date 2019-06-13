package com.game.common.enums;

/**
 * <p>动作行为枚举类</p>
 * <p>
 * 行为类型
 * 点赞 | 0
 * 回复 | 1
 * 评论 | 2
 * 分享 | 3
 * 收藏 | 4
 * 关注 | 5
 * 举报 | 6
 * 下载 | 7
 * 推荐 | 8
 * 不推荐 | 9
 * 忽略 | 10
 * 发帖 | 11
 * 浏览 | 12
 * 登录 | 13
 * 发布心情 | 14
 * 不喜欢 | 15(未使用)
 * weberror | 16
 * </p>
 * @Author: dl.zhang
 * @Date: 2019/6/11
 */
public enum ActionTypeEnum implements BaseEnum<ActionTypeEnum, String> {
    LIKE("0","点赞"),
    REPLY("1","回复"),
    COMMENT("2","评论"),
    SHARE("3","分享"),
    COLLECT("4","收藏"),
    FOLLOW("5","关注"),
    REPORT("6","举报"),
    DOWNLOAD("7","下载"),
    RECOMMEND("8","推荐"),
    UNRECOMMEND("9","不推荐"),
    IGNORE("10","忽略"),
    POST("10","发帖"),
    BROWSE("11","浏览"),
    LOGIN("12","登陆"),
    MOOD("13","心情");

    String key;
    String value;

    ActionTypeEnum(String k, String v) {
        this.key = k;
        this.value = v;
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

enum ActionTargetTypeEnum implements BaseEnum<ActionTargetTypeEnum, String> {
    REPLY("0","回复");

    String key;
    String value;

    ActionTargetTypeEnum(String k, String v){
        this.key = k;
        this.value = v;
    }
    @Override
    public String getKey() {
        return null;
    }

    @Override
    public String getValue() {
        return null;
    }
}
