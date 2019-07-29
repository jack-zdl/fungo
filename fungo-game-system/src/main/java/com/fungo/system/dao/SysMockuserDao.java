package com.fungo.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.system.entity.SysMockuser;
import com.game.common.bean.MockuserBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
  * 虚拟用户表 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-06-01
 */
@Repository
public interface SysMockuserDao extends BaseMapper<SysMockuser> {

	//获取管理员对应的虚拟用户
	public List<MockuserBean> getAssociatedUser(String adminId);
}