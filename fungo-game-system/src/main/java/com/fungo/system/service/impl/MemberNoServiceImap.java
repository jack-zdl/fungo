package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.system.dao.MemberNoDao;
import com.fungo.system.entity.Member;
import com.fungo.system.entity.MemberNo;
import com.fungo.system.service.MemberNoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员编码列表 服务实现类
 * </p>
 *
 * @author lzh
 * @since 2018-05-31
 */
@Service
public class MemberNoServiceImap extends ServiceImpl<MemberNoDao, MemberNo> implements MemberNoService {

	//@Override
	public boolean getMemberNoAndForUpdate(Member member) {
		MemberNo memberCode=this.baseMapper.getRandomMemberNo();
		memberCode.setMemberId(member.getId());
		memberCode.setState(1);
		memberCode.updateById();
		member.setMemberNo(memberCode.getMemberNo());
		member.updateById();
		return true;
	}
	
	
}
