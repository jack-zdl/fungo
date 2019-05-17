package com.fungo.community.feign;


import com.game.common.bean.MemberPulishFromCommunity;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * <p>
 *      游戏微服务接口
 * </p>
 *
 * @author mxf
 * @since 2018-11-08
 */
@FeignClient(name = "FUNGO-GAME-COMMUNITY")
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
    public double selectGameAverage(String gameId, Integer state);


    /**
     * 根据游戏id和状态查询游戏详情
     * @param gameId
     * @param state
     * @return
     */
    @ApiOperation(value = "根据游戏id和状态查询游戏详情", notes = "")
    @RequestMapping(value = "/api/game/details", method = RequestMethod.POST)
    public ResultDto<GameDto> selectGameDetails(String gameId, Integer state);


    /**
     * 查询游戏评论表中发表评论大于X条，前Y名的用户
     * @param x
     * @param y
     * @return
     */
    @ApiOperation(value = "查询游戏评论表中发表评论大于X条，前Y名的用户", notes = "")
    @RequestMapping(value = "/api/game/getRecommendMembersFromEvaluation", method = RequestMethod.POST)
    public ResultDto<List<String>> getRecommendMembersFromEvaluation(Integer x, Integer y);


    /**
     * 根据游戏id查询参与评论的用户
     * @param gameId
     * @param state
     * @return
     */
    @ApiOperation(value = "根据游戏id查询参与评论的用户", notes = "")
    @RequestMapping(value = "/api/game/getMemberOrder", method = RequestMethod.POST)
    public ResultDto<List<MemberPulishFromCommunity>> getMemberOrder(String gameId, Integer state);


    //----------
}
