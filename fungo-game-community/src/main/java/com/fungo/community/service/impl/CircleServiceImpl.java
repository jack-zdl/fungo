package com.fungo.community.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.dao.mapper.CmmCircleMapper;
import com.fungo.community.entity.CmmCircle;
import com.fungo.community.feign.SystemFeignClient;
import com.fungo.community.service.CircleService;
import com.fungo.community.service.CmmCircleService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmCircleDto;
import com.game.common.dto.system.CircleFollow;
import com.game.common.dto.system.CircleFollowVo;
import com.game.common.util.CommonUtils;
import com.game.common.util.PageTools;
import com.game.common.vo.CmmCircleVo;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private SystemFeignClient systemFeignClient;

    @Override
    public FungoPageResultDto<CmmCircleDto> selectCircle(String memberId, CmmCircleVo cmmCircleVo) {
        FungoPageResultDto<CmmCircleDto> re ;
        try {
            List<CmmCircleDto> cmmCircleDtoList =  new ArrayList<>();
            Page<CmmCircle> page = new Page<>(cmmCircleVo.getPage(), cmmCircleVo.getLimit());
            List<CmmCircle> list = new ArrayList<>();
            if(CmmCircleVo.SorttypeEnum.ALL.getKey().equals(cmmCircleVo.getSorttype())){
                list = cmmCircleMapper.selectPageByKeyword(page,cmmCircleVo);
                list.stream().forEach(r -> {
                    CmmCircleDto s = new CmmCircleDto();
                    try {
                        BeanUtils.copyProperties(s, r);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    cmmCircleDtoList.add(s);
                });
//            BeanUtils.copyProperties(list,cmmCircleDtoList);
                List<CircleFollow>  circleFollows = new ArrayList<>();
                list.stream().forEach(x -> {
                    CircleFollow circleFollow = new CircleFollow();
                    circleFollow.setCircleId(x.getId());
                    circleFollows.add(circleFollow);
                });
                CircleFollowVo circleFollowVo = new CircleFollowVo();
                circleFollowVo.setMemberId(memberId);
                circleFollowVo.setCircleFollows(circleFollows);
                ResultDto<CircleFollowVo> resultDto = systemFeignClient.circleListFollow(circleFollowVo);
                if(resultDto.isSuccess()){
                    cmmCircleDtoList.stream().forEach(s -> {
                        List<CircleFollow> circleFollow =  resultDto.getData().getCircleFollows().stream().filter(e -> e.getCircleId().equals(s.getId())).collect(Collectors.toList());
                        s.setFollow((circleFollow == null || circleFollow.size() ==0 )? false:circleFollow.get(0).isFollow());
                    });
                }
            }else if(CmmCircleVo.SorttypeEnum.BROWSE.getKey().equals(cmmCircleVo.getSorttype())){
                CircleFollowVo param = new CircleFollowVo();
                param.setMemberId(memberId);
                FungoPageResultDto<String> circleFollowVos = systemFeignClient.circleListMineFollow(param);
                if(circleFollowVos.getData().size() > 0){
                    List<String> ids =circleFollowVos.getData();
                    List<CmmCircle> cmmCircles = cmmCircleMapper.selectPageByIds(page,ids);
                    cmmCircles.stream().forEach(r -> {
                        CmmCircleDto s = new CmmCircleDto();
                        try {
                            BeanUtils.copyProperties(s, r);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        s.setFollow(true);
                        cmmCircleDtoList.add(s);
                    });
                }
            }else if(CmmCircleVo.SorttypeEnum.FOLLOW.getKey().equals(cmmCircleVo.getSorttype())){

            }

//            re = new FungoPageResultDto();
//            re.setData(cmmCircleDtoList);
            re = FungoPageResultDto.FungoPageResultDtoFactory.buildSuccess(cmmCircleDtoList,cmmCircleVo.getPage()-1,page);
//            PageTools.pageToResultDto(re,page);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("获取圈子集合",e);
            re = FungoPageResultDto.error("-1","获取圈子集合异常");
        }
        return re;
    }

}
