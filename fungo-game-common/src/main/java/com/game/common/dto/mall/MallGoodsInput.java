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
    private int goodsType;

    /**
     * 游戏id
     */
    private String gameId;

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
