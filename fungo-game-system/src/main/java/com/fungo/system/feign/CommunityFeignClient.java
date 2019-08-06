package com.fungo.system.feign;

import com.fungo.system.facede.impl.CommunityFacedeHystrixService;
import com.game.common.bean.CollectionBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.*;
import com.game.common.vo.CircleGamePostVo;
import com.game.common.vo.CmmCircleVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @Author lyc
 * @create 2019/5/7 10:26
 */
@FeignClient(name = "FUNGO-GAME-COMMUNITY",fallbackFactory = CommunityFacedeHystrixService.class)
public interface CommunityFeignClient {

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


    /**
     * 分页查询 我的动态 - 我的评论 数据
     * @return
     */
    @PostMapping("/ms/service/cmm/user/comments")
    FungoPageResultDto<CommentBean> getAllComments(@RequestParam("pageNum") int pageNum,
                                                          @RequestParam("limit") int limit, @RequestParam("userId") String userId);

    /**
     * 获取我的收藏（文章）
     * @return
     */
    @PostMapping("/ms/service/cmm/post/user/collect")
    FungoPageResultDto<CollectionBean> listCmmPostUsercollect(@RequestParam("pageNum") int pageNum,
                                                              @RequestParam("limit") int limit, @RequestParam("postIds") List<String> postIds);
    /**
     * 查询文章表中发表文章大于10条的前10名用户
     * @param ccnt 达到文章条数
     * @param limitSize 达到的用户数
     * @param wathMbsSet 需要排除的用户id集合
     * @return
     */
    @PostMapping("/ms/service/cmm/user/post/ids")
    ResultDto<List<String>> getRecommendMembersFromCmmPost(@RequestParam("ccnt") long ccnt, @RequestParam("limitSize") long limitSize,
                                                                  @RequestParam("wathMbsSet") List<String> wathMbsSet);

    /**
     * 分页查询 关注社区  数据
     * @param pageNum 当前页码
     * @param limit 每页条数
     * @param communityIds 社区id
     * @return
     */
    @PostMapping("/ms/service/cmm/user/flw/cmtlists")
    public FungoPageResultDto<Map<String, Object>> getFollowerCommunity(@RequestParam("pageNum") int pageNum,
                                                                        @RequestParam("limit") int limit, @RequestParam("communityIds") List<String> communityIds);

    @GetMapping("/ms/service/cmm/post/essences")
    FungoPageResultDto<Map> queryCmmPostEssenceList();


    @GetMapping("/ms/service/cmm/cty/listOfficialCommunityIds")
    ResultDto<List<String>>listOfficialCommunityIds();

    @GetMapping("/ms/service/cmm/cty/listGameIds")
    ResultDto<List<String>> listGameIds(@RequestParam("communityIds") List<String> list);

    /**
     * PC2.0新增浏览量 根据跟用户ID获取文章的浏览量
     * @param cardId
     * @return
     */
    @PostMapping("/ms/service/cmm/post/getPostBoomWatchNumByCardId")
    ResultDto<Integer> getPostBoomWatchNumByCardId(@RequestParam("cardId")String cardId);

    @PostMapping("/ms/service/cmm/post/getGameMsgByPost")
    ResultDto<Map> getGameMsgByPost(@RequestBody CmmPostDto cmmPost);


    @PostMapping("/ms/service/cmm/post/getCircleByPost")
    ResultDto<CmmCircleDto> getCircleByPost(@RequestBody CircleGamePostVo circleGamePostVo);

    @GetMapping(value = "/ms/service/cmm/post/listCircleNameByPost")
    ResultDto<List<String>> listCircleNameByPost(@RequestParam("postId")String postId);

    @GetMapping(value = "/ms/service/cmm/post/listCircleNameByComment")
    ResultDto<List<String>> listCircleNameByComment(@RequestParam("commentId")String commentId);
}
