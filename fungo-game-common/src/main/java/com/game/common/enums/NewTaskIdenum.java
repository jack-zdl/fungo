package com.game.common.enums;

/**
 * <p></p>
 * V2.4.5 历史上新手任务已成功的id
 * 14da45efc3da46998311f53b8776b1cf  修改头像
 * 2d10aa7101254f43b5e67704191845d8  修改头像
 * 32284a74247e4989b19a040b36fb88b3 修改昵称
 * 63d6138f12d44460a8fe4177b944d5f8  修改昵称
 * 4a421f7498dc406d85a9878a36ea6edd  绑定QQ/微信/微博
 * 93ce12c813894196aff25537ecb0cefd  绑定QQ/微信/微博
 * cf8c50afe52a4083b044162ce6f04063 修改个人简介
 * 87716e60394643228507b681110e0726  修改个人简介
 * @Author: dl.zhang
 * @Date: 2019/4/11
 */
public enum NewTaskIdenum implements BaseEnum<NewTaskIdenum,String>{

    HEAD_EXP("4920526ee587415480bdd7111e1206fb","修改头像exp"),
    HEAD_CION("606ec0fb8e18417bbb1e65e02c16f4d5","修改头像Coin"),

    NICKNAME_EXP("1e62c86d504f4d908be1ff1279cb753f","修改昵称exp"),
    NICKNAME_COIN("98e3e6dd9f8b437c8ad5ae9b9acfb368","修改昵称Coin"),

    BINDING_QQ_EXP("23b739927f2e418cb788b67d27fd9a5f","绑定QQ"),
    BINDING_QQ_COIN("0dd52f0b00664e12aec718f1cb2d470d","绑定QQ"),

    BINDING_WX_EXP("ab5d7fab10a14adcb5625c0a77e7a384","绑定微信"),
    BINDING_WX_COIN("76af1ef556d34dc4bb3b35375ba2f200","绑定微信"),

    BINDING_WB_EXP("2b9e4636f3da403581b22404e6a27f20","绑定微博"),
    BINDING_WB_COIN("6a555f4013f84bb48cada2aff4d9bdb0","绑定微博"),

    RESUME_EXP("66ccf4a3083b4a60adb7a57c657b126d","修改个人简介Exp"),
    RESUME_COIN("224185f746cf465abbd5b745dbb85c46","修改个人简介Coin"),

    FOLLOWOFFICIALUSER_EXP("8","一键关注官方用户Exp"),
    FOLLOWOFFICIALUSER_COIN("4","一键关注官方用户Exp"),
    ;



    String key;
    String value;
    NewTaskIdenum(String k, String v){
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
