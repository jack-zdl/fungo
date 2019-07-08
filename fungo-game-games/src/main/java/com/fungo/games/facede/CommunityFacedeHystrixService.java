package com.fungo.games.facede;


import com.fungo.games.feign.CommunityFeignClient;
import com.fungo.games.feign.SystemFeignClient;
import com.game.common.bean.TagBean;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.community.CmmCmtReplyDto;
import com.game.common.dto.community.CmmCommunityDto;
import com.game.common.dto.game.BasTagDto;
import com.game.common.dto.game.BasTagGroupDto;
import com.game.common.dto.system.TaskDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.dto.user.MemberOutBean;
import com.game.common.vo.CircleGamePostVo;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommunityFacedeHystrixService implements FallbackFactory<CommunityFeignClient> {

    private static final Logger logger = LoggerFactory.getLogger(CommunityFacedeHystrixService.class);

    @Override
    public CommunityFeignClient create(Throwable throwable) {
        return new CommunityFeignClient(){

            @Override
            public FungoPageResultDto<CmmCmtReplyDto> getReplyDtoBysSelectPageOrderByCreatedAt(CmmCmtReplyDto replyDto) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "getReplyDtoBysSelectPageOrderByCreatedAt");
                return new FungoPageResultDto<CmmCmtReplyDto>();
            }

            @Override
            public FungoPageResultDto<CmmCommunityDto> getCmmCommunitySelectOneById(CmmCommunityDto cmmCommunityDto) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "getCmmCommunitySelectOneById");
                return new FungoPageResultDto<CmmCommunityDto>();
            }

            @Override
            public ResultDto<Map<String, Integer>> listCommunityFolloweeNum(List<String> communityIds) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "listCommunityFolloweeNum");
                return new ResultDto<Map<String, Integer>>();
            }

            @Override
            public ResultDto<String> getCircleByGame(CircleGamePostVo circleGamePostVo) {
                logger.error("--------------------SystemFeignClient--启动熔断:{}" , "getCircleByGame");
                return null;
            }
        };
    }
}
