package com.fungo.games.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 新游表
 * </p>
 *
 * @author Carlos
 * @since 2018-06-28
 */
@TableName("t_game_page")
public class HomePage extends Model<HomePage> {

    private static final long serialVersionUID = 1L;

	private String id;
	/**
	 * 游戏id
	 */
	@TableField("game_id")
	private String gameId;
	/**
	 * 游戏集合名称
	 */
	private String name;
    /**
     * 标签
     */
	private String tags;
	/**
	 * 国家
	 */
	private String origin;
	/**
	 * 评分
	 */
	private String score;
	/**
	 * 推荐语
	 */
	@TableField("rmd_lag")
	private String rmdLag;
	/**
	 * 推荐理由
	 */
	@TableField("rmd_reason")
	private String rmdReason;
	/**
	 * 视频地址
	 */
	private String video;
	/**
	 * 图片
	 */
	@TableField("app_images")
	private String appImages;
	/**
	 * 图片
	 */
	@TableField("pc_images")
	private String pcImages;
    /**
     * '状态 -1:删除 0:正常'
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
     * 创建人
     */
	@TableField("created_by")
	private String createdBy;
    /**
     * 修改人
     */
	@TableField("updated_by")
	private String updatedBy;


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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getRmdLag() {
		return rmdLag;
	}

	public void setRmdLag(String rmdLag) {
		this.rmdLag = rmdLag;
	}

	public String getRmdReason() {
		return rmdReason;
	}

	public void setRmdReason(String rmdReason) {
		this.rmdReason = rmdReason;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getAppImages() {
		return appImages;
	}

	public void setAppImages(String appImages) {
		this.appImages = appImages;
	}

	public String getPcImages() {
		return pcImages;
	}

	public void setPcImages(String pcImages) {
		this.pcImages = pcImages;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
