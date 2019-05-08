package com.game.common.dto.community;

import com.game.common.dto.AuthorBean;

import java.util.ArrayList;
import java.util.List;


public class CommentOutPageDto {
    private String content;
    private AuthorBean author;
    private List<ReplyBean> replys = new ArrayList<ReplyBean>();
    private int reply_count = 0;
    private int like_num = 0;
    private boolean reply_more = false;
    private boolean is_host = false;
    private int floor = 0;
    private String objectId;
    private String createdAt;
    private String updatedAt;
    private boolean is_liked;

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AuthorBean getAuthor() {
        return author;
    }

    public void setAuthor(AuthorBean author) {
        this.author = author;
    }

    public List<ReplyBean> getReplys() {
        return replys;
    }

    public void setReplys(List<ReplyBean> replys) {
        this.replys = replys;
    }

    public int getReply_count() {
        return reply_count;
    }

    public void setReply_count(int reply_count) {
        this.reply_count = reply_count;
    }

    public boolean isReply_more() {
        return reply_more;
    }

    public void setReply_more(boolean reply_more) {
        this.reply_more = reply_more;
    }

    public boolean isIs_host() {
        return is_host;
    }

    public void setIs_host(boolean is_host) {
        this.is_host = is_host;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isIs_liked() {
        return is_liked;
    }

    public void setIs_liked(boolean is_liked) {
        this.is_liked = is_liked;
    }

}
