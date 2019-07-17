package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.dto.ReportType;
import com.fungo.system.entity.BasConfig;
import com.fungo.system.entity.BasFeedback;
import com.fungo.system.entity.MemberFollower;
import com.fungo.system.service.BasConfigService;
import com.fungo.system.service.BasFeedbackService;
import com.fungo.system.service.ICommonService;
import com.fungo.system.service.MemberFollowerService;
import com.game.common.dto.ActionInput;
import com.game.common.dto.FeedbackBean;
import com.game.common.dto.ResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CommonServiceImpl implements ICommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonServiceImpl.class);
    @Autowired
    private BasConfigService configService;
    @Autowired
    private BasFeedbackService feedbackService;
    @Autowired
    private MemberFollowerService followService;

    @Override
    public ResultDto<List<String>> getReportType(String typeId) {
        ResultDto<List<String>> re = new ResultDto<List<String>>();
        try {
            BasConfig config = configService.selectOne(new EntityWrapper<BasConfig>().eq("key_name", "REPORT_CATEGORY"));
            ReportType type = null;
            if (config != null) {
                String value = config.getValueInfo();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    type = objectMapper.readValue(value, ReportType.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if ("9".equals(typeId)) {
                re.setData(type.getFeedback());
            } else if ("3".equals(typeId)) {
                re.setData(type.getGame());
            } else if ("1".equals(typeId)) {
                re.setData(type.getPost());
            } else if ("5678".contains(typeId)) {
                re.setData(type.getComment());
            } else if ("2".equals(typeId)) {
                re.setData(type.getMood());
            } else {
                re.setData(type.getFeedback());
            }
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("举报反馈类型异常",e);
        }

        return re;
    }

    @Override
    public ResultDto<Map<String, String>> getAppConfig() {
        ResultDto<Map<String, String>> re = new ResultDto<Map<String, String>>();
        List<String> list = new ArrayList<String>();
        try {
            BasConfig config = configService.selectOne(new EntityWrapper<BasConfig>().eq("key", "REPORT_CATEGORY"));
        }catch (Exception e){
            LOGGER.error("getAppConfig异常",e);
        }
        return re;
    }

    @Override
    public ResultDto<Map<String, String>> checkUpdate() {
        ResultDto<Map<String, String>> re = new ResultDto<Map<String, String>>();
        List<String> list = new ArrayList<String>();
        BasConfig config = configService.selectOne(new EntityWrapper<BasConfig>().eq("key", "REPORT_CATEGORY"));
        return re;
    }

    @Override
    public ResultDto<String> feedback(String memberId, FeedbackBean feedBack) throws Exception {
        ResultDto<String> re = new ResultDto<String>();
        BasFeedback feed = new BasFeedback();
        feed.setContent(feedBack.getContent());
        feed.setMemberId(memberId);
        feed.setState(0);
        feed.setUpdatedAt(new Date());
        feed.setCreatedAt(new Date());
        feed.setTitle(feedBack.getTitle());
        feedbackService.insert(feed);
//		gameProxy.addScore(Setting.ACTION_TYPE_FEEDBACK, memberId, feed.getId(), Setting.RES_TYPE_FEEDBACK);
        re.setMessage("反馈成功，感谢你对产品的支持");
        return re;
    }

    @Override
    @Transactional
    public ResultDto<String> followUser(ActionInput input) {
        String userId = input.getUser_id();
        String targetId = input.getTarget_id();
        //有没有关注
        //有没有互相关注
        MemberFollower f1 = followService.selectOne(new EntityWrapper<MemberFollower>().eq("member_id", userId).eq("follower_id", targetId));
        if (f1 == null) {
            MemberFollower fo = new MemberFollower();
            Date date = new Date();
            fo.setCreatedAt(date);
            fo.setUpdatedAt(date);
            fo.setFollowedAt(date);
            fo.setMemberId(userId);
            fo.setFollowerId(targetId);
            fo.setState(1);
            followService.insert(fo);

            MemberFollower ft = new MemberFollower();
            ft.setCreatedAt(date);
            ft.setUpdatedAt(date);
            ft.setFollowedAt(date);
            ft.setMemberId(targetId);
            ft.setFollowerId(userId);
            ft.setState(4);
            followService.insert(ft);
        } else {
            MemberFollower f2 = followService.selectOne(new EntityWrapper<MemberFollower>().eq("member_id", targetId).eq("follower_id", userId));
            Integer state = f2.getState();
            if (1 == state) {//用户被关注
                f1.setState(2);
                f2.setState(2);
                followService.updateById(f1);
                followService.updateById(f2);
            } else if (3 == state) {//双方取消关注
                f1.setState(1);
                f2.setState(4);
                followService.updateById(f1);
                followService.updateById(f2);
            }
        }

        return ResultDto.success();
    }

    @Override
    @Transactional
    public ResultDto<String> unFollowUser(ActionInput input) {
        String userId = input.getUser_id();
        String targetId = input.getTarget_id();
        MemberFollower f1 = followService.selectOne(new EntityWrapper<MemberFollower>().eq("member_id", userId).eq("follower_id", targetId));
        if (f1 != null) {
            MemberFollower f2 = followService.selectOne(new EntityWrapper<MemberFollower>().eq("member_id", targetId).eq("follower_id", userId));
            Integer state = f2.getState();
            if (2 == state) {//互相关注
                f1.setState(4);
                f2.setState(1);
                followService.updateById(f1);
                followService.updateById(f2);
            } else if (4 == state) {
                f1.setState(3);
                f2.setState(3);
                followService.updateById(f1);
                followService.updateById(f2);
            }
        }
        return ResultDto.success();
    }

}
