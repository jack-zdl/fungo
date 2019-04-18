package com.fungo.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="规则介绍",description="规则介绍")
public class DignityRule {
	
	@ApiModelProperty(value="用户id",example="")
	private String memberId;
	@ApiModelProperty(value="身份id",example="")
	private String dignityId;
	@ApiModelProperty(value="身份名称",example="")
	private String dignityName;
	@ApiModelProperty(value="身份图标",example="")
	private String image;
	@ApiModelProperty(value="身份状态",example="")
	private int state;

	public String getDignityId() {
		return dignityId;
	}
	public void setDignityId(String dignityId) {
		this.dignityId = dignityId;
	}
	public String getDignityName() {
		return dignityName;
	}
	public void setDignityName(String dignityName) {
		this.dignityName = dignityName;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
