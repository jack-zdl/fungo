package com.fungo.community.service;

import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmCircleDto;
import com.game.common.vo.CmmCircleVo;

import java.lang.reflect.InvocationTargetException;

/**
 * <p>圈子接口</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/11
 */
public interface CircleService {

    FungoPageResultDto<CmmCircleDto> selectCircle(String memberId, CmmCircleVo cmmCircleVo);

    ResultDto<CmmCircleDto> selectCircleById(String memberId, String circleId) throws InvocationTargetException, IllegalAccessException;


}
