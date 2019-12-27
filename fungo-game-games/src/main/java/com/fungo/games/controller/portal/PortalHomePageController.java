package com.fungo.games.controller.portal;

import com.fungo.games.service.GameHomeService;
import com.fungo.games.service.IIndexService;
import com.game.common.api.InputPageDto;
import com.game.common.bean.AdminCollectionGroup;
import com.game.common.bean.HomePageBean;
import com.game.common.bean.NewGameBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.util.annotation.Anonymous;
import com.game.common.vo.AdminCollectionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 游戏
 *
 * @author Carlos
 */
@RestController
@Api(value = "", description = "游戏")
public class PortalHomePageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PortalHomePageController.class);

    @Autowired
    private GameHomeService gameHomeService;

    @Autowired
    private IIndexService indexService;


    @ApiOperation(value = "首页查询(pc2.1)", notes = "")
    @RequestMapping(value="/api/portal/games/content/game/queryHomePage", method= RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<HomePageBean> queryHomePage(@Anonymous MemberUserProfile memberUserPrefile, HttpServletRequest request, @RequestBody InputPageDto inputPageDto) {
        String memberId = "";
        String os = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        os = (String) request.getAttribute("os");
        return gameHomeService.queryHomePage(inputPageDto,memberId, os);
    }



}
