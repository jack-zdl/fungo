package com.fungo.system.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

@TableName("t_member_coupon")
public class MemberCoupon extends Model<MemberCoupon> {

    @TableId(value = "id",type = IdType.UUID)
    private String id;

    /**
     * 功能描述:  1 普通用户类型购买 2 邀请人类型 3 被邀请人类型
     * @date: 2019/10/15 16:56
     */
    @TableField("member_type")
    private Integer memberType;

    /**
     * 功能描述: 用户id
     * @date: 2019/10/15 16:56
     */
    @TableField("member_id")
    private String memberId;

    /**
     * 功能描述: 邀请人id主要针对type为3的
     * @date: 2019/10/15 16:56
     */
    @TableField("invitee_id")
    private String inviteeId;

    /**
     * 功能描述: 优惠券id
     * @date: 2019/10/15 16:56
     */
    @TableField("coupon_id")
    private String couponId;

    /**
     * 功能描述: 是否有效
     * @date: 2019/10/15 16:56
     */
    @TableField("isactive")
    private String isactive;

    /**
     * 功能描述: 创建人
     * @date: 2019/10/15 16:56
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 功能描述:  创建时间
     * @date: 2019/10/15 16:56
     */
    @TableField("created_at")
    private Date createdAt;

    /**
     * 功能描述: 修改人
     * @date: 2019/10/15 16:56
     */
    @TableField("updated_by")
    private String updatedBy;

    /**
     * 功能描述: 修改时间
     * @date: 2019/10/15 16:56
     */
    @TableField("updated_at")
    private Date updatedAt;

    /**
     * 功能描述: 版本
     * @date: 2019/10/15 16:56
     */
    @TableField("rversion")
    private Integer rversion;

    /**
     * 功能描述: 描述
     * @date: 2019/10/15 16:56
     */
    @TableField("description")
    private String description;

    /**
     * 功能描述:  发送状态 0  未发送 1 发送成功 2 发送失败
     * @date: 2019/10/15 16:56
     */
    @TableField("state")
    private int state;

    /**
     * 功能描述: 发送时间
     * @date: 2019/10/15 16:56
     */
    @TableField("send_date")
    private Date sendDate;

    /**
     * 功能描述: 用于记录零卡返回数据
     * @date: 2019/10/15 16:56
     */
    @TableField("send_log")
    private String sendLog;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Integer getMemberType() {
        return memberType;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId == null ? null : memberId.trim();
    }

    public String getInviteeId() {
        return inviteeId;
    }

    public void setInviteeId(String inviteeId) {
        this.inviteeId = inviteeId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId == null ? null : couponId.trim();
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive == null ? null : isactive.trim();
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy == null ? null : updatedBy.trim();
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getRversion() {
        return rversion;
    }

    public void setRversion(Integer rversion) {
        this.rversion = rversion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getSendLog() {
        return sendLog;
    }

    public void setSendLog(String sendLog) {
        this.sendLog = sendLog;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", memberType=").append(memberType);
        sb.append(", memberId=").append(memberId);
        sb.append(", inviteeId=").append(inviteeId);
        sb.append(", couponId=").append(couponId);
        sb.append(", isactive=").append(isactive);
        sb.append(", createdBy=").append(createdBy);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedBy=").append(updatedBy);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", rversion=").append(rversion);
        sb.append(", description=").append(description);
        sb.append("]");
        return sb.toString();
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}