package com.game.common.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.game.common.bean.MockuserBean;
import com.game.common.entity.SysMockuser;

/**
 * <p>
  * 虚拟用户表 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-06-01
 */
public interface SysMockuserDao extends BaseMapper<SysMockuser> {

	//获取管理员对应的虚拟用户
	public List<MockuserBean> getAssociatedUser(String adminId);
}