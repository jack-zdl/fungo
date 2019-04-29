package com.fungo.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.system.entity.DeveloperGameRel;

import java.util.Map;

/**
 * <p>
  * 游戏开发者关联表 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-06-08
 */
public interface DeveloperGameRelDao extends BaseMapper<DeveloperGameRel> {
	// 删除用户游戏关联 
	public boolean delLinkedMember(Map<String, String> map);
}