package com.fungo.system.dto;

public class BindInfo {
	private Integer platformtype;
	private boolean is_bind;
	private String name;
	
	public Integer getPlatformtype() {
		return platformtype;
	}
	public void setPlatformtype(Integer platformtype) {
		this.platformtype = platformtype;
	}
	public Boolean getIs_bind() {
		return is_bind;
	}
	public void setIs_bind(Boolean is_bind) {
		this.is_bind = is_bind;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
