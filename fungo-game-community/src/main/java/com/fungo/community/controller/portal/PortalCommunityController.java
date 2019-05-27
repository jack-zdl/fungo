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
@Api(value = "", description = "社区")
public class PortalCommunityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PortalCommunityController.class);

    @Autowired
    private ICommunityService communityService;


    @ApiOperation(value = "搜索社区", notes = "")
    @RequestMapping(value = "/api/portal/communitys/search/communitys", method = RequestMethod.POST)
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
    //-----------

}
