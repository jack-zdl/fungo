package com.fungo.community.controller.portal;

import com.fungo.community.service.ICommunityService;
import com.fungo.community.service.portal.IPortalCommunityService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.*;
import com.game.common.dto.community.portal.CmmCommunityIndexDto;
import com.game.common.dto.search.SearchInputPageDto;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("all")
@RestController
@Api(value = "", description = "PC2.0社区")
public class PortalCommunityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PortalCommunityController.class);

    @Autowired
    private ICommunityService communityService;

    @Autowired
    private IPortalCommunityService iPortalCommunityService;


    @ApiOperation(value = "PC2.0社区详情", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "communityId", value = "社区id", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType = "form", dataType = "string")
    })
    @RequestMapping(value = "/api/portal/community/content/community/{communityId}", method = RequestMethod.GET)
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


    @ApiOperation(value = "搜索社区", notes = "")
    @RequestMapping(value = "/api/portal/community/search/communitys", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数号", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "每页显示数", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "key_word", value = "关键字", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType = "form", dataType = "string"),
    })
    public FungoPageResultDto<CommunitySearchOut> searchCommunitys(@Anonymous MemberUserProfile memberUserPrefile,
                                                                   @RequestBody SearchInputPageDto searchInputDto) {
        String userId = "";
        if (memberUserPrefile != null) {
            userId = memberUserPrefile.getLoginId();
        }
        int page = searchInputDto.getPage();
        int limit = searchInputDto.getLimit();
        String keyword = searchInputDto.getKey_word();
        return communityService.searchCommunitys(page, limit, keyword, userId);
    }

    @ApiOperation(value = "PC2.0社区列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "sort", value = "排序字段", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "页数", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "userid", value = "社区id", paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "filter", value = "过滤字段", paramType = "form", dataType = "String"),
    })
    @RequestMapping(value = "/api/portal/community/content/communitys", method = RequestMethod.POST)
    public FungoPageResultDto<CommunityOutPageDto> getCommunityList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody CommunityInputPageDto communityInputPageDto) {
        String userId = "";
        if (memberUserPrefile != null) {
            userId = memberUserPrefile.getLoginId();
        }
        return iPortalCommunityService.getCmmCommunityList(userId, communityInputPageDto);

    }

    @ApiOperation(value = "PC2.0圈子首页列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "页数", paramType = "form", dataType = "int"),
    })
    @RequestMapping(value = "/api/portal/community/content/communitysPCList", method = RequestMethod.POST)
    public FungoPageResultDto<CmmCommunityIndexDto> getCommunityListPC2_0(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody CommunityInputPageDto communityInputPageDto) {
        String userId = "";
        if (memberUserPrefile != null) {
            userId = memberUserPrefile.getLoginId();
        }
        return iPortalCommunityService.getCommunityListPC2_0(userId, communityInputPageDto);

    }

}
