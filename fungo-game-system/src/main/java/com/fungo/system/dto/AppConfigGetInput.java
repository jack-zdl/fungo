package com.fungo.system.dto;

import io.swagger.annotations.ApiModelProperty;

//@ApiModel(value="",description="")
public class AppConfigGetInput {

	@ApiModelProperty(value="渠道码 001:vivo,002:oppo,003:xiaomi,004:tencent,005:huawei,006:samsung,007:baidu,008:ali",example="")
	private String channelCode;
	@ApiModelProperty(value="当前版本号",example="")
	private String version;
	
	
	public String getChannelCode() {
		return channelCode;
	}
	public String getVersion() {
		return version;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
