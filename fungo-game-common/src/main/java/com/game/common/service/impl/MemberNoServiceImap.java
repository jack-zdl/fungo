package com.game.common.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.game.common.entity.Member;
import com.game.common.entity.MemberNo;
import com.game.common.mapper.MemberNoDao;
import com.game.common.service.MemberNoService;
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
