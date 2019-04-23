package com.fungo.system.dto;

import com.game.common.dto.InputDto;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;

/**
 * 用户登录相关信息
 * @author sam
 *
 */
//@ApiModel(value="用户验证信息(2.4)",description="用户验证信息(2.4)")
public class MsgInput extends InputDto {
	private static final long serialVersionUID = 1L;
	private String code;
	private String mobile;
	private String password;
//	@ApiModelProperty(value="设备号",example="")
	private String deviceId;
	
	private String new_password;
	private String old_password;
	private String type;
	private String token;
	
	private String step;
	private String captcha;
	
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getNew_password() {
		return new_password;
	}
	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}
	public String getOld_password() {
		return old_password;
	}
	public void setOld_password(String old_password) {
		this.old_password = old_password;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	
}
