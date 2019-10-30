package com.fungo.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.system.entity.MemberPlayLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p></p>
 * @Date: 2019/10/22
 */
@Repository
public interface MemberPlayLogDao   extends BaseMapper<MemberPlayLog> {

    List<MemberPlayLog> getAllByState(@Param( "state" )String state);
}
