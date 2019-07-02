package com.fungo.community.config;


import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;


/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/7/1
 */
@Service
@RefreshScope
public class NacosFungoCircleConfig   { //implements CommandLineRunner


    private static final Logger LOGGER = LoggerFactory.getLogger(NacosFungoCircleConfig.class);

//    /**
//     * ${name:hello}:key=name,默认值=hello
//     */
    @Value(value = "${cirlce.number:3}")
    public String circle;

//    @Override
    public void run(String... args) throws Exception {
        while (true) {
            Thread.sleep(4000);
            LOGGER.info("[NacosConfigAnnoatationService]注解方式获取到的配置项目,circle={}");
        }
    }
}
