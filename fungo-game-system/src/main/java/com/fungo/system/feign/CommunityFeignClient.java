package com.fungo.system.feign;

import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.*;
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

    /**
     * 查询社区帖子|文章数据
     * @return
     */
    @PostMapping("/ms/service/cmm/mood/lists")
    FungoPageResultDto<MooMoodDto> queryCmmMoodList(@RequestBody MooMoodDto mooMoodDto);

    /**
     * 心情评论的 分页查询
     * @return
     */
    @PostMapping("/ms/service/cmm/mood/cmt/lists")
    FungoPageResultDto<MooMessageDto> queryCmmMoodCommentList(@RequestBody MooMessageDto mooMessageDto);

    /**
     * 查询 社区心情总数
     * @return
     */
    @PostMapping("/ms/service/cmm/mood/count")
    ResultDto<Integer> queryCmmMoodCount(@RequestBody MooMoodDto mooMoodDto);

    /**
     * 二级评论总数
     * @return
     */
    @PostMapping("/ms/service/cmm/cmt/s/count")
    ResultDto<Integer> querySecondLevelCmtCount(@RequestBody CmmCmtReplyDto replyDto);

    /**
     * 分页查询 二级评论 数据
     * @return
     */
    @PostMapping("/ms/service/cmm/cmt/s/lists")
    FungoPageResultDto<CmmCmtReplyDto> querySecondLevelCmtList(@RequestBody CmmCmtReplyDto replyDto);

    /**
     * 分页查询 社区 数据
     * @return
     */
    @PostMapping("/ms/service/cmm/cty/lists")
    FungoPageResultDto<CmmCommunityDto> queryCmmPostList(@RequestBody CmmCommunityDto cmmCommunityDto);


    /**
     * 查询 社区置顶文章集合
     * @return
     */
    @PostMapping("/ms/service/cmm/post/topicPosts")
    FungoPageResultDto<CmmPostDto> listCmmPostTopicPost(@RequestBody CmmPostDto cmmPostDto);

    /**
     * 查询单个社区详情数据
     * @return
     */
    @PostMapping("/ms/service/cmm/cty")
    FungoPageResultDto<CmmCommunityDto> queryCmmCtyDetail(@RequestBody CmmCommunityDto cmmCommunityDto);

    /**
     * 查询 社区帖子总数
     * @return
     */
    @PostMapping("/ms/service/cmm/post/count")
    ResultDto<Integer> queryCmmPostCount(@RequestBody CmmPostDto cmmPostDto);

    /**
     * 分页查询 一级评论 数据
     * @return
     */
    @PostMapping("/ms/service/cmm/cmt/f/lists")
    FungoPageResultDto<CmmCommentDto> queryFirstLevelCmtList(@RequestBody CmmCommentDto cmmCommentDto);
}
