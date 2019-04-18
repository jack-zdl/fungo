package com.game.common.mall.mapper;

import com.game.common.mall.entity.MallSeckill;
import com.baomidou.mybatisplus.mapper.BaseMapper;
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
     * @return
     */
    public List<Map<String,Object>> querySeckillGoods(@Param("startTime") String startTime,@Param("endTime") String endTime);


}