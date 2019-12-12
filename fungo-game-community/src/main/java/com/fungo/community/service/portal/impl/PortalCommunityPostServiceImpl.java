package com.fungo.community.service.portal.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.community.dao.mapper.CmmPostCircleMapper;
import com.fungo.community.dao.mapper.CmmPostDao;
import com.fungo.community.dao.service.CmmCommunityDaoService;
import com.fungo.community.dao.service.CmmPostDaoService;
import com.fungo.community.entity.CmmCircle;
import com.fungo.community.entity.CmmCommunity;
import com.fungo.community.entity.CmmPost;
import com.fungo.community.facede.SystemFacedeService;
import com.fungo.community.helper.RedisActionHelper;
import com.fungo.community.service.impl.PostServiceImpl;
import com.fungo.community.service.portal.IPortalCommunityPostService;
import com.game.common.consts.FunGoGameConsts;
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
import com.game.common.vo.CircleGamePostVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.terracotta.modules.ehcache.ToolkitInstanceFactoryImpl.LOGGER;

@Service
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
	@Autowired
    private CmmPostDao cmmPostDao;
	 @Autowired
    private CmmPostCircleMapper cmmPostCircleMapper;

	/**
	 * 功能描述:  根据传递的游戏id查询社区的文章和游戏关联的文章（不在社区内）
	 * @param: [memberId, circleGamePostVo]
	 * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.community.PostOutBean>
	 * @auther: dl.zhang
	 * @date: 2019/6/21 14:28
	 */
	@Cacheable(cacheNames={FunGoGameConsts.CACHE_EH_KEY_POST} ,key = "'" + FungoCoreApiConstant.FUNGO_CORE_API_GAMR_POSTS_CACHE +" ' +#userId + #circleGamePostVo.gameId + #circleGamePostVo.page + #circleGamePostVo.limit ")
	@Override
	public FungoPageResultDto<PostOutBean> selectCircleGamePost(String userId, CircleGamePostVo circleGamePostVo) {
		FungoPageResultDto<PostOutBean> re = null;
		List<PostOutBean> relist = null;
		Page page = new Page(circleGamePostVo.getPage(), circleGamePostVo.getLimit());
		String gameId = circleGamePostVo.getGameId();
		String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_GAMR_POSTS;
		String keySuffix = JSON.toJSONString(circleGamePostVo);
		relist = (List<PostOutBean>) fungoCacheArticle.getIndexDecodeCache(keyPrefix, keySuffix);
		if (null != relist &&  relist.size() > 0) {
			re.setData(relist);
			PageTools.pageToResultDto(re, page);
			return re;
		}
		try {
			relist = new ArrayList<>();
			List<CmmPost> cmmPosts = cmmPostDao.getGamePostByGameId(page, gameId);
			for (CmmPost post : cmmPosts) {
				//表情解码
				if (org.apache.commons.lang.StringUtils.isNotBlank(post.getTitle())) {
					String interactTitle = FilterEmojiUtil.decodeEmoji(post.getTitle());
					post.setTitle(interactTitle);
				}
				if (StringUtils.isNotBlank(post.getContent())) {
					String interactContent = FilterEmojiUtil.decodeEmoji(post.getContent());

					//bean.setContent(content.length() > 100 ? CommonUtils.filterWord(content.substring(0, 100)) : CommonUtils.filterWord(content));
					interactContent = interactContent.length() > 40 ? Html2Text.removeHtmlTag(interactContent.substring(0, 40)) : Html2Text.removeHtmlTag(interactContent);

					post.setContent(interactContent);
				}

				PostOutBean bean = new PostOutBean();
				//!fixme 查询用户数据
				//bean.setAuthor(iUserService.getAuthor(post.getMemberId()));
				try {
					ResultDto<AuthorBean> authorBeanResultDto = systemFacedeService.getAuthor(post.getMemberId());
					if (null != authorBeanResultDto) {
						AuthorBean authorBean = authorBeanResultDto.getData();
						bean.setAuthor(authorBean);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
//                if (bean.getAuthor() == null) {
//                    continue;
//                }
				String content = post.getContent();
				if (!CommonUtil.isNull(content)) {
					// bean.setContent(content.length() > 100 ? CommonUtils.filterWord(content.substring(0, 100)) : CommonUtils.filterWord(content));
					bean.setContent(CommonUtils.filterWord(content));
				}

				bean.setUpdated_at(DateTools.fmtDate(post.getUpdatedAt()));

				//fix bug:把V2.4.2存在的createdAt字段，恢复回来 [by mxf 2019-01-08]
				bean.setCreatedAt(DateTools.fmtDate(post.getCreatedAt()));
				//end

				bean.setVideoUrl(post.getVideo() == null ? "" : post.getVideo());
				bean.setImageUrl(post.getCoverImage() == null ? "" : post.getCoverImage());
				bean.setLikeNum(post.getLikeNum() == null ? 0 : post.getLikeNum());
				bean.setPostId(post.getId());
				bean.setReplyNum(post.getCommentNum() == null ? 0 : post.getCommentNum());
				bean.setTitle(CommonUtils.filterWord(post.getTitle()));
				try {
					if (!CommonUtil.isNull(post.getImages())) {
						ArrayList<String> readValue = new ArrayList<String>();
						ObjectMapper mapper = new ObjectMapper();
						readValue = mapper.readValue(post.getImages(), ArrayList.class);

						//fix bug: Could not read JSON: Cannot construct instance of `java.util.ArrayList$SubList` [by mxf 2019-03-20]
						int readValueSize = readValue.size();
						List readValueList = new ArrayList();
						if (readValueSize > 3) {
							readValueList.addAll(readValue.subList(0, 3));
							bean.setImages(readValueList);
						} else {
							bean.setImages(readValue);
						}
						//老代码
						//bean.setImages(readValue.size() > 3 ? readValue.subList(0, 3) : readValue);
						//end
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (CommonUtil.isNull(userId)) {
					bean.setLiked(false);
				} else {
					//!fixme 查询用户点赞数
					//int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).ne("state", "-1").eq("target_id", post.getId()).eq("member_id", userId));
					BasActionDto basActionDto = new BasActionDto();
					basActionDto.setMemberId(userId);
					basActionDto.setType(0);
					basActionDto.setState(0);
					basActionDto.setTargetId(post.getId());
					int liked = 0;
					try {
						ResultDto<Integer> resultDto = systemFacedeService.countActionNum(basActionDto);
						if (null != resultDto) {
							liked = resultDto.getData();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					bean.setLiked(liked > 0 ? true : false);
				}
				//
				bean.setVideoCoverImage(post.getVideoCoverImage());
				bean.setType(post.getType());
				/**
				 * 功能描述: 根据文章查询是否有圈子
				 * @auther: dl.zhang
				 * @date: 2019/6/27 15:40
				 */
				CmmCircle cmmCircle = cmmPostCircleMapper.getCircleEntityByPostId(bean.getPostId());
				if (cmmCircle != null) {
					bean.setCircleId(cmmCircle.getId());
					bean.setCircleName(cmmCircle.getCircleName());
					bean.setCircleIcon(cmmCircle.getCircleIcon());
				}
				relist.add(bean);
			}
			re = new FungoPageResultDto<>();
			fungoCacheArticle.excIndexDecodeCache(true, keyPrefix, keySuffix, relist, RedisActionHelper.getRandomRedisCacheTime());
			re.setData(relist);
			PageTools.pageToResultDto(re, page);
		} catch (Exception e) {
			logger.error("根据游戏id获取文章,游戏id="+gameId, e);
			re = FungoPageResultDto.error("-1", "根据游戏id获取文章异常");
		}
		return re;
	}
}
