package com.game.common.buriedpoint.model;

import com.game.common.buriedpoint.util.map.annotation.MapKeyMapping;

/**
 *  埋点 - 任务实体
 */
public class BuriedPointTaskModel extends BuriedPointModel{

    /**
     * 任务ID
     */
    @MapKeyMapping("quest_id")
    private String questId;
    /**
     * 任务名称
     */
    @MapKeyMapping("quest_name")
    private String questName;
    /**
     * 任务奖励经验
     */
    @MapKeyMapping("quest_exp")
    private Integer questExp;
/*    *//**
     * 任务奖励金币
     *//*
    @MapKeyMapping("quest_gold")
    private int questGold;*/
    /**
     * 任务一级分类 新手任务/每日任务/每周任务/竞品任务
     */
    @MapKeyMapping("first_category")
    private String firstCategory;
    /**
     * 任务二级分类
     */
    @MapKeyMapping("second_category")
    private String secondCategory;
    /**
     * 是否是当前任务类型的最后一个任务
     */
    @MapKeyMapping("is_final_quest")
    private boolean finalQuest;

    public String getQuestId() {
        return questId;
    }

    public void setQuestId(String questId) {
        this.questId = questId;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public Integer getQuestExp() {
        return questExp;
    }

    public void setQuestExp(Integer questExp) {
        this.questExp = questExp;
    }


    public String getFirstCategory() {
        return firstCategory;
    }

    public void setFirstCategory(String firstCategory) {
        this.firstCategory = firstCategory;
    }

    public String getSecondCategory() {
        return secondCategory;
    }

    public void setSecondCategory(String secondCategory) {
        this.secondCategory = secondCategory;
    }

    public boolean isFinalQuest() {
        return finalQuest;
    }

    public void setFinalQuest(boolean finalQuest) {
        this.finalQuest = finalQuest;
    }
}
