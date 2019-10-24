package com.fungo.system.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

@TableName("t_member_info")
public class MemberInfo extends Model<MemberInfo> {

    @TableId(value = "id",type = IdType.UUID)
    private String id;

    /**
     * 功能描述: t_member的主键,逻辑外键
     * @auther: dl.zhang
     * @date: 2019/8/26 16:00
     */
    @TableField("md_id")
    private String mdId;

    /**
     * 功能描述: 分享状态 x 1 是否中秋分享
     * @auther: dl.zhang
     * @date: 2019/8/26 16:00
     */
    @TableField("share_type")
    private Integer shareType;

    /**
     * 功能描述: 是否有效
     * @auther: dl.zhang
     * @date: 2019/8/26 16:00
     */
    @TableField("isactive")
    private String isactive;

    /**
     * 功能描述: 创建人
     * @auther: dl.zhang
     * @date: 2019/8/26 16:00
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 功能描述: 创建时间
     * @auther: dl.zhang
     * @date: 2019/8/26 16:00
     */
    @TableField("created_at")
    private Date createdAt;

    /**
     * 功能描述: 修改人
     * @auther: dl.zhang
     * @date: 2019/8/26 16:00
     */
    @TableField("updated_by")
    private String updatedBy;

    /**
     * 功能描述: 修改时间
     * @auther: dl.zhang
     * @date: 2019/8/26 16:00
     */
    @TableField("updated_at")
    private Date updatedAt;

    /**
     * 功能描述: 版本
     * @auther: dl.zhang
     * @date: 2019/8/26 16:00
     */
    @TableField("rversion")
    private Integer rversion;

    /**
     * 功能描述: 描述
     * @auther: dl.zhang
     * @date: 2019/8/26 16:00
     */
    @TableField("description")
    private String description;

    /**
     * 功能描述: 邀请人id,如果没有邀请人就为空
     * @date: 2019/10/24 11:34
     */
    @TableField("parent_member_id")
    private String parentMemberId;

    /**
     * 功能描述: 状态 1 正常 0 未绑定微信
     * @date: 2019/10/24 11:34
     */
    @TableField("parent_member_id")
    private int  state;


    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMdId() {
        return mdId;
    }

    public void setMdId(String mdId) {
        this.mdId = mdId == null ? null : mdId.trim();
    }

    public Integer getShareType() {
        return shareType;
    }

    public void setShareType(Integer shareType) {
        this.shareType = shareType;
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

    public String getParentMemberId() {
        return parentMemberId;
    }

    public void setParentMemberId(String parentMemberId) {
        this.parentMemberId = parentMemberId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    protected Serializable pkVal() {
       return this.id;
    }
}