package com.game.common.entity.incent;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 用户权益-任务完成汇总表
 * </p>
 *
 * @author lzh
 * @since 2018-12-18
 */
@TableName("t_incent_tasked")
public class IncentTasked extends Model<IncentTasked> {

    private static final long serialVersionUID = 1L;

	private Long id;
    /**
     * 会员ID
     */
	@TableField("mb_id")
	private String mbId;
    /**
     * 当前所处任务ID
     */
	@TableField("current_task_id")
	private String currentTaskId;
    /**
     * 当前任务名称
     */
	@TableField("current_task_name")
	private String currentTaskName;
    /**
     * 会员完成的任务Ids，json格式:[   {1:taskid,2:taskname,3:score,4:type,5:count}   ]，(任务id,任务名称，获取的分值|虚拟币数，任务类型，完成数量)用来过滤限制单位已完成任务
     */
	@TableField("task_idt_ids")
	private String taskIdtIds;
    /**
     * 任务类型
1 分值
   11 任务 获取经验值
2 虚拟币
    21 营销活动  获取fungo币
    22  签到获取fungo币
    23  任务 获取fungo币
     */
	@TableField("task_type")
	private Integer taskType;
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

	public String getMbId() {
		return mbId;
	}

	public void setMbId(String mbId) {
		this.mbId = mbId;
	}

	public String getCurrentTaskId() {
		return currentTaskId;
	}

	public void setCurrentTaskId(String currentTaskId) {
		this.currentTaskId = currentTaskId;
	}

	public String getCurrentTaskName() {
		return currentTaskName;
	}

	public void setCurrentTaskName(String currentTaskName) {
		this.currentTaskName = currentTaskName;
	}

	public String getTaskIdtIds() {
		return taskIdtIds;
	}

	public void setTaskIdtIds(String taskIdtIds) {
		this.taskIdtIds = taskIdtIds;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
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
