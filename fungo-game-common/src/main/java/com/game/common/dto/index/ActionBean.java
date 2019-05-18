package com.game.common.dto.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="行为对象",description="行为对象")
public class ActionBean {
	@ApiModelProperty(value="行为类型  (1:内部跳转;2:外连接)",example="")
	private String actionType = "";
	@ApiModelProperty(value="网页地址 ",example="")
	private String href = "";
	@ApiModelProperty(value="数据id",example="")
	private String targetId = "";
	@ApiModelProperty(value="跳转类型",example="")
	private int targetType;
	@ApiModelProperty(value="图标",example="")
	private String icon = "";
	@ApiModelProperty(value="名称",example="")
	private String name = "";
	@ApiModelProperty(value="图标类型(待定)",example="")
	private String iconType = "";
	public String getActionType() {
		return actionType;
	}
	public String getHref() {
		return href;
	}
	public String getTargetId() {
		return targetId;
	}
	public int getTargetType() {
		return targetType;
	}
	public String getIcon() {
		return icon;
	}
	public String getName() {
		return name;
	}
	public String getIconType() {
		return iconType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setIconType(String iconType) {
		this.iconType = iconType;
	}
	
}
