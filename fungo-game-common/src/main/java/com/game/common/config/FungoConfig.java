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

    private String pctag  =  SpringBeanFactory.getProperty("spring.redis.pckeysuffix");  //"_cloudv2.5_";

    private String ALIYUN_ACCESS_KEY_ID = SpringBeanFactory.getProperty("aliyun.green.ALIYUN_ACCESS_KEY_ID");

    private String ALIYUN_ACCESS_KEY_SECRET = SpringBeanFactory.getProperty("aliyun.green.ALIYUN_ACCESS_KEY_SECRET");

    private String REGION_ID = SpringBeanFactory.getProperty("aliyun.green.REGION_ID");

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPctag() {
        return pctag;
    }

    public void setPctag(String pctag) {
        this.pctag = pctag;
    }

    public String getALIYUN_ACCESS_KEY_ID() {
        return ALIYUN_ACCESS_KEY_ID;
    }

    public void setALIYUN_ACCESS_KEY_ID(String ALIYUN_ACCESS_KEY_ID) {
        this.ALIYUN_ACCESS_KEY_ID = ALIYUN_ACCESS_KEY_ID;
    }

    public String getALIYUN_ACCESS_KEY_SECRET() {
        return ALIYUN_ACCESS_KEY_SECRET;
    }

    public void setALIYUN_ACCESS_KEY_SECRET(String ALIYUN_ACCESS_KEY_SECRET) {
        this.ALIYUN_ACCESS_KEY_SECRET = ALIYUN_ACCESS_KEY_SECRET;
    }

    public String getREGION_ID() {
        return REGION_ID;
    }

    public void setREGION_ID(String REGION_ID) {
        this.REGION_ID = REGION_ID;
    }
}
