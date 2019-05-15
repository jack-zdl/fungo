package com.fungo.games.controller;


import com.fungo.games.service.IGameService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.search.GameSearchOut;
import com.game.common.dto.search.SearchInputPageDto;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 搜索服务
 * @author sam
 *
 */
@RestController
@Api(value = "", description = "搜索服务")
public class SearchController {

    @Autowired
    private IGameService iGameService;




    @ApiOperation(value = "搜索游戏", notes = "")
    @RequestMapping(value = "/api/search/games", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数号", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "每页显示数", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "key_word", value = "关键字", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "tag", value = "游戏分类", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "sort", value = "排序字段（‘+，- ，表示返回顺序）", paramType = "form", dataType = "string")
    })
    public FungoPageResultDto<GameSearchOut> searchGames(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody SearchInputPageDto searchInputDto, HttpServletRequest request) {
        int page = searchInputDto.getPage();

        //fix: 页码 小于1 返回空 [by mxf 2019-01-30]
        if (page < 1) {
            return new FungoPageResultDto<GameSearchOut>();
        }

        int limit = searchInputDto.getLimit();
        String keyword = searchInputDto.getKey_word();
        String tag = searchInputDto.getTag();
        String sort = searchInputDto.getSorts();
        String os = "";
        os = (String) request.getAttribute("os");
        String memberId = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        try {
            return iGameService.searchGames(page, limit, keyword, tag, sort, os, memberId);
        } catch (Exception e) {
            e.printStackTrace();
            return FungoPageResultDto.error("-1", "操作失败");
        }
    }

}
