package com.game.common.dto.community;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 心情评论
 * </p>
 *
 * @author lzh
 * @since 2018-07-02
 */
public class MooMessageDto implements Serializable {

	private static final long serialVersionUID = -7187367211556797518L;

	private String id;
    /**
     * 心情id
     */
	private String moodId;

	private String memberId;
    /**
     * 内容
     */
	private String content;
    /**
     * 状态
     */
	private Integer state;
    /**
     * 回复数
     */
	private Integer replyNum;
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
     * 资源类型
     */
	private Integer type;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMoodId() {
		return moodId;
	}

	public void setMoodId(String moodId) {
		this.moodId = moodId;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(Integer replyNum) {
		this.replyNum = replyNum;
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

}
