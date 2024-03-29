package com.fungo.games.controller;

import com.alibaba.fastjson.JSON;
import com.fungo.games.entity.Game;
import com.fungo.games.service.GameService;
import com.fungo.games.service.IGameService;
import com.fungo.games.utils.GameCommonUtil;
import com.game.common.api.InputPageDto;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.*;
import com.game.common.repo.cache.facade.FungoCacheGame;
import com.game.common.util.AesUtil;
import com.game.common.util.CommonUtil;
import com.game.common.util.StringUtil;
import com.game.common.util.annotation.Anonymous;
import com.game.common.util.annotation.MD5ParanCheck;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.game.common.enums.AbstractResultEnum.CODE_GAME_FIVE;

/**
 * 游戏
 * @author sam
 *
 */
@RestController
@Api(value = "", description = "游戏")
public class GameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private IGameService gameService;
    @Autowired
    private GameService gameServicer;
    @Autowired
    private FungoCacheGame fungoCacheGame;

    @ApiOperation(value = "游戏详情", notes = "")
    @RequestMapping(value = "/api/content/game/number/{gameId}", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameId", value = "游戏id", paramType = "path", dataType = "string"),
    })
    public ResultDto<GameOut> getGameDetailByNumber(@Anonymous MemberUserProfile memberUserPrefile, HttpServletRequest request, @PathVariable("gameId") String gameId) {
        String memberId = "";
        String os = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        os = (String) request.getAttribute("os");
        if (StringUtils.isBlank(os)) {
            os = request.getHeader("os");
        }
        try {
            return gameService.getGameDetail(gameId, memberId, os);
        } catch (Exception e) {
            return ResultDto.error("-1", "操作失败");
        }
    }

    @ApiOperation(value = "游戏详情(2.4修改/api/content/evaluations|)", notes = "")
    @RequestMapping(value = "/api/content/game/{gameId}", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameId", value = "游戏id", paramType = "path", dataType = "string"),
    })
    @MD5ParanCheck()
    public ResultDto<GameOut> getGameDetail(@Anonymous MemberUserProfile memberUserPrefile, HttpServletRequest request, @PathVariable("gameId") String gameId) {
        String memberId = "";
        String os = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        os = (String) request.getAttribute("os");
        if (StringUtils.isBlank(os)) {
            os = request.getHeader("os");
        }
        try {
            return gameService.getGameDetail(gameId, memberId, os);
        } catch (Exception e) {
            return ResultDto.error("-1", "操作失败");
        }
    }

    @ApiOperation(value = "根据id集合获取游戏列表", notes = "")
    @RequestMapping(value = "/api/content/game/listGameByids", method = RequestMethod.GET)
    public ResultDto<List<GameOut>> listGameByids(@RequestParam("gameIds") String gameIds) {
        if(CommonUtil.isNull(gameIds)){
            ArrayList<GameOut> games = new ArrayList<>();
            return ResultDto.success(games);
        }
        try {
            return gameService.listGameByids(gameIds);
        } catch (Exception e) {
            return ResultDto.error("-1", "操作失败");
        }
    }

    @ApiOperation(value = "游戏列表", notes = "")
    @RequestMapping(value = "/api/content/games", method = RequestMethod.POST)
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

    @ApiOperation(value = "最近浏览游戏列表", notes = "")
    @RequestMapping(value = "/api/content/viewGames", method = RequestMethod.GET)
    public ResultDto<List<GameOutPage>> viewGames(@Anonymous MemberUserProfile memberUserPrefile) {
        String memberId = "";
        String os = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        if(StringUtil.isNull(memberId)){
            return ResultDto.error("-1","请登录");
        }
        return gameService.viewGames(memberId);
    }

    @ApiOperation(value = "官方游戏分类", notes = "")
    @RequestMapping(value = "/api/recommend/tag/game", method = RequestMethod.GET)
    @ApiImplicitParams({
    })
    public ResultDto<List<TagOutPage>> getGameTags(@Anonymous MemberUserProfile memberUserPrefile) {
        return gameService.getGameTags();
    }

    @ApiOperation(value = "游戏包名修改", notes = "")
    @RequestMapping(value = "/api/content/game/package", method = RequestMethod.PUT)
    @ApiImplicitParams({
    })
    public ResultDto<String> updateGamePackage(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody GamePackageInput input) throws Exception {
        String gameStr = AesUtil.getInstance().decrypt(input.getGameToken());
        GameIntro intro = JSON.parseObject(gameStr, GameIntro.class);
        System.out.println(intro);
        if (intro == null) {
            return ResultDto.success();
        }
        if (!CommonUtil.isNull(intro.getGameId()) && !CommonUtil.isNull(intro.getPackageName())) {
            Game game = gameServicer.selectById(intro.getGameId());
            if (game != null) {
                String androidPackageName = game.getAndroidPackageName();
                // 包名不一致才做更新
                if(!Objects.equals(androidPackageName,intro.getPackageName())){
                    game.setAndroidPackageName(intro.getPackageName());
                    gameServicer.updateAllColumnById(game);
                }
            }
        }
        return ResultDto.success();
    }

    @ApiOperation(value = "获取最近评论的游戏(2.4.3)", notes = "")
    @RequestMapping(value = "/api/content/game/recenteva", method = RequestMethod.POST)
    @ApiImplicitParams({
    })
    public FungoPageResultDto<GameOutPage> recentEvaluatedGamesByMember(MemberUserProfile memberUserPrefile, @RequestBody InputPageDto input) {
        String userId = memberUserPrefile.getLoginId();
        return gameService.recentEvaluatedGamesByMember(userId, input);
    }

    @ApiOperation(value = "游戏合集列表(2.4.3)", notes = "")
    @RequestMapping(value = "/api/content/game/collections", method = RequestMethod.GET)
    @ApiImplicitParams({
    })
    public FungoPageResultDto<Map<String, String>> getGameCollections(@Anonymous MemberUserProfile memberUserPrefile) {
        return null;
    }

    @ApiOperation(value = "游戏合集项列表(2.4.3)", notes = "")
    @PostMapping(value = "/api/content/game/items")
    @ApiImplicitParams({
    })
    public FungoPageResultDto<GameItem> getGameItems(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody GameItemInput input, HttpServletRequest request) {
        String memberId = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        String os = "";
        os = (String) request.getAttribute("os");
        FungoPageResultDto<GameItem> resultDto = null;
        try {
            String keySuffix = memberId + JSON.toJSONString(input) + os;
            FungoPageResultDto<GameItem> re = (FungoPageResultDto<GameItem>) fungoCacheGame.getIndexCache( FungoCoreApiConstant.FUNGO_CORE_API_GAME_ITEMS,keySuffix);
            if (null != re && null != re.getData() && re.getData().size() > 0) {
                return re;
            }
            resultDto =  gameService.getGameItems(memberId, input, os);
            fungoCacheGame.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_GAME_ITEMS, keySuffix, re);
        }catch (Exception e){
            LOGGER.error( "游戏合集项列表获取异常",e );
            resultDto = FungoPageResultDto.FungoPageResultDtoFactory.buildError( "游戏合集项列表获取异常" );
        }
        return resultDto;
    }

    /**
     * @todo
     * @param memberUserPrefile
     * @param input
     * @return
     */
    @ApiOperation(value = "根据id集合查询游戏合集项列表", notes = "")
    @RequestMapping(value = "/api/content/gameList", method = RequestMethod.POST)
    @ApiImplicitParams({
    })
    public FungoPageResultDto<GameOutBean> getGameList(HttpServletRequest request, HttpServletResponse response, @Anonymous MemberUserProfile memberUserPrefile, @RequestBody GameListVO input) {
        try {
//            Map map = new HashMap();
//            map.put("sessionId", request.getSession().getId());
//            map.put("message", request.getSession().getAttribute("imageCode"));
//            String imageCode = (String) request.getSession().getAttribute("imageCode");
//            System.out.println("HttpSession session" + imageCode);
//            ResultDto<String>  test = systemFeignClient.test();
//            ResultDto<MemberOutBean> memberOutBeanResultDto = systemFeignClient.getUserInfo();
            FungoPageResultDto<GameOutBean> re = gameService.getGameList(input, memberUserPrefile.getLoginId());
            return re;
        } catch (Exception e) {
            return FungoPageResultDto.error("-1", "未指定用户");
        }
    }

    /**
     * @todo
     * @param GameReleaseLog
     * @return
     */
    @ApiOperation(value = "根据id游戏版本日志审批", notes = "")
    @RequestMapping(value = "/api/gamereleaselog", method = RequestMethod.POST)
    @ApiImplicitParams({
    })
    public FungoPageResultDto<GameReleaseLogDto> selectOne(GameReleaseLogDto GameReleaseLog) {
        return new FungoPageResultDto<GameReleaseLogDto>();
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/api/game/{gameId}", method = RequestMethod.POST)
    public Game selectOne(@PathVariable("gameId") String gameId) {
        Game game = gameServicer.selectById(gameId);
        return game;
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/api/gameSurveyRel", method = RequestMethod.POST)
    public int selectCount(GameSurveyRelDto gameSurveyRel) {
//        new EntityWrapper<GameSurveyRel>().eq("game_id", gameId).eq("phone_model", "Android");
//        gameSurveyRelService.selectCount();
        return 1;
    }

    /**
     * 游戏评价 服务类
     * @return
     */
    @RequestMapping(value = "/api/gameEvaluation", method = RequestMethod.POST)
    public int selectGameEvaluationCount(GameEvaluationDto gameEvaluation) {
//        new EntityWrapper<GameSurveyRel>().eq("game_id", gameId).eq("phone_model", "Android");
//        gameSurveyRelService.selectCount();
        return 1;
    }

    @PostMapping("/api/game/listGameByTags")
    public FungoPageResultDto<GameKuDto> listGameByTags(@Anonymous MemberUserProfile memberUserPrefile,@RequestBody TagGameDto tagGameDto){
        List<String> tags = tagGameDto.getTags();
        if(tags == null||tags.isEmpty()){
            tagGameDto.setTagIds(null);
            tagGameDto.setSize(0);
        }else{
            tagGameDto.setTagIds( GameCommonUtil.getTagString(tags));
            tagGameDto.setSize(tags.size());
        }
        String memberId = null;
        if(memberUserPrefile!=null){
            memberId = memberUserPrefile.getLoginId();
        }
        return gameService.listGameByTags(memberId,tagGameDto);
    }

    @PostMapping("/api/game/listGameByBang")
    public FungoPageResultDto<GameKuDto> listGameByBang(@Anonymous MemberUserProfile memberUserPrefile,@RequestBody BangGameDto bangGameDto){
        String memberId = null;
        if(memberUserPrefile!=null){
            memberId = memberUserPrefile.getLoginId();
        }
        return gameService.listGameByBang(memberId,bangGameDto);
    }

    /**
     * 功能描述: 根据包名集合获取要更新的游戏包集合
     * @auther: dl.zhang
     * @date: 2020/2/27 15:59
     */
    @PostMapping("/api/game/listGameByStatus")
    public FungoPageResultDto<GameKuDto> listGameByStatus(@Anonymous MemberUserProfile memberUserPrefile,@RequestBody BangGameDto bangGameDto){
        String memberId = memberUserPrefile!=null ? memberUserPrefile.getLoginId() : null;
        if(bangGameDto == null || bangGameDto.getGameInfo() == null || bangGameDto.getGameInfo().size() == 0)
            return FungoPageResultDto.FungoPageResultDtoFactory.buildWarning( CODE_GAME_FIVE.getKey(),CODE_GAME_FIVE.getFailevalue());
        return gameService.listGameByStatus(memberId,bangGameDto);
    }



}
