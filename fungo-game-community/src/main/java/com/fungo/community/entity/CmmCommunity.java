package com.fungo.community.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 社区
 * </p>
 *
 * @author lzh
 * @since 2018-12-12
 */
@TableName("t_cmm_community")
public class CmmCommunity extends Model<CmmCommunity> {

    private static final long serialVersionUID = 1L;

	@TableId(value = "id",type = IdType.UUID)
	private String id;
    /**
     * 游戏id
     */
	@TableField("game_id")
	private String gameId;
    /**
     * 名称
     */
	private String name;
    /**
     * icon
     */
	private String icon;
    /**
     * 介绍
     */
	private String intro;
    /**
     * 图片
     */
	@TableField("cover_image")
	private String coverImage;
    /**
     * 社区类型
     */
	private Integer type;
    /**
     * 状态  -1:已删除,0:未上架,1:运营中
     */
	private Integer state;
    /**
     * 推荐数
     */
	@TableField("followee_num")
	private Integer followeeNum;
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
     * 创建人
     */
	@TableField("created_by")
	private String createdBy;
    /**
     * 更新人
     */
	@TableField("updated_by")
	private String updatedBy;
    /**
     * 热度值
     */
	@TableField("hot_value")
	private Integer hotValue;
    /**
     * 推荐状态 0:未推荐,1:推荐
     */
	@TableField("recommend_state")
	private Integer recommendState;
    /**
     * 标记
     */
	private Integer sort;
    /**
     * 帖子数
     */
	@TableField("post_num")
	private Integer postNum;


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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getFolloweeNum() {
		return followeeNum;
	}

	public void setFolloweeNum(Integer followeeNum) {
		this.followeeNum = followeeNum;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getHotValue() {
		return hotValue;
	}

	public void setHotValue(Integer hotValue) {
		this.hotValue = hotValue;
	}

	public Integer getRecommendState() {
		return recommendState;
	}

	public void setRecommendState(Integer recommendState) {
		this.recommendState = recommendState;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getPostNum() {
		return postNum;
	}

	public void setPostNum(Integer postNum) {
		this.postNum = postNum;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
