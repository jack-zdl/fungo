package com.fungo.community.controller.portal;

import com.fungo.community.job.CircleHotValueJob;
import com.fungo.community.service.CircleService;
import com.fungo.community.service.portal.IPortalCommunityPostService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.*;
import com.game.common.enums.CommonEnum;
import com.game.common.util.CommonUtil;
import com.game.common.util.CommonUtils;
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
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
    private IPortalCommunityPostService portalCommunityPostService;

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

    /**
     * 功能描述: pc端获取游戏圈子的文章
     * @param: [memberUserPrefile, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "pc2.1", notes = "")
    @PostMapping(value = "/api/portal/community/circle/game/post")
    @ApiImplicitParams({})
    public FungoPageResultDto<PostOutBean>  selectCircleGamePost(@Anonymous MemberUserProfile memberUserPrefile, @Valid @RequestBody CircleGamePostVo circleGamePostVo, Errors errors) {
        if(errors.hasErrors()) return FungoPageResultDto.FungoPageResultDtoFactory.buildError( errors.getAllErrors().get(0).getDefaultMessage());
        FungoPageResultDto<PostOutBean>  re = null;
        String memberId = memberUserPrefile == null ? "" : memberUserPrefile.getLoginId();
        try {
            re = portalCommunityPostService.selectCircleGamePost(memberId,circleGamePostVo);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("app端获取游戏圈子的文章",e);
            re = FungoPageResultDto.error("-1","app端获取游戏圈子的文章");
        }
        return re;
    }

    /**
     * 功能描述:pc端获取游戏相关的圈子
     * @param: [memberUserPrefile, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "pc2.1", notes = "")
    @PostMapping(value = "/api/portal/community/game/circle")
    @ApiImplicitParams({})
    public FungoPageResultDto<CmmCircleDto>  selectGameCircle(@Anonymous MemberUserProfile memberUserPrefile,@Valid @RequestBody CircleGamePostVo circleGamePostVo,Errors errors) {
        if(errors.hasErrors()) return FungoPageResultDto.FungoPageResultDtoFactory.buildError( errors.getAllErrors().get(0).getDefaultMessage());
        FungoPageResultDto<CmmCircleDto>  re = null;
        String memberId = memberUserPrefile == null ? "" : memberUserPrefile.getLoginId();
        try {
            re = circleServiceImpl.selectGameCircle (memberId,circleGamePostVo);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("app端获取游戏相关的圈子",e);
            re = FungoPageResultDto.error("-1","app端获取游戏相关的圈子");
        }
        return re;
    }

    /**
     * 功能描述: app端获取圈子列表列表及详情
     * @param: [memberUserPrefile, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "pc2.1", notes = "")
    @GetMapping(value = "/api/portal/community/circle/{circleId}")
    @ApiImplicitParams({})
    public ResultDto<CmmCircleDto> selectCircle(@Anonymous MemberUserProfile memberUserPrefile, @PathVariable("circleId")  String circleId) {
        if(CommonUtil.isNull(circleId) || circleId.length() != 32) return  ResultDto.ResultDtoFactory.buildError( "圈子id不符合规范" );

        ResultDto<CmmCircleDto> re = null;
        String memberId = memberUserPrefile == null ? "" : memberUserPrefile.getLoginId();
        try {
            re = circleServiceImpl.selectCircleById(memberId,circleId);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("获取活动列表异常",e);
            re = ResultDto.error(CommonEnum.ERROR.code(),"获取圈子列表异常，请联系管理员");
        }
        return re;
    }

    /**
     * 功能描述: app端获取圈子下属的文章
     * @param: [memberUserPrefile, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "pc2.1", notes = "")
    @PostMapping(value = "/api/portal/community/circle/post")
    @ApiImplicitParams({})
    public FungoPageResultDto<PostOutBean> selectCirclePost(@Anonymous MemberUserProfile memberUserPrefile ,@Valid @RequestBody CmmCirclePostVo cmmCirclePostVo,Errors errors) {
        if(errors.hasErrors()) return FungoPageResultDto.FungoPageResultDtoFactory.buildError( errors.getAllErrors().get(0).getDefaultMessage() );
        FungoPageResultDto<PostOutBean> re = null;
        String memberId = memberUserPrefile == null ? "" : memberUserPrefile.getLoginId();
        try {
            re = circleServiceImpl.selectCirclePost(memberId,cmmCirclePostVo);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("app端获取圈子下属的文章",e);
            re = FungoPageResultDto.error(CommonEnum.ERROR.code(),"app端获取圈子下属的文章，请联系管理员");
        }
        return re;
    }

    /**
     * 功能描述: app端获取圈子文章是否分类
     * @param: [memberUserPrefile, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "pc2.1", notes = "")
    @PostMapping(value = "/api/portal/community/circle/post/type")
    @ApiImplicitParams({})
    public ResultDto<List<CirclePostTypeDto>> selectCirclePostType(@Anonymous MemberUserProfile memberUserPrefile , @RequestBody CmmCirclePostVo cmmCirclePostVo) {
        if(CommonUtil.isNull( cmmCirclePostVo.getCircleId())) return ResultDto.ResultDtoFactory.buildError( "圈子id不能为空" );
        ResultDto<List<CirclePostTypeDto>> re = null;
        String memberId = memberUserPrefile == null ? "" : memberUserPrefile.getLoginId();
        try {
            re =  circleServiceImpl.selectCirclePostType(memberId,cmmCirclePostVo);
            if(re != null && re.getData() != null && !re.getData().isEmpty()){
                return re;
            }
            re = ResultDto.success("暂无分类");
            re.setData(Collections.emptyList());
            return re;
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("app端获取圈子下属的文章",e);
            re = ResultDto.error(CommonEnum.ERROR.code(),"app端获取圈子下属的文章，请联系管理员");
        }
        return re;
    }




    /**
     * 功能描述: PC端获取圈子列表列表及详情
     * @param: [memberUserPrefile, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "pc2.1", notes = "")
    @PostMapping(value = "/api/portal/community/community/circle")
    @ApiImplicitParams({})
    public FungoPageResultDto<CmmCircleDto> circleEventList(@Anonymous MemberUserProfile memberUserPrefile,  @RequestBody CmmCircleVo cmmCircleVo) {
        FungoPageResultDto<CmmCircleDto> re = null;
        String memberId = memberUserPrefile == null ? "" : memberUserPrefile.getLoginId();
        try {
            cmmCircleVo.setQueryType("0");
            re = circleServiceImpl.selectCircle(memberId,cmmCircleVo);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("获取圈子列表列表及详情",e);
            re = FungoPageResultDto.FungoPageResultDtoFactory.buildError("获取圈子列表异常，请联系管理员");
        }
        return re;
    }


}
