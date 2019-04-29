package com.game.common.dto.user;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemberOutBean {

	String mb_id;

	private String sign;
	private Float exp;
	private String username;
	private int state;
	private int publish_num;
	private boolean has_password;
	private int level;
	private boolean emailVerified;
	private int followee_num;
	private String mobilePhoneNumber;
	private String avatar;
	private int gender;
	private int follower_num;
	private Object authData;
	private int report_num;
	private boolean importFromParse;
	private boolean mobilePhoneVerified;
	private String objectId ;
	private String createdAt;
	private String updatedAt;
	@ApiModelProperty(value="Fun币(2.4.3)",example="")
	private long funCoin;
	@ApiModelProperty(value="用户等级身份(2.4.3)",example="")
    private String dignityImg;
    @ApiModelProperty(value="用户勋章(2.4.3)",example="")
    private List<String> honorImgList = new ArrayList<>();
    @ApiModelProperty(value="用户官方身份(2.4.3)",example="")
    private List<HashMap<String,Object>> statusImg = new ArrayList<>();
    @ApiModelProperty(value="Fun身份证(2.4.3)",example="")
    private String funImg;
    
    private boolean  is_followed;
	
	public String getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}
	private String memberNo;
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public Float getExp() {
		return exp;
	}
	public void setExp(Float exp) {
		this.exp = exp;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getPublish_num() {
		return publish_num;
	}
	public void setPublish_num(int publish_num) {
		this.publish_num = publish_num;
	}
	public boolean isHas_password() {
		return has_password;
	}
	public void setHas_password(boolean has_password) {
		this.has_password = has_password;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public boolean isEmailVerified() {
		return emailVerified;
	}
	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}
	public int getFollowee_num() {
		return followee_num;
	}
	public void setFollowee_num(int followee_num) {
		this.followee_num = followee_num;
	}
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}
	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public int getFollower_num() {
		return follower_num;
	}
	public void setFollower_num(int follower_num) {
		this.follower_num = follower_num;
	}
	public Object getAuthData() {
		return authData;
	}
	public void setAuthData(Object authData) {
		this.authData = authData;
	}
	public int getReport_num() {
		return report_num;
	}
	public void setReport_num(int report_num) {
		this.report_num = report_num;
	}
	public boolean isImportFromParse() {
		return importFromParse;
	}
	public void setImportFromParse(boolean importFromParse) {
		this.importFromParse = importFromParse;
	}
	public boolean isMobilePhoneVerified() {
		return mobilePhoneVerified;
	}
	public void setMobilePhoneVerified(boolean mobilePhoneVerified) {
		this.mobilePhoneVerified = mobilePhoneVerified;
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
	public long getFunCoin() {
		return funCoin;
	}
	public void setFunCoin(long funCoin) {
		this.funCoin = funCoin;
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
	public String getFunImg() {
		return funImg;
	}
	public void setFunImg(String funImg) {
		this.funImg = funImg;
	}
	public List<HashMap<String, Object>> getStatusImg() {
		return statusImg;
	}
	public void setStatusImg(List<HashMap<String, Object>> statusImg) {
		this.statusImg = statusImg;
	}
	public boolean isIs_followed() {
		return is_followed;
	}
	public void setIs_followed(boolean is_followed) {
		this.is_followed = is_followed;
	}

	public String getMb_id() {
		return mb_id;
	}

	public void setMb_id(String mb_id) {
		this.mb_id = mb_id;
	}
}
