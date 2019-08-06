package com.game.common.buriedpoint.model;

/**
 * 埋点数据基本属性实体
 */
public class BuriedPointModel {
    /**
     *  当前触发统计埋点的用户id
     */
    private String distinctId;

    /**
     * 平台
     */
    private String platForm;

    /**
     * 埋点事件名称
     */
    private String eventName;

    public String getDistinctId() {
        return distinctId;
    }

    public void setDistinctId(String distinctId) {
        this.distinctId = distinctId;
    }

    public String getPlatForm() {
        return platForm;
    }

    public void setPlatForm(String platForm) {
        this.platForm = platForm;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
