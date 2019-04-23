package com.game.common.dto.game;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

public class GameHistoryOut {

	private String createdAt;
	@ApiModelProperty(value="游戏版本",example="")
	private String version;
	@ApiModelProperty(value="更新日志",example="")
	private String message;
	private int checkState;
	@ApiModelProperty(value="更新备注",example="")
	private List<String> remark = new ArrayList<>();
	
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getRemark() {
		return remark;
	}
	public void setRemark(List<String> remark) {
		this.remark = remark;
	}
	public int getCheckState() {
		return checkState;
	}
	public void setCheckState(int checkState) {
		this.checkState = checkState;
	}
	
	
}
