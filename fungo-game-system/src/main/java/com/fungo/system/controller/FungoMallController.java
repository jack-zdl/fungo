package com.fungo.system.controller;

import com.fungo.system.dto.FungoMallDto;
import com.fungo.system.mall.entity.MallGoods;
import com.fungo.system.mall.service.IFungoMallGoodsService;
import com.game.common.dto.ResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
