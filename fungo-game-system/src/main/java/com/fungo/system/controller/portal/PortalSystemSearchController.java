package com.fungo.system.controller.portal;

import com.fungo.system.service.ISeacherService;
import com.fungo.system.service.portal.PortalSystemISeacherService;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.search.SearCount;
import com.game.common.dto.search.SearchInputPageDto;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *  PC端门户-搜索
 * @author mxf
 * @update 2019/5/5 16:26
 */
@RestController
public class PortalSystemSearchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PortalSystemRecommendController.class);

    @Autowired
    private ISeacherService searchService;
    @Autowired
    private PortalSystemISeacherService portalSystemISeacherService;

    @ApiOperation(value = "获取搜索关键词", notes = "")
    @RequestMapping(value = "/api/portal/system/search/keywords", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", paramType = "form", dataType = "string"),
    })
    public ResultDto getKeywords(@Anonymous MemberUserProfile memberUserPrefile) {
        try {
            return searchService.getKeywords();
        } catch (Exception e) {
            LOGGER.error( "获取搜索关键词异常",e);
            return ResultDto.error("-1", "操作失败");
        }
    }

    @ApiOperation(value = "搜索用户", notes = "")
    @RequestMapping(value = "/api/portal/system/search/users", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key_word", value = "关键字", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "page", value = "页数号", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "每页显示数", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "userId", value = "是否关注的数据,如果需要返回则传入当前用户的user_id", paramType = "form", dataType = "string")
    })
    public FungoPageResultDto<AuthorBean> searchUsers(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody SearchInputPageDto searchInputDto) {
        int page = searchInputDto.getPage();
        int limit = searchInputDto.getLimit();
        if (page < 1) {
            return new FungoPageResultDto<AuthorBean>();
        }
        String keyword = searchInputDto.getKey_word();
        String userId = "";
        if (memberUserPrefile != null) {
            userId = memberUserPrefile.getLoginId();
        }
        try {
            return portalSystemISeacherService.searchUsers(keyword, page, limit, userId);
        } catch (Exception e) {
            LOGGER.error( "搜索用户异常",e);
            return FungoPageResultDto.error("-1", "操作失败");
        }
    }

    @ApiOperation(value = "搜索数据统计", notes = "")
    @RequestMapping(value = "/api/portal/system/search/searchcount/{keyword}", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<SearCount> getSearchCount(@PathVariable("keyword") String keyword) {
        return searchService.getSearchCount(keyword);
    }

}
