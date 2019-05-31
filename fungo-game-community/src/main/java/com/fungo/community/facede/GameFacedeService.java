package com.fungo.community.facede;

import com.fungo.community.feign.GameFeignClient;
import com.game.common.bean.MemberPulishFromCommunity;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.EvaluationInputPageDto;
import com.game.common.dto.game.GameEvaluationDto;
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

    public double selectGameAverage(String gameId, Integer state) {
        try {
            return gameFeignClient.selectGameAverage(gameId, state);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }


    /**
     * 根据游戏id和状态查询游戏详情
     * @param gameId
     * @param state
     * @return
     */

    public ResultDto<GameDto> selectGameDetails(String gameId, Integer state) {
        try {
            return gameFeignClient.selectGameDetails(gameId, state);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<GameDto>();
    }


    /**
     * 查询游戏评论表中发表评论大于X条，前Y名的用户
     * @param x
     * @param y
     * @return
     */

    public ResultDto<List<String>> getRecommendMembersFromEvaluation(Integer x, Integer y, List<String> wathMbsSet) {
        try {
            return gameFeignClient.getRecommendMembersFromEvaluation(x, y, wathMbsSet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<List<String>>();
    }


    /**
     * 根据游戏id查询参与评论的用户
     * @param gameId
     * @param state
     * @return
     */

    public ResultDto<List<MemberPulishFromCommunity>> getMemberOrder(String gameId, Integer state) {
        try {
            return gameFeignClient.getMemberOrder(gameId, state);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<List<MemberPulishFromCommunity>>();
    }


    public ResultDto<GameEvaluationDto> getGameEvaluationSelectOne(String memberId, String targetId) {
        try {
            return gameFeignClient.getGameEvaluationSelectOne(memberId, targetId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<GameEvaluationDto>();
    }

    public ResultDto<GameEvaluationDto> getGameEvaluationSelectById(String commentId) {
        try {
            return gameFeignClient.getGameEvaluationSelectById(commentId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<GameEvaluationDto>();
    }


    public ResultDto<List<GameEvaluationDto>> getEvaluationEntityWrapper(String memberId, String startDate, String endDate) {
        try {
            return gameFeignClient.getEvaluationEntityWrapper(memberId, startDate, endDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<List<GameEvaluationDto>>();
    }


    public ResultDto<GameEvaluationDto> getPreGameEvaluation(String createdAt, String id) {
        try {
            return gameFeignClient.getPreGameEvaluation(createdAt, id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<GameEvaluationDto>();
    }


    public ResultDto<GameEvaluationDto> getNextGameEvaluation(String createdAt, String id) {
        try {
            return gameFeignClient.getNextGameEvaluation(createdAt, id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<GameEvaluationDto>();
    }


    public FungoPageResultDto<GameEvaluationDto> getEvaluationEntityWrapperByPageDtoAndMemberId(EvaluationInputPageDto pageDto, String memberId) {
        try {
            return gameFeignClient.getEvaluationEntityWrapperByPageDtoAndMemberId(pageDto, memberId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new FungoPageResultDto<GameEvaluationDto>();
    }


}
