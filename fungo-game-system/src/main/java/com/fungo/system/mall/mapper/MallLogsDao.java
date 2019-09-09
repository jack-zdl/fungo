package com.fungo.system.mall.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.system.mall.entity.MallLogs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author mxf
 * @since 2019-01-18
 */
public interface MallLogsDao extends BaseMapper<MallLogs> {
    List<MallLogs> selectMallLogsByUserId(@Param( "userId" ) String userId);
}