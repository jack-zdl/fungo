package com.game.common.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 用户打标签列表
 * </p>
 *
 * @author lzh
 * @since 2018-05-09
 */
@TableName("t_game_tag_attitude")
public class GameTagAttitude extends Model<GameTagAttitude> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 游戏标签Id
     */
	@TableField("game_tag_id")
	private String gameTagId;
    /**
     * 会员ID
     */
	@TableField("member_id")
	private String memberId;
    /**
     * 打标签状态 0无态度，1支持，2反对
     */
	private Integer attitude;
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
     * 标签id
     */
	@TableField("tag_id")
	private String tagId;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGameTagId() {
		return gameTagId;
	}

	public void setGameTagId(String gameTagId) {
		this.gameTagId = gameTagId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Integer getAttitude() {
		return attitude;
	}

	public void setAttitude(Integer attitude) {
		this.attitude = attitude;
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

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
