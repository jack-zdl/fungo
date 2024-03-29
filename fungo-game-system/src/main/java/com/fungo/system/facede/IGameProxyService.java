package com.fungo.system.facede;

import com.game.common.dto.GameDto;
import com.game.common.dto.community.CmmCommentDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.community.MooMessageDto;
import com.game.common.dto.community.MooMoodDto;
import com.game.common.dto.game.GameEvaluationDto;
import com.game.common.dto.game.GameInviteDto;
import com.game.common.dto.index.CardIndexBean;

import java.util.List;
import java.util.Map;

/**
 * <p>GameProxyImpl 对接外部接口</p>
 * @Author: dl.zhang
 * @Date: 2019/5/13
 */
public interface IGameProxyService {



    CmmPostDto selectCmmPostById(CmmPostDto param);

    CmmCommentDto selectCmmCommentById(String id);

    GameEvaluationDto selectGameEvaluationById(GameEvaluationDto gameEvaluationDto);

    GameDto selectGameById(GameDto param);

    MooMoodDto selectMooMoodById(String id);

    MooMessageDto selectMooMessageById(String id);

    int getGameSelectCountByLikeNameAndState(GameDto gameDto);

    List<GameEvaluationDto> getEvaluationEntityWrapper(String memberId, String startDate, String endDate);

    GameInviteDto selectGameInvite(GameInviteDto gameInviteDto);


    List<Map<String,Object>> getEvaluationFeeds(Map<String, Object> map);

    List<String> getRecommendMembersFromEvaluation(Integer x, Integer y, List<String> wathMbsSet);

    CardIndexBean selectedGames();

    List<GameEvaluationDto> selectGameEvaluationPage();

    List<Map> getHonorQualificationOfEssenceEva();
}
