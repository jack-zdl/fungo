package com.game.common.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 社区帖子
 * </p>
 *
 * @author lzh
 * @since 2018-12-27
 */
@TableName("t_cmm_post")
public class CmmPost extends Model<CmmPost> {

    private static final long serialVersionUID = 1L;

    /**
     * 社区ID
     */
	@TableField("community_id")
	private String communityId;
	private String id;
    /**
     * 会员id
     */
	@TableField("member_id")
	private String memberId;
    /**
     * 标题
     */
	private String title;
    /**
     * 标签
     */
	private String tags;
    /**
     * html内容
     */
	@TableField("html_origin")
	private String htmlOrigin;
    /**
     * 内容
     */
	private String content;
    /**
     * 图片
     */
	private String images;
	@TableField("cover_image")
	private String coverImage;
    /**
     * 编辑时间
     */
	@TableField("edited_at")
	private Date editedAt;
    /**
     * 组织
     */
	private String origin;
    /**
     * 状态 -1:已删除 0:压缩转码中 1:正常
     */
	private Integer state;
    /**
     * 评论数
     */
	@TableField("comment_num")
	private Integer commentNum;
    /**
     * 点赞数
     */
	@TableField("like_num")
	private Integer likeNum;
    /**
     * 收藏数
     */
	@TableField("collect_num")
	private Integer collectNum;
    /**
     * 查看数
     */
	@TableField("watch_num")
	private Integer watchNum;
    /**
     * 举报数
     */
	@TableField("report_num")
	private Integer reportNum;
    /**
     * 创建时间
     */
	@TableField("created_at")
	private Date createdAt;
    /**
     * 更新时间
     */
	@TableField("updated_at")
	private Date updatedAt;
    /**
     * 类型 1:普通 2:精华 3:置顶
     */
	private Integer type;
    /**
     * 标记
     */
	private Integer sort;
    /**
     * 视频 v2.4
     */
	private String video;
    /**
     * 游戏链接json集合
     */
	@TableField("game_list")
	private String gameList;
    /**
     * 置顶状态
     */
	private Integer topic;
    /**
     * 视频地址集合，json格式
V2.4.3版本添加
     */
	@TableField("video_urls")
	private String videoUrls;
    /**
     * 最后回复时间
     */
	@TableField("last_reply_at")
	private Date lastReplyAt;
    /**
     * 视频封面
     */
	@TableField("video_cover_image")
	private String videoCoverImage;

	@TableField("post_id")
	private Long postId;



	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getHtmlOrigin() {
		return htmlOrigin;
	}

	public void setHtmlOrigin(String htmlOrigin) {
		this.htmlOrigin = htmlOrigin;
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

	public Date getEditedAt() {
		return editedAt;
	}

	public void setEditedAt(Date editedAt) {
		this.editedAt = editedAt;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
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

	public Integer getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
	}

	public Integer getWatchNum() {
		return watchNum;
	}

	public void setWatchNum(Integer watchNum) {
		this.watchNum = watchNum;
	}

	public Integer getReportNum() {
		return reportNum;
	}

	public void setReportNum(Integer reportNum) {
		this.reportNum = reportNum;
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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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

	public Integer getTopic() {
		return topic;
	}

	public void setTopic(Integer topic) {
		this.topic = topic;
	}

	public String getVideoUrls() {
		return videoUrls;
	}

	public void setVideoUrls(String videoUrls) {
		this.videoUrls = videoUrls;
	}

	public Date getLastReplyAt() {
		return lastReplyAt;
	}

	public void setLastReplyAt(Date lastReplyAt) {
		this.lastReplyAt = lastReplyAt;
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

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}
}
