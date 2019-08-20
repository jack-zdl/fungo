package com.game.common.buriedpoint.model;

import com.game.common.buriedpoint.util.map.annotation.MapKeyMapping;

/**
 *  埋点实体 - 签到埋点
 */
public class BuriedPointSignModel extends BuriedPointModel{
    /**
     *  签到获取的 funbi数
     */
    @MapKeyMapping("quest_gold")
    private Integer questGold;
    /**
     *  连续签到天数
     */
    @MapKeyMapping("serial_days")
    private Integer serialDays;

    public Integer getQuestGold() {
        return questGold;
    }

    public void setQuestGold(Integer questGold) {
        this.questGold = questGold;
    }

    public Integer getSerialDays() {
        return serialDays;
    }

    public void setSerialDays(Integer serialDays) {
        this.serialDays = serialDays;
    }
}
