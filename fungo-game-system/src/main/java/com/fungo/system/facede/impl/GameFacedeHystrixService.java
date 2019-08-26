package com.fungo.system.facede.impl;



import com.fungo.system.feign.GamesFeignClient;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.*;
import com.game.common.dto.index.CardIndexBean;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameFacedeHystrixService implements FallbackFactory<GamesFeignClient> {

    private static final Logger logger = LoggerFactory.getLogger(GameFacedeHystrixService.class);

    @Override
    public GamesFeignClient create(Throwable throwable) {
        return new GamesFeignClient() {
            @Override
            public FungoPageResultDto<GameOutBean> getGameList(GameItemInput input) {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "getGameList");
                return null;
            }

            @Override
            public FungoPageResultDto<GameOutPage> getGameList(GameInputPageDto gameInputDto) {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "getGameList");
                return null;
            }

            @Override
            public FungoPageResultDto<GameReleaseLogDto> selectOne(GameReleaseLogDto GameReleaseLog) {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "selectOne");
                return null;
            }

            @Override
            public FungoPageResultDto<GameReleaseLogDto> getGameReleaseLogPage(GameReleaseLogDto gameReleaseLogDto) {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "getGameReleaseLogPage");
                return null;
            }

            @Override
            public GameDto selectOne(String gameId) {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "selectOne");
                return null;
            }

            @Override
            public boolean updateCounter(Map<String, String> map) {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "updateCounter");
                return false;
            }

            @Override
            public int gameSurveySelectCount(GameSurveyRelDto gameSurveyRel) {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "gameSurveySelectCount");
                return 0;
            }

            @Override
            public int gameEvaluationSelectCount(GameEvaluationDto gameEvaluation) {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "gameEvaluationSelectCount");
                return 0;
            }

            @Override
            public String getMemberIdByTargetId(Map<String, String> map) {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "getMemberIdByTargetId");
                return null;
            }

            @Override
            public FungoPageResultDto<GameEvaluationDto> getGameEvaluationPage(GameEvaluationDto gameEvaluationDto) {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "getGameEvaluationPage");
                return null;
            }

            @Override
            public FungoPageResultDto<GameDto> getGamePage(GameDto gameDto) {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "getGamePage");
                return null;
            }

            @Override
            public FungoPageResultDto<GameSurveyRelDto> getGameSurveyRelPage(GameSurveyRelDto gameSurveyDto) {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "getGameSurveyRelPage");
                return null;
            }

            @Override
            public int getGameSelectCountByLikeNameAndState(GameDto gameDto) {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "getGameSelectCountByLikeNameAndState");
                return 0;
            }

            @Override
            public ResultDto<CardIndexBean> getSelectedGames() {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "getSelectedGames");
                return null;
            }

            @Override
            public ResultDto<CardIndexBean> getSelectedGames(GameDto gameDto) {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "getSelectedGames");
                return null;
            }

            @Override
            public ResultDto<HashMap<String, BigDecimal>> getRateData(String gameId) {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "getRateData");
                return null;
            }

            @Override
            public ResultDto<List<GameEvaluationDto>> getEvaluationEntityWrapper(String memberId, String startDate, String endDate) {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "getEvaluationEntityWrapper");
                return null;
            }

            @Override
            public FungoPageResultDto<GameInviteDto> getGameInvitePage(GameInviteDto gameInviteDto) {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "getGameInvitePage");
                return null;
            }

            @Override
            public ResultDto<List<String>> getRecommendMembersFromEvaluation(Integer x, Integer y, List<String> wathMbsSet) {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "getRecommendMembersFromEvaluation");
                return null;
            }

            @Override
            public CardIndexBean selectedGames() {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "selectedGames");
                return null;
            }

            @Override
            public FungoPageResultDto<GameEvaluationDto> selectGameEvaluationPage() {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "selectGameEvaluationPage");
                return null;
            }

            @Override
            public ResultDto<List<Map>> getUserGameReviewBoutiqueNumber() {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "getUserGameReviewBoutiqueNumber");
                return null;
            }

            @Override
            public FungoPageResultDto<GameSurveyRelDto> getMemberNoticeByGame() {
                logger.error("--------------------GameFeignClient--启动熔断:{}" , "getMemberNoticeByGame");
                return null;
            }
        };

    }
}
