package com.game.common.enums;

/**
 * <p>返回给前端code</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/21
 */
public enum AbstractResultEnum {

    CODE_ONE("1001","该游戏已开通圈子",""),
    CODE_TWO("1002","该游戏未开通圈子","");


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
