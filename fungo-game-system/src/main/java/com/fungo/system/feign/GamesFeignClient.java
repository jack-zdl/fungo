package com.fungo.system.feign;

import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.game.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <p></p>
 * @Author: dl.zhang
 * @Date: 2019/4/28
 */
@FeignClient(name = "FUNGO-GAME-GAMES")
public interface GamesFeignClient {

    @RequestMapping(value = "/api/content/gameList", method = RequestMethod.POST)
    FungoPageResultDto<GameOutBean> getGameList( @RequestBody GameItemInput input);

    @RequestMapping(value = "/api/content/games", method = RequestMethod.POST)
    FungoPageResultDto<GameOutPage> getGameList(@RequestBody GameInputPageDto gameInputDto);


    @RequestMapping(value = "/api/gamereleaselog", method = RequestMethod.POST)
    FungoPageResultDto<GameReleaseLogDto> selectOne(GameReleaseLogDto GameReleaseLog);

    @RequestMapping(value = "/api/game/{gameId}", method = RequestMethod.POST)
    GameDto selectOne(@PathVariable("gameId") String gameId);

    @RequestMapping(value = "/api/gameSurveyRel", method = RequestMethod.POST)
    int selectCount(  GameSurveyRelDto gameSurveyRel);

    @RequestMapping(value = "/api/gameEvaluation", method = RequestMethod.POST)
    int selectGameEvaluationCount(  GameEvaluationDto gameEvaluation);
    }
