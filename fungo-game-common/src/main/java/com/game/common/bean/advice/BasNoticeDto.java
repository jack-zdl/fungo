package com.game.common.bean.advice;


import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *  消息数据DTO
 * </p>
 *
 * @author mxf
 * @since 2018-05-15
 */
public class BasNoticeDto implements Serializable {

    private String id;
    /**
     * 类型
     */
    private Integer type;

    /**
     * 是否已读
     */
    private Integer isRead;
    /**
     * 会员ID
     */
    private String memberId;
    /**
     * 数据
     */
    private String data;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 更新时间
     */
    private Date updatedAt;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
        return "BasNoticeDto{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", isRead=" + isRead +
                ", memberId='" + memberId + '\'' +
                ", data='" + data + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
