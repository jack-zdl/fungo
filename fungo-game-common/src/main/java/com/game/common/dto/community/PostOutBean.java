package com.game.common.dto.community;

import com.game.common.dto.AuthorBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Setter
@Getter
@ToString
@ApiModel(value="帖子对象",description="帖子对象")
public class PostOutBean {

	public PostOutBean(){

	}

	@ApiModelProperty(value="会员信息",example="")
	private AuthorBean author;

	@ApiModelProperty(value="帖子id",example="")
	private String postId;

	@ApiModelProperty(value="图片",example="")
	private String imageUrl;

	@ApiModelProperty(value="视频地址v2.4",example="")
	private String videoUrl;

	@ApiModelProperty(value="标题",example="")
	private String title;

	@ApiModelProperty(value="内容",example="")
	private String content;

	@ApiModelProperty(value="点赞数",example="")
	private int likeNum;
	@ApiModelProperty(value="是否点攒",example="")
	private boolean isLiked;
	@ApiModelProperty(value="回复数",example="")
	private int replyNum;
	
	@ApiModelProperty(value="社区id",example="")
	private String communityId;

	@ApiModelProperty(value="社区图标",example="")
	private String communityIcon;

	@ApiModelProperty(value="社区名称",example="")
	private String communityName;

	@ApiModelProperty(value="是否点赞",example="")
	private boolean is_liked;

	@ApiModelProperty(value="图片数组",example="")
	private List<String> images = new ArrayList<>();

	@ApiModelProperty(value="最后更新时间",example="")
	private String updated_at;

	@ApiModelProperty(value="发布时间",example="")
	private String createdAt;

	@ApiModelProperty(value="帖子状态 1:普通 2:精华 3:置顶",example="")
	private int type;

	private String videoCoverImage;

	/**
	 * 文章数据行ID
	 */
	private Long rowId;

	/**
	 * 功能描述: 增加圈子相关的属性
	 * @auther: dl.zhang
	 * @date: 2019/6/27 15:59
	 */
	@ApiModelProperty(value="圈子id",example="")
	private String circleId;

	@ApiModelProperty(value="圈子图标",example="")
	private String circleIcon;

	@ApiModelProperty(value="圈子名称",example="")
	private String circleName;

	private String memberId;

	/**
	 * 视频详情(2.4.3)
	 */
	private List<StreamInfo> videoList = new ArrayList<>();


	private String tagName;

}
