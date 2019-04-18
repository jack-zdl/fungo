package com.fungo.system.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户级别、身份、荣誉规则分类表
 * </p>
 *
 * @author lzh
 * @since 2018-12-19
 */
@TableName("t_incent_rule_rank_group")
public class IncentRuleRankGroup extends Model<IncentRuleRankGroup> {

    private static final long serialVersionUID = 1L;

	private Long id;
    /**
     * 所属父分组ID,-1无父分组
     */
	@TableField("group_parent_id")
	private Long groupParentId;
    /**
     * 种类名称
     */
	@TableField("group_name")
	private String groupName;
    /**
     * 种类简介
     */
	@TableField("group_intro")
	private String groupIntro;
    /**
     * 权益类型
				1 级别
				2 身份
				3 荣誉
				4 特权服务
     */
	@TableField("rank_type")
	private Integer rankType;
    /**
     * 荣誉标识：
		1 FunGo身份证
		2  会心一击
		3 拓荒者
		4 神之手
		5 Fun之意志
		6 专属活动
		7 其他
     */
	@TableField("rank_flag")
	private Integer rankFlag;
    /**
     * 优先级排序
     */
	private Integer sort;
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
     * 扩展字段1
     */
	private String ext1;
    /**
     * 扩展字段2
     */
	private String ext2;
    /**
     * 扩展字段3
     */
	private String ext3;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGroupParentId() {
		return groupParentId;
	}

	public void setGroupParentId(Long groupParentId) {
		this.groupParentId = groupParentId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupIntro() {
		return groupIntro;
	}

	public void setGroupIntro(String groupIntro) {
		this.groupIntro = groupIntro;
	}

	public Integer getRankType() {
		return rankType;
	}

	public void setRankType(Integer rankType) {
		this.rankType = rankType;
	}

	public Integer getRankFlag() {
		return rankFlag;
	}

	public void setRankFlag(Integer rankFlag) {
		this.rankFlag = rankFlag;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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
