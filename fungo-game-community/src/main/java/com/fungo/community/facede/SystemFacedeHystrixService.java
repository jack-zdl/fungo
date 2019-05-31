package com.fungo.community.facede;

import com.fungo.community.feign.SystemFeignClient;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.system.TaskDto;
import com.game.common.dto.user.IncentRankedDto;
import com.game.common.dto.user.IncentRuleRankDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.dto.user.MemberFollowerDto;
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
        };
    }
}
