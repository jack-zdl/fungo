package com.fungo.games.controller;

import com.fungo.games.entity.HomePage;
import com.fungo.games.service.GameHomeService;
import com.fungo.games.service.IIndexService;
import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.index.CardIndexBean;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * 游戏
 * @author Carlos
 *
 */
@RestController
@Api(value = "", description = "游戏")
public class HomePageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomePageController.class);

    @Autowired
    private GameHomeService gameHomeService;

    @Autowired
    private IIndexService indexService;




    @ApiOperation(value = "首页(v2.6)", notes = "")
    @RequestMapping(value = "/api/recommend/home", method = RequestMethod.GET)
    public FungoPageResultDto<HomePage> recommendHomeList(@Anonymous MemberUserProfile memberUserPrefile, HttpServletRequest request) {
        //首页信息查询
        return gameHomeService.index();
    }





    @ApiOperation(value = "首页(v2.4)", notes = "")
    @RequestMapping(value = "/api/recommend", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<CardIndexBean> recommendList(@Anonymous MemberUserProfile memberUserPrefile, HttpServletRequest request, @RequestBody InputPageDto inputPageDto) {
        //iOS渠道
        String iosChannel = "";
        String os = "";
        os = (String) request.getAttribute("os");
        if (request.getHeader("iosChannel") != null) {
            iosChannel = request.getHeader("iosChannel");
        }
        //app渠道编码
        String app_channel = request.getHeader("appChannel");
        String appVersion = request.getHeader("appVersion");
        return gameHomeService.index(inputPageDto, os, iosChannel, app_channel, appVersion);
    }


}
