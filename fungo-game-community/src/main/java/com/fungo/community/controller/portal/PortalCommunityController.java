package com.fungo.community.controller.portal;

import com.fungo.community.service.ICommunityService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.*;
import com.game.common.dto.search.SearchInputPageDto;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(value = "", description = "PC2.0社区")
public class PortalCommunityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PortalCommunityController.class);

    @Autowired
    private ICommunityService communityService;


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
        return communityService.getCmmCommunityList(userId, communityInputPageDto);

    }

}
