package com.game.common.dto.community;

import io.swagger.annotations.ApiModelProperty;

public class CommunitySearchOut {

	private String name;
	private String objectId;
	private String createdAt;
	private String updatedAt;
	private boolean is_followed;
	private String icon;
	private int hot_value;
	@ApiModelProperty(value="内容",example="")
	private String intro;
	@ApiModelProperty(value="社区类型",example="")
	private int type;
	
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public int getHot_value() {
		return hot_value;
	}
	public void setHot_value(int hot_value) {
		this.hot_value = hot_value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public boolean isIs_followed() {
		return is_followed;
	}
	public void setIs_followed(boolean is_followed) {
		this.is_followed = is_followed;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	
	
}
