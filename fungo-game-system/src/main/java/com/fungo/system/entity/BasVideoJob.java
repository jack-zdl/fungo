package com.fungo.system.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 压缩视频
 * </p>
 *
 * @author lzh
 * @since 2018-12-27
 */
@TableName("t_bas_video_job")
public class BasVideoJob extends Model<BasVideoJob> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 对象类型
     */
	@TableField("biz_type")
	private Integer bizType;
    /**
     * 对象id
     */
	@TableField("biz_id")
	private String bizId;
    /**
     * 压缩状态 0:未压缩 1: 已压缩 2:已确认并覆盖
     */
	private Integer status;
	@TableField("created_at")
	private Date createdAt;
	@TableField("updated_at")
	private Date updatedAt;
    /**
     * 阿里云播放视频id
     */
	@TableField("video_id")
	private String videoId;
    /**
     * 原始视频地址
     */
	@TableField("biz_video_url")
	private String bizVideoUrl;
    /**
     * 压缩视频地址
     */
	@TableField("re_video_url")
	private String reVideoUrl;
    /**
     * 压缩视频地址集合
     */
	@TableField("video_urls")
	private String videoUrls;
    /**
     * 视频封面
     */
	@TableField("video_cover_image")
	private String videoCoverImage;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getBizVideoUrl() {
		return bizVideoUrl;
	}

	public void setBizVideoUrl(String bizVideoUrl) {
		this.bizVideoUrl = bizVideoUrl;
	}

	public String getReVideoUrl() {
		return reVideoUrl;
	}

	public void setReVideoUrl(String reVideoUrl) {
		this.reVideoUrl = reVideoUrl;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
