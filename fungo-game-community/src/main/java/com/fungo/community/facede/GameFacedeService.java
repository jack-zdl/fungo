package com.fungo.community.facede;

import com.fungo.community.feign.GameFeignClient;
import com.game.common.bean.MemberPulishFromCommunity;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.EvaluationInputPageDto;
import com.game.common.dto.game.GameEvaluationDto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameFacedeService {

    private static final Logger logger = LoggerFactory.getLogger(GameFacedeService.class);


    @Autowired(required = false)
    private GameFeignClient gameFeignClient;


    /**
     * 获取游戏平均分
     * @param gameId
     * @param state
     * @return
     */
    @HystrixCommand(fallbackMethod = "hystrixSelectGameAverage", ignoreExceptions = {Exception.class},
            commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    public double selectGameAverage(String gameId, Integer state) {
        try {
            return gameFeignClient.selectGameAverage(gameId, state);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public double hystrixSelectGameAverage(String gameId, Integer state) {

        return 0;
    }


    /**
     * 根据游戏id和状态查询游戏详情
     * @param gameId
     * @param state
     * @return
     */
    @HystrixCommand(fallbackMethod = "hystrixSelectGameDetails", ignoreExceptions = {Exception.class},
            commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    public ResultDto<GameDto> selectGameDetails(String gameId, Integer state) {
        try {
            return gameFeignClient.selectGameDetails(gameId, state);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<GameDto>();
    }

    public ResultDto<GameDto> hystrixSelectGameDetails(String gameId, Integer state) {
        return new ResultDto<GameDto>();
    }


    /**
     * 查询游戏评论表中发表评论大于X条，前Y名的用户
     * @param x
     * @param y
     * @return
     */
    @HystrixCommand(fallbackMethod = "hystrixGetRecommendMembersFromEvaluation", ignoreExceptions = {Exception.class},
            commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    public ResultDto<List<String>> getRecommendMembersFromEvaluation(Integer x, Integer y, List<String> wathMbsSet) {
        try {
            return gameFeignClient.getRecommendMembersFromEvaluation(x, y, wathMbsSet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<List<String>>();
    }

    public ResultDto<List<String>> hystrixGetRecommendMembersFromEvaluation(Integer x, Integer y, List<String> wathMbsSet) {
        return new ResultDto<List<String>>();
    }


    /**
     * 根据游戏id查询参与评论的用户
     * @param gameId
     * @param state
     * @return
     */
    @HystrixCommand(fallbackMethod = "hystrixGetMemberOrder", ignoreExceptions = {Exception.class},
            commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    public ResultDto<List<MemberPulishFromCommunity>> getMemberOrder(String gameId, Integer state) {
        try {
            return gameFeignClient.getMemberOrder(gameId, state);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<List<MemberPulishFromCommunity>>();
    }

    public ResultDto<List<MemberPulishFromCommunity>> hystrixGetMemberOrder(String gameId, Integer state) {
        return new ResultDto<List<MemberPulishFromCommunity>>();
    }


    @HystrixCommand(fallbackMethod = "hystrixGetGameEvaluationSelectOne", ignoreExceptions = {Exception.class},
            commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    public ResultDto<GameEvaluationDto> getGameEvaluationSelectOne(String memberId, String targetId) {
        try {
            return gameFeignClient.getGameEvaluationSelectOne(memberId, targetId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<GameEvaluationDto>();
    }

    public ResultDto<GameEvaluationDto> hystrixGetGameEvaluationSelectOne(String memberId, String targetId) {
        return new ResultDto<GameEvaluationDto>();
    }


    @HystrixCommand(fallbackMethod = "hystrixGetGameEvaluationSelectById", ignoreExceptions = {Exception.class},
            commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    public ResultDto<GameEvaluationDto> getGameEvaluationSelectById(String commentId) {
        try {
            return gameFeignClient.getGameEvaluationSelectById(commentId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<GameEvaluationDto>();
    }

    public ResultDto<GameEvaluationDto> hystrixGetGameEvaluationSelectById(String commentId) {
        return new ResultDto<GameEvaluationDto>();
    }

    @HystrixCommand(fallbackMethod = "hystrixGetEvaluationEntityWrapper", ignoreExceptions = {Exception.class},
            commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    public ResultDto<List<GameEvaluationDto>> getEvaluationEntityWrapper(String memberId, String startDate, String endDate) {
        try {
            return gameFeignClient.getEvaluationEntityWrapper(memberId, startDate, endDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<List<GameEvaluationDto>>();
    }

    public ResultDto<List<GameEvaluationDto>> hystrixGetEvaluationEntityWrapper(String memberId, String startDate, String endDate) {
        return new ResultDto<List<GameEvaluationDto>>();
    }


    @HystrixCommand(fallbackMethod = "hystrixGetPreGameEvaluation", ignoreExceptions = {Exception.class},
            commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    public ResultDto<GameEvaluationDto> getPreGameEvaluation(String createdAt, String id) {
        try {
            return gameFeignClient.getPreGameEvaluation(createdAt, id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<GameEvaluationDto>();
    }

    public ResultDto<GameEvaluationDto> hystrixGetPreGameEvaluation(String createdAt, String id) {
        return new ResultDto<GameEvaluationDto>();
    }


    @HystrixCommand(fallbackMethod = "hystrixGetNextGameEvaluation", ignoreExceptions = {Exception.class},
            commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    public ResultDto<GameEvaluationDto> getNextGameEvaluation(String createdAt, String id) {
        try {
            return gameFeignClient.getNextGameEvaluation(createdAt, id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<GameEvaluationDto>();
    }

    public ResultDto<GameEvaluationDto> hystrixGetNextGameEvaluation(String createdAt, String id) {
        return new ResultDto<GameEvaluationDto>();
    }


    @HystrixCommand(fallbackMethod = "hystrixGetEvaluationEntityWrapperByPageDtoAndMemberId", ignoreExceptions = {Exception.class},
            commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    public FungoPageResultDto<GameEvaluationDto> getEvaluationEntityWrapperByPageDtoAndMemberId(EvaluationInputPageDto pageDto, String memberId) {
        try {
            return gameFeignClient.getEvaluationEntityWrapperByPageDtoAndMemberId(pageDto, memberId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new FungoPageResultDto<GameEvaluationDto>();
    }

    public FungoPageResultDto<GameEvaluationDto> hystrixGetEvaluationEntityWrapperByPageDtoAndMemberId(EvaluationInputPageDto pageDto, String memberId) {
        return new FungoPageResultDto<GameEvaluationDto>();
    }

}
