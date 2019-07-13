package com.fungo.community.controller;

import com.fungo.community.config.NacosFungoCircleConfig;
import com.fungo.community.job.CircleHotValueJob;
import com.fungo.community.service.CircleService;
import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.*;
import com.game.common.dto.index.CardIndexBean;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/11
 */
@RestController
@RefreshScope
@Api(value = "", description = "圈子首页")
public class CircleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CircleController.class);

    @Autowired
    private CircleService circleServiceImpl;

    @Autowired
    private CircleHotValueJob circleHotValueJob;

    /**
     * 功能描述: app端获取圈子列表列表及详情
     * @param: [memberUserPrefile, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "v2.5", notes = "")
    @PostMapping(value = "/api/community/circle")
    @ApiImplicitParams({})
    public FungoPageResultDto<CmmCircleDto> circleEventList(@Anonymous MemberUserProfile memberUserPrefile,  @RequestBody CmmCircleVo cmmCircleVo) {
        FungoPageResultDto<CmmCircleDto> re = null;
        String memberId = memberUserPrefile == null ? "" : memberUserPrefile.getLoginId();
       try {
           re = circleServiceImpl.selectCircle(memberId,cmmCircleVo);
       }catch (Exception e){
           e.printStackTrace();
           LOGGER.error("获取圈子列表列表及详情",e);
            re = FungoPageResultDto.FungoPageResultDtoFactory.buildWarning(CommonEnum.ERROR.code(),"获取圈子列表异常，请联系管理员");
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
    @ApiOperation(value = "v2.5", notes = "")
    @GetMapping(value = "/api/community/circle/{circleId}")
    @ApiImplicitParams({})
    public ResultDto<CmmCircleDto> selectCircle(@Anonymous MemberUserProfile memberUserPrefile, @PathVariable("circleId") String circleId) {
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
     * 功能描述: app端获取圈子分类
     * @param: [memberUserPrefile, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "v2.5", notes = "")
    @GetMapping(value = "/api/community/circle/type")
    @ApiImplicitParams({})
    public ResultDto<List<CircleTypeDto>> selectCircleType(@Anonymous MemberUserProfile memberUserPrefile ) {
        ResultDto<List<CircleTypeDto>> re = null;
        String memberId = memberUserPrefile == null ? "" : memberUserPrefile.getLoginId();
        try {
            re = circleServiceImpl.selectCircleType(memberId);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("app端获取圈子下属的文章",e);
            re = ResultDto.error(CommonEnum.ERROR.code(),"app端获取圈子下属的文章，请联系管理员");
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
    @ApiOperation(value = "v2.5", notes = "")
    @PostMapping(value = "/api/community/circle/post/type")
    @ApiImplicitParams({})
    public ResultDto<List<CirclePostTypeDto>> selectCirclePostType(@Anonymous MemberUserProfile memberUserPrefile , @RequestBody CmmCirclePostVo cmmCirclePostVo) {
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
     * 功能描述: app端获取圈子下属的文章
     * @param: [memberUserPrefile, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "v2.5", notes = "")
    @PostMapping(value = "/api/community/circle/post")
    @ApiImplicitParams({})
    public FungoPageResultDto<PostOutBean> selectCirclePost(@Anonymous MemberUserProfile memberUserPrefile , @RequestBody CmmCirclePostVo cmmCirclePostVo) {
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
     * 功能描述: app端获取圈子文章顺序类型
     * @param: [memberUserPrefile, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "v2.5", notes = "")
    @GetMapping(value = "/api/community/circle/post/sort")
    @ApiImplicitParams({})
    public ResultDto<List<CirclePostSortDto>> selectCirclePostSort(@Anonymous MemberUserProfile memberUserPrefile ) {
        List<CirclePostSortDto>  circlePostSortDtos = new ArrayList<>();
        circlePostSortDtos.add(new CirclePostSortDto(CmmCirclePostVo.SortTypeEnum.PUBDATE.getKey(), CmmCirclePostVo.SortTypeEnum.PUBDATE.getValue()));
        circlePostSortDtos.add(new CirclePostSortDto(CmmCirclePostVo.SortTypeEnum.PUBREPLY.getKey(), CmmCirclePostVo.SortTypeEnum.PUBREPLY.getValue()));
        circlePostSortDtos.add(new CirclePostSortDto(CmmCirclePostVo.SortTypeEnum.ESSENCE.getKey(), CmmCirclePostVo.SortTypeEnum.ESSENCE.getValue()));
        circlePostSortDtos.add(new CirclePostSortDto(CmmCirclePostVo.SortTypeEnum.DISCUSS.getKey(), CmmCirclePostVo.SortTypeEnum.DISCUSS.getValue()));
        return ResultDto.success(circlePostSortDtos);
    }


    /**
     * 功能描述: app端该游戏是否开通圈子
     * @param: [memberUserPrefile, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "v2.5", notes = "")
    @GetMapping(value = "/api/community/circle/game/{gameId}")
    @ApiImplicitParams({})
    public ResultDto<CmmCircleDto> selectCircleGame(@Anonymous MemberUserProfile memberUserPrefile,@PathVariable("gameId") String gameId ) {
        ResultDto<CmmCircleDto> re = null;
        String memberId = memberUserPrefile == null ? "" : memberUserPrefile.getLoginId();
        try {
            CircleGamePostVo circleGamePostVo = new CircleGamePostVo(CircleGamePostVo.CircleGamePostTypeEnum.GAMESID.getKey(),gameId,"");
            re = circleServiceImpl.selectCircleByGame(memberId,circleGamePostVo);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("该游戏是否开通圈子",e);
            re = ResultDto.error("-1","该游戏是否开通圈子");
        }
        return re;
    }


    /**
     * 功能描述: app端获取游戏圈子的文章
     * @param: [memberUserPrefile, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "v2.5", notes = "")
    @PostMapping(value = "/api/community/circle/game/post")
    @ApiImplicitParams({})
    public FungoPageResultDto<PostOutBean>  selectCircleGamePost(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody CircleGamePostVo circleGamePostVo) {
        FungoPageResultDto<PostOutBean>  re = null;
        String memberId = memberUserPrefile == null ? "" : memberUserPrefile.getLoginId();
        try {
            re = circleServiceImpl.selectCircleGamePost(memberId,circleGamePostVo);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("app端获取游戏圈子的文章",e);
            re = FungoPageResultDto.error("-1","app端获取游戏圈子的文章");
        }
        return re;
    }

    /**
     * 功能描述:app端获取游戏相关的圈子
     * @param: [memberUserPrefile, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "v2.5", notes = "")
    @PostMapping(value = "/api/community/game/circle")
    @ApiImplicitParams({})
    public FungoPageResultDto<CmmCircleDto>  selectGameCircle(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody CircleGamePostVo circleGamePostVo) {
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
     * 功能描述:app端获取圈子的所有玩家榜
     * @param: [memberUserPrefile, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "v2.5", notes = "")
    @PostMapping(value = "/api/community/circle/player")
    @ApiImplicitParams({})
    public FungoPageResultDto<CommunityMember>  selectCirclePlayer(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody CmmCirclePostVo cmmCirclePostVo) {
        FungoPageResultDto<CommunityMember>  re = null;
        String memberId = memberUserPrefile == null ? "" : memberUserPrefile.getLoginId();
        try {
            re = circleServiceImpl.selectCirclePlayer(memberId,cmmCirclePostVo);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("app端获取圈子的所有玩家榜",e);
            re = FungoPageResultDto.error("-1","app端获取圈子的所有玩家榜异常");
        }
        return re;
    }


    /**
     * 功能描述: app端获取圈子文章顺序类型
     * @param: [memberUserPrefile, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "v2.5", notes = "")
    @GetMapping(value = "/api/community/circle/hotvalue/job")
    @ApiImplicitParams({})
    public ResultDto<String> updateCircleHotValueJob( ) {
        try {
            circleHotValueJob.execute();
        } catch (Exception e) {
            return ResultDto.error("-1","圈子热度失败");
        }
        return ResultDto.success();
    }

}
