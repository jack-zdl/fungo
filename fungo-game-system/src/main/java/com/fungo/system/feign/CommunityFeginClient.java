package com.fungo.system.feign;

import com.game.common.dto.community.CmmPostDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/6
 */
@FeignClient(name = "FUNGO-GAME-COMMUNITY")
public interface CommunityFeginClient {

    @RequestMapping(value = "/api/content/community/post", method = RequestMethod.POST)
    int selectPostCount(CmmPostDto cmmPost);
}
