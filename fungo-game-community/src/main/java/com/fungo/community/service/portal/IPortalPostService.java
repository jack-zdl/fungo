package com.fungo.community.service.portal;


import com.game.common.dto.*;
import com.game.common.dto.community.PostInput;
import com.game.common.dto.community.PostInputPageDto;
import com.game.common.dto.community.PostOutBean;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public interface IPortalPostService {

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



}
