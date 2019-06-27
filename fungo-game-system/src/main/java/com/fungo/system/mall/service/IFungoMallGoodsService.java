package com.fungo.system.mall.service;


import com.game.common.dto.ResultDto;
import com.game.common.dto.mall.MallGoodsInput;

import java.util.Map;

/**
 * <p>
 *    fungo商城-商品业务层
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public interface IFungoMallGoodsService {


    /**
     * 添加商品
     * @return
     */
    public boolean addGoodsAndSeckill(Map<String, Object> param);


    /**
     * 获取某个游戏的游戏礼包数量
     * @param mallGoodsInput
     * @return
     */
    public ResultDto<Map<String,Object>> queryGoodsCountWithGame(MallGoodsInput mallGoodsInput);



}
