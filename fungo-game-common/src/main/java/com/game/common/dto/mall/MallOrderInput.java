package com.game.common.dto.mall;


import java.io.Serializable;

/**
 * <p>
 *    创建订单输入信息封装Bean
 * </p>
 *
 * @author mxf
 * @since 2019-01-14
 */
public class MallOrderInput implements Serializable {

    /**
     * 用户id
     */
    private String mbId;

    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 订单Id
     *
     */
    private String orderId;

    /**
     * 订单编号
     *
     */
    private String orderSn;

    /**
     * 下单授权码
     */
    private String code;


    /**
     * 收货人的姓名
     */
    private String consigneeName;


    /**
     * 收货人地址
     */
    private String csgAddress;

    /**
     * 收货人手机
     */
    private String csgMobile;

    public String getMbId() {
        return mbId;
    }

    public void setMbId(String mbId) {
        this.mbId = mbId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getCsgAddress() {
        return csgAddress;
    }

    public void setCsgAddress(String csgAddress) {
        this.csgAddress = csgAddress;
    }

    public String getCsgMobile() {
        return csgMobile;
    }

    public void setCsgMobile(String csgMobile) {
        this.csgMobile = csgMobile;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "MallOrderInput{" +
                "mbId='" + mbId + '\'' +
                ", goodsId='" + goodsId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", orderSn='" + orderSn + '\'' +
                ", code='" + code + '\'' +
                ", consigneeName='" + consigneeName + '\'' +
                ", csgAddress='" + csgAddress + '\'' +
                ", csgMobile='" + csgMobile + '\'' +
                '}';
    }
}
