package com.fungo.community.service.impl;

import com.fungo.community.controller.CircleController;
import com.fungo.community.service.CircleService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.community.CmmCircleDto;
import com.game.common.vo.CmmCircleVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * <p>圈子接口实现类</p>
 * @Author: dl.zhang
 * @Date: 2019/6/11
 */
@Service
public class CircleServiceImpl implements CircleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CircleServiceImpl.class);

    @Override
    public FungoPageResultDto<CmmCircleDto> selectCircle(String memberId, CmmCircleVo cmmCircleVo) {
        FungoPageResultDto<CmmCircleDto> re ;
        try {
            CmmCircleDto cmmCircleDto = new CmmCircleDto();
            re = new FungoPageResultDto<>();
            re.setData(Arrays.asList(cmmCircleDto));
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("获取圈子集合",e);
            re = FungoPageResultDto.error("-1","获取圈子集合异常");
        }
        return re;
    }

}
