package com.game.common.dto.community;

import io.swagger.annotations.ApiModelProperty;

public class CommunityOutPageDto {

	private String name;
	private String objectId;
	private String createdAt;
	private String updatedAt;
	private String intro;
	private int hot_value;
	@ApiModelProperty(value="社区类型 0普通 1官方",example="")
	private int type;
	
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public void setHot_value(int hot_value) {
		this.hot_value = hot_value;
	}
	public Integer getHot_value() {
		return hot_value;
	}
	public void setHot_value(Integer hot_value) {
		this.hot_value = hot_value;
	}
	private String icon;
	private boolean is_followed;
	
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
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public boolean isIs_followed() {
		return is_followed;
	}
	public void setIs_followed(boolean is_followed) {
		this.is_followed = is_followed;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
}
