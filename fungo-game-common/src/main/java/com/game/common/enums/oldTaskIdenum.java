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
public enum oldTaskIdenum implements BaseEnum<oldTaskIdenum, String> {
    HEAD_EXP("14da45efc3da46998311f53b8776b1cf", "修改头像exp"),
    HEAD_COIN("2d10aa7101254f43b5e67704191845d8", "修改头像Coin"),

    NICKNAME_EXP("32284a74247e4989b19a040b36fb88b3", "修改昵称Exp"),
    NICKNAME_COIN("63d6138f12d44460a8fe4177b944d5f8", "修改昵称Coin"),

    BINDING_EXP("4a421f7498dc406d85a9878a36ea6edd", "绑定QQ/微信/微博Exp"),
    BINDING_COIN("93ce12c813894196aff25537ecb0cefd", "绑定QQ/微信/微博Coin"),

    RESUME_EXP("cf8c50afe52a4083b044162ce6f04063", "修改个人简介Exp"),
    RESUME_COIN("87716e60394643228507b681110e0726", "修改个人简介Coin");

    String key;
    String value;

    oldTaskIdenum(String k, String v) {
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
