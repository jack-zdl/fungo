package com.fungo.community.controller;

import com.fungo.community.service.ICommunityService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.*;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(value = "", description = "社区")
public class CommunityController {

    @Autowired
    private ICommunityService communityService;

    @ApiOperation(value = "社区列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "sort", value = "排序字段", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "页数", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "userid", value = "社区id", paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "filter", value = "过滤字段", paramType = "form", dataType = "String"),
    })
    @RequestMapping(value = "/api/content/communitys", method = RequestMethod.POST)
    public FungoPageResultDto<CommunityOutPageDto> getCommunityList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody CommunityInputPageDto communityInputPageDto) {
        String userId = "";
        if (memberUserPrefile != null) {
            userId = memberUserPrefile.getLoginId();
        }
        return communityService.getCmmCommunityList(userId, communityInputPageDto);

    }


    @ApiOperation(value = "社区详情", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "communityId", value = "社区id", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType = "form", dataType = "string")
    })
    @RequestMapping(value = "/api/content/community/{communityId}", method = RequestMethod.GET)
    public ResultDto<CommunityOut> getCommunityDetail(@Anonymous MemberUserProfile memberUserPrefile,
                                                      @PathVariable("communityId") String communityId) {
        String userId = "";
        if (memberUserPrefile != null) {
            userId = memberUserPrefile.getLoginId();
        }
        try {
            return communityService.getCommunityDetail(communityId, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDto.error("-1", "操作失败");
        }
    }


    @ApiOperation(value = "社区玩家榜(2.4.3)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "communityId", value = "社区id", paramType = "path", dataType = "string"),
    })
    @RequestMapping(value = "/api/content/community/memberlist", method = RequestMethod.POST)
    public FungoPageResultDto<CommunityMember> memberList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody CommunityInputPageDto input) {
        String userId = "";
        if (memberUserPrefile != null) {
            userId = memberUserPrefile.getLoginId();
        }
        return communityService.memberList(userId, input);

    }

    @RequestMapping(value = "/api/content/community/post", method = RequestMethod.POST)
    public int selectPostCount(CmmPostDto cmmPost) {
        return 1;
    }

    //-----------

}
