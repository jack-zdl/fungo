package com.game.common.dto;

import io.swagger.annotations.ApiModelProperty;

public class AidTool {

	@ApiModelProperty(value="工具名称",example="")
	private String toolName;
	@ApiModelProperty(value="工具图标",example="")
	private String toolIcon;
	@ApiModelProperty(value="工具简介",example="")
	private String toolIntro;
	@ApiModelProperty(value="工具id",example="")
	private String toolId;
	@ApiModelProperty(value="下载地址",example="")
	private String downloadUrl;
	
	public String getToolName() {
		return toolName;
	}
	public void setToolName(String toolName) {
		this.toolName = toolName;
	}
	public String getToolIcon() {
		return toolIcon;
	}
	public void setToolIcon(String toolIcon) {
		this.toolIcon = toolIcon;
	}
	public String getToolIntro() {
		return toolIntro;
	}
	public void setToolIntro(String toolIntro) {
		this.toolIntro = toolIntro;
	}
	public String getToolId() {
		return toolId;
	}
	public void setToolId(String toolId) {
		this.toolId = toolId;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	
	
}
