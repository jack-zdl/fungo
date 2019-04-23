package com.fungo.system.dto;

import io.swagger.annotations.ApiModelProperty;

public class HonorInfo {

	/*
	 * 荣誉类型
荣誉ID 
荣誉名称
荣誉说明
荣誉数量
荣誉img
荣誉状态
获取时间
	 */
	@ApiModelProperty(value="荣誉类型",example="")
	private int honorType;
	@ApiModelProperty(value="荣誉id",example="")
	private String honorId;
	@ApiModelProperty(value="荣誉名称",example="")
	private String honorName;
	@ApiModelProperty(value="荣誉说明",example="")
	private String honorIntro;
	@ApiModelProperty(value="荣誉数量",example="")
	private int honorNum;
	@ApiModelProperty(value="荣誉状态",example="")
	private int honorState;
	@ApiModelProperty(value="荣誉图标",example="")
	private String honorImg;
	@ApiModelProperty(value="获取时间",example="")
	private String gainTime;

	public int getHonorType() {
		return honorType;
	}

	public void setHonorType(int honorType) {
		this.honorType = honorType;
	}

	public String getHonorId() {
		return honorId;
	}

	public void setHonorId(String honorId) {
		this.honorId = honorId;
	}

	public String getHonorName() {
		return honorName;
	}

	public void setHonorName(String honorName) {
		this.honorName = honorName;
	}

	public String getHonorIntro() {
		return honorIntro;
	}

	public void setHonorIntro(String honorIntro) {
		this.honorIntro = honorIntro;
	}

	public int getHonorNum() {
		return honorNum;
	}

	public void setHonorNum(int honorNum) {
		this.honorNum = honorNum;
	}

	public int getHonorState() {
		return honorState;
	}

	public void setHonorState(int honorState) {
		this.honorState = honorState;
	}

	public String getHonorImg() {
		return honorImg;
	}

	public void setHonorImg(String honorImg) {
		this.honorImg = honorImg;
	}

	public String getGainTime() {
		return gainTime;
	}

	public void setGainTime(String gainTime) {
		this.gainTime = gainTime;
	}
	
	
	

}
