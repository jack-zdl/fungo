package com.fungo.community.service.portal;


import com.game.common.dto.*;
import com.game.common.dto.community.PostInput;
import com.game.common.dto.community.PostInputPageDto;
import com.game.common.dto.community.PostOutBean;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public interface IPortalCommunityPostService {

	/**
	 * 全部帖子列表（包含有社区和没有社区）
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	FungoPageResultDto<PostOutBean> getAllPostList(String userId, PostInputPageDto postInputPageDto) throws Exception;

	/**
	 * 精华帖子列表(包含有社区和没有社区)
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	FungoPageResultDto<PostOutBean> getCreamPostList(String userId, PostInputPageDto postInputPageDto) throws Exception;


}
