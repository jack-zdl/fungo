package com.fungo.community.config;


import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.api.config.annotation.NacosProperty;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import jdk.nashorn.internal.objects.annotations.Property;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Properties;


/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/7/1
 */
@Component
@Slf4j
@RefreshScope
public class NacosFungoCircleConfig   { //implements CommandLineRunner


    private static final Logger LOGGER = LoggerFactory.getLogger(NacosFungoCircleConfig.class);

    @Value(value = "${cirlce.number:3}")
    public int value ;


    @Value(value = "${nacos.config:1}")
    public int config;

    public int getValue(){
        System.out.println("-----------------"+value);
        System.out.println("-----------------"+config);
        return value;
    }



}
