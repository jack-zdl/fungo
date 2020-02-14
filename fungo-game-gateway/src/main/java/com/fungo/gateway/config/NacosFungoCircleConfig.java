package com.fungo.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * <p>nacos的config配置
 * dl.zhang
 * @Date: 2019/7/1
 */
@Component
@RefreshScope
public class NacosFungoCircleConfig  implements ApplicationRunner {

    @Value( value = "${fungo.cloud.rsa.publicKey}")
    public String rsaPublicKey;

    @Value( value = "${fungo.cloud.rsa.privateKey}")
    public String rsaPrivateKey;

    @Value( value = "${fungo.cloud.festival.days}")
    public int festivalDays;

    @Value( value = "${fungo.cloud.rsa.keyString}")
    public String keyString;

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

    public String getKeyString() {
        return keyString;
    }

    public void setKeyString(String keyString) {
        this.keyString = keyString;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        System.out.println("我第一个启动");
//        System.out.println("哈哈ip" + keyString);
//        System.out.println("哈哈port" + rsaPrivateKey);
//        NacosFungoCircleConfig cbeConfigAfter = new  NacosFungoCircleConfig();
//        System.out.println("哈哈1ip" + cbeConfigAfter.getKeyString());
//        System.out.println("哈哈1port" + cbeConfigAfter.getRsaPrivateKey());
//        NacosFungoCircleConfig cbeConfig = NacosFungoCircleConfig.getInstance();
//        System.out.println("哈哈2ip" + cbeConfig.getKeyString());
//        System.out.println("哈哈2port" + cbeConfig.getRsaPrivateKey());
//        System.out.println("设置完毕");

    }

//
//    private static NacosFungoCircleConfig cbeConfigAfter;
//
//    public static synchronized NacosFungoCircleConfig getInstance() {
//        if (cbeConfigAfter == null) {
//            cbeConfigAfter = new NacosFungoCircleConfig();
//        }
//        return cbeConfigAfter;
//    }


}
