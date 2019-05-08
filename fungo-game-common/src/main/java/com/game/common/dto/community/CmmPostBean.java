package com.game.common.dto.community;


import java.util.Date;

public class CmmPostBean {

    /**
     * 社区ID
     */
    private String communityId;
    private String id;
    /**
     * 会员id
     */
    private String memberId;
    /**
     * 标题
     */
    private String title;
    /**
     * 标签
     */
    private String tags;
    /**
     * html内容
     */
    private String htmlOrigin;
    /**
     * 内容
     */
    private String content;
    /**
     * 图片
     */
    private String images;
    private String coverImage;
    /**
     * 编辑时间
     */
    private Date editedAt;
    /**
     * 组织
     */
    private String origin;
    /**
     * 状态 -1:已删除 0:压缩转码中 1:正常
     */
    private Integer state;
    /**
     * 评论数
     */
    private Integer commentNum;
    /**
     * 点赞数
     */
    private Integer likeNum;
    /**
     * 收藏数
     */
    private Integer collectNum;
    /**
     * 查看数
     */
    private Integer watchNum;
    /**
     * 举报数
     */
    private Integer reportNum;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 更新时间
     */
    private Date updatedAt;
    /**
     * 类型 1:普通 2:精华 3:置顶
     */
    private Integer type;
    /**
     * 标记
     */
    private Integer sort;
    /**
     * 视频 v2.4
     */
    private String video;
    /**
     * 游戏链接json集合
     */
    private String gameList;
    /**
     * 置顶状态
     */
    private Integer topic;
    /**
     * 视频地址集合，json格式
     V2.4.3版本添加
     */
    private String videoUrls;
    /**
     * 最后回复时间
     */
    private Date lastReplyAt;
    /**
     * 视频封面
     */
    private String videoCoverImage;

    private Long postId;



    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getHtmlOrigin() {
        return htmlOrigin;
    }

    public void setHtmlOrigin(String htmlOrigin) {
        this.htmlOrigin = htmlOrigin;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Date getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(Date editedAt) {
        this.editedAt = editedAt;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    public Integer getWatchNum() {
        return watchNum;
    }

    public void setWatchNum(Integer watchNum) {
        this.watchNum = watchNum;
    }

    public Integer getReportNum() {
        return reportNum;
    }

    public void setReportNum(Integer reportNum) {
        this.reportNum = reportNum;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getGameList() {
        return gameList;
    }

    public void setGameList(String gameList) {
        this.gameList = gameList;
    }

    public Integer getTopic() {
        return topic;
    }

    public void setTopic(Integer topic) {
        this.topic = topic;
    }

    public String getVideoUrls() {
        return videoUrls;
    }

    public void setVideoUrls(String videoUrls) {
        this.videoUrls = videoUrls;
    }

    public Date getLastReplyAt() {
        return lastReplyAt;
    }

    public void setLastReplyAt(Date lastReplyAt) {
        this.lastReplyAt = lastReplyAt;
    }

    public String getVideoCoverImage() {
        return videoCoverImage;
    }

    public void setVideoCoverImage(String videoCoverImage) {
        this.videoCoverImage = videoCoverImage;
    }



    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
