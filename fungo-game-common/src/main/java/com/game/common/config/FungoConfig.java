package com.game.common.config;

/**
 * <p>每个版本的redis的key的tag
 * dl.zhang
 * @Date: 2019/7/1
 */
public class FungoConfig {

//    @Value(value = "${fungo.redis.key.tag:v2.5}")
    private String tag  = "_cloudv2.5_";

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
