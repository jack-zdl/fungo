package com.fungo.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="appleId对象",description="appleId对象")
public class AppleInputBean {
	@ApiModelProperty(value="appleId",example="")
	private String appleId;
	@ApiModelProperty(value="姓名",example="")
	private String surname;
	@ApiModelProperty(value="名称",example="")
	private String name;
	
	public String getAppleId() {
		return appleId;
	}
	public void setAppleId(String appleId) {
		this.appleId = appleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
}
