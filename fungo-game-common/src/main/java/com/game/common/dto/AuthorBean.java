package com.game.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@ApiModel(value="会员信息",description="会员信息")
@ToString
public class AuthorBean {

    public AuthorBean() {

    }

    @ApiModelProperty(value="会员名称",example="")
	private String username;


    @ApiModelProperty(value="头像",example="")
	private String avatar;


    @ApiModelProperty(value="会员ID",example="")
	private String objectId;


    @ApiModelProperty(value="会员级别",example="")
	private int level;


    @ApiModelProperty(value="创建时间",example="")
	private String createdAt;


    @ApiModelProperty(value="更新时间",example="")
	private String updatedAt;


    @ApiModelProperty(value="会员号",example="")
	private String memberNo;


    @ApiModelProperty(value="粉丝人数",example="")
    private Integer followeeNum;


    @ApiModelProperty(value="关注人数",example="")
    private Integer followerNum;


    @ApiModelProperty(value="关注社区数",example="")
    private Integer communityCount;


    @ApiModelProperty(value="用户签名",example="")
    private String sign;


    @ApiModelProperty(value="开发者状态 -1:未申请 0: 未审核 1: 审核中 2: 审核通过  3: 审核失败",example="")
    private int developerState = -1;


    @ApiModelProperty(value="是否关联游戏",example="")
    private boolean hasLinkedGame;
    
    @ApiModelProperty(value="用户等级身份(2.4.3)",example="")
    private String dignityImg;


    @ApiModelProperty(value="用户勋章(2.4.3)",example="")
    private List<String> honorImgList = new ArrayList<>();


    @ApiModelProperty(value="用户官方身份(2.4.3)",example="")
    private List<HashMap<String,Object>> statusImg = new ArrayList<>();


    @ApiModelProperty(value="Fun身份证(2.4.3)",example="")
    private String funImg;


    @ApiModelProperty(value="最新获得的勋章名字",example="")
    private String lastestHonorName;


    @ApiModelProperty(value="是否关注",example="")
    private boolean  is_followed;


    @ApiModelProperty(value="性别：0：女  1:男 2:未知",example="")
    private int gender;

    @ApiModelProperty(value="PC2.0互相关注：0：否  1:是",example="")
    private String mutualFollowed;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public Integer getFolloweeNum() {
        return followeeNum;
    }

    public void setFolloweeNum(Integer followeeNum) {
        this.followeeNum = followeeNum;
    }

    public Integer getFollowerNum() {
        return followerNum;
    }

    public void setFollowerNum(Integer followerNum) {
        this.followerNum = followerNum;
    }

    public Integer getCommunityCount() {
        return communityCount;
    }

    public void setCommunityCount(Integer communityCount) {
        this.communityCount = communityCount;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getDeveloperState() {
        return developerState;
    }

    public void setDeveloperState(int developerState) {
        this.developerState = developerState;
    }

    public boolean isHasLinkedGame() {
        return hasLinkedGame;
    }

    public void setHasLinkedGame(boolean hasLinkedGame) {
        this.hasLinkedGame = hasLinkedGame;
    }

    public String getDignityImg() {
        return dignityImg;
    }

    public void setDignityImg(String dignityImg) {
        this.dignityImg = dignityImg;
    }

    public List<String> getHonorImgList() {
        return honorImgList;
    }

    public void setHonorImgList(List<String> honorImgList) {
        this.honorImgList = honorImgList;
    }

    public List<HashMap<String, Object>> getStatusImg() {
        return statusImg;
    }

    public void setStatusImg(List<HashMap<String, Object>> statusImg) {
        this.statusImg = statusImg;
    }

    public String getFunImg() {
        return funImg;
    }

    public void setFunImg(String funImg) {
        this.funImg = funImg;
    }

    public String getLastestHonorName() {
        return lastestHonorName;
    }

    public void setLastestHonorName(String lastestHonorName) {
        this.lastestHonorName = lastestHonorName;
    }

    public boolean isIs_followed() {
        return is_followed;
    }

    public void setIs_followed(boolean is_followed) {
        this.is_followed = is_followed;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getMutualFollowed() {
        return mutualFollowed;
    }

    public void setMutualFollowed(String mutualFollowed) {
        this.mutualFollowed = mutualFollowed;
    }
}

