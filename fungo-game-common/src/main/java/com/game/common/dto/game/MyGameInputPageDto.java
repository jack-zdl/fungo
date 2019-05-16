package com.game.common.dto.game;

import com.game.common.api.InputPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="我的游戏对象",description="广告banner")
public class MyGameInputPageDto extends InputPageDto {
	private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="类型",example="1。下载。2。预约")
	private int type=1;
    @ApiModelProperty(value="手机型号",example="")
  	private String phoneModel;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

}
