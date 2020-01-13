package com.fungo.system.service.impl;

import com.fungo.system.dao.MemberCircleMapper;
import com.fungo.system.entity.MemberCircle;
import com.fungo.system.function.UserTaskFilterService;
import com.fungo.system.service.IMemberCircleService;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.ResultDto;
import com.game.common.dto.user.MemberNameDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2020/1/3
 */
@Service
public class MemberCircleServiceImpl implements IMemberCircleService {

    @Autowired
    private MemberCircleMapper memberCircleMapper;
    @Autowired
    private UserTaskFilterService userTaskFilterService;

    @Override
    public ResultDto<List<MemberNameDTO>> getCircleMainByMemberId(String circleId) {
        ResultDto<List<MemberNameDTO>> resultDto = null;
        try{
         List<MemberNameDTO> memberNameDTOs =  memberCircleMapper.selectMemberCircleBycircleId(circleId);
         memberNameDTOs.stream().forEach( s ->{
             AuthorBean authorBean = userTaskFilterService.getStatusImages( s.getId());
            s.setStatusImgs( authorBean.getStatusImgs());
         });
         resultDto = ResultDto.ResultDtoFactory.buildSuccess( memberNameDTOs );
        }catch (Exception e){
          resultDto = ResultDto.ResultDtoFactory.buildError( "获取圈子下的圈主异常" );
        }
        return resultDto;
    }

}
