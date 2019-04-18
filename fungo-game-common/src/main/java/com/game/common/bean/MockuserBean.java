package com.game.common.bean;

import java.util.Date;

public class MockuserBean {

	private int publish_num;
	private String id;
	private String user_name;
	private Date created_at;
	private Date updated_at;
	private int gender;
	private String sign;
	public int getPublish_num() {
		return publish_num;
	}
	public void setPublish_num(int publish_num) {
		this.publish_num = publish_num;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	
	
	
	
}
