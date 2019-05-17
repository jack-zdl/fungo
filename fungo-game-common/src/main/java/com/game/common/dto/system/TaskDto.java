package com.game.common.dto.system;

public class TaskDto {
    /**
     * 本次任务的唯一请求标识
     */
    private String requestId;
    private String mbId;
    private Integer taskGroupFlag;
    private Integer taskType;
    private Integer typeCodeIdt;

    private String targetId;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

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
