package com.fungo.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.system.entity.BasConfig;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
  * 配置信息 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-05-23
 */
public interface BasConfigDao extends BaseMapper<BasConfig> {
	public Map<String,Object> getObjectByTable(@Param("tableName") String tableName, @Param("target_id") String targetId);
}