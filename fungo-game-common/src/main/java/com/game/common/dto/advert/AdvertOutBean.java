package com.game.common.dto.advert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@ApiModel(value="广告banner",description="广告banner")
public class AdvertOutBean {
    @ApiModelProperty(value="业务类型",example="")
	private int bizType;
    @ApiModelProperty(value="业务主键",example="")
	private String bizId;
    @ApiModelProperty(value="名称",example="")
	private String name;
    @ApiModelProperty(value="标题",example="")
	private String title;
    @ApiModelProperty(value="描述",example="")
	private String content;
    @ApiModelProperty(value="图片",example="")
	private String imageUrl;
	@ApiModelProperty(value="PC2.0图片",example="")
    private String pcImagesUrl;

	/**
	 * 游戏icon URL
	 */
	private String gameIconURL;


}
