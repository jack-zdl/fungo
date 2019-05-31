package com.fungo.community.feign;


import com.fungo.community.facede.GameFacedeHystrixService;
import com.game.common.bean.MemberPulishFromCommunity;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.EvaluationInputPageDto;
import com.game.common.dto.game.GameEvaluationDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 *      游戏微服务接口
 * </p>
 *
 * @author mxf
 * @since 2018-11-08
 */
@FeignClient(name = "FUNGO-GAME-GAMES",fallbackFactory = GameFacedeHystrixService.class)
@RequestMapping("/ms/service/game")
public interface GameFeignClient {


    /**
     * 获取游戏平均分
     * @param gameId
     * @param state
     * @return
     */
    @ApiOperation(value = "获取游戏平均分", notes = "")
    @RequestMapping(value = "/api/game/average", method = RequestMethod.POST)
    public double selectGameAverage(@RequestParam("gameId") String gameId, @RequestParam("state") Integer state);


    /**
     * 根据游戏id和状态查询游戏详情
     * @param gameId
     * @param state
     * @return
     */
    @ApiOperation(value = "根据游戏id和状态查询游戏详情", notes = "")
    @RequestMapping(value = "/api/game/details", method = RequestMethod.POST)
    public ResultDto<GameDto> selectGameDetails(@RequestParam("gameId") String gameId, @RequestParam("state") Integer state);



    /**
     * 查询游戏评论表中发表评论大于X条，前Y名的用户
     * @param x
     * @param y
     * @return
     */
    @ApiOperation(value = "查询游戏评论表中发表评论大于X条，前Y名的用户", notes = "")
    @RequestMapping(value = "/api/game/getRecommendMembersFromEvaluation", method = RequestMethod.POST)
    ResultDto<List<String>> getRecommendMembersFromEvaluation(@RequestParam("x") Integer x, @RequestParam("y") Integer y, @RequestParam("wathMbsSet") List<String> wathMbsSet);

    /**
     * 根据游戏id查询参与评论的用户
     * @param gameId
     * @param state
     * @return
     */
    @ApiOperation(value = "根据游戏id查询参与评论的用户", notes = "")
    @RequestMapping(value = "/api/game/getMemberOrder", method = RequestMethod.POST)
    public ResultDto<List<MemberPulishFromCommunity>> getMemberOrder(@RequestParam("gameId") String gameId,@RequestParam("state") Integer state);



    @ApiOperation(value = "gameEvaluationService.selectOne", notes = "")
    @RequestMapping(value = "/api/game/getGameEvaluationSelectOne", method = RequestMethod.POST)
    ResultDto<GameEvaluationDto> getGameEvaluationSelectOne(@RequestParam("memberId") String memberId, @RequestParam("targetId") String targetId);




    @ApiOperation(value = "gameEvaluationService.selectById", notes = "")
    @RequestMapping(value = "/api/game/getGameEvaluationSelectById", method = RequestMethod.POST)
    ResultDto<GameEvaluationDto> getGameEvaluationSelectById(@RequestParam("commentId") String commentId);



    @ApiOperation(value = "getEvaluationEntityWrapper", notes = "")
    @RequestMapping(value = "/api/game/getEvaluationEntityWrapper", method = RequestMethod.POST)
    ResultDto<List<GameEvaluationDto>> getEvaluationEntityWrapper(@RequestParam("memberId") String memberId, @RequestParam("startDate") String startDate,
                                                                  @RequestParam("endDate") String endDate);




    @ApiOperation(value = "getPreGameEvaluation上一评论", notes = "")
    @RequestMapping(value = "/api/game/getPreGameEvaluation", method = RequestMethod.POST)
    ResultDto<GameEvaluationDto> getPreGameEvaluation(@RequestParam("createdAt") String createdAt, @RequestParam("id") String id);




    @ApiOperation(value = "getNextGameEvaluation下一评论", notes = "")
    @RequestMapping(value = "/api/game/getNextGameEvaluation", method = RequestMethod.POST)
    ResultDto<GameEvaluationDto> getNextGameEvaluation(@RequestParam("createdAt") String createdAt, @RequestParam("id") String id);



    @ApiOperation(value = "getEvaluationEntityWrapperByPageDtoAndMemberId", notes = "")
    @RequestMapping(value = "/api/game/getEvaluationEntityWrapperByPageDtoAndMemberId", method = RequestMethod.POST)
    FungoPageResultDto<GameEvaluationDto> getEvaluationEntityWrapperByPageDtoAndMemberId(@RequestBody EvaluationInputPageDto pageDto, @RequestParam("memberId") String memberId);




    //----------
}
