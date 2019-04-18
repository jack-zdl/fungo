package com.game.common.entity.incent;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 权益规则与功能授权关系表
 * </p>
 *
 * @author lzh
 * @since 2018-12-21
 */
@TableName("t_incent_mb_perm_ranked")
public class IncentMbPermRanked extends Model<IncentMbPermRanked> {

    private static final long serialVersionUID = 1L;

	private Long id;
    /**
     * 级别、身份、荣誉规则ID

     */
	@TableField("rank_id")
	private String rankId;
    /**
     * 规则类型
1 级别
2 身份
3 荣誉
4 特权服务
     */
	@TableField("rank_type")
	private Integer rankType;
    /**
     * 规则编码(int型)
     */
	@TableField("rank_idt")
	private String rankIdt;
    /**
     * 权益规则名称
如：1级
     */
	@TableField("rank_name")
	private String rankName;
    /**
     * 任务规则ID
     */
	@TableField("task_id")
	private String taskId;
    /**
     * 任务编码的int型值
     */
	@TableField("task_code_idt")
	private Integer taskCodeIdt;
    /**
     * 功能权限名称，对应是任务表任务数据
     */
	@TableField("perm_action_name")
	private String permActionName;
    /**
     * 权限功能对应接口URL，对应任务表任务功能url
     */
	@TableField("perm_action_url")
	private String permActionUrl;
    /**
     * 权限功能对应页面url
     */
	@TableField("to_link_url")
	private String toLinkUrl;
    /**
     * 功能编号
     */
	@TableField("fun_idt")
	private Integer funIdt;
    /**
     * 权益授权任务和功能类型
1 功能权限
2 任务权限
3 页面权限
     */
	@TableField("able_type")
	private Integer ableType;
    /**
     * 创建时间
     */
	@TableField("created_at")
	private Date createdAt;
    /**
     * 修改时间
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

	public String getRankId() {
		return rankId;
	}

	public void setRankId(String rankId) {
		this.rankId = rankId;
	}

	public Integer getRankType() {
		return rankType;
	}

	public void setRankType(Integer rankType) {
		this.rankType = rankType;
	}

	public String getRankIdt() {
		return rankIdt;
	}

	public void setRankIdt(String rankIdt) {
		this.rankIdt = rankIdt;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Integer getTaskCodeIdt() {
		return taskCodeIdt;
	}

	public void setTaskCodeIdt(Integer taskCodeIdt) {
		this.taskCodeIdt = taskCodeIdt;
	}

	public String getPermActionName() {
		return permActionName;
	}

	public void setPermActionName(String permActionName) {
		this.permActionName = permActionName;
	}

	public String getPermActionUrl() {
		return permActionUrl;
	}

	public void setPermActionUrl(String permActionUrl) {
		this.permActionUrl = permActionUrl;
	}

	public String getToLinkUrl() {
		return toLinkUrl;
	}

	public void setToLinkUrl(String toLinkUrl) {
		this.toLinkUrl = toLinkUrl;
	}

	public Integer getFunIdt() {
		return funIdt;
	}

	public void setFunIdt(Integer funIdt) {
		this.funIdt = funIdt;
	}

	public Integer getAbleType() {
		return ableType;
	}

	public void setAbleType(Integer ableType) {
		this.ableType = ableType;
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
