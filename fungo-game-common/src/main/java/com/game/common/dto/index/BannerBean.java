package com.game.common.dto.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel(value="行为对象",description="行为对象")
@Getter
@Setter
@ToString
public class BannerBean {

	@ApiModelProperty(value="点赞总数",example="")
	private Integer count;

	@ApiModelProperty(value="是否点赞 ",example="")
	private Boolean isClick;
	
}
