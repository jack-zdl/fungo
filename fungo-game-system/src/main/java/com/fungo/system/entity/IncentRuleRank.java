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
 * 用户级别、身份、荣誉规则表
 * </p>
 *
 * @author lzh
 * @since 2018-12-03
 */
@TableName("t_incent_rule_rank")
public class IncentRuleRank extends Model<IncentRuleRank> {

    private static final long serialVersionUID = 1L;

	@TableId(value = "id",type = IdType.UUID)
	private Long id;
    /**
     * 属于的规则种类ID
     */
	@TableField("rank_group_id")
	private String rankGroupId;
    /**
     * 级别代码
     */
	@TableField("rank_code")
	private String rankCode;
    /**
     * 规则编码(int型)
     */
	@TableField("rank_idt")
	private Integer rankIdt;
    /**
     * 级别名称
     */
	@TableField("rank_name")
	private String rankName;
    /**
     * 级别介绍
     */
	@TableField("rank_intro")
	private String rankIntro;
    /**
     * 级别logo图标集,json格式
图标的状态( 1 标准、2 点亮、3 置灰)，
图标的顺序(自然序号)，
     */
	@TableField("rank_imgs")
	private String rankImgs;
    /**
     * 是否启用
1启用
2停用
     */
	@TableField("is_active")
	private Integer isActive;
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
     * 授权率
如：98.9%
     */
	@TableField("grant_rate")
	private Float grantRate;

	/**
	 *  优先级排序
	 */
	@TableField("sort")
	private Integer sort;


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

	public String getRankGroupId() {
		return rankGroupId;
	}

	public void setRankGroupId(String rankGroupId) {
		this.rankGroupId = rankGroupId;
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

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public String getRankIntro() {
		return rankIntro;
	}

	public void setRankIntro(String rankIntro) {
		this.rankIntro = rankIntro;
	}

	public String getRankImgs() {
		return rankImgs;
	}

	public void setRankImgs(String rankImgs) {
		this.rankImgs = rankImgs;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getRankType() {
		return rankType;
	}

	public void setRankType(Integer rankType) {
		this.rankType = rankType;
	}

	public Float getGrantRate() {
		return grantRate;
	}

	public void setGrantRate(Float grantRate) {
		this.grantRate = grantRate;
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

	@Override
	public String toString() {
		return "IncentRuleRank{" +
				"id=" + id +
				", rankGroupId='" + rankGroupId + '\'' +
				", rankCode='" + rankCode + '\'' +
				", rankIdt=" + rankIdt +
				", rankName='" + rankName + '\'' +
				", rankIntro='" + rankIntro + '\'' +
				", rankImgs='" + rankImgs + '\'' +
				", isActive=" + isActive +
				", rankType=" + rankType +
				", grantRate=" + grantRate +
				", sort=" + sort +
				", ext1='" + ext1 + '\'' +
				", ext2='" + ext2 + '\'' +
				", ext3='" + ext3 + '\'' +
				'}';
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
