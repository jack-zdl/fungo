package com.fungo.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MemberLevelBean {
	
	@ApiModelProperty(value="当前等级",example="")
	private int level;
	@ApiModelProperty(value="下一等级",example="")
	private int nextLevel;
	@ApiModelProperty(value="当前经验",example="")
	private int currentExp;
	@ApiModelProperty(value="还须获得多少经验可以升级",example="")
	private int upgradeExp;
	@ApiModelProperty(value="下一等级所需经验",example="")
	private int nextLevelExp;
	@ApiModelProperty(value="注册时间",example="")
	private String registerDate;
	@ApiModelProperty(value="是否新用户",example="")
	private boolean newMember;
	
}
