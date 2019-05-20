package com.fungo.system.service;


import com.fungo.system.entity.Member;
import java.util.List;

public interface ICommunityService {


	/**
	 * 查询玩家推荐榜
	 * @param limit 玩家数量
	 * @param currentMb_id 当前登录用户id
	 * @return
	 */
	List<Member> getRecomMembers(int limit, String currentMb_id);
	
	
}
