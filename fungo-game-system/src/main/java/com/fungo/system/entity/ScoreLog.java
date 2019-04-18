package com.fungo.system.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 评分日志
 * </p>
 *
 * @author lzh
 * @since 2018-12-15
 */
@TableName("t_score_log")
public class ScoreLog extends Model<ScoreLog> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 动作类型
	 *
     */
	private String type;
    /**
     * 评分
     */
	private Integer score;
    /**
     * 信息
     */
	private String info;
    /**
     * 会员id
     */
	@TableField("member_id")
	private String memberId;
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
     * 任务规则种类表id
     */
	@TableField("group_id")
	private String groupId;
    /**
     * 任务表ID
     */
	@TableField("rule_id")
	private String ruleId;
    /**
     * 任务|活动名称
     */
	@TableField("rule_name")
	private String ruleName;
    /**
     * 任务编码的int型值
     */
	@TableField("code_idt")
	private Integer codeIdt;
    /**
     * 会员用户名
     */
	@TableField("mb_user_name")
	private String mbUserName;
    /**
     * 任务类型
1 分值
   11 任务 获取经验值
2 虚拟币
    21 营销活动  获取fungo币
    22  签到获取fungo币
    23 任务 获取fungo币
     */
	@TableField("task_type")
	private Integer taskType;
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
		 3 用户消费 fungo币
		 4 用户消费经验值

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
     * 目标类型
     */
	@TableField("target_type")
	private Integer targetType;
    /**
     * 目标ID
     */
	@TableField("target_id")
	private String targetId;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public Integer getCodeIdt() {
		return codeIdt;
	}

	public void setCodeIdt(Integer codeIdt) {
		this.codeIdt = codeIdt;
	}

	public String getMbUserName() {
		return mbUserName;
	}

	public void setMbUserName(String mbUserName) {
		this.mbUserName = mbUserName;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
