package com.fungo.system.dto;

import io.swagger.annotations.ApiModelProperty;

public class IncentPutInput {

	@ApiModelProperty(value="用户id",example="")
	private String userId;
	@ApiModelProperty(value="对象id(身份 荣誉)",example="")
	private String objectId;
	@ApiModelProperty(value="积分/虚拟币 数",example="")
	private Integer number;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}


	
	
}
