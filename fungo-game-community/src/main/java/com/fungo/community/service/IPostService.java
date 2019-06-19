package com.fungo.community.service;


import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ObjectId;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.PostInput;
import com.game.common.dto.community.PostInputPageDto;
import com.game.common.dto.community.PostOutBean;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public interface IPostService {

	
	/**
	 * 发帖
	 * 请求地址：http://{{host}}/api/content/post
	 * @param userId
	 * @throws Exception
	 */
	public ResultDto<ObjectId> addPost(PostInput postInput, String userId) throws Exception;
	
	/**
	 * 删帖
	 * @param postId
	 * http://{{host}}/api/content/post/:post_id
	 * @param userId 
	 */
	public ResultDto<String> deletePost(String postId, String userId);
	
	/**
	 * 修改帖子
	 * @param postInput
	 * 请求地址：http://{{host}}/api/content/post/:post_id
	 * @param os 
	 * @param userId
	 * @throws Exception 
	 */
	public ResultDto<String> editPost(PostInput postInput, String userId, String os) throws Exception;

	/**
	 * 帖子详情
	 * @param postId
	 * http://{{host}}/api/content/post/:post_id
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	public ResultDto getPostDetails(String postId, String userId, String os) throws Exception;
	
	/**
	 * 帖子列表
	 * 请求地址：http://{{host}}/api/content/posts
	 * @param userId 
	 * @param os 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws Exception 
	 */
	public FungoPageResultDto<PostOutBean> getPostList(String userId, PostInputPageDto postInputPageDto) throws Exception;

	public FungoPageResultDto<Map<String, String>> getTopicPosts(String communityId);

	public FungoPageResultDto<PostOutBean> getTopicPosts(MemberUserProfile memberUserPrefile,  InputPageDto inputPageDto);




	/**
	 * 查看用户在指定时间段内文章上推荐/置顶的文章数量
	 * @param mb_id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Set<String> getArticleRecomAndTopCount(String mb_id, String startDate, String endDate);


	/**
	 * 搜索帖子
	 * @param keyword
	 * @param page
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public FungoPageResultDto<Map<String, Object>> searchPosts(String keyword,int page,int limit) throws Exception;

}
