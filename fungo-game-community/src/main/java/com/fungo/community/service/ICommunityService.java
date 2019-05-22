package com.fungo.community.service;

import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CommunityInputPageDto;
import com.game.common.dto.community.CommunityMember;
import com.game.common.dto.community.CommunityOutPageDto;
import com.game.common.dto.community.CommunitySearchOut;
import com.game.common.dto.user.MemberDto;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

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
	 * @param userId
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
	List<MemberDto> getRecomMembers(int limit, String currentMb_id);



	//搜索社区
	public FungoPageResultDto<CommunitySearchOut> searchCommunitys(int page, int limit, String keyword, String userId);

}
