package com.fungo.games.controller;

import com.alibaba.fastjson.JSON;
import com.fungo.games.entity.Game;
import com.fungo.games.feign.SystemFeignClient;
import com.fungo.games.helper.MQProduct;
import com.fungo.games.service.GameService;
import com.fungo.games.service.GameSurveyRelService;
import com.fungo.games.service.IGameService;
import com.game.common.api.InputPageDto;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.game.*;
import com.game.common.util.AesUtil;
import com.game.common.util.CommonUtil;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 游戏
 * @author sam
 *
 */
@RestController
@Api(value = "", description = "游戏")
public class GameController {

    @Autowired
    private  IGameService gameService;

    @Autowired
    private GameService gameServicer;

    @Autowired
    private GameSurveyRelService gameSurveyRelService;

    @ApiOperation(value = "游戏详情(2.4修改/api/content/evaluations|)", notes = "")
    @RequestMapping(value = "/api/content/game/{gameId}", method = RequestMethod.GET)
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


    @ApiOperation(value = "最近浏览游戏(社区)列表", notes = "")
    @RequestMapping(value = "/api/content/viewGames", method = RequestMethod.GET)
    public ResultDto<List<GameOutPage>> viewGames(@Anonymous MemberUserProfile memberUserPrefile) {

        String memberId = "";
        String os = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        //"cec9c9dfe70b4ba9b684f81e617f4833"
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
//		return gameService.getGameTags();
        String gameStr = AesUtil.getInstance().decrypt(input.getGameToken());
        GameIntro intro = JSON.parseObject(gameStr, GameIntro.class);
        System.out.println(intro);
        if (intro == null) {
            return ResultDto.success();
        }
        if (!CommonUtil.isNull(intro.getGameId()) && !CommonUtil.isNull(intro.getPackageName())) {
            Game game = gameServicer.selectById(intro.getGameId());
            if (game != null) {
                if (CommonUtil.isNull(game.getAndroidPackageName())) {
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
    @RequestMapping(value = "/api/content/game/items", method = RequestMethod.POST)
    @ApiImplicitParams({
    })
    public FungoPageResultDto<GameItem> getGameItems(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody GameItemInput input, HttpServletRequest request) {
        String memberId = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        String os = "";
        os = (String) request.getAttribute("os");

        return gameService.getGameItems(memberId, input, os);
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
    public FungoPageResultDto<GameOutBean> getGameList(HttpServletRequest request, HttpServletResponse response ,@Anonymous MemberUserProfile memberUserPrefile, @RequestBody GameListVO input) {
        try {
//            Map map = new HashMap();
//            map.put("sessionId", request.getSession().getId());
//            map.put("message", request.getSession().getAttribute("imageCode"));
//            String imageCode = (String) request.getSession().getAttribute("imageCode");
//            System.out.println("HttpSession session" + imageCode);
//            ResultDto<String>  test = systemFeignClient.test();
//            ResultDto<MemberOutBean> memberOutBeanResultDto = systemFeignClient.getUserInfo();

            FungoPageResultDto<GameOutBean>  re = gameService.getGameList(input, memberUserPrefile.getLoginId());
            return  re;
        }catch (Exception e){
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
    public FungoPageResultDto<GameReleaseLogDto> selectOne(GameReleaseLogDto GameReleaseLog){
        return new FungoPageResultDto<GameReleaseLogDto>();
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/api/game/{gameId}", method = RequestMethod.POST)
    public Game selectOne( @PathVariable("gameId") String gameId){
         Game game =  gameServicer.selectById(gameId);
        return game;
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/api/gameSurveyRel", method = RequestMethod.POST)
    public int selectCount(  GameSurveyRelDto gameSurveyRel){
//        new EntityWrapper<GameSurveyRel>().eq("game_id", gameId).eq("phone_model", "Android");
//        gameSurveyRelService.selectCount();
        return 1;
    }

    /**
     * 游戏评价 服务类
     * @return
     */
    @RequestMapping(value = "/api/gameEvaluation", method = RequestMethod.POST)
    public int selectGameEvaluationCount(  GameEvaluationDto gameEvaluation){
//        new EntityWrapper<GameSurveyRel>().eq("game_id", gameId).eq("phone_model", "Android");
//        gameSurveyRelService.selectCount();
        return 1;
    }
    @Autowired
    private MQProduct mqProduct;
    @Autowired
    private SystemFeignClient systemFeignClient;

//    @Autowired
//    private GameTagService gameTagService;
//
//    @Autowired
//    private IEvaluateProxyService iEvaluateProxyService;

    /**
     * ceshi
     * @return
     */
    @RequestMapping(value = "/api/feignMQDemo", method = RequestMethod.GET)
    public int feignMQDemo(){
//        测试
        /*List<GameTag> gameTags = gameTagService.selectList(new EntityWrapper<GameTag>().setSqlSelect("tag_id as tagId").eq("game_id", "b3aba058982940159c8f938d143e1b34"));
        List<String> strings = new ArrayList<>();
        if (gameTags != null && gameTags.size() > 0){
            for (GameTag gameTag : gameTags) {
                strings.add(gameTag.getTagId());
            }
        }
        List<TagBean> tags = iEvaluateProxyService.getSortTags(strings);
        for (TagBean ta : tags) {
            System.out.println(ta.toString());
        }*/
//        mqFeignClient.deleteMessageByMessageId(2019051616580096421l);
        ResultDto<AuthorBean> author = systemFeignClient.getAuthor("012689d5d62e46f3b7fd40e536842455");
        System.out.println(author.getData().toString());
        BasActionDto basActionDto = new BasActionDto();
        basActionDto.setMemberId("111111");
        basActionDto.setTargetId("测试");
        basActionDto.setState(0);
        basActionDto.setType(1);
        basActionDto.setId("ceshi");
        mqProduct.basActionInsertAllColumn(basActionDto);
        return 1;
    }


}
