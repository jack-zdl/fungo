package com.game.common.buriedpoint.model;

/**
 *  埋点 - 产生fun币埋点
 */
public class BuriedPointProductModel extends BuriedPointModel{
    /**
     *  产生fun币数量
     */
    private Integer amount;

    /**
     *  产生方式
     */
    private String method;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
