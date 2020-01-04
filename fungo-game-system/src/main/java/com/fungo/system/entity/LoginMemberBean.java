package com.fungo.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value="用户登录对象",description="用户登录对象")
public class LoginMemberBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value="手机号码",example="")
	private String mobilePhoneNumber;
	@ApiModelProperty(value="用户名称",example="")
	private String username;
	@ApiModelProperty(value="邮件是否验证",example="")
	private boolean emailVerified;
	@ApiModelProperty(value="手机号是否验证",example="")
	private boolean mobilePhoneVerified;
	@ApiModelProperty(value="会员Id",example="")
	private String objectId ;
	@ApiModelProperty(value="创建时间",example="")
	private String createdAt;
	@ApiModelProperty(value="更新时间",example="")
	private String updatedAt;
	@ApiModelProperty(value="登录Token",example="")
	private String token;
	@ApiModelProperty(value="是否设置密码(v2.4)",example="")
	private boolean has_password;
	@ApiModelProperty(value="用户头像",example="")
	private String avatar;

	private int auth;
	
	public boolean isHas_password() {
		return has_password;
	}
	public void setHas_password(boolean has_password) {
		this.has_password = has_password;
	}
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}
	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isEmailVerified() {
		return emailVerified;
	}
	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}
	public boolean isMobilePhoneVerified() {
		return mobilePhoneVerified;
	}
	public void setMobilePhoneVerified(boolean mobilePhoneVerified) {
		this.mobilePhoneVerified = mobilePhoneVerified;
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getAuth() {
		return auth;
	}

	public void setAuth(int auth) {
		this.auth = auth;
	}
}
