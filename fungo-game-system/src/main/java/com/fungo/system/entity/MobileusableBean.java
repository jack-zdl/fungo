package com.fungo.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="手机号是否注册对象",description="手机号是否注册对象")
public class MobileusableBean {
    @ApiModelProperty(value="是否注册过",example="")
	private boolean is_register;
    @ApiModelProperty(value="是否存在密码  (v.2.4)",example="")
	private boolean has_password;
	public boolean isIs_register() {
		return is_register;
	}
	public void setIs_register(boolean is_register) {
		this.is_register = is_register;
	}
	public boolean isHas_password() {
		return has_password;
	}
	public void setHas_password(boolean has_password) {
		this.has_password = has_password;
	}
	
}
