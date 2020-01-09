package com.game.common.dto.community;

import com.game.common.dto.StreamInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.*;

@Setter
@Getter
@ToString
public class MyPublishBean implements Serializable{

	@ApiModelProperty(value="标题",example="")
	private String title;
	@ApiModelProperty(value="状态1:普通 2:精华 3:置顶",example="")
	private int type;
	@ApiModelProperty(value="内容",example="")
	private String content;
	@ApiModelProperty(value="图片列表",example="")
	private List<String> images = new ArrayList<>();
	@ApiModelProperty(value="视频",example="")
	private String video;
	@ApiModelProperty(value="最后更新时间",example="")
	private String updatedAt;
	@ApiModelProperty(value="游戏链接列表 icon gameName gameId",example="")
	private List<Map<String,String>> gameList = new ArrayList<>();
	@ApiModelProperty(value="点赞数",example="")
	private int likeNum;
	@ApiModelProperty(value="评论数",example="")
	private int commentNum;
	
	private String ObjectId;
	@ApiModelProperty(value="社区信息",example="")
	private Map<String,Object> link_community = new HashMap<>();
	
	@ApiModelProperty(value="视频封面",example="")
	private String videoCoverImage;

	@ApiModelProperty(value="圈子id",example="")
	private String circleId;
	@ApiModelProperty(value="圈子name",example="")
	private String circleName;
	@ApiModelProperty(value="圈子icon",example="")
	private String circleIcon;

	private ArrayList<StreamInfo> videoList = new ArrayList<>();
	
	private String coverImage;

	/**
	 * 功能描述: 1 true  已删除  0 false 未删除
	 * @auther: dl.zhang
	 * @date: 2019/8/13 14:26
	 */
	private int deltype;
	/**
	 * 创建时间
	 */
//	private Date createdAt;
	private String createdAt;

	private int auth;

	

}
