package com.fungo.community.service.portal.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.community.dao.service.CmmCommunityDaoService;
import com.fungo.community.dao.service.CmmPostDaoService;
import com.fungo.community.entity.CmmCommunity;
import com.fungo.community.entity.CmmPost;
import com.fungo.community.facede.SystemFacedeService;
import com.fungo.community.service.impl.PostServiceImpl;
import com.fungo.community.service.portal.IPortalCommunityPostService;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.community.PostInputPageDto;
import com.game.common.dto.community.PostOutBean;
import com.game.common.repo.cache.facade.FungoCacheArticle;
import com.game.common.util.CommonUtil;
import com.game.common.util.CommonUtils;
import com.game.common.util.Html2Text;
import com.game.common.util.PageTools;
import com.game.common.util.date.DateTools;
import com.game.common.util.emoji.FilterEmojiUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PortalCommunityPostServiceImpl implements IPortalCommunityPostService {

	private static final Logger logger = LoggerFactory.getLogger( PortalCommunityPostServiceImpl.class);

	@Autowired
	private FungoCacheArticle fungoCacheArticle;
	@Autowired
	private CmmCommunityDaoService communityService;
	@Autowired
	private CmmPostDaoService postService;
	//依赖系统和用户微服务
	@Autowired
	private SystemFacedeService systemFacedeService;


	@Override
	public FungoPageResultDto<PostOutBean> getAllPostList(String userId, PostInputPageDto postInputPageDto) throws Exception {
		FungoPageResultDto<PostOutBean> result = null;
		try {

		}catch (Exception e){
			logger.error( "pc端获取社区全部文章异常",e);
			result = FungoPageResultDto.FungoPageResultDtoFactory.buildError( "pc端获取社区全部文章异常" );
		}
		return result;
	}

	@Override
	public FungoPageResultDto<PostOutBean> getCreamPostList(String userId, PostInputPageDto postInputPageDto) throws Exception {
		return null;
	}
}
