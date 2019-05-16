package com.game.common.dto.community;

import com.game.common.api.InputPageDto;

import java.util.Date;

/**
 * <p>
 * 社区一级评论
 * </p>
 *
 * @author lzh
 * @since 2018-07-02
 */
public class CmmCommentDto extends InputPageDto {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 回复会员id
     */
	private String memberId;
    /**
     * 帖子id
     */
	private String postId;
    /**
     * 内容
     */
	private String content;
    /**
     * 楼层数
     */
	private Integer floor;
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

	/**
	 * 社区id
	 */
	private String communityId;


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

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
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

	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
}
