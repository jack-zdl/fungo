package com.game.common.dto.game;

import io.swagger.annotations.ApiModel;

import java.math.BigDecimal;

@ApiModel(value="游戏特征比例对象",description="游戏特征比例对象")
public class TraitBean {

	private String key;
	private String keyName;
	private BigDecimal value;
	
	public String getKey() {
		return key;
	}
	public String getKeyName() {
		return keyName;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	
}
