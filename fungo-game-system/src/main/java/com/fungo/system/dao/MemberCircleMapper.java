package com.fungo.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.system.entity.MemberCircle;
import com.game.common.dto.community.MemberCmmCircleDto;
import com.game.common.dto.user.MemberNameDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MemberCircleMapper extends BaseMapper<MemberCircle> {

    MemberCmmCircleDto selectMemberCircleByUserId(@Param("userId") String userId);

    List<MemberNameDTO> selectMemberCircleBycircleId(@Param("circleId") String circleId);
}