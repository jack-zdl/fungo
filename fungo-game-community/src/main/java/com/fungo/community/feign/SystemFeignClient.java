package com.fungo.community.feign;


import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.user.IncentRankedDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.dto.user.MemberFollowerDto;
import com.game.common.vo.MemberFollowerVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *      系统微服务接口
 * </p>
 *
 * @author mxf
 * @since 2018-11-08
 */
@FeignClient(name = "fungo-game-system")
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
    public FungoPageResultDto<String> getFollowerUserId(@RequestBody MemberFollowerVo memberFollowerVo);



    /**
     * 功能描述: 根据用户id集合查询用户详情
     * mxf
     */
    @GetMapping(value = "/listMembersByids")
    public ResultDto<List<MemberDto>> listMembersByids(@RequestBody List<String> ids);


    /**
     * 功能描述: 根据会员关注粉丝表对象来分页查询集合
     * @param: [memberUserPrefile, memberFollowerVo]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.user.MemberFollowerDto>
     * @auther: dl.zhang
     * @date: 2019/5/10 17:15
     */
    @GetMapping(value = "/memberFollowers")
    public FungoPageResultDto<MemberFollowerDto> getMemberFollowerList(@RequestBody MemberFollowerVo memberFollowerVo);




    /**
     * 功能描述: 根据用户会员DTO对象分页查询用户会员
     * @param: [memberUserPrefile, memberDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.user.MemberDto>
     * @auther: dl.zhang
     * @date: 2019/5/10 17:41
     */
    @GetMapping(value = "/members")
    public FungoPageResultDto<MemberDto> getMemberDtoList(@RequestBody MemberDto memberDto);


    /**
     * 根据用户id和用户权益(等级、身份、荣誉)类型，获取用户权益数据
     * @param incentRankedDto
     * @return
     */
    @GetMapping(value = "/incentrankes")
    public FungoPageResultDto<IncentRankedDto> getIncentRankedList(@RequestBody IncentRankedDto incentRankedDto);






    @GetMapping(value = "/listFollowerCommunityId")
    @ApiOperation(value="获取关注社区id集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId",value = "用户id",paramType = "form",dataType = "string")
    })
    public ResultDto<List<String>> listFollowerCommunityId(@RequestParam String memberId);






    @GetMapping(value = "/countActionNum")
    @ApiOperation(value="获取动作数量(比如点赞)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "行为类型",paramType = "form",dataType = "integer"),
            @ApiImplicitParam(name = "targetid",value = "业务id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "targetType",value = "业务类型",paramType = "form",dataType = "integer"),
            @ApiImplicitParam(name = "memberId",value = "会员id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "state",value = "状态",paramType = "form",dataType = "integer")
    })
    public ResultDto<Integer> countActionNum(@RequestBody BasActionDto basActionDto);




    @GetMapping(value = "/listtargetId")
    @ApiOperation(value=" 根据用户id，动作类型，目前类型，状态获取目前id集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "行为类型",paramType = "form",dataType = "integer"),
            @ApiImplicitParam(name = "targetType",value = "业务类型",paramType = "form",dataType = "integer"),
            @ApiImplicitParam(name = "memberId",value = "会员id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "state",value = "状态",paramType = "form",dataType = "integer")
    })
    public ResultDto<List<String>> listtargetId(@RequestBody BasActionDto basActionDto);




    @GetMapping("/getAuthor")
    @ApiOperation(value="获取会员信息")
    public ResultDto<AuthorBean> getAuthor(String memberId);







    //-------------
}
