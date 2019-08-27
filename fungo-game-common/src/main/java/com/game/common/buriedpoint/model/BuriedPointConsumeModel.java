package com.game.common.buriedpoint.model;

/**
 *  埋点 - 消耗fun币埋点
 */
public class BuriedPointConsumeModel extends BuriedPointModel{
    /**
     *  消耗fun币数量
     */
    private Long amount;

    /**
     *  消耗方式
     */
    private String method;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
