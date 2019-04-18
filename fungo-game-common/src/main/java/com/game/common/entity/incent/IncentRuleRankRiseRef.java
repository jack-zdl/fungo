package com.game.common.entity.incent;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户级别、身份、荣誉与晋升事件的关系表
 * </p>
 *
 * @author lzh
 * @since 2018-12-03
 */
@TableName("t_incent_rule_rank_rise_ref")
public class IncentRuleRankRiseRef extends Model<IncentRuleRankRiseRef> {

    private static final long serialVersionUID = 1L;

	private Long id;
    /**
     * 级别ID
     */
	@TableField("rank_id")
	private Integer rankId;
    /**
     * 晋升事件ID
如任务task_id、身份ID、积分ID
     */
	@TableField("rise_id")
	private String riseId;
    /**
     * 晋升事件名称
如任务task_name、身份名称、积分等级名称
     */
	@TableField("rise_name")
	private String riseName;
    /**
     * 晋升事件来源类型
1  任务
2  身份
3  级别
4  积分
5  经验值
6 加入时长
7 FunGo官方认证
8 其他
7 其他
 
     */
	@TableField("rise_type")
	private Integer riseType;
    /**
     * 晋升事件优先级
     */
	@TableField("rise_priority")
	private Integer risePriority;
    /**
     * 晋升事件量阈值最小值
如：最小积分1000
     */
	@TableField("rise_vpt_min")
	private Integer riseVptMin;
    /**
     * 晋升事件量阈值最大值
如：最大积分6000
     */
	@TableField("rise_vpt_max")
	private Integer riseVptMax;
    /**
     * 同类多个规则是否并存才能生效
true 并存
false 非并存
     */
	@TableField("is_all")
	private byte[] isAll;
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

	public Integer getRankId() {
		return rankId;
	}

	public void setRankId(Integer rankId) {
		this.rankId = rankId;
	}

	public String getRiseId() {
		return riseId;
	}

	public void setRiseId(String riseId) {
		this.riseId = riseId;
	}

	public String getRiseName() {
		return riseName;
	}

	public void setRiseName(String riseName) {
		this.riseName = riseName;
	}

	public Integer getRiseType() {
		return riseType;
	}

	public void setRiseType(Integer riseType) {
		this.riseType = riseType;
	}

	public Integer getRisePriority() {
		return risePriority;
	}

	public void setRisePriority(Integer risePriority) {
		this.risePriority = risePriority;
	}

	public Integer getRiseVptMin() {
		return riseVptMin;
	}

	public void setRiseVptMin(Integer riseVptMin) {
		this.riseVptMin = riseVptMin;
	}

	public Integer getRiseVptMax() {
		return riseVptMax;
	}

	public void setRiseVptMax(Integer riseVptMax) {
		this.riseVptMax = riseVptMax;
	}

	public byte[] getIsAll() {
		return isAll;
	}

	public void setIsAll(byte[] isAll) {
		this.isAll = isAll;
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
