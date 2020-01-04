package com.fungo.system.service;

import com.fungo.system.entity.MemberCircle;
import com.game.common.dto.ResultDto;
import com.game.common.dto.user.MemberNameDTO;

import java.util.List;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2020/1/3
 */
public interface IMemberCircleService {

    ResultDto<List<MemberNameDTO>> getCircleMainByMemberId(String circleId);
}
