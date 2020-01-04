package com.fungo.community.facede;

import com.fungo.community.feign.SystemFeignClient;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.system.CircleFollowVo;
import com.game.common.dto.system.TaskDto;
import com.game.common.dto.user.*;
import com.game.common.vo.MemberFollowerVo;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemFacedeHystrixService implements FallbackFactory<SystemFeignClient> {

    private static final Logger logger = LoggerFactory.getLogger(SystemFacedeHystrixService.class);


    @Override
    public SystemFeignClient create(Throwable throwable) {
        return new SystemFeignClient(){

            @Override
            public FungoPageResultDto<String> getFollowerUserId(MemberFollowerVo memberFollowerVo) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "getFollowerUserId");
                return null;
            }

            @Override
            public ResultDto<List<MemberDto>> listMembersByids(List<String> ids, Integer state) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "listMembersByids");
                return null;
            }

            @Override
            public FungoPageResultDto<MemberFollowerDto> getMemberFollowerList(MemberFollowerVo memberFollowerVo) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "getMemberFollowerList");
                return null;
            }

            @Override
            public FungoPageResultDto<MemberDto> getMemberDtoList(MemberDto memberDto) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "getMemberDtoList");
                return null;
            }

            @Override
            public FungoPageResultDto<IncentRankedDto> getIncentRankedList(IncentRankedDto incentRankedDto) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "getIncentRankedList");
                return null;
            }

            @Override
            public ResultDto<List<String>> listFollowerCommunityId(String memberId) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "listFollowerCommunityId");
                return null;
            }

            @Override
            public ResultDto<Integer> countActionNum(BasActionDto basActionDto) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "countActionNum");
                return null;
            }

            @Override
            public ResultDto<List<String>> listtargetId(BasActionDto basActionDto) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "listtargetId");
                return null;
            }

            @Override
            public ResultDto<AuthorBean> getAuthor(String memberId) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "getAuthor");
                return null;
            }

            @Override
            public FungoPageResultDto<AuthorBean> getAuthorList(String memberIds) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "getAuthorList");
                return null;
            }

//            @Override
//            public FungoPageResultDto<AuthorBean> getAuthorList(List<String> memberIds) {
//                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "getAuthorList");
//                return null;
//            }

            @Override
            public ResultDto<Map<String, Object>> exTask(TaskDto taskDto) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "exTask");
                return null;
            }

            @Override
            public ResultDto<AuthorBean> getUserCard(String cardId, String memberId) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "getUserCard");
                return null;
            }

            @Override
            public ResultDto<List<HashMap<String, Object>>> getStatusImage(String memberId) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "getStatusImage");
                return null;
            }

            @Override
            public ResultDto<List<MemberDto>> listWatchMebmber(Integer limit, String currentMbId) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "listWatchMebmber");
                return null;
            }

            @Override
            public ResultDto<List<MemberDto>> listRecommendedMebmber(Integer limit, String currentMbId, List<String> wathMbsSet) {

                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "listRecommendedMebmber");
                return null;
            }

            @Override
            public ResultDto<IncentRuleRankDto> getIncentRuleRankById(String id) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "getIncentRuleRankById");
                return null;
            }

            @Override
            public ResultDto<List<BasActionDto>> listActionByCondition(BasActionDto basActionDto) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "listActionByCondition");
                return null;
            }

            @Override
            public ResultDto<MemberFollowerDto> getMemberFollower1(MemberFollowerDto memberFollowerDto) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "getMemberFollower1");
                return null;
            }

            @Override
            public ResultDto<List<String>> getRecentBrowseCommunityByUserId(String userId) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "getRecentBrowseCommunityByUserId");
                return null;
            }

            @Override
            public ResultDto<CircleFollowVo> circleListFollow(CircleFollowVo circleFollowVo) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "circleListFollow");
                return null;
            }

            @Override
            public FungoPageResultDto<String> circleListMineFollow(CircleFollowVo circleFollowVo) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "circleListMineFollow");
                return null;
            }

            @Override
            public ResultDto<Boolean> subtractMemberScoreAccount(Map<String, Object> accountParamMap) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "circleListMineFollow");
                return null;
            }

            @Override
            public ResultDto<MemberDto> getMembersByid(String memberId) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "getMembersByid");
                return null;
            }

            @Override
            public ResultDto<Map<String, Object>> getMemberFollow(MemberFollowerVo memberFollowVo) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "getMemberFollow");
                return null;
            }

            @Override
            public ResultDto<String> updateRankedMedal(String userId, Integer rankidt) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "updateRankedMedal");
                return null;
            }

            @Override
            public ResultDto<List<MemberNameDTO>> getCircleMainByMemberId(String circleId) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "getCircleMainByMemberId");
                return null;
            }
        };
    }
}
