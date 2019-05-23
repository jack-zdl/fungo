package com.fungo.regs.controller;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/21
 */
@RestController
public class BusinessController {
    private final static Logger LOG = LoggerFactory.getLogger(BusinessController.class);

    @Autowired
    private EurekaClient discoveryClient;



    @RequestMapping(path="/getServiceUrl/{servicename}", method= RequestMethod.GET)
    public String getServiceUrl(@PathVariable("servicename") String serviceName) {
        String serviceUrl = serviceName+" 无法获取微服务地址";
        try {
            InstanceInfo instance = discoveryClient.getNextServerFromEureka(serviceName, false);

            if(null != instance) {
                serviceUrl = instance.getHomePageUrl();
            }
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("无法获取微服务地址");
        }
        return serviceUrl;
    }

}
