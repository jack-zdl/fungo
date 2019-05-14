package com.fungo.system.proxy.impl;

import com.fungo.system.feign.CommunityFeignClient;
import com.fungo.system.proxy.IGameProxyService;
import com.fungo.system.service.impl.MemberIncentRiskServiceImpl;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.apache.poi.openxml4j.opc.PackagingURIHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/13
 */
@Service
public class GameProxyServiceImpl implements IGameProxyService {

    private static final Logger logger = LoggerFactory.getLogger(GameProxyServiceImpl.class);

    @Autowired
    private CommunityFeignClient communityFeignClient;

    @HystrixCommand(fallbackMethod = "hystrixGetMemberIdByTargetId",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public String getMemberIdByTargetId(Map<String, String> map) {
        return communityFeignClient.getMemberIdByTargetId(map);
    }


    public String hystrixGetMemberIdByTargetId(Map<String, String> map){
        logger.warn("GameProxyServiceImpl.getMemberIdByTargetId获取被点赞用户的id异常");
        return null;
    }

}
