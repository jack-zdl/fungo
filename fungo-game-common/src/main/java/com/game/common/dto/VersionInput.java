package com.game.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="更新信息查询",description="更新信息查询")
public class VersionInput {

	@ApiModelProperty("手机系统: iOS Android")
	private String phoneType;
	@ApiModelProperty("当前版本")
	private String currentVersion;
	@ApiModelProperty("渠道码 iOS:ios_app_store,ios_test_flight,ios_in_house")
	private String channelCode;
	
	public String getPhoneType() {
		return phoneType;
	}
	public String getCurrentVersion() {
		return currentVersion;
	}
	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}
	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	
	
}
