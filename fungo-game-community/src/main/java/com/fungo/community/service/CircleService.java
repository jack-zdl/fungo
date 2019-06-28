package com.fungo.community.service;

import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CirclePostTypeDto;
import com.game.common.dto.community.CircleTypeDto;
import com.game.common.dto.community.CmmCircleDto;
import com.game.common.dto.community.PostOutBean;
import com.game.common.vo.CircleGamePostVo;
import com.game.common.vo.CmmCirclePostVo;
import com.game.common.vo.CmmCircleVo;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * <p>圈子接口</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/11
 */
public interface CircleService {

    FungoPageResultDto<CmmCircleDto> selectCircle(String memberId, CmmCircleVo cmmCircleVo);

    ResultDto<CmmCircleDto> selectCircleById(String memberId, String circleId) throws InvocationTargetException, IllegalAccessException;

    FungoPageResultDto<PostOutBean> selectCirclePost(String memberId, CmmCirclePostVo cmmCirclePostVo);

    ResultDto<List<CirclePostTypeDto>> selectCirclePostType(String memberId, CmmCirclePostVo cmmCirclePostVo);

    ResultDto<List<CircleTypeDto>> selectCircleType(String memberId);

    ResultDto<CmmCircleDto> selectCircleByGame(String memberId,String gameId);

    FungoPageResultDto<PostOutBean>  selectCircleGamePost(String memberId, CircleGamePostVo circleGamePostVo);

    FungoPageResultDto<CmmCircleDto> selectGameCircle(String memberId,CircleGamePostVo circleGamePostVo);
}
