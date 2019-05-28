package com.fungo.system.feign;

import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.*;
import com.game.common.dto.index.CardIndexBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 * @Author: dl.zhang
 * @Date: 2019/4/28
 */
@FeignClient(name = "FUNGO-GAME-GAMES")
public interface GamesFeignClient {

    @RequestMapping(value = "/ms/service/game/api/content/gameList", method = RequestMethod.POST)
    FungoPageResultDto<GameOutBean> getGameList(@RequestBody GameItemInput input);

    @RequestMapping(value = "/ms/service/game/api/content/games", method = RequestMethod.POST)
    FungoPageResultDto<GameOutPage> getGameList(@RequestBody GameInputPageDto gameInputDto);


    @RequestMapping(value = "/ms/service/game/api/gamereleaselog", method = RequestMethod.POST)
    FungoPageResultDto<GameReleaseLogDto> selectOne(GameReleaseLogDto GameReleaseLog);


    // 根据游戏版本日志审批对象查询集合
    @SuppressWarnings("all")
    @ApiOperation(value = "根据游戏版本日志审批对象查询集合", notes = "")
    @RequestMapping(value = "/ms/service/game/api/evaluation/getGameReleaseLogPage", method = RequestMethod.POST)
    FungoPageResultDto<GameReleaseLogDto>  getGameReleaseLogPage(@RequestBody GameReleaseLogDto gameReleaseLogDto);

    @RequestMapping(value = "/ms/service/game/api/game/{gameId}", method = RequestMethod.POST)
    GameDto selectOne(@PathVariable("gameId") String gameId);

    /**
     * 动态表 辅助计数器
     * @param map
     * @return
     */
    @RequestMapping(value = "/ms/service/game/api/update/counter", method = RequestMethod.POST)
    boolean updateCounter(@RequestBody Map<String, String> map);

    @RequestMapping(value = "/ms/service/game/api/selectCount", method = RequestMethod.POST)
    int gameSurveySelectCount(GameSurveyRelDto gameSurveyRel);

    /**
     * 功能描述:
     * @param: [gameEvaluation]
     * @return: int
     * @auther: dl.zhang
     * @date: 2019/5/17 15:16
     */
    @RequestMapping(value = "/ms/service/game/api/gameEvaluation/selectCount", method = RequestMethod.POST)
    int gameEvaluationSelectCount(GameEvaluationDto gameEvaluation);

    /**
     * 被点赞用户的id
     * @param map
     * @return
     */
    @RequestMapping(value = "/ms/service/game/api/getMemberIdByTargetId", method = RequestMethod.POST)
    String getMemberIdByTargetId(@RequestBody Map<String, String> map);


    /**
     * 游戏评价的分页查询
     * @param gameEvaluationDto
     * @return
     */
    @ApiOperation(value = "游戏评价的分页查询", notes = "")
    @RequestMapping(value = "/ms/service/game/api/evaluation/getGameEvaluationPage", method = RequestMethod.POST)
    FungoPageResultDto<GameEvaluationDto> getGameEvaluationPage(@RequestBody GameEvaluationDto gameEvaluationDto);

    @ApiOperation(value = "根据游戏对象查询集合", notes = "")
    @RequestMapping(value = "/ms/service/game/api/geme/getGamePage", method = RequestMethod.POST)
    FungoPageResultDto<GameDto> getGamePage(@RequestBody GameDto gameDto);

    /**
     * 游戏测试会员关联表的分页查询
     * @param gameSurveyDto
     * @return
     */
    @SuppressWarnings("all")
    @ApiOperation(value = "游戏测试会员关联表的分页查询", notes = "")
    @RequestMapping(value = "/ms/service/game/api/evaluation/getGameSurveyRelPage", method = RequestMethod.POST)
    FungoPageResultDto<GameSurveyRelDto> getGameSurveyRelPage(@RequestBody GameSurveyRelDto gameSurveyDto);

    @ApiOperation(value = "getGameSelectCountByLikeNameAndState", notes = "")
    @RequestMapping(value = "/ms/service/game/api/game/getGameSelectCountByLikeNameAndState", method = RequestMethod.POST)
    int getGameSelectCountByLikeNameAndState(@RequestBody GameDto gameDto);

    @ApiOperation(value = "getGameSelectCountByLikeNameAndState", notes = "")
    @RequestMapping(value = "/ms/service/game/api/game/getGameSelectCountByLikeNameAndState", method = RequestMethod.GET)
    ResultDto<CardIndexBean> getSelectedGames();

    @ApiOperation(value = "getGameSelectCountByLikeNameAndState", notes = "")
    @RequestMapping(value = "/ms/service/game/api/game/getGameSelectCountByLikeNameAndState", method = RequestMethod.POST)
    ResultDto<CardIndexBean> getSelectedGames(@RequestBody GameDto gameDto);

    @ApiOperation(value = "getRateData", notes = "")
    @RequestMapping(value = "/ms/service/game/api/game/getRateData", method = RequestMethod.GET)
    ResultDto<HashMap<String, BigDecimal>> getRateData(@RequestParam("gameId") String gameId);

    @ApiOperation(value = "getEvaluationEntityWrapper", notes = "")
    @RequestMapping(value = "/ms/service/game/api/game/getEvaluationEntityWrapper", method = RequestMethod.POST)
    ResultDto<List<GameEvaluationDto>> getEvaluationEntityWrapper(@RequestParam("memberId") String memberId, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate);


    @ApiOperation(value = "游戏评价邀请的分页查询", notes = "")
    @RequestMapping(value = "/ms/service/game/api/evaluation/getGameInvitePage", method = RequestMethod.POST)
    FungoPageResultDto<GameInviteDto> getGameInvitePage(@RequestBody GameInviteDto gameInviteDto);

    @ApiOperation(value = "查询游戏评论表中发表评论大于X条，前Y名的用户", notes = "")
    @RequestMapping(value = "/ms/service/game/api/game/getRecommendMembersFromEvaluation", method = RequestMethod.POST)
    ResultDto<List<String>> getRecommendMembersFromEvaluation(@RequestParam("x") Integer x, @RequestParam("y") Integer y, @RequestParam("wathMbsSet") List<String> wathMbsSet);

    @GetMapping("/ms/service/game/api/game/selectedGames")
    CardIndexBean selectedGames();

    @GetMapping("/ms/service/game/api/game/selectGameEvaluationPage")
    FungoPageResultDto<GameEvaluationDto> selectGameEvaluationPage();

    /**
     * 用户游戏评测精品数
     *
     * @return
     */
    @RequestMapping(value = "/api/game/getUserGameReviewBoutiqueNumber", method = RequestMethod.POST)
    ResultDto<List<Map>> getUserGameReviewBoutiqueNumber();

}
