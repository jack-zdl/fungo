package com.fungo.community.facede;


import com.fungo.community.feign.GameFeignClient;
import com.game.common.bean.MemberPulishFromCommunity;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.EvaluationInputPageDto;
import com.game.common.dto.game.GameEvaluationDto;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class GameFacedeHystrixService implements FallbackFactory<GameFeignClient> {

    private static final Logger logger = LoggerFactory.getLogger(GameFacedeHystrixService.class);

    @Override
    public GameFeignClient create(Throwable throwable) {
        return new GameFeignClient() {

            @Override
            public double selectGameAverage(String gameId, Integer state) {
                logger.error("--------------------GameFeignClient--启动熔断:{}", "selectGameAverage");
                return 0;
            }


            @Override
            public ResultDto<GameDto> selectGameDetailsByIds(@RequestParam("gameIds") String gameIds) {
                logger.error("--------------------GameFeignClient--启动熔断:{}", "selectGameDetails");
                return null;
            }

            @Override
            public ResultDto<GameDto> selectGameDetails(String gameId, Integer state) {
                logger.error("--------------------GameFeignClient--启动熔断:{}", "selectGameDetails");
                return null;
            }

            @Override
            public ResultDto<List<String>> getRecommendMembersFromEvaluation(Integer x, Integer y, List<String> wathMbsSet) {
                logger.error("--------------------GameFeignClient--启动熔断:{}", "getRecommendMembersFromEvaluation");
                return null;
            }

            @Override
            public ResultDto<List<MemberPulishFromCommunity>> getMemberOrder(String gameId, Integer state) {
                logger.error("--------------------GameFeignClient--启动熔断:{}", "getMemberOrder");
                return null;
            }

            @Override
            public ResultDto<GameEvaluationDto> getGameEvaluationSelectOne(String memberId, String targetId) {
                logger.error("--------------------GameFeignClient--启动熔断:{}", "getGameEvaluationSelectOne");
                return null;
            }

            @Override
            public ResultDto<GameEvaluationDto> getGameEvaluationSelectById(String commentId) {
                logger.error("--------------------GameFeignClient--启动熔断:{}", "getGameEvaluationSelectById");
                return null;
            }

            @Override
            public ResultDto<List<GameEvaluationDto>> getEvaluationEntityWrapper(String memberId, String startDate, String endDate) {
                logger.error("--------------------GameFeignClient--启动熔断:{}", "getEvaluationEntityWrapper");
                return null;
            }

            @Override
            public ResultDto<GameEvaluationDto> getPreGameEvaluation(String createdAt, String id) {
                logger.error("--------------------GameFeignClient--启动熔断:{}", "getPreGameEvaluation");
                return null;
            }

            @Override
            public ResultDto<GameEvaluationDto> getNextGameEvaluation(String createdAt, String id) {
                logger.error("--------------------GameFeignClient--启动熔断:{}", "getNextGameEvaluation");
                return null;
            }

            @Override
            public FungoPageResultDto<GameEvaluationDto> getEvaluationEntityWrapperByPageDtoAndMemberId(EvaluationInputPageDto pageDto, String memberId) {
                logger.error("--------------------GameFeignClient--启动熔断:{}", "getEvaluationEntityWrapperByPageDtoAndMemberId");
                return null;
            }
        };
    }
}
