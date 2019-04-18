package com.game.common.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 二级评论
 * </p>
 *
 * @author lzh
 * @since 2018-12-08
 */
@TableName("t_reply")
public class Reply extends Model<Reply> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 评价ID
     */
	@TableField("target_id")
	private String targetId;
    /**
     * 会员Id
     */
	@TableField("member_id")
	private String memberId;
    /**
     * 回复会员Id
     */
	@TableField("replay_to_id")
	private String replayToId;
    /**
     * 内容
     */
	private String content;
    /**
     * 状态
     */
	private Integer state;
    /**
     * 类型，游戏，社区，心情
     */
	@TableField("target_type")
	private Integer targetType;
    /**
     * 点赞数
     */
	@TableField("like_num")
	private Integer likeNum;
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
     * 回复二级回复的用户名
     */
	@TableField("reply_name")
	private String replyName;
    /**
     * 回复二级回复的id
     */
	@TableField("reply_to_content_id")
	private String replyToContentId;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getReplayToId() {
		return replayToId;
	}

	public void setReplayToId(String replayToId) {
		this.replayToId = replayToId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getTargetType() {
		return targetType;
	}

	public void setTargetType(Integer targetType) {
		this.targetType = targetType;
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

	public String getReplyName() {
		return replyName;
	}

	public void setReplyName(String replyName) {
		this.replyName = replyName;
	}

	public String getReplyToContentId() {
		return replyToContentId;
	}

	public void setReplyToContentId(String replyToContentId) {
		this.replyToContentId = replyToContentId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
