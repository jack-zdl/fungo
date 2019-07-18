package com.fungo.system.controller;

import com.fungo.system.dto.FungoMallDto;
import com.fungo.system.mall.service.IFungoMallGoodsService;
import com.game.common.dto.ResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResultDto<String> addGoods(@RequestBody FungoMallDto fungoMallDto){
            iFungoMallGoodsService.addGoods(fungoMallDto);
            return ResultDto.success();
    }
}
