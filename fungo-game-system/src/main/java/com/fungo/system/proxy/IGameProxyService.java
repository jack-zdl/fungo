package com.fungo.system.proxy;

import com.game.common.dto.GameDto;
import com.game.common.dto.community.CmmCommentDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.community.MooMessageDto;
import com.game.common.dto.community.MooMoodDto;
import com.game.common.dto.game.GameEvaluationDto;

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
}
