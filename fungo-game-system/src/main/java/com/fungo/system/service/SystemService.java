package com.fungo.system.service;

import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.user.IncentRankedDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.dto.user.MemberFollowerDto;
import com.game.common.vo.MemberFollowerVo;

/**
 * <p>系统微服务对外服务层</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/10
 */
public interface SystemService {

    FungoPageResultDto<String> getFollowerUserId(String memberId);

    FungoPageResultDto<MemberFollowerDto> getMemberFollowerList(MemberFollowerVo vo);

    FungoPageResultDto<MemberDto> getMemberDtoList(MemberDto memberDto);

    FungoPageResultDto<IncentRankedDto> getIncentRankedList(IncentRankedDto incentRankedDto);
}
