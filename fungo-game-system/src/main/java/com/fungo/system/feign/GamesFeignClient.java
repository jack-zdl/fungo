package com.fungo.system.feign;

import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.game.*;
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

    @RequestMapping(value = "/ms/service/api/content/gameList", method = RequestMethod.POST)
    FungoPageResultDto<GameOutBean> getGameList( @RequestBody GameItemInput input);

    @RequestMapping(value = "/ms/service/api/content/games", method = RequestMethod.POST)
    FungoPageResultDto<GameOutPage> getGameList(@RequestBody GameInputPageDto gameInputDto);


    @RequestMapping(value = "/ms/service/api/gamereleaselog", method = RequestMethod.POST)
    FungoPageResultDto<GameReleaseLogDto> selectOne(GameReleaseLogDto GameReleaseLog);

    @RequestMapping(value = "/ms/service/api/game/{gameId}", method = RequestMethod.POST)
    GameDto selectOne(@PathVariable("gameId") String gameId);

    /**
     * 动态表 辅助计数器
     * @param map
     * @return
     */
    @RequestMapping(value = "/ms/service/api/update/counter", method = RequestMethod.POST)
    boolean updateCounter(@RequestBody Map<String, String> map);

    @RequestMapping(value = "/ms/service/api/gameSurveyRel", method = RequestMethod.POST)
    int selectCount(  GameSurveyRelDto gameSurveyRel);

    @RequestMapping(value = "/ms/service/api/gameEvaluation", method = RequestMethod.POST)
    int selectGameEvaluationCount(  GameEvaluationDto gameEvaluation);

    /**
     * 被点赞用户的id
     * @param map
     * @return
     */
    @RequestMapping(value = "/ms/service/api/getMemberIdByTargetId", method = RequestMethod.POST)
    String getMemberIdByTargetId(@RequestBody Map<String, String> map);
}
