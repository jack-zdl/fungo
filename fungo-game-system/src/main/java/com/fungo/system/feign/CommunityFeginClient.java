package com.fungo.system.feign;

import com.game.common.dto.community.CmmPostDto;


import java.util.Map;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/6
 */
//@FeignClient(name = "FUNGO-GAME-COMMUNITY")
// 2019-05-08
// feignclient启动冲突
// lyc
public interface CommunityFeginClient {

//    @RequestMapping(value = "/api/content/community/post", method = RequestMethod.POST)
    int selectPostCount(CmmPostDto cmmPost);

    /**
     * 动态表 辅助计数器
     * @param map
     * @return
     */
//    @RequestMapping(value = "/api/content/games", method = RequestMethod.POST)
    boolean updateCounter(Map<String, String> map);
}
