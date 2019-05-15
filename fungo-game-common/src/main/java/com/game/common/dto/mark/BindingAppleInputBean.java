package com.game.common.dto.mark;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="绑定appleId对象",description="appleId对象")
public class BindingAppleInputBean {
	@ApiModelProperty(value="appleId",example="")
	private String appleId;
	@ApiModelProperty(value="姓名",example="")
	private String surname;
	@ApiModelProperty(value="名称",example="")
	private String name;
	@ApiModelProperty(value="游戏id",example="")
	private String gameId;
	@ApiModelProperty(value="手机型号 （Ios，Android）",example="")
	private String phoneModel;
	
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getPhoneModel() {
		return phoneModel;
	}
	public void setPhoneModel(String phoneModel) {
		this.phoneModel = phoneModel;
	}
	public String getAppleId() {
		return appleId;
	}
	public void setAppleId(String appleId) {
		this.appleId = appleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
}
