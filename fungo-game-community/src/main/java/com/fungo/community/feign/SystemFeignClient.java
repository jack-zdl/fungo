package com.fungo.community.feign;


import com.fungo.community.facede.SystemFacedeHystrixService;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.system.CircleFollowVo;
import com.game.common.dto.system.TaskDto;
import com.game.common.dto.user.*;
import com.game.common.vo.MemberFollowerVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *      系统微服务接口
 * </p>
 *
 * @author mxf
 * @since 2018-11-08
 */
@FeignClient(name = "FUNGO-GAME-SYSTEM",fallbackFactory = SystemFacedeHystrixService.class)
@RequestMapping("/ms/service/system")
public interface SystemFeignClient {

    /**
     * 功能描述: 根据用户id查询被关注人的id集合
     * @param:
     * @return: java.util.List<java.lang.String>
     * @auther: mxf
     * @date: 2019/5/10 11:34
     */
    @RequestMapping(value = "/followerids", method = RequestMethod.POST)
    FungoPageResultDto<String> getFollowerUserId(@RequestBody MemberFollowerVo memberFollowerVo);

    /**
     * 功能描述: 根据用户id集合查询用户详情 state为null就不根据状态查询
     */
    @GetMapping(value = "/listMembersByids")
    ResultDto<List<MemberDto>> listMembersByids(@RequestBody List<String> ids,@RequestParam(value = "state",required = false) Integer state);

    /**
     * 功能描述: 根据会员关注粉丝表对象来分页查询集合
     * @param: [memberUserPrefile, memberFollowerVo]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.user.MemberFollowerDto>
     * @auther: dl.zhang
     * @date: 2019/5/10 17:15
     */
    @GetMapping(value = "/memberFollowers")
    FungoPageResultDto<MemberFollowerDto> getMemberFollowerList(@RequestBody MemberFollowerVo memberFollowerVo);

    /**
     * 功能描述: 根据用户会员DTO对象分页查询用户会员
     * @param: [memberUserPrefile, memberDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.user.MemberDto>
     * @auther: dl.zhang
     * @date: 2019/5/10 17:41
     */
    @GetMapping(value = "/members")
    FungoPageResultDto<MemberDto> getMemberDtoList(@RequestBody MemberDto memberDto);

    /**
     * 根据用户id和用户权益(等级、身份、荣誉)类型，获取用户权益数据
     * @param incentRankedDto
     * @return
     */
    @GetMapping(value = "/incentrankes")
    FungoPageResultDto<IncentRankedDto> getIncentRankedList(@RequestBody IncentRankedDto incentRankedDto);

    @GetMapping(value = "/listFollowerCommunityId")
    @ApiOperation(value="获取关注社区id集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId",value = "用户id",paramType = "form",dataType = "string")
    })
    ResultDto<List<String>> listFollowerCommunityId(@RequestParam("memberId") String memberId);

    @GetMapping(value = "/countActionNum")
    @ApiOperation(value="获取动作数量(比如点赞)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "行为类型",paramType = "form",dataType = "integer"),
            @ApiImplicitParam(name = "targetid",value = "业务id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "targetType",value = "业务类型",paramType = "form",dataType = "integer"),
            @ApiImplicitParam(name = "memberId",value = "会员id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "state",value = "状态",paramType = "form",dataType = "integer")
    })
    ResultDto<Integer> countActionNum(@RequestBody BasActionDto basActionDto);

    @GetMapping(value = "/listtargetId")
    @ApiOperation(value=" 根据用户id，动作类型，目前类型，状态获取目前id集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "行为类型",paramType = "form",dataType = "integer"),
            @ApiImplicitParam(name = "targetType",value = "业务类型",paramType = "form",dataType = "integer"),
            @ApiImplicitParam(name = "memberId",value = "会员id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "state",value = "状态",paramType = "form",dataType = "integer")
    })
    ResultDto<List<String>> listtargetId(@RequestBody BasActionDto basActionDto);

    @GetMapping("/getAuthor")
    @ApiOperation(value="获取会员信息")
    ResultDto<AuthorBean> getAuthor(@RequestParam("memberId") String memberId);

    @GetMapping(value = "/getAuthorList") //consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    @ApiOperation(value="获取会员信息")
    FungoPageResultDto<AuthorBean> getAuthorList(@RequestParam("memberIds")String memberIds);

    @PostMapping(value = "/exTask")
    @ApiOperation(value="执行任务")
    ResultDto<Map<String, Object>> exTask(@RequestBody TaskDto taskDto);

    @GetMapping("/getUserCard")
    @ApiOperation(value="获取会员信息")
    ResultDto<AuthorBean> getUserCard(@RequestParam("cardId") String cardId, @RequestParam("memberId") String memberId);

    @GetMapping("/getStatusImage")
    @ApiOperation(value="根据用户id获取用户身份图标")
    ResultDto<List<HashMap<String, Object>>> getStatusImage(@RequestParam("memberId") String memberId);

    /**
     * 查询指定用户所关注的其他用户列表
     */
    @GetMapping("/listWatchMebmber")
    @ApiOperation(value="查询指定用户所关注的其他用户列表")
    ResultDto<List<MemberDto>> listWatchMebmber(@RequestParam("limit") Integer limit, @RequestParam("currentMbId") String currentMbId);

    /**
     * 功能描述: .找出官方推荐玩家
     */
    @GetMapping(value = "/listRecommendedMebmber")
    ResultDto<List<MemberDto>> listRecommendedMebmber(@RequestParam("limit") Integer limit,@RequestParam("currentMbId") String currentMbId,
                                                             @RequestBody List<String> wathMbsSet);
    @GetMapping("/getIncentRuleRankById")
    @ApiOperation(value="获取用户级别、身份、荣誉规则")
    ResultDto<IncentRuleRankDto> getIncentRuleRankById(@RequestParam("id") String id);

    @GetMapping(value = "/listActionByCondition")
    @ApiOperation(value="根据条件获取动作")
    ResultDto<List<BasActionDto>> listActionByCondition(@RequestBody BasActionDto basActionDto);

    /**
     * 社区使用
     */
    @GetMapping(value = "/getMemberFollower1")
    ResultDto<MemberFollowerDto> getMemberFollower1( @RequestBody MemberFollowerDto memberFollowerDto);

    /**
     * 根据用户Id获取最近浏览圈子行为 8个
     * @param userId
     * @return
     */
    @GetMapping(value = "/getRecentBrowseCommunityByUserId")
    ResultDto<List<String>> getRecentBrowseCommunityByUserId(@RequestParam("userId") String userId);


    @ApiOperation(value = "v2.5", notes = "")
    @RequestMapping(value = "/circle/follow", method = RequestMethod.POST)
    @ApiImplicitParams({})
    ResultDto<CircleFollowVo> circleListFollow( @RequestBody CircleFollowVo circleFollowVo);

    @ApiOperation(value = "v2.5", notes = "")
    @RequestMapping(value = "/circle/mine/follow", method = RequestMethod.POST)
    @ApiImplicitParams({})
    FungoPageResultDto<String> circleListMineFollow( @RequestBody CircleFollowVo circleFollowVo);

    /**
     *  扣减用户积分
     * @return
     */
    @PostMapping("/user/account/score/sub")
    ResultDto<Boolean> subtractMemberScoreAccount(@RequestBody Map<String, Object> accountParamMap);

    @GetMapping(value = "/getMembersByid")
    ResultDto<MemberDto> getMembersByid(@RequestParam(value = "memberId" ,required = false) String memberId);

    /**
     * 功能描述: 查询用户是否关注
     * @auther: dl.zhang
     * @date: 2019/9/23 14:11
     */
    @GetMapping(value = "/member/follow")
    ResultDto<Map<String,Object>>  getMemberFollow(@RequestBody MemberFollowerVo  memberFollowVo );


    @PostMapping("/updateRankedMedal")
    ResultDto<String> updateRankedMedal(@RequestParam("userId") String userId, @RequestParam("rankidt") Integer rankidt);


    /**
     * 更新荣誉 勋章 加精次数
     */
    @PostMapping("/getCircleMainByCircleId")
    ResultDto<List<MemberNameDTO>> getCircleMainByMemberId(@RequestParam("circleId") String circleId);

}
