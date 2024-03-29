package com.fungo.community.controller.portal;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.community.dao.mapper.CmmPostDao;
import com.fungo.community.dao.service.CmmCommunityDaoService;
import com.fungo.community.dao.service.CmmPostDaoService;
import com.fungo.community.dao.service.impl.ESDAOServiceImpl;
import com.fungo.community.entity.CmmCommunity;
import com.fungo.community.entity.CmmPost;
import com.fungo.community.feign.SystemFeignClient;
import com.fungo.community.service.IPostService;
import com.fungo.community.service.portal.IPortalPostService;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.*;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.community.PostInput;
import com.game.common.dto.community.PostInputPageDto;
import com.game.common.dto.community.PostOut;
import com.game.common.dto.community.PostOutBean;
import com.game.common.dto.search.SearchInputPageDto;
import com.game.common.repo.cache.facade.FungoCacheArticle;
import com.game.common.repo.cache.facade.FungoCacheIndex;
import com.game.common.util.CommonUtil;
import com.game.common.util.CommonUtils;
import com.game.common.util.PageTools;
import com.game.common.util.StringUtil;
import com.game.common.util.annotation.Anonymous;
import com.game.common.util.annotation.LogicCheck;
import com.game.common.util.date.DateTools;
import com.game.common.util.emoji.FilterEmojiUtil;
import com.game.common.util.validate.ValidateUtil;
import com.game.common.vo.MemberFollowerVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  PC2.0帖子
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/27 13:25
 */
@SuppressWarnings("all")
@RestController
@Api(value = "", description = "PC2.0帖子")
@EnableAsync
public class PortalCommunityPostController {

    private static final Logger logger = LoggerFactory.getLogger( PortalCommunityPostController.class);

    @Autowired
    private IPostService bsPostService;
    @Autowired
    private CmmPostDaoService daoPostService;
    @Autowired
    private CmmPostDao cmmPostDao;
    @Autowired
    private CmmCommunityDaoService communityService;
    @Autowired
    private FungoCacheArticle fungoCacheArticle;
    @Autowired
    private FungoCacheIndex fungoCacheIndex;
    @Autowired
    private ESDAOServiceImpl esdaoService;
    //依赖系统和用户微服务
    @Autowired(required = false)
    private SystemFeignClient systemFeignClient;
    @Autowired
    private IPortalPostService portalPostService;

    @ApiOperation(value = "PC2.0帖子列表", notes = "")
    @RequestMapping(value = "/api/portal/community/content/posts", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "sort", value = "排序字段", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "页数", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "community_id", value = "社区id", paramType = "form", dataType = "String")
    })
    public FungoPageResultDto<PostOutBean> getPostContentList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody PostInputPageDto postInputPageDto) {
        if(ValidateUtil.checkNullAndLength(postInputPageDto.getCommunity_id()) ) return FungoPageResultDto.FungoPageResultDtoFactory.buildError( "社区id有误" );
        String userId = "";
        if (memberUserPrefile != null) {
            userId = memberUserPrefile.getLoginId();
        }
        try {
            return portalPostService.getPostList(userId, postInputPageDto);
        } catch (Exception e) {
            logger.error( "PC2.0帖子列表",e );
            return FungoPageResultDto.error("-1", "操作失败");
        }
    }

    @ApiOperation(value = "PC2.0发帖", notes = "")
    @RequestMapping(value = "/api/portal/community/content/post", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "html", value = "html内容", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "community_id", value = "社区id", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "title", value = "标题", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "content", value = "帖子内容", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "images", value = "图片", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "origin", value = "文本", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "videoId", value = "视频id,  可选", paramType = "form", dataType = "string")
    })
    @LogicCheck(loginc = {"BANNED_TEXT"})
    public ResultDto<ObjectId> addPost(MemberUserProfile memberUserPrefile, @RequestBody PostInput postInput) throws Exception {
        if (StringUtil.isNull(postInput.getHtml()) || StringUtil.isNull(postInput.getTitle())) {
            return ResultDto.error("-1", "文章内容或者标题不可为空");
        }
        String userId = memberUserPrefile.getLoginId();
        return bsPostService.addPost(postInput, userId);
    }


    @ApiOperation(value = "PC2.0删帖", notes = "")
    @RequestMapping(value = "/api/portal/community/content/post/{postId}", method = RequestMethod.DELETE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "帖子id", paramType = "form", dataType = "string"),
    })
    public ResultDto<String> deletePost(MemberUserProfile memberUserPrefile, @PathVariable("postId") String postId) {
        String userId = memberUserPrefile.getLoginId();
        return bsPostService.deletePost(postId, userId);
    }

    @ApiOperation(value = "PC2.0修改帖子", notes = "")
    @RequestMapping(value = "/api/portal/community/content/post", method = RequestMethod.PUT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "html", value = "html内容", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "postId", value = "帖子id", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "title", value = "标题", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "content", value = "帖子内容", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "images", value = "图片", paramType = "form", dataType = "string[]"),

    })
    @LogicCheck(loginc = {"BANNED_TEXT","BANNED_POST_AUTH"})
    public ResultDto<String> editPost(MemberUserProfile memberUserPrefile, HttpServletRequest request,
                                      @RequestBody PostInput postInput) throws Exception {
        String userId = memberUserPrefile.getLoginId();
        System.out.println(userId);
        String os = "";
        os = (String) request.getAttribute("os");
        return bsPostService.editPost(postInput, userId, os);
    }


    @ApiOperation(value = "PC2.0帖子详情", notes = "")
    @RequestMapping(value = "/api/portal/community/content/post/{postId}", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "帖子id", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType = "form", dataType = "string")
    })
    public ResultDto<PostOut> getPostDetail(@Anonymous MemberUserProfile memberUserPrefile, HttpServletRequest request, @PathVariable("postId") String postId) {
        if(ValidateUtil.checkNullAndLength(postId)) return ResultDto.ResultDtoFactory.buildError( "文章id有误" );
        String userId = "";
        String os = "";
        os = (String) request.getHeader("os");
        if (memberUserPrefile != null) {
            userId = memberUserPrefile.getLoginId();
        }
        try {
            return bsPostService.getPostDetails(postId, userId, os);
        } catch (Exception e) {
            logger.error( "PC2.0帖子详情异常",e);
            return ResultDto.error("-1", "操作失败");
        }
    }

    @ApiOperation(value = "PC2.0搜索帖子", notes = "")
    @RequestMapping(value = "/api/portal/community/search/posts", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key_word", value = "关键字", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "page", value = "页数号", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "每页显示数", paramType = "form", dataType = "int")
    })
    public FungoPageResultDto<Map<String, Object>> searchPosts(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody SearchInputPageDto searchInputDto) throws Exception {
        String keyword = searchInputDto.getKey_word();
        int page = searchInputDto.getPage();
        //fix: 页码 小于1 返回空 [by mxf 2019-01-30]
        if (page < 1) {
            return new FungoPageResultDto<Map<String, Object>>();
        }
        int limit = searchInputDto.getLimit();
        return bsPostService.searchPosts(keyword, page, limit);
    }


    @ApiOperation(value = "搜索帖子", notes = "")
    @PostMapping(value = "/api/portal/community/search/es/posts")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key_word", value = "关键字", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "page", value = "页数号", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "每页显示数", paramType = "form", dataType = "int")
    })
    public FungoPageResultDto<CmmPost> searchESPosts(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody SearchInputPageDto searchInputDto) throws Exception {
        String keyword = searchInputDto.getKey_word();
        int page = searchInputDto.getPage();
        if (page < 1) {
            return new FungoPageResultDto<>();
        }
        int limit = searchInputDto.getLimit();
        Page<CmmPost> cmmPosts =   esdaoService.getAllPosts(keyword,page,limit);
        return FungoPageResultDto.FungoPageResultDtoFactory.buildSuccess(cmmPosts.getRecords(),-1,new Page());
    }


    /**
     * PC2.0首页文章帖子列表
     * InputPageDto
     * @param memberUserPrefile
     * @param inputPageDto
     * @return
     */
    @ApiOperation(value = "PC2.0首页文章帖子列表(v2.4)   filter(1:关注游戏社区,2:关注用户，4:全部关注, 0:精华，3:最新)", notes = "")
    @RequestMapping(value = "/api/portal/community/amwaywall/postlist", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<PostOutBean> getPostList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody PostInputPageDto inputPageDto) {
        FungoPageResultDto<PostOutBean> re = null;
        //从redis获取
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_INDEX_POST_LIST;
        String keySuffix = JSON.toJSONString(inputPageDto);
        //与登录用户关联
        if (memberUserPrefile != null) {
            keySuffix = memberUserPrefile.getLoginId() + JSON.toJSONString(inputPageDto);
        }
        re = (FungoPageResultDto<PostOutBean>) fungoCacheIndex.getIndexCache(keyPrefix, keySuffix);
        if (null != re && null != re.getData() && re.getData().size() > 0) {
            return re;
        }
        /***********************************逻辑开始**************/
        re = new FungoPageResultDto<PostOutBean>();
        List<PostOutBean> list = new ArrayList<PostOutBean>();
        re.setData(list);
        String filter = inputPageDto.getFilter();
        Page<CmmPost> page = null;
        //查询的分页文章(帖子)数据
        List<CmmPost> plist = null;
        //总记录数
        int total = 0;
        Long rowId = inputPageDto.getRowId();
        String lastUpdateDate = inputPageDto.getLastUpdateDate();
        boolean isHaveRowIDAndLastUpdateDate = true;
        if (null == rowId || rowId.longValue() == 0 || StringUtils.isBlank(lastUpdateDate)) {
            isHaveRowIDAndLastUpdateDate = false;
        }
        //filter(0:精华，1:关注游戏社区,2:关注用户, 3:最新，4:全部关注)
        if ("0".equals(filter)) {
            EntityWrapper<CmmPost> postEntityWrapper = new EntityWrapper<CmmPost>();
            postEntityWrapper.eq("type", 2).eq("state", 1).ne( "auth" ,1);
            int sort = inputPageDto.getSort();
            if (sort == 1) {//  时间正序
                postEntityWrapper.orderBy("edited_at", true);
            } else if (sort == 2) {//  时间倒序
                postEntityWrapper.orderBy("edited_at", false);
            } else if (sort == 3) {// 热力值正序
//            wrapper.orderBy("comment_num,like_num", true);
                postEntityWrapper.last("ORDER BY comment_num ASC,like_num ASC");
            } else if (sort == 4) {// 热力值倒序
//            wrapper.orderBy("comment_num,like_num", false);
                postEntityWrapper.last("ORDER BY comment_num DESC,like_num DESC");
            } else if (sort == 5) {//最后回复时间
                postEntityWrapper.orderBy("last_reply_at", false);
            } else {
                postEntityWrapper.orderBy("edited_at", false);
            }
//            postEntityWrapper.last("ORDER BY sort DESC,updated_at DESC");
            Page<CmmPost> pageRecommend = this.daoPostService.selectPage(new Page<CmmPost>(inputPageDto.getPage(), inputPageDto.getLimit()), postEntityWrapper);
            if (null != pageRecommend) {
                plist = pageRecommend.getRecords();
                total = pageRecommend.getTotal();
            }
        } else if ("1".equals(filter)) {
            if (memberUserPrefile != null) {
                //!fixme 获取关注社区id集合
                //List<String> olist = actionDao.getFollowerCommunityId(memberUserPrefile.getLoginId());
                // 获取关注社区ID集合
                List<String> olist = new ArrayList<String>();
                try {
                    ResultDto<List<String>> listFollowerCommunityIdResult = systemFeignClient.listFollowerCommunityId(memberUserPrefile.getLoginId());
                    if (null != listFollowerCommunityIdResult) {
                        olist.addAll(listFollowerCommunityIdResult.getData());
                    }
                } catch (Exception ex) {
                    logger.error( "获取关注社区id集合异常",ex);
                }
                if (olist.size() > 0) {
                    EntityWrapper<CmmPost> postEntityWrapper = new EntityWrapper<CmmPost>();
                    postEntityWrapper.in("community_id", olist).eq("state", 1).ne( "auth" ,1);
                    //设置数据rowId和最后更新时间
                    queryPagePostWithRowIdUpdate(rowId, lastUpdateDate, postEntityWrapper);
                    postEntityWrapper.orderBy("updated_at", false);
                    //fix bug: 修改查询小于id和updated_at分页数据 [by mxf 2019-05-05]
                    if (!isHaveRowIDAndLastUpdateDate) {
                        page = this.daoPostService.selectPage(new Page<CmmPost>(inputPageDto.getPage(), inputPageDto.getLimit()), postEntityWrapper);
                        plist = page.getRecords();
                        total = page.getTotal();
                    } else {
                        //设置分页
                        postEntityWrapper.last("limit " + inputPageDto.getLimit());
                        plist = daoPostService.selectList(postEntityWrapper);
                        //查询总记录数
                        EntityWrapper<CmmPost> postEntityWrapperCount = new EntityWrapper<CmmPost>();
                        postEntityWrapperCount.in("community_id", olist).eq("state", 1);
                        total = daoPostService.selectCount(postEntityWrapperCount);
                    }
                }
            }
        } else if ("2".equals(filter)) {
            if (memberUserPrefile != null) {
                //!fixme 获取关注用户id集合
                //List<String> olist = actionDao.getFollowerUserId(memberUserPrefile.getLoginId());
                //获取关注用户id集合
                List<String> olist = new ArrayList<String>();
                MemberFollowerVo memberFollowerVo = new MemberFollowerVo();
                memberFollowerVo.setMemberId(memberUserPrefile.getLoginId());
                FungoPageResultDto<String> followerUserIdResult = null;
                try {
                    followerUserIdResult = systemFeignClient.getFollowerUserId(memberFollowerVo);
                    if (null != followerUserIdResult) {
                        olist.addAll(followerUserIdResult.getData());
                    }
                } catch (Exception ex) {
                    logger.error( "获取关注用户id集合异常",ex);
                }
                if (olist.size() > 0) {
                    EntityWrapper<CmmPost> postEntityWrapper = new EntityWrapper<CmmPost>();
                    postEntityWrapper.in("member_id", olist).eq("state", 1).ne( "auth" ,1);
                    //设置数据rowId和最后更新时间
                    queryPagePostWithRowIdUpdate(rowId, lastUpdateDate, postEntityWrapper);
                    postEntityWrapper.orderBy("updated_at", false);
                    //fix bug: 修改查询小于id和updated_at分页数据 [by mxf 2019-05-05]
                    if (!isHaveRowIDAndLastUpdateDate) {
                        page = this.daoPostService.selectPage(new Page<CmmPost>(inputPageDto.getPage(), inputPageDto.getLimit()), postEntityWrapper);
                        plist = page.getRecords();
                        total = page.getTotal();
                    } else {
                        //设置分页
                        postEntityWrapper.last("limit " + inputPageDto.getLimit());
                        plist = daoPostService.selectList(postEntityWrapper);
                        //查询总记录数
                        EntityWrapper<CmmPost> postEntityWrapperCount = new EntityWrapper<CmmPost>();
                        postEntityWrapperCount.in("member_id", olist).eq("state", 1);
                        total = daoPostService.selectCount(postEntityWrapperCount);
                    }
                }
            }
        } else if ("3".equals(filter)) {
//            最新
            EntityWrapper<CmmPost> postEntityWrapper = new EntityWrapper<CmmPost>();
            postEntityWrapper.eq("state", 1).ne( "auth" ,1);
            //设置数据rowId和最后更新时间
            queryPagePostWithRowIdUpdate(rowId, lastUpdateDate, postEntityWrapper);
            postEntityWrapper.orderBy("updated_at", false);
            //fix bug: 修改查询小于id和updated_at分页数据 [by mxf 2019-05-05]
            if (!isHaveRowIDAndLastUpdateDate) {
                page = this.daoPostService.selectPage(new Page<CmmPost>(inputPageDto.getPage(), inputPageDto.getLimit()), postEntityWrapper);
                plist = page.getRecords();
                total = page.getTotal();
            } else {
                //设置分页
                postEntityWrapper.last("limit " + inputPageDto.getLimit());
                plist = daoPostService.selectList(postEntityWrapper);
                //查询总记录数
                EntityWrapper<CmmPost> postEntityWrapperCount = new EntityWrapper<CmmPost>();
                postEntityWrapperCount.eq("state", 1);
                total = daoPostService.selectCount(postEntityWrapperCount);
            }

        } else if ("4".equals(filter)) {
            if (memberUserPrefile != null) {
                //!fixme 获取关注用户id集合 和 社区ID集合
                //List<String> memberIdList = actionDao.getFollowerUserId(memberUserPrefile.getLoginId());
                //List<String> communityIdList = actionDao.getFollowerCommunityId(memberUserPrefile.getLoginId());
                //获取关注用户id集合
                List<String> memberIdList = new ArrayList<String>();
                MemberFollowerVo memberFollowerVo = new MemberFollowerVo();
                memberFollowerVo.setMemberId(memberUserPrefile.getLoginId());
                try {
                    FungoPageResultDto<String> followerUserIdResult = systemFeignClient.getFollowerUserId(memberFollowerVo);
                    if (null != followerUserIdResult) {
                        memberIdList.addAll(followerUserIdResult.getData());
                    }
                } catch (Exception ex) {
                    logger.error( "获取关注用户id集合 和 社区ID集合异常",ex);
                }
                // 获取关注社区ID集合
                List<String> communityIdList = new ArrayList<String>();
                try {
                    ResultDto<List<String>> listFollowerCommunityIdResult = systemFeignClient.listFollowerCommunityId(memberUserPrefile.getLoginId());
                    if (null != listFollowerCommunityIdResult) {
                        communityIdList.addAll(listFollowerCommunityIdResult.getData());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Map<String, Object> map = new HashMap<String, Object>();
                if (memberIdList.size() > 0) {
                    map.put("memberIdList", memberIdList);
                }
                if (communityIdList.size() > 0) {
                    map.put("communityIdList", communityIdList);
                }
                if (map.get("memberIdList") != null || map.get("communityIdList") != null) {
                    Page<CmmPost> pageAllLook = new Page<CmmPost>(inputPageDto.getPage(), inputPageDto.getLimit());
                    //设置 小于id 和 updated_at分页数据
                    if (null != rowId && rowId.longValue() > 0 && StringUtils.isNotBlank(lastUpdateDate)) {
                        map.put("post_id", rowId);
                        map.put("updated_at", lastUpdateDate);
                    }
                    map.put("pageSize", inputPageDto.getLimit());
                    //查询分页数据
                    plist = cmmPostDao.getAllFollowerPostWithPage(map);
                    //查询总记录数
                    Map<String, Object> postCountMap = cmmPostDao.getAllFollowerPostCount(pageAllLook, map);
                    if (null != postCountMap && postCountMap.containsKey("ct")) {
                        Long ct = (Long) postCountMap.get("ct");
                        if (null != ct) {
                            total = ct.intValue();
                        }
                    }
                }
            }
        } else {
            EntityWrapper<CmmPost> postEntityWrapper = new EntityWrapper<CmmPost>();
            postEntityWrapper.eq("state", 1).ne( "auth" ,1);
            //设置数据rowId和最后更新时间
            queryPagePostWithRowIdUpdate(rowId, lastUpdateDate, postEntityWrapper);
            postEntityWrapper.orderBy("updated_at", false);
            //fix bug: 修改查询小于id和updated_at分页数据 [by mxf 2019-05-05]
            if (!isHaveRowIDAndLastUpdateDate) {
                page = this.daoPostService.selectPage(new Page<CmmPost>(inputPageDto.getPage(), inputPageDto.getLimit()), postEntityWrapper);
                plist = page.getRecords();
                total = page.getTotal();
            } else {
                //设置分页
                postEntityWrapper.last("limit " + inputPageDto.getLimit());
                plist = daoPostService.selectList(postEntityWrapper);
                //查询总记录数
                EntityWrapper<CmmPost> postEntityWrapperCount = new EntityWrapper<CmmPost>();
                postEntityWrapperCount.eq("state", 1);
                total = daoPostService.selectCount(postEntityWrapperCount);
            }
        }
        //fix bug: 修改查询小于id和updated_at分页数据 [by mxf 2019-05-05]
        /*if (page == null) {
            return re;
        }
        if (!"4".equals(filter)) {
            plist = page.getRecords();
        }*/
        //end
        if (null == plist) {
            return re;
        }
        ObjectMapper mapper = new ObjectMapper();
        for (CmmPost cmmPost : plist) {
            //表情解码
            if (StringUtils.isNotBlank(cmmPost.getTitle())) {
                String interactTitle = FilterEmojiUtil.decodeEmoji(cmmPost.getTitle());
                //String interactTitle = EmojiParser.parseToUnicode(cmmPost.getTitle() );
                cmmPost.setTitle(interactTitle);
            }
            if (StringUtils.isNotBlank(cmmPost.getContent())) {
                String interactContent = FilterEmojiUtil.decodeEmoji(cmmPost.getContent());
                //String interactContent = EmojiParser.parseToUnicode(cmmPost.getContent());
                cmmPost.setContent(interactContent);
            }
//	        if (StringUtils.isNotBlank(cmmPost.getHtmlOrigin())) {
//	            String interactHtmlOrigin = FilterEmojiUtil.resolveToEmojiFromByte(cmmPost.getHtmlOrigin());
//	            cmmPost.setHtmlOrigin(interactHtmlOrigin);
//	        }
//
            PostOutBean bean = new PostOutBean();
            CmmCommunity community = communityService.selectById(cmmPost.getCommunityId());
//            if (community == null || community.getState() != 1) {
////				page.setTotal(page.getTotal() - 1);
//                continue;
//            }
            //!fixme 根据用户id查询用户详情
            //bean.setAuthor(userService.getAuthor(cmmPost.getMemberId()));
            AuthorBean authorBean = new AuthorBean();
            try {
                ResultDto<AuthorBean> beanResultDto = systemFeignClient.getAuthor(cmmPost.getMemberId());
                if (null != beanResultDto) {
                    authorBean = beanResultDto.getData();
                }
            } catch (Exception ex) {
                logger.error( "根据用户id查询用户详情异常",ex);
            }
            bean.setAuthor(authorBean);
            //systemFeignClient.list
            if (bean.getAuthor() == null) {
                continue;
            }
            String content = cmmPost.getContent();
            if (!CommonUtil.isNull(content)) {
                //bean.setContent(content.length() > 100 ? CommonUtils.filterWord(content.substring(0, 100)) : CommonUtils.filterWord(content));
                bean.setContent(CommonUtils.filterWord(content));
            }
            bean.setUpdated_at(DateTools.fmtDate(cmmPost.getUpdatedAt()));
            bean.setCreatedAt(DateTools.fmtDate(cmmPost.getCreatedAt()));
            bean.setVideoUrl(cmmPost.getVideo());
            bean.setImageUrl(cmmPost.getCoverImage());
            bean.setLikeNum(cmmPost.getLikeNum());
            bean.setPostId(cmmPost.getId());
            bean.setReplyNum(cmmPost.getCommentNum());
            bean.setTitle(CommonUtils.filterWord(cmmPost.getTitle()));
            bean.setCommunityIcon(community == null ? "":community.getIcon());
            bean.setCommunityId(community == null ? "":community.getId());
            bean.setCommunityName(community == null ? "":community.getName());

            //文章 row_id
            bean.setRowId(cmmPost.getPostId());
            if (!CommonUtil.isNull(cmmPost.getVideo()) && CommonUtil.isNull(cmmPost.getCoverImage())) {
                bean.setImageUrl(community == null ? "":community.getCoverImage());
            }
            try {
                if (!CommonUtil.isNull(cmmPost.getImages())) {
                    ArrayList<String> readValue = new ArrayList<String>();
                    readValue = mapper.readValue(cmmPost.getImages(), ArrayList.class);
                    int readValueListSize = readValue.size();
                    if (readValueListSize > 3) {
                        List threeReadValueList = new ArrayList(readValue.subList(0, 3));
                        bean.setImages(threeReadValueList);
                    } else {
                        bean.setImages(readValue);
                    }
                    //bean.setImages(readValue.size() > 3 ? readValue.subList(0, 3) : readValue);
                }
            } catch (IOException e) {
                logger.error( "获取文章异常",e);
            }
            //是否点赞
            if (memberUserPrefile == null) {
                bean.setLiked(false);
            } else {
                //!fixme 获取点赞数
                //行为类型
                //点赞 | 0
                //int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).ne("state", "-1").eq("target_id", cmmPost.getId()).eq("member_id", memberUserPrefile.getLoginId()));
                BasActionDto basActionDto = new BasActionDto();
                basActionDto.setMemberId(memberUserPrefile.getLoginId());
                basActionDto.setType(0);
                basActionDto.setState(0);
                basActionDto.setTargetId(cmmPost.getId());
                int liked = 0;
                try {
                    ResultDto<Integer> resultDto = systemFeignClient.countActionNum(basActionDto);
                    if (null != resultDto) {
                        liked = resultDto.getData();
                    }
                } catch (Exception ex) {
                    logger.error( "获取点赞数异常",ex);
                }
                bean.setLiked(liked > 0 ? true : false);
            }
            bean.setVideoCoverImage(cmmPost.getVideoCoverImage());
            bean.setType(cmmPost.getType());
            list.add(bean);
        }
        //PageTools.pageToResultDto(re, page);
        //设置分页参数
        PageTools.pageToResultDto(re, total, inputPageDto.getLimit(), inputPageDto.getPage());
        //redis cache
        fungoCacheIndex.excIndexCache(true, keyPrefix, keySuffix, re);
        return re;
    }

    @ApiOperation(value = "管控台推荐文章", notes = "")
    @PostMapping(value = "/api/portal/community/content/post/topic")
    public FungoPageResultDto<PostOutBean> getTopicPosts(@Anonymous MemberUserProfile memberUserPrefile,@Valid @RequestBody PostInputPageDto inputPageDto, Errors errors) {
        if(errors.hasErrors())
            return FungoPageResultDto.FungoPageResultDtoFactory.buildError( errors.getAllErrors().get(0).getDefaultMessage());

        return bsPostService.getTopicPosts(memberUserPrefile , inputPageDto);
    }

    /**
     * 设置社区-帖子|文章分页查询条件
     * @param rowId
     * @param lastUpdateDate
     * @param postEntityWrapper
     */
    private void queryPagePostWithRowIdUpdate(Long rowId, String lastUpdateDate, EntityWrapper<CmmPost> postEntityWrapper) {
        if (null != rowId && rowId.longValue() > 0 && StringUtils.isNotBlank(lastUpdateDate)) {
            postEntityWrapper.lt("post_id", rowId);
            postEntityWrapper.le("updated_at", lastUpdateDate);
        }
    }

    @ApiOperation(value = "帖子權限", notes = "")
    @GetMapping(value = "/api/portal/community/content/post/auth/{postId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "帖子id", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType = "form", dataType = "string")
    })
    public ResultDto<Map<String,Object>> getPostAuth(MemberUserProfile memberUserPrefile, @PathVariable("postId") String postId) {
        String userId = "";
        if (memberUserPrefile != null) {
            userId = memberUserPrefile.getLoginId();
        }
        try {
            return bsPostService.getPostAuth(postId, userId);
        } catch (Exception e) {
            logger.error( "帖子權限异常",e);
            return ResultDto.error("-1", "操作失败");
        }
    }

    @ApiOperation(value = "修改文章", notes = "")
    @PutMapping(value = "/api/portal/community/content/post/edit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "html", value = "html内容", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "postId", value = "帖子id", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "title", value = "标题", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "content", value = "帖子内容", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "images", value = "图片", paramType = "form", dataType = "string[]"),

    })
    @LogicCheck(loginc = {"BANNED_TEXT"})
    public ResultDto<String> editPost(MemberUserProfile memberUserPrefile, @RequestBody PostInput postInput) throws Exception {
        String userId = memberUserPrefile.getLoginId();
        return bsPostService.editPost(postInput, userId);
    }

}
