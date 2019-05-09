package com.fungo.system.mall.service;


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

}
