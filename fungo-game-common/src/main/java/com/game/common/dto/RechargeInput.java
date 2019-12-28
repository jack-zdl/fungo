package com.game.common.dto;

/**
 *  充值任务参数接收实体
 */
public class RechargeInput {
    private String orderId;

    /**
     * 充值类型，1-月卡 2-季卡 3-年卡
     */
    private Integer rechargeType;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(Integer rechargeType) {
        this.rechargeType = rechargeType;
    }
}
