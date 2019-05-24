package com.fungo.system.service;


import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.community.FollowUserOutBean;
import com.game.common.dto.mark.MakeInputPageDto;

public interface IMakeAndInviteGameService {
	
	/**
	 *
	 * @param memberId
	 * @param inputPageDto
	 * @return
	 * @throws Exception
	 */
	public FungoPageResultDto<FollowUserOutBean> getInviteUserList(String memberId, MakeInputPageDto inputPageDto) throws Exception;

}
