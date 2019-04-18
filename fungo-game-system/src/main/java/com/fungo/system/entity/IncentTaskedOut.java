package com.fungo.system.entity;


import java.io.Serializable;

/**
 * <p>
 *      用户权益-任务完成汇总数据封装
 * </p>
 *
 *
 *
 * @author mxf
 * @since 2018-12-03
 */
public class IncentTaskedOut implements Serializable {


    /**
     * 任务ID
     */
    private String taskId;



    /**
     * 任务名称
     */
    private String taskName;


    /**
     * 执行获取的分值
     */
    private String score;



    /**
     * 任务类型
     */
    private int taskType;


    /**
     * 已完成任务数量
     */
    private int taskedCount;

    /**
     * 任务完成时间
     */
    private String doneDate;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public int getTaskedCount() {
        return taskedCount;
    }

    public void setTaskedCount(int taskedCount) {
        this.taskedCount = taskedCount;
    }

    public String getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(String doneDate) {
        this.doneDate = doneDate;
    }

    @Override
    public String toString() {
        return "IncentTaskedOut{" +
                "taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", score='" + score + '\'' +
                ", taskType=" + taskType +
                ", taskedCount=" + taskedCount +
                ", doneDate='" + doneDate + '\'' +
                '}';
    }
}
