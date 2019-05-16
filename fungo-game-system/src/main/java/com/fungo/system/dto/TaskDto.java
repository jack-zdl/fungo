package com.fungo.system.dto;

public class TaskDto {
    private String mbId;
    Integer taskGroupFlag;
    Integer taskType;
    Integer typeCodeIdt;

    String targetId;

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getMbId() {
        return mbId;
    }

    public void setMbId(String mbId) {
        this.mbId = mbId;
    }

    public Integer getTaskGroupFlag() {
        return taskGroupFlag;
    }

    public void setTaskGroupFlag(Integer taskGroupFlag) {
        this.taskGroupFlag = taskGroupFlag;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Integer getTypeCodeIdt() {
        return typeCodeIdt;
    }

    public void setTypeCodeIdt(Integer typeCodeIdt) {
        this.typeCodeIdt = typeCodeIdt;
    }
}
