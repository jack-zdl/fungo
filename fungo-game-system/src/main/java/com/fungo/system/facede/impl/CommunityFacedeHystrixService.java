package com.fungo.system.facede.impl;



import com.fungo.system.feign.CommunityFeignClient;
import com.game.common.bean.CollectionBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.*;
import com.game.common.vo.CircleGamePostVo;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class CommunityFacedeHystrixService implements FallbackFactory<CommunityFeignClient> {

    private static final Logger logger = LoggerFactory.getLogger(CommunityFacedeHystrixService.class);

    private static final String logKey = "--------------------CommunityFeignClient--启动熔断:{}";
    @Override
    public CommunityFeignClient create(Throwable throwable) {
        return new CommunityFeignClient() {
            @Override
            public boolean updateCounter(Map<String, String> map) {
                logger.error(logKey , "updateCounter");
                return false;
            }

            @Override
            public String getMemberIdByTargetId(Map<String, String> map) {
                logger.error(logKey , "getMemberIdByTargetId");
                return null;
            }

            @Override
            public FungoPageResultDto<CmmPostDto> queryCmmPostList(CmmPostDto cmmPostDto) {
                logger.error(logKey , "queryCmmPostList");
                return null;
            }

            @Override
            public FungoPageResultDto<MooMoodDto> queryCmmMoodList(MooMoodDto mooMoodDto) {
                logger.error(logKey , "queryCmmMoodList");
                return null;
            }

            @Override
            public FungoPageResultDto<MooMessageDto> queryCmmMoodCommentList(MooMessageDto mooMessageDto) {
                logger.error(logKey , "queryCmmMoodCommentList");
                return null;
            }

            @Override
            public ResultDto<Integer> queryCmmMoodCount(MooMoodDto mooMoodDto) {
                logger.error(logKey , "queryCmmMoodCount");
                return null;
            }

            @Override
            public ResultDto<Integer> querySecondLevelCmtCount(CmmCmtReplyDto replyDto) {
                logger.error(logKey , "querySecondLevelCmtCount");
                return null;
            }

            @Override
            public FungoPageResultDto<CmmCmtReplyDto> querySecondLevelCmtList(CmmCmtReplyDto replyDto) {
                logger.error(logKey , "querySecondLevelCmtList");
                return null;
            }

            @Override
            public FungoPageResultDto<CmmCommunityDto> queryCmmPostList(CmmCommunityDto cmmCommunityDto) {
                logger.error(logKey , "queryCmmPostList");
                return null;
            }

            @Override
            public FungoPageResultDto<CmmPostDto> listCmmPostTopicPost(CmmPostDto cmmPostDto) {
                logger.error(logKey , "listCmmPostTopicPost");
                return null;
            }

            @Override
            public FungoPageResultDto<CmmCommunityDto> queryCmmCtyDetail(CmmCommunityDto cmmCommunityDto) {
                logger.error(logKey , "queryCmmCtyDetail");
                return null;
            }

            @Override
            public ResultDto<Integer> queryCmmPostCount(CmmPostDto cmmPostDto) {
                logger.error(logKey , "queryCmmPostCount");
                return null;
            }

            @Override
            public FungoPageResultDto<CmmCommentDto> queryFirstLevelCmtList(CmmCommentDto cmmCommentDto) {
                logger.error(logKey , "queryFirstLevelCmtList");
                return null;
            }

            @Override
            public FungoPageResultDto<CommentBean> getAllComments(int pageNum, int limit, String userId) {
                logger.error(logKey , "getAllComments");
                return null;
            }

            @Override
            public FungoPageResultDto<CollectionBean> listCmmPostUsercollect(int pageNum, int limit, List<String> postIds) {
                logger.error(logKey , "listCmmPostUsercollect");
                return null;
            }

            @Override
            public ResultDto<List<String>> getRecommendMembersFromCmmPost(long ccnt, long limitSize, List<String> wathMbsSet) {
                logger.error(logKey , "getRecommendMembersFromCmmPost");
                return null;
            }

            @Override
            public FungoPageResultDto<Map<String, Object>> getFollowerCommunity(int pageNum, int limit, List<String> communityIds) {
                logger.error(logKey , "getFollowerCommunity");
                return null;
            }

            @Override
            public FungoPageResultDto<Map> queryCmmPostEssenceList() {
                logger.error(logKey , "queryCmmPostEssenceList");
                return null;
            }

            @Override
            public ResultDto<List<String>> listOfficialCommunityIds() {
                logger.error(logKey , "listOfficialCommunityIds");
                return null;
            }

            @Override
            public ResultDto<List<String>> listGameIds(List<String> list) {
                logger.error(logKey , "listGameIds");
                return null;
            }


            public ResultDto<Integer> getPostBoomWatchNumByCardId(String cardId) {
                logger.error(logKey , "getPostBoomWatchNumByCardId");
                return null;
            }

            @Override
            public ResultDto<Map> getGameMsgByPost(CmmPostDto cmmPost) {
                logger.error(logKey , "getGameMsgByPost");
                return null;
            }

            @Override
            public ResultDto<CmmCircleDto> getCircleByPost(CircleGamePostVo circleGamePostVo) {
                logger.error(logKey , "getCircleByPost");
                return null;
            }

            @Override
            public ResultDto<List<String>> listCircleNameByPost(String postId) {
                logger.error(logKey , "listCircleNameByPost");
                return null;
            }

            @Override
            public ResultDto<List<String>> listCircleNameByComment(String commentId) {
                logger.error(logKey , "listCircleNameByComment");
                return null;
            }

            /**
             * 查询 用户心情及文章总数 - 排除已删除
             */
            @Override
            public ResultDto<Map<String, Integer>> countMoodAndPost(String userId) {
                logger.error(logKey , "countMoodAndPost");
                return null;
            }

            @Override
            public FungoPageResultDto<CmmCircleDto> getCircleListByType(CircleGamePostVo circleGamePostVo) {
                logger.error(logKey , "getCircleListByType");
                return null;
            }
        };

    }
}
