package com.fungo.games.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 游戏标签
 * </p>
 *
 * @author lzh
 * @since 2018-05-09
 */
@TableName("t_game_tag")
public class GameTag extends Model<GameTag> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 游戏ID
     */
	@TableField("game_id")
	private String gameId;
    /**
     * 类型 0:标签，1，分类标签
     */
	private Integer type;
    /**
     * 支持数
     */
	@TableField("like_num")
	private Integer likeNum;
    /**
     * 反对
     */
	@TableField("dislike_num")
	private Integer dislikeNum;
    /**
     * 标签id
     */
	@TableField("tag_id")
	private String tagId;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}

	public Integer getDislikeNum() {
		return dislikeNum;
	}

	public void setDislikeNum(Integer dislikeNum) {
		this.dislikeNum = dislikeNum;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
