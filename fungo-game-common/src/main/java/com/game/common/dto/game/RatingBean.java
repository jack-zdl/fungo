package com.game.common.dto.game;

import io.swagger.annotations.ApiModel;

@ApiModel(value="游戏评分比例对象",description="游戏评分比例对象")
public class RatingBean {
	
	private String key;
	private Double value;
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	
	

}
