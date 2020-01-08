package com.game.common.dto.community;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.ws.rs.GET;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ToString
@Getter
@Setter
@ApiModel(value="关注用户对象",description="关注用户对象")
public class FollowUserOutBean {
	@ApiModelProperty(value="会员名称",example="")
	private String username;
	@ApiModelProperty(value="头像",example="")
	private String avatar;
	@ApiModelProperty(value="会员ID",example="")
	private String objectId;
	@ApiModelProperty(value="会员级别",example="")
	private int level;
	@ApiModelProperty(value="创建时间",example="")
	private String createdAt;
	@ApiModelProperty(value="更新时间",example="")
	private String updatedAt;
	@ApiModelProperty(value="会员号",example="")
	private String memberNo;
	@ApiModelProperty(value="是否关注",example="")
	private boolean isFollowed;
	@ApiModelProperty(value="PC2.0互相关注：0：否  1:是",example="")
	private String mutualFollowed;
	
	private String dignityImg;
	
	private List<HashMap<String,Object>> statusImg = new ArrayList<>();


	@ApiModelProperty(value="用户官方身份(2.4.3)",example="")
	private List<List<HashMap<String,Object>>> statusImgs = new ArrayList<>();


	@ApiModelProperty(value="签名",example="")
	private String sign;


}
