package com.game.common.service.impl.member;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.game.common.entity.member.MemberNotice;
import com.game.common.mapper.member.MemberNoticeDao;
import com.game.common.service.member.MemberNoticeDaoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户消息临时表 服务实现类
 * </p>
 *
 * @author mxf
 * @since 2019-03-15
 */
@Service
public class MemberNoticeDaoServiceImpl extends ServiceImpl<MemberNoticeDao, MemberNotice> implements MemberNoticeDaoService {
	
}
