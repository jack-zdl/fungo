package com.fungo.community.msService.circle;

import com.fungo.community.service.CircleService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CirclePostTypeDto;
import com.game.common.dto.community.CmmCircleDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.enums.CommonEnum;
import com.game.common.util.annotation.Anonymous;
import com.game.common.vo.CircleGamePostVo;
import com.game.common.vo.CmmCirclePostVo;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>圈子的接口</p>
 * @Author: dl.zhang
 * @Date: 2019/6/24
 */
@RestController
public class MSServiceCircleController {

    private static Logger logger = LoggerFactory.getLogger(MSServiceCircleController.class);

    @Autowired
    private CircleService circleServiceImpl;

    @PostMapping(value = "/ms/service/cmm/circle")
    public ResultDto<String> getCircleByGame( @RequestBody CircleGamePostVo circleGamePostVo){
        ResultDto<String> re = null;
        try {
            CmmCircleDto  cmmCircleDto = new CmmCircleDto();
            ResultDto<CmmCircleDto> resultDto = circleServiceImpl.selectCircleByGame ("",circleGamePostVo);
            if(resultDto.getData() != null){
                cmmCircleDto =resultDto.getData();
            }
            re =new ResultDto();
            re.setData(cmmCircleDto.getId());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("根据游戏id查询圈子id");
            re = ResultDto.error("-1","根据游戏id查询圈子id异常");
        }
        return re;
    }

    @GetMapping(value = "/ms/service/cmm/post/listCircleNameByPost")
    public ResultDto<List<String>> listCircleNameByPost(@RequestParam("postId")String postId){
        ResultDto<List<String>> re = null;
        try {
            re = circleServiceImpl.listCircleNameByPost(postId);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("根据文章id查询圈子名称列表");
            re = ResultDto.error("-1","根据文章id查询圈子名称列表");
        }
        return re;
    }

    @GetMapping(value = "/ms/service/cmm/post/listCircleNameByComment")
    ResultDto<List<String>> listCircleNameByComment(@RequestParam("commentId")String commentId){
        ResultDto<List<String>> re = null;
        try {
            re = circleServiceImpl.listCircleNameByComment(commentId);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("根据评论id查询圈子名称列表");
            re = ResultDto.error("-1","根据评论id查询圈子名称列表");
        }
        return re;
    }


    @PostMapping(value = "/ms/service/cmm/post/getCircleByPost")
    public ResultDto<CmmCircleDto> getCircleByPost( @RequestBody CircleGamePostVo circleGamePostVo){
        ResultDto<CmmCircleDto> re = null;
        try {
            re = circleServiceImpl.selectCircleByGame ("",circleGamePostVo);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("根据游戏id查询圈子id");
            re = ResultDto.error("-1","根据游戏id查询圈子id异常");
        }
        return re;
    }


    @PostMapping(value = "/ms/service/cmm/circle/type")
    public FungoPageResultDto<CmmCircleDto> getCircleListByType( @RequestBody CircleGamePostVo circleGamePostVo){
        FungoPageResultDto<CmmCircleDto> re = null;
        return  circleServiceImpl.getCircleListByType (circleGamePostVo);
    }



}
