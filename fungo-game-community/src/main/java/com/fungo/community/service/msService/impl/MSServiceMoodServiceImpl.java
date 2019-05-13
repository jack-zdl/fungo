package com.fungo.community.service.msService.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.dao.service.MooMoodDaoService;
import com.fungo.community.entity.MooMood;
import com.fungo.community.service.msService.IMSServiceMoodService;
import com.game.common.dto.community.MooMoodDto;
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




    @Override
    public List<MooMoodDto> queryCmmMoodList(MooMoodDto mooMoodDto) {

        List<MooMoodDto> cmmPostList = null;

        try {


            int page = mooMoodDto.getPage();
            int limit = mooMoodDto.getLimit();


            EntityWrapper<MooMood>  moodDtoEntityWrapper = new EntityWrapper<MooMood>();
            HashMap<String, Object> param = new HashMap<String, Object>();

            Page<MooMood> mooMoodPage = null;

            if (page > 0 && limit > 0) {
                mooMoodPage = new Page<MooMood>(page, limit);
            }








            //根据修改时间倒叙
            moodDtoEntityWrapper.orderBy("updated_at", false);

            List<MooMood> selectRecords = null;

            if (null != mooMoodPage) {

                Page<MooMood> cmmMoodPageResult = this.mooMoodDaoService.selectPage(mooMoodPage, moodDtoEntityWrapper);

                if (null != cmmMoodPageResult) {
                    selectRecords = cmmMoodPageResult.getRecords();
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
            LOGGER.error("/ms/service/cmm/mood/lists----queryCmmPostList-出现异常:", ex);
        }
        return cmmPostList;
    }


    //------
}
