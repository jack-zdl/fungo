package com.game.common.enums;

/**
 * <p>返回给前端code</p>
 * <p>
 *     1xxx 代表游戏相关的
 * </p>
 * <p>
 *     2xxx 代表社区相关的
 * </p>
 * <p>
 *     3xxx 代表系统相关的
 * </p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/21
 */
public enum AbstractResultEnum {

    CODE_ONE("1001","该游戏已开通圈子",""),
    CODE_TWO("1002","该游戏未开通圈子",""),
    CODE_GAME_THREE("1003","","游戏已下架"),
    CODE_GAME_FOUR("1004","已删除该游戏评论","没有传入游戏评论主键"),
    CODE_GAME_FIVE("1005","并无查询对象","没有传入游戏信息"),

    CODE_SYSTEM_ONE("3001","查询banner列表成功",""),
    CODE_SYSTEM_TWO("3002","查询成功","查询失败，请联系管理员"),
    CODE_SYSTEM_THREE("3003","相互关注成功","相互关注失败"),
    CODE_SYSTEM_FOUR("3004","关注成功","关注失败"),
    CODE_SYSTEM_FESTIVAL_SWITCH_ON("3005","中秋开关已开启",""),
    CODE_SYSTEM_FESTIVAL_SWITCH_OFF("3006","中秋开关已关闭",""),
    CODE_SYSTEM_FIVE("3005","","用户信息异常"),
    CODE_SYSTEM_SIX("3006","","好遗憾，你的Fun币不够哦，快去做任务攒Fun币吧～"),
    CODE_SYSTEM_SEVEN("3007","","抱歉,中秋抽奖库存已无!"),
    CODE_SYSTEM_FESTIVAL_EIGHT("3008","分享成功!","抱歉你已经分享过了"),
    CODE_SYSTEM_FESTIVAL_NINE("3009","","参数有误"),
    CODE_SYSTEM_INVITATION_DAY("3010","你获得一张全局线路0.5天的免费体验券,赶紧去“福利”页面去点击使用吧","参数有误"),
    CODE_SYSTEM_INVITATION_TWODAY("3011","恭喜你到达LV2,系统赠送一张2天免费体验游戏线路的券,快去“福利”页面看看!","参数有误"),
    CODE_SYSTEM_INVITATION_FIVEDAY("3012","5天免费使用的全局线路体验券已到账，快去“福利”页面查看！","参数有误"),
    CODE_SYSTEM_INVITATION_TENSIXDAY("3013","16天免费使用的全局线路体验券已到账，快去“福利”页面查看！","参数有误"),
    CODE_SYSTEM_INVITATION_SIXSIXDAY("3014","66天免费使用的全局线路体验券已到账，快去“福利”页面查看！","参数有误"),
    CODE_SYSTEM_INVITATION_NINE("3015","您获得了一张九折加速器优惠券，快去“福利”页面点击使用吧！赶紧去邀请更多的用户，八折和七折券等你来拿哦~","参数有误"),
    CODE_SYSTEM_INVITATION_NGIHT("3016","您获得了一张八折加速器优惠券，快去“福利”页面点击使用吧！赶紧去邀请更多的用户，七折券等你来拿哦~","参数有误"),
    CODE_SYSTEM_FESTIVAL_SEVEN("3017","您获得了一张七折加速器优惠券，快去“福利”页面点击使用吧！","参数有误"),

    CODE_CLOUD_RSA_AUTHORITY("4001","","秘钥验证失败,请刷新页面"),
    CODE_CLOUD_MD5_AUTHORITY("4002","","参数验证失败"),
    CODE_CLOUD_IP_LIMIT_AUTHORITY("4003","","访问过于频繁"),
    CODE_CLOUD_NOT_FOUND("4004","","查询不到指定内容"),
    CODE_CLOUD_NOT_AUTH("4005","","对不起该用户已被禁言"),
    CODE_CLOUD_USER_NAME("4006","","昵称已被使用，请更换");

    String key;
    String successValue;
    String failevalue;

    AbstractResultEnum(String s, String successValue,String failevalue) {
        this.key = s;
        this.successValue = successValue;
        this.failevalue = failevalue;
    }

    public String getKey() {
        return key;
    }

    public String getSuccessValue() {
        return successValue;
    }

    public String getFailevalue() {
        return failevalue;
    }

}
