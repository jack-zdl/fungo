package com.fungo.system.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


/**
 * <p>
 *     虚拟币产生明细数据封装
 * </p>
 *
 * @author mxf
 * @since 2018-12-03
 */
public class CoinBean implements Serializable  {

	@ApiModelProperty(value="用户行为",example="")
	private String action;
	@ApiModelProperty(value="虚拟币变化",example="")
	private int coinChange;
	@ApiModelProperty(value="发生日期",example="")
	private String changeTime;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getCoinChange() {
		return coinChange;
	}

	public void setCoinChange(int coinChange) {
		this.coinChange = coinChange;
	}

	public String getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(String changeTime) {
		this.changeTime = changeTime;
	}
	
	
}
