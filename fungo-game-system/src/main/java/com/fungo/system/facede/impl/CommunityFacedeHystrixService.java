package com.fungo.system.facede.impl;



import com.fungo.system.feign.CommunityFeignClient;
import com.fungo.system.feign.GamesFeignClient;
import com.game.common.bean.CollectionBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.*;
import com.game.common.dto.game.*;
import com.game.common.dto.index.CardIndexBean;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommunityFacedeHystrixService implements FallbackFactory<CommunityFeignClient> {

    private static final Logger logger = LoggerFactory.getLogger(CommunityFacedeHystrixService.class);

    public static void main(String[] args) {
        String a = "abc";
        String b= new String("abc").intern();
        System.out.println(a==b);
    }

    @Override
    public CommunityFeignClient create(Throwable throwable) {
        return new CommunityFeignClient() {
            @Override
            public boolean updateCounter(Map<String, String> map) {
                logger.error("--------------------CommunityFeignClient--启动熔断:{}" , "updateCounter");
                return false;
            }

            @Override
            public String getMemberIdByTargetId(Map<String, String> map) {
                logger.error("--------------------CommunityFeignClient--启动熔断:{}" , "getMemberIdByTargetId");
                return null;
            }

            @Override
            public FungoPageResultDto<CmmPostDto> queryCmmPostList(CmmPostDto cmmPostDto) {
                logger.error("--------------------CommunityFeignClient--启动熔断:{}" , "queryCmmPostList");
                return null;
            }

            @Override
            public FungoPageResultDto<MooMoodDto> queryCmmMoodList(MooMoodDto mooMoodDto) {
                logger.error("--------------------CommunityFeignClient--启动熔断:{}" , "queryCmmMoodList");
                return null;
            }

            @Override
            public FungoPageResultDto<MooMessageDto> queryCmmMoodCommentList(MooMessageDto mooMessageDto) {
                logger.error("--------------------CommunityFeignClient--启动熔断:{}" , "queryCmmMoodCommentList");
                return null;
            }

            @Override
            public ResultDto<Integer> queryCmmMoodCount(MooMoodDto mooMoodDto) {
                logger.error("--------------------CommunityFeignClient--启动熔断:{}" , "queryCmmMoodCount");
                return null;
            }

            @Override
            public ResultDto<Integer> querySecondLevelCmtCount(CmmCmtReplyDto replyDto) {
                logger.error("--------------------CommunityFeignClient--启动熔断:{}" , "querySecondLevelCmtCount");
                return null;
            }

            @Override
            public FungoPageResultDto<CmmCmtReplyDto> querySecondLevelCmtList(CmmCmtReplyDto replyDto) {
                logger.error("--------------------CommunityFeignClient--启动熔断:{}" , "querySecondLevelCmtList");
                return null;
            }

            @Override
            public FungoPageResultDto<CmmCommunityDto> queryCmmPostList(CmmCommunityDto cmmCommunityDto) {
                logger.error("--------------------CommunityFeignClient--启动熔断:{}" , "queryCmmPostList");
                return null;
            }

            @Override
            public FungoPageResultDto<CmmPostDto> listCmmPostTopicPost(CmmPostDto cmmPostDto) {
                logger.error("--------------------CommunityFeignClient--启动熔断:{}" , "listCmmPostTopicPost");
                return null;
            }

            @Override
            public FungoPageResultDto<CmmCommunityDto> queryCmmCtyDetail(CmmCommunityDto cmmCommunityDto) {
                logger.error("--------------------CommunityFeignClient--启动熔断:{}" , "queryCmmCtyDetail");
                return null;
            }

            @Override
            public ResultDto<Integer> queryCmmPostCount(CmmPostDto cmmPostDto) {
                logger.error("--------------------CommunityFeignClient--启动熔断:{}" , "queryCmmPostCount");
                return null;
            }

            @Override
            public FungoPageResultDto<CmmCommentDto> queryFirstLevelCmtList(CmmCommentDto cmmCommentDto) {
                logger.error("--------------------CommunityFeignClient--启动熔断:{}" , "queryFirstLevelCmtList");
                return null;
            }

            @Override
            public FungoPageResultDto<CommentBean> getAllComments(int pageNum, int limit, String userId) {
                logger.error("--------------------CommunityFeignClient--启动熔断:{}" , "getAllComments");
                return null;
            }

            @Override
            public FungoPageResultDto<CollectionBean> listCmmPostUsercollect(int pageNum, int limit, List<String> postIds) {
                logger.error("--------------------CommunityFeignClient--启动熔断:{}" , "listCmmPostUsercollect");
                return null;
            }

            @Override
            public ResultDto<List<String>> getRecommendMembersFromCmmPost(long ccnt, long limitSize, List<String> wathMbsSet) {
                logger.error("--------------------CommunityFeignClient--启动熔断:{}" , "getRecommendMembersFromCmmPost");
                return null;
            }

            @Override
            public FungoPageResultDto<Map<String, Object>> getFollowerCommunity(int pageNum, int limit, List<String> communityIds) {
                logger.error("--------------------CommunityFeignClient--启动熔断:{}" , "getFollowerCommunity");
                return null;
            }

            @Override
            public FungoPageResultDto<Map> queryCmmPostEssenceList() {
                logger.error("--------------------CommunityFeignClient--启动熔断:{}" , "queryCmmPostEssenceList");
                return null;
            }
        };

    }
}
