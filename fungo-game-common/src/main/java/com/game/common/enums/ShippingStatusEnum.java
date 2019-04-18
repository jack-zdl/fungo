package com.game.common.enums;

/**
 * <p>商品配送情况枚举类</p>
 * @Author: dl.zhang
 * @Date: 2019/4/2
 */
public enum ShippingStatusEnum implements BaseEnum<ShippingStatusEnum,String> {
    NOTSEND("-1","未发货"),
    SEND("1","已发货"),
    RECEIVE("2","已收货"),
    STOCKUPING ("3","备货中"),
    NOTRECEIVEINFO("4","无收货信息"),
    RECEIVEINFO("5","有收货信息");

    String key;
    String value;

    ShippingStatusEnum(String s, String s1) {
        this.key = s;
        this.value = s1;
    }

    @Override
    public String  getKey() {
        return this.key;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
