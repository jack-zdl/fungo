package com.fungo.system.dto;

import io.swagger.annotations.ApiModelProperty;

public class DignityPutInput {

	/*
	 * 接口验签数据，身份id,身份名称，身份img
	      身份状态，获取条件，占比
	 */
	
	@ApiModelProperty(value="身份id",example="")
	private String dignityId;
	@ApiModelProperty(value="身份名称",example="")
	private String dignityName;
	@ApiModelProperty(value="身份图标",example="")
	private String image;
	@ApiModelProperty(value="身份状态",example="")
	private Integer state;
	@ApiModelProperty(value="获取条件",example="")
	private String qualification ;
	@ApiModelProperty(value="占比",example="")
	private String percent;
	
	public String getDignityId() {
		return dignityId;
	}
	public void setDignityId(String dignityId) {
		this.dignityId = dignityId;
	}
	public String getDignityName() {
		return dignityName;
	}
	public void setDignityName(String dignityName) {
		this.dignityName = dignityName;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public String getPercent() {
		return percent;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	
	
}
