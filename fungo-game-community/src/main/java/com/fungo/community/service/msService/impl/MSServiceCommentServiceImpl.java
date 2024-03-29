package com.fungo.community.service.msService.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.dao.service.CmmCommentDaoService;
import com.fungo.community.dao.service.ReplyDaoService;
import com.fungo.community.entity.CmmComment;
import com.fungo.community.entity.Reply;
import com.fungo.community.service.msService.IMSServiceCommentService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.community.CmmCmtReplyDto;
import com.game.common.dto.community.CmmCommentDto;
import com.game.common.util.PageTools;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
public class MSServiceCommentServiceImpl implements IMSServiceCommentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MSServiceCommentServiceImpl.class);


    @Autowired
    private ReplyDaoService replyDaoService;

    @Autowired
    private CmmCommentDaoService cmmCommentDaoService;

    @Override
    public FungoPageResultDto<CmmCmtReplyDto> querySecondLevelCmtList(CmmCmtReplyDto postDto) {

        List<CmmCmtReplyDto> cmmCmtReplyDtoList = null;
        FungoPageResultDto<CmmCmtReplyDto> fungoPageResultDto = new FungoPageResultDto<>();
        String id = postDto.getId();
        try {


            int page = postDto.getPage();
            int limit = postDto.getLimit();


            EntityWrapper<Reply> replyEntityWrapper = new EntityWrapper<Reply>();
            HashMap<String, Object> param = new HashMap<String, Object>();

            Page<Reply> replyPage = null;

            if (page > 0 && limit > 0) {
                replyPage = new Page<Reply>(page, limit);
            }

            //PK

            if (StringUtils.isNotBlank(id)) {
                param.put("id", id);
            }

            //评价ID
            String targetId = postDto.getTargetId();
            if (StringUtils.isNotBlank(targetId)) {
                param.put("target_id", targetId);
            }

            //会员Id
            String member_id = postDto.getMemberId();
            if (StringUtils.isNotBlank(member_id)) {
                param.put("member_id", member_id);
            }


            //回复会员Id
            String replay_to_id = postDto.getReplayToId();
            if (StringUtils.isNotBlank(replay_to_id)) {
                param.put("replay_to_id", replay_to_id);
            }


            //状态 0正常 -1删除
            Integer state = postDto.getState();
            if (null != state) {
                param.put("state", state);
            }


            //类型，游戏，社区，心情
            Integer target_type = postDto.getTargetType();
            if (null != target_type) {
                param.put("target_type", target_type);
            }


            //被回复二级回复的用户名
            String reply_name = postDto.getReplyName();
            if (StringUtils.isNotBlank(reply_name)) {
                param.put("reply_name", reply_name);
            }


            //被回复二级回复的id
            String reply_to_content_id = postDto.getReplyToContentId();
            if (StringUtils.isNotBlank(reply_to_content_id)) {
                param.put("reply_to_content_id", reply_to_content_id);
            }

            replyEntityWrapper.allEq(param);

            //内容
            String content = postDto.getReplayToId();
            if (StringUtils.isNotBlank(content)) {
                replyEntityWrapper.orNew("content like '%" + content + "%'");
            }

            //根据修改时间倒叙
            replyEntityWrapper.orderBy("updated_at", false);


            List<Reply> selectRecords = null;

            if (null != replyPage) {

                Page<Reply> cmmPostPageSelect = this.replyDaoService.selectPage(replyPage, replyEntityWrapper);

//                需要总数修改
//                2019-05-28
//                lyc
//                if (null != cmmPostPageSelect) {
//                    selectRecords = cmmPostPageSelect.getRecords();
//                }
                if (null != cmmPostPageSelect) {
                    PageTools.pageToResultDto(fungoPageResultDto, cmmPostPageSelect);
                    selectRecords = cmmPostPageSelect.getRecords();

                    //设置分页数据
                    fungoPageResultDto.setCount(cmmPostPageSelect.getTotal());
                    fungoPageResultDto.setPages(cmmPostPageSelect.getPages());
                }

            } else {
                selectRecords = this.replyDaoService.selectList(replyEntityWrapper);
            }

            if (null != selectRecords) {

                cmmCmtReplyDtoList = new ArrayList<CmmCmtReplyDto>();

                for (Reply reply : selectRecords) {

                    CmmCmtReplyDto replyDto = new CmmCmtReplyDto();

                    BeanUtils.copyProperties(reply, replyDto);

                    cmmCmtReplyDtoList.add(replyDto);
                }
            }

        } catch (Exception ex) {
            LOGGER.error("/ms/service/cmm/cmt/s/list--querySecondLevelCmtList-出现异常:id="+id, ex);
        }
        fungoPageResultDto.setData(cmmCmtReplyDtoList);
        return fungoPageResultDto;
    }

    //    getReplyDtoBysSelectPageOrderByCreatedAt
    @Override
    public FungoPageResultDto<CmmCmtReplyDto> getReplyDtoBysSelectPageOrderByCreatedAt(CmmCmtReplyDto postDto) {

        List<CmmCmtReplyDto> cmmCmtReplyDtoList = null;
        FungoPageResultDto<CmmCmtReplyDto> fungoPageResultDto = new FungoPageResultDto<>();
        //PK
        String id = postDto.getId();
        try {


            int page = postDto.getPage();
            int limit = postDto.getLimit();


            EntityWrapper<Reply> replyEntityWrapper = new EntityWrapper<Reply>();
            HashMap<String, Object> param = new HashMap<String, Object>();

            Page<Reply> replyPage = null;

            if (page > 0 && limit > 0) {
                replyPage = new Page<Reply>(page, limit);
            }


            if (StringUtils.isNotBlank(id)) {
                param.put("id", id);
            }

            //评价ID
            String targetId = postDto.getTargetId();
            if (StringUtils.isNotBlank(targetId)) {
                param.put("target_id", targetId);
            }

            //会员Id
            String member_id = postDto.getMemberId();
            if (StringUtils.isNotBlank(member_id)) {
                param.put("member_id", member_id);
            }


            //回复会员Id
            String replay_to_id = postDto.getReplayToId();
            if (StringUtils.isNotBlank(replay_to_id)) {
                param.put("replay_to_id", replay_to_id);
            }


            //状态 0正常 -1删除
            Integer state = postDto.getState();
            if (null != state) {
                param.put("state", state);
            }


            //类型，游戏，社区，心情
            Integer target_type = postDto.getTargetType();
            if (null != target_type) {
                param.put("target_type", target_type);
            }


            //被回复二级回复的用户名
            String reply_name = postDto.getReplyName();
            if (StringUtils.isNotBlank(reply_name)) {
                param.put("reply_name", reply_name);
            }


            //被回复二级回复的id
            String reply_to_content_id = postDto.getReplyToContentId();
            if (StringUtils.isNotBlank(reply_to_content_id)) {
                param.put("reply_to_content_id", reply_to_content_id);
            }

            replyEntityWrapper.allEq(param);

            //内容
            String content = postDto.getReplayToId();
            if (StringUtils.isNotBlank(content)) {
                replyEntityWrapper.orNew("content like '%" + content + "%'");
            }

            //根据修改时间倒叙
            replyEntityWrapper.orderBy("created_at", true);


            List<Reply> selectRecords = null;

            if (null != replyPage) {

                Page<Reply> cmmPostPageSelect = this.replyDaoService.selectPage(replyPage, replyEntityWrapper);

                //                需要总数修改
                //                2019-05-28
                //                lyc
                //                if (null != cmmPostPageSelect) {
                //                    selectRecords = cmmPostPageSelect.getRecords();
                //                }
                if (null != cmmPostPageSelect) {
                    PageTools.pageToResultDto(fungoPageResultDto, cmmPostPageSelect);
                    selectRecords = cmmPostPageSelect.getRecords();

                    //设置分页数据
                    fungoPageResultDto.setCount(cmmPostPageSelect.getTotal());
                }

            } else {
                selectRecords = this.replyDaoService.selectList(replyEntityWrapper);
            }

            if (null != selectRecords) {

                cmmCmtReplyDtoList = new ArrayList<CmmCmtReplyDto>();

                for (Reply reply : selectRecords) {

                    CmmCmtReplyDto replyDto = new CmmCmtReplyDto();

                    BeanUtils.copyProperties(reply, replyDto);

                    cmmCmtReplyDtoList.add(replyDto);
                }
            }

        } catch (Exception ex) {
            LOGGER.error("/ms/service/cmm/cmt/s/list--querySecondLevelCmtList-出现异常:id"+id, ex);
        }
        fungoPageResultDto.setData(cmmCmtReplyDtoList);
        return fungoPageResultDto;
    }

    @Override
    public FungoPageResultDto<CmmCommentDto> queryFirstLevelCmtList(CmmCommentDto cmmCommentDto) {

        FungoPageResultDto<CmmCommentDto> fungoPageResultDto = new FungoPageResultDto<CmmCommentDto>();
        List<CmmCommentDto> cmmCommentDtoList = null;

        try {

            int page = cmmCommentDto.getPage();
            int limit = cmmCommentDto.getLimit();


            EntityWrapper<CmmComment> cmmCommentEntityWrapper = new EntityWrapper<CmmComment>();
            HashMap<String, Object> param = new HashMap<String, Object>();

            Page<CmmComment> cmmCommentPage = null;

            if (page > 0 && limit > 0) {
                cmmCommentPage = new Page<CmmComment>(page, limit);
            }

            //pk
            String id = cmmCommentDto.getId();
            if (StringUtils.isNotBlank(id)) {
                param.put("id", id);
            }

            //回复会员id
            String replyMemberId = cmmCommentDto.getMemberId();
            if (StringUtils.isNotBlank(replyMemberId)) {
                param.put("member_id", replyMemberId);
            }


            //帖子id
            String post_id = cmmCommentDto.getPostId();
            if (StringUtils.isNotBlank(post_id)) {
                param.put("post_id", post_id);
            }

            //帖子id
            Integer state = cmmCommentDto.getState();
            if (null != state) {
                param.put("state", state);
            }


            //资源类型
            Integer type = cmmCommentDto.getType();
            if (null != type) {
                param.put("type", type);
            }

            // 社区id
            String communityId = cmmCommentDto.getCommunityId();
            if (StringUtils.isNotBlank(communityId)) {
                param.put("community_id", communityId);
            }

            cmmCommentEntityWrapper.allEq(param);
            //内容
            String content = cmmCommentDto.getContent();
            if (StringUtils.isNotBlank(content)) {
                cmmCommentEntityWrapper.orNew("content like '%" + content + "%'");
            }


            //根据修改时间倒叙
            cmmCommentEntityWrapper.orderBy("updated_at", false);

            List<CmmComment> selectRecords = null;

            if (null != cmmCommentPage) {

                Page<CmmComment> cmmCommentPageList = this.cmmCommentDaoService.selectPage(cmmCommentPage, cmmCommentEntityWrapper);

                if (null != cmmCommentPageList) {
                    selectRecords = cmmCommentPageList.getRecords();

                    //设置分页数据
                    fungoPageResultDto.setCount(cmmCommentPageList.getTotal());
                    fungoPageResultDto.setPages(cmmCommentPageList.getPages());
                }


            } else {
                selectRecords = this.cmmCommentDaoService.selectList(cmmCommentEntityWrapper);
            }

            if (null != selectRecords) {

                cmmCommentDtoList = new ArrayList<CmmCommentDto>();

                for (CmmComment cmmComment : selectRecords) {

                    CmmCommentDto cmmCommentDtoResult = new CmmCommentDto();

                    BeanUtils.copyProperties(cmmComment, cmmCommentDtoResult);

                    cmmCommentDtoList.add(cmmCommentDtoResult);
                }
            }


        } catch (Exception ex) {
            LOGGER.error("/ms/service/cmm/cmt/f/lists--queryFirstLevelCmtList-出现异常:", ex);
        }

        fungoPageResultDto.setData(cmmCommentDtoList);

        return fungoPageResultDto;
    }


    @Override
    public Integer querySecondLevelCmtCount(CmmCmtReplyDto replyDto) {

        try {

            EntityWrapper<Reply> replyEntityWrapper = new EntityWrapper<Reply>();
            HashMap<String, Object> param = new HashMap<String, Object>();


            //PK
            String id = replyDto.getId();
            if (StringUtils.isNotBlank(id)) {
                param.put("id", id);
            }

            //评价ID
            String targetId = replyDto.getTargetId();
            if (StringUtils.isNotBlank(targetId)) {
                param.put("target_id", targetId);
            }

            //会员Id
            String member_id = replyDto.getMemberId();
            if (StringUtils.isNotBlank(member_id)) {
                param.put("member_id", member_id);
            }


            //回复会员Id
            String replay_to_id = replyDto.getReplayToId();
            if (StringUtils.isNotBlank(replay_to_id)) {
                param.put("replay_to_id", replay_to_id);
            }


            //状态 0正常 -1删除
            Integer state = replyDto.getState();
            if (null != state) {
                param.put("state", state);
            }


            //类型，游戏，社区，心情
            Integer target_type = replyDto.getTargetType();
            if (null != target_type) {
                param.put("target_type", target_type);
            }


            //被回复二级回复的用户名
            String reply_name = replyDto.getReplyName();
            if (StringUtils.isNotBlank(reply_name)) {
                param.put("reply_name", reply_name);
            }


            //被回复二级回复的id
            String reply_to_content_id = replyDto.getReplyToContentId();
            if (StringUtils.isNotBlank(reply_to_content_id)) {
                param.put("reply_to_content_id", reply_to_content_id);
            }


            //内容
            String content = replyDto.getReplayToId();
            if (StringUtils.isNotBlank(content)) {
                replyEntityWrapper.orNew("content like '%" + content + "%'");
            }

            int selectCount = replyDaoService.selectCount(replyEntityWrapper);
            return selectCount;

        } catch (Exception ex) {
            LOGGER.error("/ms/service/cmm/cmt/s/count--querySecondLevelCmtCount-出现异常:", ex);
        }

        return 0;

    }


    //------
}
