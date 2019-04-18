package com.game.common.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 标签
 * </p>
 *
 * @author lzh
 * @since 2018-05-09
 */
@TableName("t_bas_tag")
public class BasTag extends Model<BasTag> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 名称
     */
	private String name;
    /**
     * 游戏数
     */
	@TableField("game_num")
	private Integer gameNum;
    /**
     * 标签组id
     */
	@TableField("group_id")
	private String groupId;
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
     * 排序
     */
	private Integer sort;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGameNum() {
		return gameNum;
	}

	public void setGameNum(Integer gameNum) {
		this.gameNum = gameNum;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
