package com.fungo.community.controller;

import com.fungo.community.service.CircleService;
import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CirclePostSortDto;
import com.game.common.dto.community.CircleTypeDto;
import com.game.common.dto.community.CmmCircleDto;
import com.game.common.dto.community.PostOutBean;
import com.game.common.dto.index.CardIndexBean;
import com.game.common.enums.CommonEnum;
import com.game.common.util.annotation.Anonymous;
import com.game.common.vo.CmmCirclePostVo;
import com.game.common.vo.CmmCircleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/11
 */
@RestController
@Api(value = "", description = "圈子首页")
public class CircleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CircleController.class);

    @Autowired
    private CircleService circleServiceImpl;

    /**
     * 功能描述: app端获取圈子列表列表及详情
     * @param: [memberUserPrefile, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "v2.5", notes = "")
    @RequestMapping(value = "/api/community/circle", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<CmmCircleDto> circleEventList(@Anonymous MemberUserProfile memberUserPrefile,  @RequestBody CmmCircleVo cmmCircleVo) {
        FungoPageResultDto<CmmCircleDto> re = null;
        String memberId = memberUserPrefile == null ? "" : memberUserPrefile.getLoginId();
       try {
           re = circleServiceImpl.selectCircle(memberId,cmmCircleVo);
       }catch (Exception e){
           e.printStackTrace();
           LOGGER.error("获取活动列表异常",e);
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
    @RequestMapping(value = "/api/community/circle/{circleId}", method = RequestMethod.POST)
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
     * 功能描述: app端获取圈子文章是否分类
     * @param: [memberUserPrefile, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "v2.5", notes = "")
    @RequestMapping(value = "/api/community/circle/post/type", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<List<CircleTypeDto>> selectCirclePostType(@Anonymous MemberUserProfile memberUserPrefile , @RequestBody CmmCirclePostVo cmmCirclePostVo) {
        ResultDto<List<CircleTypeDto>> re = null;
        String memberId = memberUserPrefile == null ? "" : memberUserPrefile.getLoginId();
        try {
            re = circleServiceImpl.selectCirclePostType(memberId,cmmCirclePostVo);
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
    @RequestMapping(value = "/api/community/circle/post", method = RequestMethod.POST)
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
    @RequestMapping(value = "/api/community/circle/post/sort", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<List<CirclePostSortDto>> selectCirclePostSort(@Anonymous MemberUserProfile memberUserPrefile ) {
        List<CirclePostSortDto>  circlePostSortDtos = new ArrayList<>();
        circlePostSortDtos.add(new CirclePostSortDto(CmmCirclePostVo.SortTypeEnum.PUBDATE.getKey(), CmmCirclePostVo.SortTypeEnum.PUBDATE.getValue()));
        circlePostSortDtos.add(new CirclePostSortDto(CmmCirclePostVo.SortTypeEnum.PUBREPLY.getKey(), CmmCirclePostVo.SortTypeEnum.PUBREPLY.getValue()));
        circlePostSortDtos.add(new CirclePostSortDto(CmmCirclePostVo.SortTypeEnum.ESSENCE.getKey(), CmmCirclePostVo.SortTypeEnum.ESSENCE.getValue()));
        circlePostSortDtos.add(new CirclePostSortDto(CmmCirclePostVo.SortTypeEnum.DISCUSS.getKey(), CmmCirclePostVo.SortTypeEnum.DISCUSS.getValue()));
        return ResultDto.success(circlePostSortDtos);
    }
}
