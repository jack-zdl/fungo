package com.fungo.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.system.entity.MemberNo;

/**
 * <p>
  * 会员编码列表 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-05-31
 */
public interface MemberNoDao extends BaseMapper<MemberNo> {
	
	//获取随机用户号
	public MemberNo getRandomMemberNo();
}