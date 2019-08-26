package com.fungo.system.facede;

import com.baomidou.mybatisplus.plugins.Page;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.community.CmmCmtReplyDto;
import com.game.common.dto.community.CmmCommunityDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.community.MooMoodDto;
import com.game.common.dto.game.GameEvaluationDto;
import com.game.common.dto.game.GameSurveyRelDto;

/**
 * <p>MerberServiceImpl的对外接口层</p>
 * @Author: dl.zhang
 * @Date: 2019/5/15
 */
public interface IMemeberProxyService {

    CmmPostDto selectCmmPost(String id);

    FungoPageResultDto<GameSurveyRelDto> selectGameSurveyRelPage(int page,int limit,String memberId,int status);

    int selectMooMoodCount(MooMoodDto mooMoodDto);

//    Page<MooMoodDto> selectMooMoodPage(MooMoodDto mooMoodDto);

    int selectReplyCount(CmmCmtReplyDto replyDto);

    CmmCmtReplyDto selectReplyById(CmmCmtReplyDto replyDto);

    Page<GameEvaluationDto> selectGameEvaluationPage(GameEvaluationDto gameEvaluationDto);

//    FungoPageResultDto<CmmPostDto> selectCmmPostpage(CmmPostDto  cmmPostDto);

    CmmCommunityDto selectCmmCommunityById(CmmCommunityDto cmmCommunityDto);

    int CmmPostCount(CmmPostDto cmmPostDto);

}
