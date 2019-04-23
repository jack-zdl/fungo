package com.fungo.system.dto;

import io.swagger.annotations.ApiModelProperty;

public class ScoreBean {

	@ApiModelProperty(value="用户行为",example="")
	private String action;
	@ApiModelProperty(value="积分变化",example="")
	private String scoreChange;
	@ApiModelProperty(value="发生时间",example="")
	private String changeTime;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getScoreChange() {
		return scoreChange;
	}

	public void setScoreChange(String scoreChange) {
		this.scoreChange = scoreChange;
	}

	public String getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(String changeTime) {
		this.changeTime = changeTime;
	}
	
	
	
	
}
