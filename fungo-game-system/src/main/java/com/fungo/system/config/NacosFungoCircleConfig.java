package com.fungo.system.config;

import lombok.extern.slf4j.Slf4j;
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
public class NacosFungoCircleConfig {


    @Value(value = "${fungo.mall.mallGoodImagesType}")
    private boolean mallGoodImagesType;


    public boolean isMallGoodImagesType() {
        return mallGoodImagesType;
    }

    public void setMallGoodImagesType(boolean mallGoodImagesType) {
        this.mallGoodImagesType = mallGoodImagesType;
    }
}
