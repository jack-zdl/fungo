package com.fungo.system.dto;

import io.swagger.annotations.ApiModelProperty;

public class MemberLevelBean {
	
	@ApiModelProperty(value="当前等级",example="")
	private int level;
	@ApiModelProperty(value="下一等级",example="")
	private int nextLevel;
	@ApiModelProperty(value="当前经验",example="")
	private int currentExp;
	@ApiModelProperty(value="还须获得多少经验可以升级",example="")
	private int upgradeExp;
	@ApiModelProperty(value="下一等级所需经验",example="")
	private int nextLevelExp;
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getNextLevel() {
		return nextLevel;
	}
	public void setNextLevel(int nextLevel) {
		this.nextLevel = nextLevel;
	}
	public int getCurrentExp() {
		return currentExp;
	}
	public void setCurrentExp(int currentExp) {
		this.currentExp = currentExp;
	}
	public int getUpgradeExp() {
		return upgradeExp;
	}
	public void setUpgradeExp(int upgradeExp) {
		this.upgradeExp = upgradeExp;
	}
	public int getNextLevelExp() {
		return nextLevelExp;
	}
	public void setNextLevelExp(int nextLevelExp) {
		this.nextLevelExp = nextLevelExp;
	}
	
}
