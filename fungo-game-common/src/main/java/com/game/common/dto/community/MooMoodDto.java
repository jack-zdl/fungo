package com.game.common.dto.community;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 心情
 * </p>
 *
 * @author lzh
 * @since 2018-12-29
 */
public class MooMoodDto implements Serializable{

	private static final long serialVersionUID = -831216539130416959L;

	private String id;
    /**
     * 会员id
     */
	private String memberId;
    /**
     * 内容
     */
	private String content;
    /**
     * 图片
     */
	private String images;
    /**
     * 图片
     */
	private String coverImage;
    /**
     * 状态 0正常 1视频处理中
     */
	private Integer state;
    /**
     * 评论数
     */
	private Integer commentNum;
    /**
     * 点赞数
     */
	private Integer likeNum;
    /**
     * 创建时间
     */
	private Date createdAt;
    /**
     * 更新时间
     */
	private Date updatedAt;
    /**
     * 类型 1:普通 2:精选
     */
	private Integer type;
    /**
     * 视频URL
     */
	private String video;
    /**
     * 游戏链接json集合
     */
	private String gameList;
    /**
     * 视频地址集合，json格式
V2.4.3版本添加
     */
	private String videoUrls;
    /**
     * 视频封面图
     */
	private String videoCoverImage;

	private Long mooId;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public Integer getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getGameList() {
		return gameList;
	}

	public void setGameList(String gameList) {
		this.gameList = gameList;
	}

	public String getVideoUrls() {
		return videoUrls;
	}

	public void setVideoUrls(String videoUrls) {
		this.videoUrls = videoUrls;
	}

	public String getVideoCoverImage() {
		return videoCoverImage;
	}

	public void setVideoCoverImage(String videoCoverImage) {
		this.videoCoverImage = videoCoverImage;
	}

	public Long getMooId() {
		return mooId;
	}

	public void setMooId(Long mooId) {
		this.mooId = mooId;
	}
}
