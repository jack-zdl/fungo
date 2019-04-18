package com.fungo.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="绑定设备信息",description="绑定设备信息")
public class DeviceInput {
	@ApiModelProperty(value="设备ID",example="")
	private String deviceId;
	@ApiModelProperty(value="账户ID",example="")
	private String accountId;
	@ApiModelProperty(value="平台类型(Ios,Andriod)",example="")
	private String phoneModel;
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getPhoneModel() {
		return phoneModel;
	}
	public void setPhoneModel(String phoneModel) {
		this.phoneModel = phoneModel;
	}
	
}
