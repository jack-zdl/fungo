package com.game.common.buriedpoint.enums;

import com.game.common.buriedpoint.util.map.annotation.MapKeyMapping;

/**
 *  按钮事件- 埋点属性枚举
 */
public enum BtnEnum {
    SIGN("签到页","签到"),
    GIFTBAG("我的_礼包乐园页","立即兑换");

    @MapKeyMapping("page_name")
    private String pageName;

    @MapKeyMapping("btn_name")
    private String btnName ;

    BtnEnum(String pageName, String btnName) {
        this.pageName = pageName;
        this.btnName = btnName;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getBtnName() {
        return btnName;
    }

    public void setBtnName(String btnName) {
        this.btnName = btnName;
    }

    }
