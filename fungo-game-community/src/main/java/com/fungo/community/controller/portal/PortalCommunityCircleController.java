package com.fungo.community.controller.portal;

import com.fungo.community.job.CircleHotValueJob;
import com.fungo.community.service.CircleService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.*;
import com.game.common.enums.CommonEnum;
import com.game.common.util.annotation.Anonymous;
import com.game.common.vo.CircleGamePostVo;
import com.game.common.vo.CmmCirclePostVo;
import com.game.common.vo.CmmCircleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/11
 */
@RestController
@RefreshScope
@Api(value = "", description = "圈子首页")
public class PortalCommunityCircleController {

    private static final Logger LOGGER = LoggerFactory.getLogger( PortalCommunityCircleController.class);

    @Autowired
    private CircleService circleServiceImpl;

    @Autowired
    private CircleHotValueJob circleHotValueJob;

    /**
     * 功能描述:pc
     * @param: [memberUserPrefile, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "v2.5", notes = "")
    @GetMapping(value = "/api/portal/community/community/circle/{postId}")
    @ApiImplicitParams({})
    public ResultDto<CmmCircleDto>  selectCircleByPostId(@Anonymous MemberUserProfile memberUserPrefile, @PathVariable("postId") String postId) {
        ResultDto<CmmCircleDto>  re = null;
        String memberId = memberUserPrefile == null ? "" : memberUserPrefile.getLoginId();
        try {
            re = circleServiceImpl.selectCircleByPostId(postId);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("app端获取圈子的所有玩家榜",e);
            re = ResultDto.error("-1","app端获取圈子的所有玩家榜异常");
        }
        return re;
    }


}
