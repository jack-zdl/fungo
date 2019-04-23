package com.game.common.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class InputDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(allowEmptyValue = true, required = false, value = "请求IP")
	private String ip;

	@ApiModelProperty(allowEmptyValue = true, required = false, value = "操作人")
	private String operator;

	/**
	 * app版本号
	 */
	private String appVersion;

	/**
	 * 当前设备系统
	 * Android/iOS二选一
	 */
	private String os;

	/**
	 * 用户登录唯一标识
	 */
	private  String token;

	/**
	 * 当前设备品牌
	 */
	private  String brand;


	/**
	 * 当前设备屏幕分辨率高度
	 */
	private  String height;

	/**
	 * 当前设备屏幕分辨率宽度
	 */
	private  String width;

	/**
	 * 当前设备当前系统版本名称
	 */
	private  String version;


	/**
	 * 当前设备唯一ID
	 */
	private  String udid;


	/**
	 * iOS上架渠道名
	 */
	private  String iosChannel;


	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public String getIosChannel() {
		return iosChannel;
	}

	public void setIosChannel(String iosChannel) {
		this.iosChannel = iosChannel;
	}
}
