package com.fungo.community.service.msService.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.dao.service.ReplyDaoService;
import com.fungo.community.entity.Reply;
import com.fungo.community.service.msService.IMSServiceCommentService;
import com.game.common.dto.community.CmmCmtReplyDto;
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

    @Override
    public List<CmmCmtReplyDto> querySecondLevelCmtList(CmmCmtReplyDto postDto) {

        List<CmmCmtReplyDto> cmmCmtReplyDtoList = null;

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
            String id = postDto.getId();
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

                if (null != cmmPostPageSelect) {
                    selectRecords = cmmPostPageSelect.getRecords();
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
            LOGGER.error("/ms/service/cmm/cmt/s/list--querySecondLevelCmtList-出现异常:", ex);
        }
        return cmmCmtReplyDtoList;
    }


    //------
}
