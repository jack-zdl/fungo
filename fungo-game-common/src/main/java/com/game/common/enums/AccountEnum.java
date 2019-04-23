package com.game.common.enums;


/**
 * <p></p>     * 0 微博
 *      * 1 微信
 *      * 4 QQ
 *
 * @Author: dl.zhang
 * @Date: 2019/4/11
 */
public enum AccountEnum implements BaseEnum<AccountEnum,String>{

    WEIBO("0","微博"),
    WEIXIN ("1","微信"),
    QQ("4","QQ");

    String key;
    String value;

    AccountEnum(String s, String s1) {
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

    public static String getValue(int i){
        switch (i){
            case 0:
                return WEIBO.value;
            case 1:
                return WEIXIN.value;
            case 4:
                return QQ.value;
            default:
                return null;
        }
    }
}
