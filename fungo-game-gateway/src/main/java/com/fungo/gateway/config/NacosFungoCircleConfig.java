package com.fungo.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * <p>nacos的config配置
 * dl.zhang
 * @Date: 2019/7/1
 */
@Component
@RefreshScope
public class NacosFungoCircleConfig {

    @Value( value = "${fungo.cloud.rsa.publicKey}")
    public String rsaPublicKey;

    @Value( value = "${fungo.cloud.rsa.privateKey}")
    public String rsaPrivateKey;

    @Value( value = "${fungo.cloud.festival.days}")
    public int festivalDays;

    public String getRsaPublicKey() {
        return rsaPublicKey;
    }

    public void setRsaPublicKey(String rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
    }

    public String getRsaPrivateKey() {
        return rsaPrivateKey;
    }

    public void setRsaPrivateKey(String rsaPrivateKey) {
        this.rsaPrivateKey = rsaPrivateKey;
    }

    public int getFestivalDays() {
        return festivalDays;
    }

    public void setFestivalDays(int festivalDays) {
        this.festivalDays = festivalDays;
    }
}
