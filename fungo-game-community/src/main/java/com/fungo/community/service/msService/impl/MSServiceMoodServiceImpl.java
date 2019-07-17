package com.fungo.community.service.msService.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.dao.service.MooMessageDaoService;
import com.fungo.community.dao.service.MooMoodDaoService;
import com.fungo.community.entity.MooMessage;
import com.fungo.community.entity.MooMood;
import com.fungo.community.service.msService.IMSServiceMoodService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.community.MooMessageDto;
import com.game.common.dto.community.MooMoodDto;
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
public class MSServiceMoodServiceImpl implements IMSServiceMoodService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MSServiceMoodServiceImpl.class);


    @Autowired
    private MooMoodDaoService mooMoodDaoService;

    @Autowired
    private MooMessageDaoService mooMessageDaoService;


    @Override
    public FungoPageResultDto<MooMoodDto> queryCmmMoodList(MooMoodDto mooMoodDto) {

        FungoPageResultDto<MooMoodDto> resultDto = new FungoPageResultDto<MooMoodDto>();
        List<MooMoodDto> cmmPostList = null;
        String id = mooMoodDto.getId();
        try {

            int page = mooMoodDto.getPage();
            int limit = mooMoodDto.getLimit();


            EntityWrapper<MooMood> moodDtoEntityWrapper = new EntityWrapper<MooMood>();
            HashMap<String, Object> param = new HashMap<String, Object>();

            Page<MooMood> mooMoodPage = null;

            if (page > 0 && limit > 0) {
                mooMoodPage = new Page<MooMood>(page, limit);
            }

            //id
            if (StringUtils.isNotBlank(id)) {
                param.put("id", id);
            }
            //会员id
            String member_id = mooMoodDto.getMemberId();
            if (StringUtils.isNotBlank(member_id)) {
                param.put("member_id", member_id);
            }

            //状态 0正常 1视频处理中 -1删除
            Integer state = mooMoodDto.getState();
            if (null != state) {
                param.put("state", state);
            }


            //类型 1:普通 2:精选
            Integer type = mooMoodDto.getType();
            if (null != type) {
                param.put("type", type);
            }

            moodDtoEntityWrapper.allEq(param);

            //内容
            String content = mooMoodDto.getContent();
            if (StringUtils.isNotBlank(content)) {
                moodDtoEntityWrapper.orNew("content like '%" + content + "%'");
            }


            //根据修改时间倒叙
            moodDtoEntityWrapper.orderBy("updated_at", false);

            List<MooMood> selectRecords = null;

            if (null != mooMoodPage) {

                Page<MooMood> cmmMoodPageResult = this.mooMoodDaoService.selectPage(mooMoodPage, moodDtoEntityWrapper);

                if (null != cmmMoodPageResult) {
                    selectRecords = cmmMoodPageResult.getRecords();

                    resultDto.setCount(cmmMoodPageResult.getTotal());
                    resultDto.setPages(cmmMoodPageResult.getPages());
                }

            } else {
                selectRecords = this.mooMoodDaoService.selectList(moodDtoEntityWrapper);
            }

            if (null != selectRecords) {

                cmmPostList = new ArrayList<MooMoodDto>();

                for (MooMood mooMoodEntity : selectRecords) {

                    MooMoodDto mooMoodDtoResult = new MooMoodDto();

                    BeanUtils.copyProperties(mooMoodEntity, mooMoodDtoResult);

                    cmmPostList.add(mooMoodDtoResult);
                }
            }

        } catch (Exception ex) {
            LOGGER.error("/ms/service/cmm/mood/lists----queryCmmPostList-出现异常:心情id="+id, ex);
        }

        resultDto.setData(cmmPostList);
        return resultDto;
    }

    @Override
    public FungoPageResultDto<MooMessageDto> queryCmmMoodCommentList(MooMessageDto mooMessageDto) {

        FungoPageResultDto<MooMessageDto> resultDto = new FungoPageResultDto<MooMessageDto>();
        List<MooMessageDto> mooMessageDtoList = null;
        String id = mooMessageDto.getId();
        try {

            int page = mooMessageDto.getPage();
            int limit = mooMessageDto.getLimit();


            EntityWrapper<MooMessage> mooMessageEntityWrapper = new EntityWrapper<MooMessage>();
            HashMap<String, Object> param = new HashMap<String, Object>();

            Page<MooMessage> mooMessagePage = null;

            if (page > 0 && limit > 0) {
                mooMessagePage = new Page<MooMessage>(page, limit);
            }

            //pk
//            String id = mooMessageDto.getId();
            if (StringUtils.isNotBlank(id)) {
                param.put("id", id);
            }


            //心情id
            String moodId = mooMessageDto.getMoodId();
            if (StringUtils.isNotBlank(moodId)) {
                param.put("mood_id", moodId);
            }


            //用户id
            String member_id = mooMessageDto.getMemberId();
            if (StringUtils.isNotBlank(member_id)) {
                param.put("member_id", member_id);
            }


            //状态 0正常 -1删除
            Integer state = mooMessageDto.getState();
            if (null != state) {
                param.put("state", state);
            }


            //未用到
            //加精
            //热门
            Integer type = mooMessageDto.getType();
            if (null != type) {
                param.put("type", type);
            }

            mooMessageEntityWrapper.allEq(param);

            //内容
            String content = mooMessageDto.getContent();
            if (StringUtils.isNotBlank(content)) {
                mooMessageEntityWrapper.orNew("content like '%" + content + "%'");
            }

            //根据修改时间倒叙
            mooMessageEntityWrapper.orderBy("updated_at", false);

            List<MooMessage> selectRecords = null;

            if (null != mooMessagePage) {

                Page<MooMessage> cmmMoodPageResult = this.mooMessageDaoService.selectPage(mooMessagePage, mooMessageEntityWrapper);

                if (null != cmmMoodPageResult) {
                    selectRecords = cmmMoodPageResult.getRecords();

                    resultDto.setCount(cmmMoodPageResult.getTotal());
                    resultDto.setPages(cmmMoodPageResult.getPages());
                }

            } else {
                selectRecords = this.mooMessageDaoService.selectList(mooMessageEntityWrapper);
            }

            if (null != selectRecords) {

                mooMessageDtoList = new ArrayList<MooMessageDto>();

                for (MooMessage mooMessageEntity : selectRecords) {

                    MooMessageDto mooMessageDtoResult = new MooMessageDto();

                    BeanUtils.copyProperties(mooMessageEntity, mooMessageDtoResult);

                    mooMessageDtoList.add(mooMessageDtoResult);
                }
            }


        } catch (Exception ex) {
            LOGGER.error("/ms/service/cmm/mood/cmt/lists----queryCmmMoodCommentList-出现异常:心情评论id="+id, ex);
        }
        resultDto.setData(mooMessageDtoList);
        return resultDto;
    }

    @Override
    public Integer queryCmmMoodCount(MooMoodDto mooMoodDto) {
        try {


            int page = mooMoodDto.getPage();
            int limit = mooMoodDto.getLimit();


            EntityWrapper<MooMood> moodDtoEntityWrapper = new EntityWrapper<MooMood>();
            HashMap<String, Object> param = new HashMap<String, Object>();

            Page<MooMood> mooMoodPage = null;

            if (page > 0 && limit > 0) {
                mooMoodPage = new Page<MooMood>(page, limit);
            }

            //会员id
            String member_id = mooMoodDto.getMemberId();
            if (StringUtils.isNotBlank(member_id)) {
                param.put("member_id", member_id);
            }

            //状态 0正常 1视频处理中 -1删除
            Integer state = mooMoodDto.getState();
            if (null != state) {
                param.put("state", state);
            }


            //类型 1:普通 2:精选
            Integer type = mooMoodDto.getType();
            if (null != type) {
                param.put("type", type);
            }
            moodDtoEntityWrapper.allEq(param);
            //内容
            String content = mooMoodDto.getContent();
            if (StringUtils.isNotBlank(content)) {
                moodDtoEntityWrapper.orNew("content like '%" + content + "%'");
            }

            return mooMoodDaoService.selectCount(moodDtoEntityWrapper);

        } catch (Exception ex) {
            LOGGER.error("/ms/service/cmm/mood/count----queryCmmPostList-出现异常:参数="+mooMoodDto.toString(), ex);
        }
        return 0;
    }


    //------
}
