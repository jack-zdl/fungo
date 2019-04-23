package com.fungo.system.dto;

public class ThirdLoginInput {

	private Integer platformType;
	private String uid;
	private String expiration ;
	private String accessToken;
	private String name;
	private String iconurl;
	private String openid;
	private Integer gender ;

	/**
	 * 用户统一标识。
	 * 如：针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
	 */
	private String unionid;
	
	public Integer getPlatformType() {
		return platformType;
	}
	public void setPlatformType(Integer platformType) {
		this.platformType = platformType;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getExpiration() {
		return expiration;
	}
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIconurl() {
		return iconurl;
	}
	public void setIconurl(String iconurl) {
		this.iconurl = iconurl;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
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
		return "ThirdLoginInput{" +
				"platformType=" + platformType +
				", uid='" + uid + '\'' +
				", expiration='" + expiration + '\'' +
				", accessToken='" + accessToken + '\'' +
				", name='" + name + '\'' +
				", iconurl='" + iconurl + '\'' +
				", openid='" + openid + '\'' +
				", gender=" + gender +
				", unionid='" + unionid + '\'' +
				'}';
	}
}
