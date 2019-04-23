/**
 * 第三方用户实体类
 */
package com.game.common.dto;

public class ThirdPartyUser {

	private String account;// 用户
	private String userName;// 用户昵称
	private String avatarUrl;// 用户头像地址
	private String gender;// 用户性别
	private String token;// 用户认证
	private String openid;// 用户第三方id
	private String provider;// 用户类型
	private Integer userId;// 用户id

	/**
	 * 用户统一标识。
	 * 如：针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
	 */
	private String unionid;


	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	@Override
	public String toString() {
		return "ThirdPartyUser{" +
				"account='" + account + '\'' +
				", userName='" + userName + '\'' +
				", avatarUrl='" + avatarUrl + '\'' +
				", gender='" + gender + '\'' +
				", token='" + token + '\'' +
				", openid='" + openid + '\'' +
				", provider='" + provider + '\'' +
				", userId=" + userId +
				", unionid='" + unionid + '\'' +
				'}';
	}
}
