package com.fungo.community.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_moo_message")
public class ContentGreen extends Model<MooMessage> {

    @TableField("id")
    private String id;

    @TableField("type")
    private Integer type;

    @TableField("content_id")
    private String contentId;

    @TableField("filter_type")
    private int filterType;

    @TableField("filter_content")
    private String filterContent;

    @TableField("isactive")
    private String isactive;

    @TableField("created_at")
    private Date createdAt;

//    @TableField("description")
//    private String description;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId == null ? null : contentId.trim();
    }

    public String getFilterContent() {
        return filterContent;
    }

    public void setFilterContent(String filterContent) {
        this.filterContent = filterContent == null ? null : filterContent.trim();
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive == null ? null : isactive.trim();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getFilterType() {
        return filterType;
    }

    public void setFilterType(int filterType) {
        this.filterType = filterType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", contentId=").append(contentId);
        sb.append(", filterContent=").append(filterContent);
        sb.append(", isactive=").append(isactive);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}