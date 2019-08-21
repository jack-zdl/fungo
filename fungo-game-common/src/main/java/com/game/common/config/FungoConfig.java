package com.game.common.config;

import com.game.common.util.SpringBeanFactory;

/**
 * <p>每个版本的redis的key的tag
 * dl.zhang
 * @Date: 2019/7/1
 */
public class FungoConfig {

//    @Value(value = "${fungo.redis.key.tag:v2.5}")
    private String tag  =  SpringBeanFactory.getProperty("spring.redis.keysuffix");  //"_cloudv2.5_";

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
