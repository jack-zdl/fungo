package com.game.common.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.game.common.entity.BasConfig;

/**
 * <p>
  * 配置信息 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-05-23
 */
public interface BasConfigDao extends BaseMapper<BasConfig> {
	public Map<String,Object> getObjectByTable(@Param("tableName") String tableName,@Param("target_id") String targetId);
}