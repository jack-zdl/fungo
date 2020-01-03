package com.fungo.system.service.impl;

import com.fungo.system.dao.MemberCircleMapper;
import com.fungo.system.entity.MemberCircle;
import com.fungo.system.service.IMemberCircleService;
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

    @Override
    public MemberCircle getCircleMainByMemberId(String circleId) {
        try{
         List<MemberNameDTO> memberNameDTO =  memberCircleMapper.selectMemberCircleBycircleId(circleId);
        }catch (Exception e){

        }
        return null;
    }

}
