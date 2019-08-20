package com.fungo.games.facede;


import com.fungo.games.feign.SystemFeignClient;
import com.game.common.bean.TagBean;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.game.BasTagDto;
import com.game.common.dto.game.BasTagGroupDto;
import com.game.common.dto.mall.MallGoodsInput;
import com.game.common.dto.system.CircleFollowVo;
import com.game.common.dto.system.TaskDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.dto.user.MemberOutBean;
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
        return new SystemFeignClient() {
            @Override
            public ResultDto<MemberOutBean> getUserInfo() throws Exception {
                logger.error("--------------------SystemFeignClient--启动熔断:{}", "getUserInfo");
                return new ResultDto<MemberOutBean>();
            }

            @Override
            public ResultDto<String> test() {
                logger.error("--------------------SystemFeignClient--启动熔断:{}", "test");
                return new ResultDto<String>();
            }

            @Override
            public ResultDto<Map<String, Object>> exTask(TaskDto taskDto) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}", "exTask");
                return new ResultDto<Map<String, Object>>();
            }

            @Override
            public ResultDto<AuthorBean> getAuthor(String memberId) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}", "getAuthor");
                return new ResultDto<AuthorBean>();
            }

            @Override
            public ResultDto<Integer> getBasActionSelectCount(BasActionDto basActionDto) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}", "getBasActionSelectCount");
                return new ResultDto<Integer>();
            }

            @Override
            public ResultDto<MemberDto> getMembersByid(String id) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}", "getMembersByid");
                return new ResultDto<MemberDto>();
            }

            @Override
            public ResultDto<List<HashMap<String, Object>>> getStatusImageByMemberId(String memberId) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}", "getStatusImageByMemberId");
                return new ResultDto<List<HashMap<String, Object>>>();
            }

            @Override
            public ResultDto<List<BasTagDto>> getBasTagBySelectListInId(List<String> collect) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}", "getBasTagBySelectListInId");
                return new ResultDto<List<BasTagDto>>();
            }

            @Override
            public ResultDto<List<BasTagDto>> getBasTagBySelectListGroupId(String groupId) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}", "getBasTagBySelectListGroupId");
                return new ResultDto<List<BasTagDto>>();
            }

            @Override
            public void push(String inviteMemberId, int i, String appVersion) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}", "push");
            }

            @Override
            public ResultDto<BasTagDto> getBasTagBySelectById(String id) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}", "getBasTagBySelectById");
                return new ResultDto<BasTagDto>();
            }

            @Override
            public ResultDto<List<BasTagGroupDto>> getBasTagGroupBySelectList(BasTagGroupDto basTagGroupDto) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}", "getBasTagGroupBySelectList");
                return new ResultDto<List<BasTagGroupDto>>();
            }

            @Override
            public ResultDto<List<TagBean>> listSortTags(List<String> tags) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}", "listSortTags");
                return new ResultDto<List<TagBean>>();
            }

            @Override
            public FungoPageResultDto<MemberDto> listMemberDtoPag(MemberDto memberDto) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}", "listMemberDtoPag");
                return new FungoPageResultDto<MemberDto>();
            }

            @Override
            public ResultDto<List<String>> listGameHisIds(String memberid) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}", "listGameHisIds");
                return null;
            }

            @Override
            public ResultDto<Map<String, Object>> queryGoodsCountWithGame(MallGoodsInput mallGoodsInput) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}", "queryGoodsCountWithGame");
                return null;
            }

            @Override
            public FungoPageResultDto<String> gameListMineDownload(CircleFollowVo circleFollowVo) {
                logger.error("--------------------gameListMineDownload--启动熔断:{}", "gameListMineDownload");
                return null;
            }
        };
    }
}
