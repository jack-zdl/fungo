package com.fungo.system.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 游戏开发者关联表
 * </p>
 *
 * @author lzh
 * @since 2018-06-08
 */
@TableName("t_developer_game_rel")
public class DeveloperGameRel extends Model<DeveloperGameRel> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 游戏ID
     */
	@TableField("game_id")
	private String gameId;
    /**
     * 开发者ID
     */
	@TableField("developer_id")
	private String developerId;
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
     * 会员ID
     */
	@TableField("member_id")
	private String memberId;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getDeveloperId() {
		return developerId;
	}

	public void setDeveloperId(String developerId) {
		this.developerId = developerId;
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

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
