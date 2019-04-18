package com.game.common.mall.mapper;

import com.game.common.dto.GoodsDto;
import com.game.common.dto.GoodsValiDto;
import com.game.common.mall.entity.MallOrder;
import com.baomidou.mybatisplus.mapper.BaseMapper;
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

    long selectVirtualTotal();
}