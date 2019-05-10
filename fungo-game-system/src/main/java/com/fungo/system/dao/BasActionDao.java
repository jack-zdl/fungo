package com.fungo.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.entity.BasAction;
import com.game.common.bean.CollectionBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 动作 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-05-07
 */
@Repository
public interface BasActionDao extends BaseMapper<BasAction> {
	//计数器
	public boolean updateCountor(Map<String, String> map);

	//获取我的收藏（文章）
	public List<CollectionBean> getCollection(Page<CollectionBean> page, String memberId);


	public String getMemberIdByTargetId(Map map);

	//获取关注社区
	public List<Map<String,Object>> getFollowerCommunity(Page page, String memberId);

	//获取关注用户
	public List<Map<String,Object>> getFollowerUser(Page page, String memberId);

	//用户时间线-帖子 新版已弃用
	public List<Map<String,Object>> getPostFeeds(Map<String, Object> map);

	//用户时间线-游戏评论 新版已弃用
	public List<Map<String,Object>> getEvaluationFeeds(Map<String, Object> map);
	//用户时间线-心情 新版已弃用
	public List<Map<String,Object>> getMoodFeeds(Map<String, Object> map);

	public boolean updateSort(Map<String, Object> map);

	//获取关注用户id
	public List<String> getFollowerUserId(String memberId);

	//获取关注社区id
	public List<String> getFollowerCommunityId(String memberId);

	//计数器
	public boolean batchSubCountor(Map<String, Object> map);

}