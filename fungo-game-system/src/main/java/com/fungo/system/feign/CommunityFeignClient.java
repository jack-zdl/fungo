package com.fungo.system.feign;

import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.community.CmmPostDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @RequestMapping(value = "/api/content/community/post", method = RequestMethod.POST)
    int selectPostCount(CmmPostDto cmmPost);

    /**
     * 动态表 辅助计数器
     * @param map
     * @return
     */
    @RequestMapping(value = "/api/update/counter", method = RequestMethod.POST)
    boolean updateCounter(Map<String, String> map);

    /**
     * 被点赞用户的id
     * @param map
     * @return
     */
    @RequestMapping(value = "/api/getMemberIdByTargetId", method = RequestMethod.POST)
    String getMemberIdByTargetId(Map<String, String> map);

    /**
     * 查询社区帖子|文章数据
     * @return
     */
    @PostMapping("/ms/service/cmm/post/lists")
    FungoPageResultDto<CmmPostDto> queryCmmPostList(@RequestBody CmmPostDto cmmPostDto);
}
