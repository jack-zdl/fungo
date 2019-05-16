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
 * 用户权益-等级、身份、荣誉已获取汇总表
 * </p>
 *
 * @author lzh
 * @since 2018-12-03
 */
@TableName("t_incent_ranked")
public class IncentRanked extends Model<IncentRanked> {

    private static final long serialVersionUID = 1L;

	@TableId(value = "id",type = IdType.UUID)
	private Long id;
    /**
     * 会员ID
     */
	@TableField("mb_id")
	private String mbId;

	/**
	 * 会员用户名
	 */
	@TableField("mb_user_name")
	private String mbUserName;


    /**
     * 当前所处等级、身份、荣誉ID
	 * 若是等级 可以视同 等级规则表的rank_code字段值
     */
	@TableField("current_rank_id")
	private Long currentRankId;
    /**
     * 当前所处等级、身份、荣誉规则名称
     */
	@TableField("current_rank_name")
	private String currentRankName;
    /**
     * 会员完成的等级、身份、荣誉Ids，json格式:{rankid,rank_name}
     */
	@TableField("rank_idt_ids")
	private String rankIdtIds;
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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMbId() {
		return mbId;
	}

	public void setMbId(String mbId) {
		this.mbId = mbId;
	}

	public Long getCurrentRankId() {
		return currentRankId;
	}

	public void setCurrentRankId(Long currentRankId) {
		this.currentRankId = currentRankId;
	}

	public String getCurrentRankName() {
		return currentRankName;
	}

	public void setCurrentRankName(String currentRankName) {
		this.currentRankName = currentRankName;
	}

	public String getRankIdtIds() {
		return rankIdtIds;
	}

	public void setRankIdtIds(String rankIdtIds) {
		this.rankIdtIds = rankIdtIds;
	}

	public Integer getRankType() {
		return rankType;
	}

	public void setRankType(Integer rankType) {
		this.rankType = rankType;
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


	public String getMbUserName() {
		return mbUserName;
	}

	public void setMbUserName(String mbUserName) {
		this.mbUserName = mbUserName;
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
