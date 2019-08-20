package com.game.common.buriedpoint.model;

import com.game.common.buriedpoint.enums.BtnEnum;
import com.game.common.buriedpoint.util.map.annotation.CatchAllProperty;

/**
 *  埋点实体 - 签到按钮埋点
 *  属于客户端埋点 - 暂由后端处理
 */
public class BuriedPointSignBtnModel extends BuriedPointModel{

    @CatchAllProperty
    private  BtnEnum btnEnum;

    public BtnEnum getBtnEnum() {
        return btnEnum;
    }

    public void setBtnEnum(BtnEnum btnEnum) {
        this.btnEnum = btnEnum;
    }
}
