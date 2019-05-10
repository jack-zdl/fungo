package com.fungo.system.mall.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.system.mall.dto.GoodsDto;
import com.fungo.system.mall.dto.GoodsValiDto;
import com.fungo.system.mall.entity.MallOrder;

import java.util.List;
import java.util.Map;




/**
 * <p>
  * 订单表 Mapper 接口
 * </p>
 *
 * @author mxf
 * @since 2019-01-14
 */

public interface MallOrderDao extends BaseMapper<MallOrder> {

    List<GoodsDto> search(GoodsValiDto goodsValiDto);


    List<Map<String,String>> selectGoodsName();

    List<Map<String, Object>> selectTotal();

    List<Map<String,Object>> selectVirtualTotal();
}