package com.fungo.games.controller.portal;

import com.fungo.games.service.GameService;
import com.fungo.games.service.IGameService;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.GameOut;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  PC游戏
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/25 11:54
 */
@RestController
@Api(value = "", description = "PC游戏")
public class PortalGamesGameController {

    @Autowired
    private IGameService gameService;

    @ApiOperation(value = "PC游戏详情(2.4修改/api/content/evaluations|)", notes = "")
    @RequestMapping(value = "/api/portal/content/game/{gameId}", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameId", value = "游戏id", paramType = "path", dataType = "string"),
    })
    public ResultDto<GameOut> getGameDetail(@Anonymous MemberUserProfile memberUserPrefile, HttpServletRequest request, @PathVariable("gameId") String gameId) {
        String memberId = "";
        String os = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        os = (String) request.getAttribute("os");
        try {
            return gameService.getGameDetail(gameId, memberId, os);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDto.error("-1", "操作失败");
        }
    }
}
