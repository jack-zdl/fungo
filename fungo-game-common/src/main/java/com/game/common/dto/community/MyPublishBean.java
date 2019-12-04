package com.game.common.dto.community;

import com.game.common.dto.StreamInfo;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.*;

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

	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public List<Map<String, String>> getGameList() {
		return gameList;
	}
	public void setGameList(List<Map<String, String>> gameList) {
		this.gameList = gameList;
	}

	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getLikeNum() {
		return likeNum;
	}
	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public String getObjectId() {
		return ObjectId;
	}
	public void setObjectId(String objectId) {
		ObjectId = objectId;
	}
	public Map<String, Object> getLink_community() {
		return link_community;
	}
	public void setLink_community(Map<String, Object> link_community) {
		this.link_community = link_community;
	}
	public String getVideoCoverImage() {
		return videoCoverImage;
	}
	public void setVideoCoverImage(String videoCoverImage) {
		this.videoCoverImage = videoCoverImage;
	}
	public ArrayList<StreamInfo> getVideoList() {
		return videoList;
	}
	public void setVideoList(ArrayList<StreamInfo> videoList) {
		this.videoList = videoList;
	}
	public String getCoverImage() {
		return coverImage;
	}
	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}

	public int getDeltype() {
		return deltype;
	}

	public void setDeltype(int deltype) {
		this.deltype = deltype;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getCircleId() {
		return circleId;
	}

	public void setCircleId(String circleId) {
		this.circleId = circleId;
	}
}
