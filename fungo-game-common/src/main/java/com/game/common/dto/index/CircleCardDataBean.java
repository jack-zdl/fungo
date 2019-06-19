package com.game.common.dto.index;

import com.game.common.dto.AuthorBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CircleCardDataBean {
	@ApiModelProperty(value="主标题",example="")
	private String	mainTitle;
//	@ApiModelProperty(value="子标题",example="")
//	private String subtitle;
	@ApiModelProperty(value="图片url",example="")
	private String imageUrl;
	@ApiModelProperty(value="视频url",example="")
	private String videoUrl;
//	@ApiModelProperty(value="显示时间",example="")
//	private String showTime;
	@ApiModelProperty(value="内容",example="")
	private String content;
	

	@ApiModelProperty(value="左下角内容",example="")
	private String StartDate;
	@ApiModelProperty(value="右下角内容",example="")
	private String EndDate;
	
//	@ApiModelProperty(value="行为类型   (1:内部跳转;2:外连接)",example="")
//	private String actionType="1";
//	@ApiModelProperty(value="网页地址     需要前台App端在url后追加?token=token",example="")
//	private String href;
	@ApiModelProperty(value="业务id",example="")
	private String targetId;
	@ApiModelProperty(value="业务类型",example="")
	private int targetType;
//
//	@ApiModelProperty(value="创建时间",example="")
//	private String createdAt;
//	@ApiModelProperty(value="更新时间",example="")
//	private String updatedAt;
//	@ApiModelProperty(value="点赞数",example="")
//	private int likeNum;
//	@ApiModelProperty(value="回复数",example="")
//	private int replyNum;
//	@ApiModelProperty(value="是否点攒",example="")
//	private boolean isLiked;
//	@ApiModelProperty(value="是否关注",example="")
//	private boolean isFollowed;
	
//	@ApiModelProperty(value="会员信息",example="")
//	private AuthorBean userinfo = new AuthorBean();
//
//	@ApiModelProperty(value="来源（文章来源于社区，评价来源游戏）",example="")
//	private ActionBean source = new ActionBean();

}
