package com.game.common.dto.index;

import com.game.common.dto.AuthorBean;
import com.game.common.dto.GameDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;

public class CardDataBean {
    @ApiModelProperty(value = "主标题", example = "")
    private String mainTitle;
    @ApiModelProperty(value = "子标题", example = "")
    private String subtitle;
    @ApiModelProperty(value = "图片url", example = "")
    private String imageUrl;
    @ApiModelProperty(value = "视频url", example = "")
    private String videoUrl;
    @ApiModelProperty(value = "显示时间", example = "")
    private String showTime;
    @ApiModelProperty(value = "内容", example = "")
    private String content;

    @ApiModelProperty(value = "左上角内容", example = "")
    private String upperLeftCorner;
    @ApiModelProperty(value = "右上角内容", example = "")
    private String upperRightCorner;
    @ApiModelProperty(value = "左下角内容", example = "")
    private String lowerLeftCorner;
    @ApiModelProperty(value = "右下角内容", example = "")
    private String lowerRightCorner;

    @ApiModelProperty(value = "行为类型   (1:内部跳转;2:外连接)", example = "")
    private String actionType = "1";
    @ApiModelProperty(value = "网页地址     需要前台App端在url后追加?token=token", example = "")
    private String href;
    @ApiModelProperty(value = "业务id", example = "")
    private String targetId;
    @ApiModelProperty(value = "业务类型", example = "")
    private int targetType;

    @ApiModelProperty(value = "创建时间", example = "")
    private String createdAt;
    @ApiModelProperty(value = "更新时间", example = "")
    private String updatedAt;
    @ApiModelProperty(value = "点赞数", example = "")
    private int likeNum;
    @ApiModelProperty(value = "回复数", example = "")
    private int replyNum;
    @ApiModelProperty(value = "是否点攒", example = "")
    private boolean isLiked;
    @ApiModelProperty(value = "是否关注", example = "")
    private boolean isFollowed;

    @ApiModelProperty(value = "会员信息", example = "")
    private AuthorBean userinfo = new AuthorBean();

    @ApiModelProperty(value = "来源（文章来源于社区，评价来源游戏）", example = "")
    private ActionBean source = new ActionBean();


    /**
     * 首页
     *  安利墙
     *  游戏攻略
     *  精华文章区
     *  关联的游戏数据
     * @return
     */
    private ArrayList<GameDto> gameDatas = new ArrayList<GameDto>();


    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public ActionBean getSource() {
        return source;
    }

    public void setSource(ActionBean source) {
        this.source = source;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpperLeftCorner() {
        return upperLeftCorner;
    }

    public void setUpperLeftCorner(String upperLeftCorner) {
        this.upperLeftCorner = upperLeftCorner;
    }

    public String getUpperRightCorner() {
        return upperRightCorner;
    }

    public void setUpperRightCorner(String upperRightCorner) {
        this.upperRightCorner = upperRightCorner;
    }

    public String getLowerLeftCorner() {
        return lowerLeftCorner;
    }

    public void setLowerLeftCorner(String lowerLeftCorner) {
        this.lowerLeftCorner = lowerLeftCorner;
    }

    public String getLowerRightCorner() {
        return lowerRightCorner;
    }

    public void setLowerRightCorner(String lowerRightCorner) {
        this.lowerRightCorner = lowerRightCorner;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
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

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public AuthorBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(AuthorBean userinfo) {
        this.userinfo = userinfo;
    }


    public ArrayList<GameDto> getGameDatas() {
        return gameDatas;
    }

    public void setGameDatas(ArrayList<GameDto> gameDatas) {
        this.gameDatas = gameDatas;
    }
}
