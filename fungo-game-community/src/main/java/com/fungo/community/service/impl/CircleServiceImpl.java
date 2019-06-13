package com.fungo.community.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.dao.mapper.CmmCircleMapper;
import com.fungo.community.entity.CmmCircle;
import com.fungo.community.service.CircleService;
import com.fungo.community.service.CmmCircleService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.community.CmmCircleDto;
import com.game.common.vo.CmmCircleVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CmmCircleService cmmCircleServiceImap;

    @Autowired
    private CmmCircleMapper cmmCircleMapper;

    @Override
    public FungoPageResultDto<CmmCircleDto> selectCircle(String memberId, CmmCircleVo cmmCircleVo) {
        FungoPageResultDto<CmmCircleDto> re ;
        try {
            Page<CmmCircle> page = new Page<>(cmmCircleVo.getPage(), cmmCircleVo.getLimit());
            CmmCircle cmmCircle = new CmmCircle();
            Wrapper wrapper = new EntityWrapper<CmmCircle>().orderBy("created_by", false);;
            if(CmmCircleVo.SorttypeEnum.ALL.getKey().equals(cmmCircleVo.getSorttype())){
                cmmCircleMapper.selectPageByKeyword(page,cmmCircleVo.getKeyword());
            }else if(CmmCircleVo.SorttypeEnum.BROWSE.getKey().equals(cmmCircleVo.getSorttype())){

            }else if(CmmCircleVo.SorttypeEnum.FOLLOW.getKey().equals(cmmCircleVo.getSorttype())){

            }

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
