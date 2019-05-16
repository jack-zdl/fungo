package com.fungo.system.feign;

import com.baomidou.mybatisplus.plugins.Page;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.game.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p></p>
 * @Author: dl.zhang
 * @Date: 2019/4/28
 */
@FeignClient(name = "FUNGO-GAME-GAMES")
public interface GamesFeignClient {

    @RequestMapping(value = "/ms/service/game/api/content/gameList", method = RequestMethod.POST)
    FungoPageResultDto<GameOutBean> getGameList( @RequestBody GameItemInput input);

    @RequestMapping(value = "/ms/service/game/api/content/games", method = RequestMethod.POST)
    FungoPageResultDto<GameOutPage> getGameList(@RequestBody GameInputPageDto gameInputDto);


    //    @RequestMapping(value = "/ms/service/game/api/gamereleaselog", method = RequestMethod.POST)
    //    FungoPageResultDto<GameReleaseLogDto> selectOne(GameReleaseLogDto GameReleaseLog);
    //    根据游戏版本日志审批对象查询集合
    @SuppressWarnings("all")
    @ApiOperation(value = "根据游戏版本日志审批对象查询集合", notes = "")
    @RequestMapping(value = "/ms/service/game/api/evaluation/getGameReleaseLogPage", method = RequestMethod.POST)
    Page<GameReleaseLogDto> getGameReleaseLogPage(@RequestBody GameReleaseLogDto gameReleaseLogDto);

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
    int gameSurveySelectCount(  GameSurveyRelDto gameSurveyRel);

    @RequestMapping(value = "/ms/service/game/api/gameEvaluation", method = RequestMethod.POST)
    int gameEvaluationSelectCount(  GameEvaluationDto gameEvaluation);

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
    Page<GameEvaluationDto> getGameEvaluationPage(@RequestBody GameEvaluationDto gameEvaluationDto);

    @ApiOperation(value = "根据游戏对象查询集合", notes = "")
    @RequestMapping(value = "/ms/service/game/api/geme/getGamePage", method = RequestMethod.POST)
    Page<GameDto> getGamePage(@RequestBody GameDto gameDto);

    /**
     * 游戏测试会员关联表的分页查询
     * @param gameSurveyDto
     * @return
     */
    @SuppressWarnings("all")
    @ApiOperation(value = "游戏测试会员关联表的分页查询", notes = "")
    @RequestMapping(value = "/ms/service/game/api/evaluation/getGameSurveyRelPage", method = RequestMethod.POST)
    Page<GameSurveyRelDto> getGameSurveyRelPage(@RequestBody GameSurveyRelDto gameSurveyDto);
}
