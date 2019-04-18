package com.game.common.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.game.common.entity.CmmPost;

/**
 * <p>
  * 社区帖子 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-04-20
 */
public interface CmmPostDao extends BaseMapper<CmmPost> {

	//获取关注用户和社区的帖子
	public List<CmmPost> getAllFollowerPost(Page page,Map<String,Object> map);
	
	//获取帖子最后回复时间
	public List<HashMap<String,Object>> getLastReplyTime(String postId);
}