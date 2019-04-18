package com.fungo.system.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 会员关注粉丝表
 * </p>
 *
 * @author lzh
 * @since 2018-07-23
 */
@TableName("t_member_follower")
public class MemberFollower extends Model<MemberFollower> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 发起会员id
     */
	@TableField("member_id")
	private String memberId;
    /**
     * 被关注ID
     */
	@TableField("follower_id")
	private String followerId;
    /**
     * 状态 1 :A关注B 2:AB互相关注，3:AB取消关注，4：B关注A
     */
	private Integer state;
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
     * 关注时间
     */
	@TableField("followed_at")
	private Date followedAt;


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

	public String getFollowerId() {
		return followerId;
	}

	public void setFollowerId(String followerId) {
		this.followerId = followerId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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

	public Date getFollowedAt() {
		return followedAt;
	}

	public void setFollowedAt(Date followedAt) {
		this.followedAt = followedAt;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
