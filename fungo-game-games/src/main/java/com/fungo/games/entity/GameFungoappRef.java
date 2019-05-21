package com.fungo.games.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 游戏与Fungo app关系表
 * </p>
 *
 * @author mxf
 * @since 2019-04-25
 */
@TableName("t_game_fungoapp_ref")
public class GameFungoappRef extends Model<GameFungoappRef> {

    private static final long serialVersionUID = 1L;
	@TableId(value = "id",type = IdType.UUID)
	private Long id;
    /**
     * 游戏id
     */
	@TableField("game_id")
	private String gameId;
    /**
     * 游戏名称
     */
	@TableField("game_name")
	private String gameName;
    /**
     * fungo渠道id
     */
	@TableField("fungo_channel_id")
	private String fungoChannelId;
    /**
     * fungo渠道名称
     */
	@TableField("fungo_channel_name")
	private String fungoChannelName;
    /**
     * fungoapp 渠道包下载地址
     */
	@TableField("fungo_app_url")
	private String fungoAppUrl;
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
	private String ext1;
	private String ext2;
	private String ext3;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getFungoChannelId() {
		return fungoChannelId;
	}

	public void setFungoChannelId(String fungoChannelId) {
		this.fungoChannelId = fungoChannelId;
	}

	public String getFungoChannelName() {
		return fungoChannelName;
	}

	public void setFungoChannelName(String fungoChannelName) {
		this.fungoChannelName = fungoChannelName;
	}

	public String getFungoAppUrl() {
		return fungoAppUrl;
	}

	public void setFungoAppUrl(String fungoAppUrl) {
		this.fungoAppUrl = fungoAppUrl;
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

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
