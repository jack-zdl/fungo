package com.fungo.system.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 *
 * @Author lyc
 * @create 2019/5/7 10:26
 */
@FeignClient(name = "FUNGO-GAME-COMMUNITY")
public interface CommunityFeignClient {

    @RequestMapping(value = "/api/content/games", method = RequestMethod.POST)
    boolean updateCounter(Map<String, String> map);
}
