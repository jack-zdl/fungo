package com.fungo.system.dto;



import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserBean {

	@Length( min = 1,max = 100,message = "用户名长度不能超过100")
	private String user_name;
	@Min( value = 0,message = "用户传递性别标示错误")
	@Max( value = 2,message = "用户传递性别标示错误")
	private int gender;
	@Length(min = 1,max = 1024,message = "用户签名异常") //过长,不应超过1024
	private String sign;
	private String avatar;
	
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
	
}
