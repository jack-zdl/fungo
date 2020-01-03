package com.game.common.enums;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/12/27
 */
public enum  NewTaskStatusEnum implements BaseEnum<NewTaskStatusEnum,String> {

    JOINOFFICIALCIRLCE_EXP("2","加入三个官方圈子Exp"),
    JOINOFFICIALCIRLCE_COIN("1","加入三个官方圈子Coin"),
    FOLLOWOFFICIALUSER_EXP("8","一键关注官方用户Exp"),
    FOLLOWOFFICIALUSER_COIN("4","一键关注官方用户Coin"),
    EDITUSER_EXP("2048","完善个人简介Exp"),
    EDITUSER_COIN("1024","完善个人简介Coin"),
    BROWSESHOP_EXP("32","浏览礼包乐园Exp"),
    BROWSESHOP_COIN("64","浏览礼包乐园Coin"),
    BINDQQ_EXP("4096","绑定QQExp"),
    BINDQQ_COIN("8192","绑定QQCoin"),
    BINDWECHAT_Exp("32768","绑定微信Coin"),
    BINDWECHAT_COIN("16384","绑定微信Coin"),
    BINDWEIBO_EXP("65536","绑定微博Exp"),
    BINDWEIBO_COIN("131072","绑定微信Coin"),
    OPENPUSH_EXP("128","开启推送Exp"),
    OPENPUSH_COIN("64","开启推送Coin"),
    VPN_EXP("512","加速器界面成功导入一款游戏Exp"),
    VPN_COIN("256","加速器界面成功导入一款游戏Coin")
            ;

    String key;
    String value;
    NewTaskStatusEnum(String k, String v){
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
