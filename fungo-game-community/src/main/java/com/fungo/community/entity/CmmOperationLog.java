package com.fungo.community.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
*  t_cmm_operation_log
* @author ysx 2020-01-03
*/
@TableName("t_cmm_operation_log")
public class CmmOperationLog extends Model<CmmOperationLog> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @TableId(value = "id",type = IdType.UUID)
    private String id;

    /**
    * 圈子id
    */
    @TableField("circle_id")
    private String circleId;

    /**
    * 圈主id
    */
    @TableField("member_id")
    private String memberId;

    /**
    * 操作目标id
    */
    @TableField("target_id")
    private String targetId;

    /**
    * 目标类型 1.文章 2.用户
    */
    @TableField("target_type")
    private Integer targetType;

    /**
    * 目标操作前状态
    */
    @TableField("old_target_state")
    private String oldTargetState;

    /**
    * 目标操作后状态
    */
    @TableField("new_target_state")
    private String newTargetState;

    /**
    * 1.加精 2.置顶 3.修改分类 4.隐藏 5.关闭评论 6.禁言 各状态数字*100 为对应的解除操作 例 100 解除加精
    */
    @TableField("action_type")
    private Integer actionType;

    /**
    * created_at
    */
    @TableField("created_at")
    private Date createdAt;

    /**
    * updated_at
    */
    @TableField("updated_at")
    private Date updatedAt;


    public CmmOperationLog() {
    }

    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Integer getTargetType() {
        return targetType;
    }

    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }

    public String getOldTargetState() {
        return oldTargetState;
    }

    public void setOldTargetState(String oldTargetState) {
        this.oldTargetState = oldTargetState;
    }

    public String getNewTargetState() {
        return newTargetState;
    }

    public void setNewTargetState(String newTargetState) {
        this.newTargetState = newTargetState;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
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

}