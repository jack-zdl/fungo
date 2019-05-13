package com.game.bus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RefreshScope注解的作用: 如果刷新了bean，那么下一次访问bean(即执行一个方法)时就会创建一个新实例
@RestController
@RefreshScope
public class GetPropertyFromConfigServerController {

    private static Logger logger = LoggerFactory.getLogger(GetPropertyFromConfigServerController.class);

//    @Value("${com.fungo.system.status}")
//    String appName;

//    @Value("${my-config.appName}")
//    String message;

    @RequestMapping("/getPropertyFromConfigServer")
    public String getPropertyFromConfigServer() {
        String msg = "hello, i am " ; //+appName  + ", i'm come from config server";
        logger.info(msg);
        return msg;
    }
}
