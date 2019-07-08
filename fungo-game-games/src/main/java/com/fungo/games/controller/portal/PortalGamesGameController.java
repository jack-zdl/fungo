package com.fungo.games.controller.portal;

import com.fungo.games.service.GameService;
import com.fungo.games.service.IGameService;
import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.GameInputPageDto;
import com.game.common.dto.game.GameOut;
import com.game.common.dto.game.GameOutPage;
import com.game.common.dto.game.TagOutPage;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @ApiOperation(value = "PC2.0游戏详情(2.4修改/api/content/evaluations|)", notes = "")
    @RequestMapping(value = "/api/portal/games/content/game/{gameId}", method = RequestMethod.GET)
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

    @ApiOperation(value = "PC2.0官方游戏分类", notes = "")
    @RequestMapping(value = "/api/portal/games/recommend/tag/game", method = RequestMethod.GET)
    @ApiImplicitParams({
    })
    public ResultDto<List<TagOutPage>> getGameTags(@Anonymous MemberUserProfile memberUserPrefile) {
        return gameService.getGameTags();
    }


    @ApiOperation(value = "获取最近评论的游戏(2.4.3)", notes = "")
    @RequestMapping(value = "/api/portal/games/content/game/recenteva", method = RequestMethod.POST)
    @ApiImplicitParams({
    })
    public FungoPageResultDto<GameOutPage> recentEvaluatedGamesByMember(MemberUserProfile memberUserPrefile, @RequestBody InputPageDto input) {

        String userId = memberUserPrefile.getLoginId();
        return gameService.recentEvaluatedGamesByMember(userId, input);
    }

    @ApiOperation(value = "PC2.0游戏列表", notes = "")
    @RequestMapping(value = "/api/portal/games/content/games", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "当前页数", paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "sort", value = "排序字段", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "tag", value = "游戏分类", paramType = "path", dataType = "string")
    })
    public FungoPageResultDto<GameOutPage> getGameList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody GameInputPageDto gameInputDto, HttpServletRequest request) {

        String memberId = "";
        String os = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        os = (String) request.getAttribute("os");

        return gameService.getGameList(gameInputDto, memberId, os);
    }
}
