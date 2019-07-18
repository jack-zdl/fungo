package com.fungo.community.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * <p>nacos的config配置
 * dl.zhang
 * @Date: 2019/7/1
 */
@Component
@Slf4j
@RefreshScope
public class NacosFungoCircleConfig   {

    /**
     * 功能描述: 圈子文章限制分类文章的数目
     * @auther: dl.zhang
     * @date: 2019/7/4 13:58
     */
    @Value(value = "${cirlce.postnumber}")
    private int value ;

    @Value(value = "${nacos.config:1}")
    private int config;

    @Value(value = "${fungo.redis.key.tag:v2.5}")
    private String tag;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getConfig() {
        return config;
    }

    public void setConfig(int config) {
        this.config = config;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
