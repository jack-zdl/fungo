package com.fungo.community.entity;

import java.util.Date;

public class CmmPostCircle {
    private String id;

    private String postId;

    private String circleId;

    private Integer defaultLink;

    private Date createdAt;

    private Date updatedAt;

    public Integer getDefaultLink() {
        return defaultLink;
    }

    public void setDefaultLink(Integer defaultLink) {
        this.defaultLink = defaultLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
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