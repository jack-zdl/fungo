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
 * 用户权益-等级、身份、荣誉获取明细表
 * </p>
 *
 * @author lzh
 * @since 2018-12-03
 */
@TableName("t_incent_ranked_log")
public class IncentRankedLog extends Model<IncentRankedLog> {

    private static final long serialVersionUID = 1L;

	@TableId(value = "id",type = IdType.UUID)
	private Long id;
    /**
     * 用户级别、身份、荣誉规则种类ID
     */
	@TableField("rank_group_id")
	private Long rankGroupId;
    /**
     * 用户级别、身份、荣誉规则ID
     */
	@TableField("rank_rule_id")
	private Long rankRuleId;
    /**
     * 规则编码
     */
	@TableField("rank_code")
	private String rankCode;
    /**
     * 规则编码(int型)
     */
	@TableField("rank_idt")
	private Integer rankIdt;
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
     * 获取的值:(积分，级别)
     */
	private Integer score;
    /**
     * 获取时间
     */
	@TableField("gain_time")
	private Date gainTime;
    /**
     * 权益信息json格式
     */
	@TableField("ranked_info")
	private String rankedInfo;
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
     * 产生类型
1 用户
2 管理员
     */
	@TableField("produce_type")
	private Integer produceType;
    /**
     * 创建人ID
     */
	@TableField("creator_id")
	private String creatorId;
    /**
     * 创建人名称
     */
	@TableField("creator_name")
	private String creatorName;


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

	public Long getRankGroupId() {
		return rankGroupId;
	}

	public void setRankGroupId(Long rankGroupId) {
		this.rankGroupId = rankGroupId;
	}

	public Long getRankRuleId() {
		return rankRuleId;
	}

	public void setRankRuleId(Long rankRuleId) {
		this.rankRuleId = rankRuleId;
	}

	public String getRankCode() {
		return rankCode;
	}

	public void setRankCode(String rankCode) {
		this.rankCode = rankCode;
	}

	public Integer getRankIdt() {
		return rankIdt;
	}

	public void setRankIdt(Integer rankIdt) {
		this.rankIdt = rankIdt;
	}

	public String getMbId() {
		return mbId;
	}

	public void setMbId(String mbId) {
		this.mbId = mbId;
	}

	public String getMbUserName() {
		return mbUserName;
	}

	public void setMbUserName(String mbUserName) {
		this.mbUserName = mbUserName;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Date getGainTime() {
		return gainTime;
	}

	public void setGainTime(Date gainTime) {
		this.gainTime = gainTime;
	}

	public String getRankedInfo() {
		return rankedInfo;
	}

	public void setRankedInfo(String rankedInfo) {
		this.rankedInfo = rankedInfo;
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

	public Integer getProduceType() {
		return produceType;
	}

	public void setProduceType(Integer produceType) {
		this.produceType = produceType;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
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
