package com.fungo.community.entity;

import java.util.Date;

public class CmmPostGame {
    private String id;

    private String postId;

    private String gameId;

    private String cmmId;


    private Date createdAt;

    private Date updatedAt;

    private String postTitle;
    private String gameName;
    private String cmmName;

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getCmmName() {
        return cmmName;
    }

    public void setCmmName(String cmmName) {
        this.cmmName = cmmName;
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

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getCmmId() {
        return cmmId;
    }

    public void setCmmId(String cmmId) {
        this.cmmId = cmmId;
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