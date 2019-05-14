package com.fungo.games.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 游戏评价邀请
 * </p>
 *
 * @author lzh
 * @since 2018-07-04
 */
@TableName("t_game_invite")
public class GameInvite extends Model<GameInvite> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 会员id
     */
	@TableField("member_id")
	private String memberId;
    /**
     * 游戏ID
     */
	@TableField("game_id")
	private String gameId;
    /**
     * 状态
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
     * 被邀请人
     */
	@TableField("invite_member_id")
	private String inviteMemberId;

	@TableField("notice_id")
	private String noticeId;

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
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

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
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

	public String getInviteMemberId() {
		return inviteMemberId;
	}

	public void setInviteMemberId(String inviteMemberId) {
		this.inviteMemberId = inviteMemberId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
