package com.game.common.mall.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.game.common.mall.entity.MallOrderGoods;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
  * 订单的商品信息表 Mapper 接口
 * </p>
 *
 * @author mxf
 * @since 2019-01-14
 */
public interface MallOrderGoodsDao extends BaseMapper<MallOrderGoods> {

    /**
     * 查询秒杀成功的商品数量
     * @param created_at
     * @param goods_id
     * @return
     */
    public Map querySeckillGoodsTradeCount(@Param("created_at") String created_at, @Param("goods_id") String goods_id);

    /**
     * 查询秒杀成功的商品fungo币总数
     * @param created_at
     * @param goods_id
     * @return
     */
    public Map querySeckillGoodsTradeFungoCoinSum(@Param("created_at") String created_at, @Param("goods_id") String goods_id);


    /**
     * 查询秒杀成功的商品数据
     * @param created_at
     * @param goods_id
     * @return
     */
    public Map querySeckillGoodsTradeSuccess(@Param("created_at") String created_at, @Param("goods_id") String goods_id);
}