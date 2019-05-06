package com.fungo.community.service;

import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CommunityInputPageDto;
import com.game.common.dto.community.CommunityMember;
import com.game.common.dto.community.CommunityOutPageDto;

import java.lang.reflect.InvocationTargetException;

public interface ICommunityService {

	/**
	 * 社区详情
	 * 请求地址：http://{{host}}/api/content/post/:post_id
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws Exception 
	 */
	public ResultDto getCommunityDetail(String communityId, String userId) throws Exception;

	/**
	 * 社区列表
	 * 请求地址：http://{{host}}/api/content/communitys
	 * @param keyword
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public FungoPageResultDto<CommunityOutPageDto> getCmmCommunityList(String userId, CommunityInputPageDto communityInputPageDto);

	/**
	 * 社区玩家榜
	 * @param userId
	 * @param input
	 * @return
	 */
	public FungoPageResultDto<CommunityMember> memberList(String userId, CommunityInputPageDto input);

	/**
	 * 查询玩家推荐榜
	 * @param limit 玩家数量
	 * @param currentMb_id 当前登录用户id
	 * @return
	 */
//	List<Member> getRecomMembers(int limit, String currentMb_id);
	
	
}
