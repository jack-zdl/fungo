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
    CODE_GAME_THREE("1003","","该游戏不存在"),
    CODE_GAME_FOUR("1004","已删除该游戏评论","没有传入游戏评论主键"),

    CODE_SYSTEM_ONE("3001","查询banner列表成功",""),
    CODE_SYSTEM_TWO("3002","查询成功","查询失败，请联系管理员"),
    CODE_SYSTEM_THREE("3003","相互关注成功","相互关注失败"),
    CODE_SYSTEM_FOUR("3004","关注成功","关注失败"),
    CODE_SYSTEM_FESTIVAL_SWITCH_ON("3005","中秋开关已开启",""),
    CODE_SYSTEM_FESTIVAL_SWITCH_OFF("3006","中秋开关已关闭",""),
    CODE_SYSTEM_FIVE("3005","","用户信息异常"),
    CODE_SYSTEM_SIX("3006","","好遗憾，你的Fun币不够哦，快去做任务攒Fun币吧～"),
    CODE_SYSTEM_SEVEN("3007","","抱歉,中秋抽奖库存已无!"),
    CODE_SYSTEM_FESTIVAL_EIGHT("3008","分享成功!","抱歉你已经分享过了");

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
