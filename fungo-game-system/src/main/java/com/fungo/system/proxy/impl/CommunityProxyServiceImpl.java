package com.fungo.system.proxy.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.controller.SystemController;
import com.fungo.system.entity.Member;
import com.fungo.system.feign.CommunityFeignClient;
import com.fungo.system.feign.GamesFeignClient;
import com.fungo.system.proxy.ICommunityProxyService;
import com.fungo.system.service.ICommunityService;
import com.game.common.bean.CollectionBean;

import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CommentBean;
import com.game.common.enums.CommonEnum;
import com.game.common.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/22
 */
@Service
public class CommunityProxyServiceImpl implements ICommunityProxyService {

    @Autowired
    private CommunityFeignClient communityFeignClient;

    @Autowired
    private GamesFeignClient gamesFeignClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityProxyServiceImpl.class);



    @Override
    public List<CollectionBean> getCollection(Page<CollectionBean> page, List<String> list) {
        int pageNum = page.getPages();
        int limit  = page.getLimit();
        try {
        FungoPageResultDto<CollectionBean>  re = communityFeignClient.listCmmPostUsercollect(pageNum,limit,list);
        if(Integer.valueOf(CommonEnum.SUCCESS.code()).equals(re.getStatus()) && re.getData().size() > 0){
            return CommonUtils.deepCopy(re.getData(),CollectionBean.class);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getFollowerCommunity(Page page, List<String> communityIds) {
        int pageNum = page.getPages();
        int limit = page.getLimit();
        try{
            FungoPageResultDto<Map<String, Object>> resultDto = communityFeignClient.getFollowerCommunity(pageNum,limit,communityIds);
            if(Integer.valueOf(CommonEnum.SUCCESS.code()).equals(resultDto.getStatus()) && resultDto.getData().size() > 0){
                return resultDto.getData();
            }
        }catch (Exception e){
            LOGGER.error("社区远程调用异常:"+e);
        }

        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getPostFeeds(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<Map<String, Object>> getMoodFeeds(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<CommentBean> getAllComments(Page<CommentBean> page, String userId) {
        int pageNum = page.getPages();
        int limit  = page.getLimit();
        try {
        FungoPageResultDto<com.game.common.dto.community.CommentBean>  re = communityFeignClient.getAllComments(pageNum,limit,userId);
        if(Integer.valueOf(CommonEnum.SUCCESS.code()).equals(re.getStatus()) && re.getData().size() > 0){

                return CommonUtils.deepCopy(re.getData(),CommentBean.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<CommentBean>();
    }

    @Override
    public List<String> getRecommendMembersFromCmmPost(long ccnt, long limitSize, List<String> wathMbsSet) {
        try{
            ResultDto<List<String>> resultDto = communityFeignClient.getRecommendMembersFromCmmPost(ccnt,limitSize,wathMbsSet);
            return resultDto.getData();
        }catch (Exception e){
            LOGGER.error("社区远程调用异常:"+e);
        }

        return new ArrayList<>();
    }

    @Override
    public List<Map> getHonorQualificationOfEssencePost() {
        try{
            FungoPageResultDto<Map> re = communityFeignClient.queryCmmPostEssenceList();
            if(Integer.valueOf(CommonEnum.SUCCESS.code()).equals(re.getStatus())){
                return  re.getData();
            }
        }catch (Exception e){
            LOGGER.error("社区远程调用异常:"+e);
        }
        return new ArrayList<>();
    }
}
