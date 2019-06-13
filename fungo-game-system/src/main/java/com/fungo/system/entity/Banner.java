package com.fungo.system.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 广告
 * </p>
 *
 * @author lzh
 * @since 2018-12-27
 */
@TableName("t_banner")
public class Banner extends Model<Banner> {

    private static final long serialVersionUID = 1L;

	@TableId(value = "id",type = IdType.UUID)
	private String id;
    /**
     * 跳转类型，1：App内部跳转，2:跳转url
     */
	@TableField("action_type")
	private Integer actionType;
    /**
     * 位置_id
     */
	@TableField("position_id")
	private String positionId;
    /**
     * 位置代码
     */
	@TableField("position_code")
	private String positionCode;
    /**
     * 发布日期
鸡汤文所属日期
     */
	@TableField("release_time")
	private Date releaseTime;
    /**
     * 业务类型，3：游戏，1：帖子
     */
	@TableField("target_type")
	private Integer targetType;
    /**
     * 业务id，（游戏id或帖子id）
     */
	@TableField("target_id")
	private String targetId;
    /**
     * 标题
     */
	private String title;
    /**
     * 简介
鸡汤文内容
     */
	private String intro;
    /**
     * 标签（推荐理由）
     */
	private String tag;
    /**
     * 图片
     */
	@TableField("cover_image")
	private String coverImage;
    /**
     * 外链地址
鸡汤文图片链接
     */
	private String href;
    /**
     * 状态 -1:删除,  0：上线，1：草稿，2：下线
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
     * 创建人名称
     */
	@TableField("created_name")
	private String createdName;
    /**
     * 排序
     */
	private Integer sort;
    /**
     * 业务类型:
1  广告
2  签到文案
     */
	@TableField("adv_type")
	private Integer advType;

	/**
	 * 活动推广标题
	 */
	@TableField("generalize_title;")
	private String  generalizeTitle;

	/**
	 * 活动开始时间
	 */
	@TableField("begin_date")
	private Date beginDate;
	/**
	 *  活动结束时间
	 */
	@TableField("end_date")
	private Date endDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getActionType() {
		return actionType;
	}

	public void setActionType(Integer actionType) {
		this.actionType = actionType;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public Integer getTargetType() {
		return targetType;
	}

	public void setTargetType(Integer targetType) {
		this.targetType = targetType;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedName() {
		return createdName;
	}

	public void setCreatedName(String createdName) {
		this.createdName = createdName;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getAdvType() {
		return advType;
	}

	public void setAdvType(Integer advType) {
		this.advType = advType;
	}

	public String getGeneralizeTitle() {
		return generalizeTitle;
	}

	public void setGeneralizeTitle(String generalizeTitle) {
		this.generalizeTitle = generalizeTitle;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
