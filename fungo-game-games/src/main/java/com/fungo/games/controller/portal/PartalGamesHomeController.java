package com.fungo.games.controller.portal;

import com.fungo.games.entity.HomePage;
import com.fungo.games.service.GameHomeService;
import com.fungo.games.service.IIndexService;
import com.game.common.api.InputPageDto;
import com.game.common.bean.AdminCollectionGroup;
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
import java.util.List;

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




    @ApiOperation(value = "首页查询(v2.6)", notes = "")
    @RequestMapping(value = "/api/portal/games/make/queryHomePage", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public FungoPageResultDto<HomePage> queryHomePage(@Anonymous MemberUserProfile memberUserPrefile, HttpServletRequest request) {
        LOGGER.info("首页信息查询**************queryHomePage");
        return gameHomeService.queryHomePage();
    }





    @ApiOperation(value = "新游信息查询(v2.6)", notes = "")
    @RequestMapping(value = "/api/portal/games/make/queryNewGame", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<List<NewGameBean>> queryNewGame(@Anonymous MemberUserProfile memberUserPrefile, HttpServletRequest request) {
        LOGGER.info("首页信息查询**************queryNewGame");
        return gameHomeService.queryNewGame();
    }


    @ApiOperation(value = "查看往期新游信息(v2.6)", notes = "")
    @RequestMapping(value = "/api/portal/games/make/queryOldGame", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<List<NewGameBean>> queryOldGame(@Anonymous MemberUserProfile memberUserPrefile, HttpServletRequest request, @RequestBody InputPageDto inputPageDto) {
        LOGGER.info("首页信息查询**************queryNewGame");
        return gameHomeService.queryOldGame(inputPageDto);
    }



    @ApiOperation(value="合集信息查询", notes="")
    @RequestMapping(value="/api/portal/games/make/queryCollectionGroup", method= RequestMethod.POST)
    @ApiImplicitParams({ })
    public ResultDto<List<AdminCollectionGroup>> queryCollectionGroup(MemberUserProfile memberUserPrefile, @RequestBody AdminCollectionVo input) {
        String loginId = "";
        if(memberUserPrefile != null) {
            loginId = memberUserPrefile.getLoginId();
        }
        return gameHomeService.queryCollectionGroup(input);
    }


}
