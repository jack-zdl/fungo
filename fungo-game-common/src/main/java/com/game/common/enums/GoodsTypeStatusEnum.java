package com.game.common.enums;

/**
 * <p>商品类型枚举类</p>
 * @Author: dl.zhang
 * @Date: 2019/4/2
 */
public enum GoodsTypeStatusEnum implements BaseEnum<GoodsTypeStatusEnum,String>{
    ENTITY("1","实物"),
    VIRTUALZEROCARD ("21","虚拟"),
    VIRTUALJINGDONGCARD("22","虚拟"),
    VIRTUALQBCARD("23","虚拟");

    String key;
    String value;

    GoodsTypeStatusEnum(String s, String s1) {
        this.key = s;
        this.value = s1;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
