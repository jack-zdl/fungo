package com.fungo.system.dto;

import io.swagger.annotations.ApiModelProperty;

public class ThirdLoginPcInput {

	@ApiModelProperty(value="验证code",example="")
	private String code;
	@ApiModelProperty(value="第三方平台 0:新浪微博 1:微信 4:qq",example="")
	private int platformType;
	
	public String getCode() {
		return code;
	}
	public int getPlatformType() {
		return platformType;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setPlatformType(int platformType) {
		this.platformType = platformType;
	}

	
}
