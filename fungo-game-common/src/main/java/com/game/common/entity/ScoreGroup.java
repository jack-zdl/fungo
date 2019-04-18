package com.game.common.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 评分组件
 * </p>
 *
 * @author lzh
 * @since 2018-12-19
 */
@TableName("t_score_group")
public class ScoreGroup extends Model<ScoreGroup> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 名称
     */
	private String name;
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
     * 任务类型
			1 分值
			   11 任务 获取经验值
			2 虚拟币
				21 营销活动  获取fungo币
				22  签到获取fungo币
	 					220 V2.4.6签到版本签到获取fungo币
				23 任务 获取fungo币
			3 分值和虚拟币共有
     */
	@TableField("task_type")
	private Integer taskType;
    /**
     * 任务类型
		1 新手任务
		2 社区任务
		3 游戏任务
		4 精品
		5 其他

     */
	@TableField("task_flag")
	private Integer taskFlag;
    /**
     * 营销活动有效期开始日期
     */
	@TableField("indate_start")
	private Date indateStart;
    /**
     * 营销活动有效期结束日期
     */
	@TableField("indate_end")
	private Date indateEnd;
    /**
     * 活动图片集，json结构
     */
	@TableField("market_imgs")
	private String marketImgs;
    /**
     * 活动简介
     */
	@TableField("market_intro")
	private String marketIntro;
    /**
     * 排序号
     */
	private Integer sort;
    /**
     * 创建人ID
     */
	@TableField("creator_id")
	private String creatorId;
    /**
     * 是否启用
0-未启动
1-启用
     */
	@TableField("is_active")
	private Integer isActive;
    /**
     * 创建人名称
     */
	@TableField("creator_name")
	private String creatorName;
    /**
     * 扩展字段1
     */
	private String ext1;
    /**
     * 扩展字段1
     */
	private String ext2;
    /**
     * 扩展字段1
     */
	private String ext3;


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

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	public Integer getTaskFlag() {
		return taskFlag;
	}

	public void setTaskFlag(Integer taskFlag) {
		this.taskFlag = taskFlag;
	}

	public Date getIndateStart() {
		return indateStart;
	}

	public void setIndateStart(Date indateStart) {
		this.indateStart = indateStart;
	}

	public Date getIndateEnd() {
		return indateEnd;
	}

	public void setIndateEnd(Date indateEnd) {
		this.indateEnd = indateEnd;
	}

	public String getMarketImgs() {
		return marketImgs;
	}

	public void setMarketImgs(String marketImgs) {
		this.marketImgs = marketImgs;
	}

	public String getMarketIntro() {
		return marketIntro;
	}

	public void setMarketIntro(String marketIntro) {
		this.marketIntro = marketIntro;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
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
