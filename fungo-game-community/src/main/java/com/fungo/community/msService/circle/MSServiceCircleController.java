package com.fungo.community.msService.circle;

import com.fungo.community.service.CircleService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CirclePostTypeDto;
import com.game.common.dto.community.CmmCircleDto;
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
            FungoPageResultDto<CmmCircleDto> resultDto = circleServiceImpl.selectGameCircle ("",circleGamePostVo);
            if(resultDto.getData().size()>0){
                cmmCircleDto =resultDto.getData().get(0);
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

}
