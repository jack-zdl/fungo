package com.fungo.system.mall.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.system.mall.entity.MallSeckill;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 商城秒杀信息表 Mapper 接口
 * </p>
 *
 * @author mxf
 * @since 2019-01-14
 */
public interface MallSeckillDao extends BaseMapper<MallSeckill> {



    /**
     * 查询秒杀的商品
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param goods_type 商品类型
     * @param goods_status 商品状态
     * @param gameId 游戏Id
     * @return
     */
    public List<Map<String,Object>> querySeckillGoods(@Param("startTime") String startTime, @Param("endTime") String endTime,
                                                      @Param("goods_types") List<String> goods_types,@Param("goods_status") int goods_status);


    /**
     * 查询秒杀的商品
     * @param  goodId 商品id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public MallSeckill querySeckillGoodsByGoodId(@Param("goodId") String goodId,@Param("startTime") String startTime, @Param("endTime") String endTime);





}