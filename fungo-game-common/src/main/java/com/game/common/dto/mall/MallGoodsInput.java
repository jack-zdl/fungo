package com.game.common.dto.mall;

import java.io.Serializable;


/**
 * <p>
 *  查询商品数据入参封装Bean
 * </p>
 *
 * @author mxf
 * @since 2019-01-14
 */
public class MallGoodsInput implements Serializable {


    /**
     * 商品类型:<br />
     * 1 商城实物商品 默认 ;<br/>
     * 2 商城虚拟商品 默认 ;<br/>
     * 3 游戏礼包商品
     */
    private int goods_type;


    public int getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(int goods_type) {
        this.goods_type = goods_type;
    }
}
