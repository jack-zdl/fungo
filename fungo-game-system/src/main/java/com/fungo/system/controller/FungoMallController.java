package com.fungo.system.controller;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.fungo.system.dto.FungoMallDto;
import com.fungo.system.mall.entity.MallGoods;
import com.fungo.system.mall.service.IFungoMallGoodsService;
import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.mall.MallGoodsOutBean;
import com.game.common.enums.AbstractResultEnum;
import com.game.common.enums.CommonEnum;
import com.game.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>添加商品</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/7/18
 */
@RestController
@RequestMapping("/api/system/")
public class FungoMallController {


    @Autowired
    private IFungoMallGoodsService iFungoMallGoodsService;

    @PostMapping("/mall/addGoods")
    public ResultDto<Object> addGoods(@RequestBody FungoMallDto fungoMallDto){
        Object isOk = iFungoMallGoodsService.addGoods(fungoMallDto);
        if(isOk != null){
            return ResultDto.success(isOk);
        }
        return ResultDto.error("-1", "添加商品数据失败");
    }



    /**
     * 功能描述:
     * <p>为中秋礼物添加一次性秒杀量，与礼包乐园（日余量）相比。他只有一个总量</p>
     * @Param
     * @return: com.game.common.dto.ResultDto<java.lang.Object>
     * @auther: dl.zhang
     * @date: 2019/8/20 19:46
     */
    @GetMapping("/mall/addseckill")
    public ResultDto<String> addSeckill(String goodId,String stocks,String start,String end){
        ResultDto<String> isOk = iFungoMallGoodsService.addSeckill(goodId,stocks,start,end);
        if(CommonEnum.SUCCESS.code().equals(String.valueOf(isOk.getStatus()))){
            return isOk;
        }
        return ResultDto.error("-1", "添加商品秒杀数据失败");
    }


    /**
     * 功能描述:
     * <p>查询中秋节日礼品</p>
     * @Param
     * @return: com.game.common.dto.ResultDto<java.lang.Object>
     * @auther: dl.zhang
     * @date: 2019/8/20 19:46
     */
    @PostMapping("/mall/festival")
    public FungoPageResultDto<MallGoodsOutBean> getFestivalMall(@RequestBody InputPageDto inputPageDto){
        FungoPageResultDto<MallGoodsOutBean> isOk = iFungoMallGoodsService.getFestivalMall(inputPageDto);
        if(CommonEnum.SUCCESS.code().equals(String.valueOf(isOk.getStatus()))){
            return isOk;
        }
        return FungoPageResultDto.error("-1", "查询中秋节日礼品失败");
    }

    /**
     * 功能描述:
     * <p>中秋节日抽奖</p>
     * @Param
     * @return: com.game.common.dto.ResultDto<java.lang.Object>
     * @auther: dl.zhang
     * @date: 2019/8/20 19:46
     */
    @PostMapping("/mall/draw")
    public FungoPageResultDto<MallGoodsOutBean> drawFestivalMall(MemberUserProfile memberUserPrefile,@RequestBody InputPageDto inputPageDto){
        String memberId = memberUserPrefile.getLoginId();
        if(CommonUtil.isNull(memberId)){
            return FungoPageResultDto.FungoPageResultDtoFactory.buildError( AbstractResultEnum.CODE_SYSTEM_FIVE.getFailevalue());
        }
        FungoPageResultDto<MallGoodsOutBean> isOk = iFungoMallGoodsService.drawFestivalMall(memberId,inputPageDto);
        if(CommonEnum.SUCCESS.code().equals(String.valueOf(isOk.getStatus()))){
            return isOk;
        }
        return FungoPageResultDto.error("-1", "抽取中秋节日礼品失败");
    }





}
