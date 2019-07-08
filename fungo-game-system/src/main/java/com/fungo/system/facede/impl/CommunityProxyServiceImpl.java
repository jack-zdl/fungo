package com.fungo.system.facede.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.feign.CommunityFeignClient;
import com.fungo.system.facede.ICommunityProxyService;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityProxyServiceImpl.class);

    //    @HystrixCommand(fallbackMethod = "hystrixGetCollection")
    @Override
    public List<CollectionBean> getCollection(Page<CollectionBean> page, List<String> postIds) {
        int pageNum = page.getPages();
        int limit  = page.getLimit();
        try {
        FungoPageResultDto<CollectionBean>  re = communityFeignClient.listCmmPostUsercollect(pageNum,limit,postIds);
        if(Integer.valueOf(CommonEnum.SUCCESS.code()).equals(re.getStatus()) && re.getData().size() > 0){
            return CommonUtils.deepCopy(re.getData(),CollectionBean.class);
            }
        }catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("CommunityProxyServiceImpl.getCollection",e);
        }
        return new ArrayList<>();
    }

    public List<CollectionBean> hystrixGetCollection(Page<CollectionBean> page, List<String> postIds) {
        LOGGER.warn("CommunityProxyServiceImpl.hystrixGetCollection 熔断器打开");
        return new ArrayList<>();
    }

    //    @HystrixCommand(fallbackMethod = "hystrixGetFollowerCommunity")
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
            e.printStackTrace();
            LOGGER.error("社区远程调用异常:"+e);
        }

        return new ArrayList<>();
    }

    public List<Map<String, Object>> hystrixGetFollowerCommunity(Page page, List<String> communityIds) {
        LOGGER.warn("CommunityProxyServiceImpl.hystrixGetFollowerCommunity 熔断器打开");
        return new ArrayList<>();
    }

    //    @HystrixCommand(fallbackMethod = "hystrixGetPostFeeds")
    @Override
    public List<Map<String, Object>> getPostFeeds(Map<String, Object> map) {
        return null;
    }

    public List<Map<String, Object>> hystrixGetPostFeeds(Map<String, Object> map) {
        LOGGER.warn("CommunityProxyServiceImpl.hystrixGetPostFeeds 熔断器打开");
        return null;
    }

    //    @HystrixCommand(fallbackMethod = "hystrixGetMoodFeeds")
    @Override
    public List<Map<String, Object>> getMoodFeeds(Map<String, Object> map) {
        return null;
    }

    public List<Map<String, Object>> hystrixGetMoodFeeds(Map<String, Object> map) {
        LOGGER.warn("CommunityProxyServiceImpl.hystrixGetMoodFeeds 熔断器打开");
        return null;
    }

    //    @HystrixCommand(fallbackMethod = "hystrixGetAllComments")
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
        return new ArrayList<>();
    }

    public List<CommentBean> hystrixGetAllComments(Page<CommentBean> page, String userId) {
        LOGGER.warn("CommunityProxyServiceImpl.hystrixGetAllComments 熔断器打开");
        return new ArrayList<>();
    }

    //    @HystrixCommand(fallbackMethod = "hystrixGetRecommendMembersFromCmmPost")
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

    public List<String> hystrixGetRecommendMembersFromCmmPost(long ccnt, long limitSize, List<String> wathMbsSet) {
        LOGGER.warn("CommunityProxyServiceImpl.hystrixGetRecommendMembersFromCmmPost 熔断器打开");
        return new ArrayList<>();
    }

    //    @HystrixCommand(fallbackMethod = "hystrixGetHonorQualificationOfEssencePost")
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

    @Override
    public List<String> listOfficialCommunityIds() {
        ResultDto<List<String>> resultDto = communityFeignClient.listOfficialCommunityIds();
        if(!resultDto.isSuccess()||resultDto.getData()==null){
            return  new ArrayList<>();
        }
        return resultDto.getData();
    }

    @Override
    public List<String> listGameIds(List<String> list) {
        ResultDto<List<String>> resultDto = communityFeignClient.listGameIds(list);
        if(!resultDto.isSuccess()||resultDto.getData()==null){
            return  new ArrayList<>();
        }
        return resultDto.getData();
    }

    public List<Map> hystrixGetHonorQualificationOfEssencePost() {
        LOGGER.warn("CommunityProxyServiceImpl.hystrixGetHonorQualificationOfEssencePost 熔断器打开");
        return new ArrayList<>();
    }
}
