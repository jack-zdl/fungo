package com.fungo.system.mall.service;


import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.mall.MallGoodsInput;
import com.game.common.dto.mall.MallGoodsOutBean;
import com.game.common.dto.mall.MallOrderInput;
import com.game.common.dto.mall.MallOrderOutBean;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *    fungo商城-秒杀业务层
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public interface IFungoMallSeckillService {


    /**
     * 查询秒杀活动的商品
     * @param mb_id 用户信息
     * @return 返回商品对象集合
     */
    List<MallGoodsOutBean> getGoodsListForSeckill(String mb_id, String realIp);


    /**
     * 查询秒杀活动的商品
     * @param mb_id 用户信息
     * @return 返回商品对象集合
     */
    List<MallGoodsOutBean> getGoodsListForGame(String mb_id, String realIp, MallGoodsInput mallGoodsInput);


    /**
     * 查询秒杀活动的商品
     * @param mb_id 用户信息
     * @return  返回商品集合json字符串
     */
    String getGoodsListForSeckillJson(String mb_id, String realIp);



    /**
     *  获取秒杀授权码
     * @param mb_id
     * @return
     */
    String getSeckillGrantCode(String mb_id);


    /**
     * 秒杀下单
     * @param orderInput
     * @return
     */
    Map<String,Object> createOrderWithSeckill(MallOrderInput orderInput, String realIp);

    /**
     * 秒杀下单-游戏礼包
     * @param orderInput
     * @return
     */
    Map<String,Object> createOrderWithSeckillWithGame(MallOrderInput orderInput, String realIp);

    /**
     * 秒杀成功，修改订单
     * 修改收货人信息
     *  手机
     *  地址
     *  姓名
     * @param mallOrderInput
     * @return
     */
    boolean updateOrderWithSeckill(MallOrderInput mallOrderInput);


    /**
     * 查询秒杀成功的订单
     * @param mb_id 用户ID
     * @param  orderId 订单ID
     * @param  orderSn 订单编号
     * @return 返回订单数据对象
     */
    List<MallOrderOutBean> getOrdersWithSeckillGame(String mb_id, String orderId, String orderSn, String orderType);



    /**
     * 查询秒杀成功的订单
     * @param mb_id 用户ID
     * @param  orderId 订单ID
     * @param  orderSn 订单编号
     * @return 返回订单数据json字符串
     */
    String getOrdersWithSeckillWithJsonStr(String mb_id, String orderId, String orderSn);


    /**
     * 查询用户游戏礼包订单接口
     * @param mb_id
     * @param param
     * @return
     */
    FungoPageResultDto<Map<String, Object>> queryMemberGameOrderList(String mb_id , Map<String, Object> param);


    void insertMallSeckillOrder(String goodId,String number);

    ResultDto<String> browseMall(String memberId);
}
