package com.game.common.dto.user;

import com.game.common.api.InputPageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户会员Dto
 * </p>
 *
 * @author lzh
 * @since 2018-10-25
 */
public class MemberDto extends InputPageDto implements Serializable  {

	private static final long serialVersionUID = 6368560375678982536L;

	private String id;
    /**
     * 用户加密key
     */
	private String salt;
    /**
     * 最后访问时间
     */
	private Date lastVisit;
    /**
     * 邮箱
     */
	private String email;
    /**
     * 用户token
     */
	private String sesionToken;
    /**
     * 密码
	 *  两次加密：
	 *  客户端一次且大写
	 *  后端一次未大写
     */
	private String password;
    /**
     * 经验(积分)
     */
	private Integer exp;
    /**
     * 用户名称
     */
	private String userName;
    /**
     * 状态 :0正常，1:锁定，2：禁止 -1:删除
     */
	private Integer state;
    /**
     * 是否有密码  1:有，0：没有
     */
	private String hasPassword;
    /**
     * 用户等级
     */
	private Integer level;
    /**
     * 是否邮箱验证
     */
	private String emailVerified;
    /**
     * 手机号
     */
	private String mobilePhoneNum;
    /**
     * 头像
     */
	private String avatar;
    /**
     * 是否注册完成
     */
	private String complete;
    /**
     * 性别：0：女  1:男 2:未知
     */
	private Integer gender;
    /**
     * 第三方登录
     */
	private String authData;
    /**
     * 是否导出
     */
	private String importFromParse;
    /**
     * 是否手机认证
     */
	private String mobilePhoneVerified;
    /**
     * 粉丝数
     */
	private Integer followeeNum;
    /**
     * 发布数量
     */
	private Integer publishNum;
    /**
     * 关注数
     */
	private Integer followerNum;
    /**
     * 被举报数
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
     * 用户签名
     */
	private String sign;
    /**
     * 用户会员编号
     */
	private String memberNo;
    /**
     * qq_open_id
     */
	private String qqOpenId;
    /**
     * 微信OpenId
     */
	private String weixinOpenId;
    /**
     * 微博open_id
     */
	private String weiboOpenId;
    /**
     * 身份 1:普通 2:官方
     */
	private Integer status;
    /**
     * 标记
     */
	private Integer sort;
    /**
     * 类型 1:普通 2:推荐
     */
	private Integer type;

	/**
	 * 是否关注
	 * true 关注
	 * false 未关注
	 */
	private boolean isFollowed;

	private String test;
	//权限  1 权限正常 2:禁言
	private int auth;

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Date getLastVisit() {
		return lastVisit;
	}

	public void setLastVisit(Date lastVisit) {
		this.lastVisit = lastVisit;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSesionToken() {
		return sesionToken;
	}

	public void setSesionToken(String sesionToken) {
		this.sesionToken = sesionToken;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getExp() {
		return exp;
	}

	public void setExp(Integer exp) {
		this.exp = exp;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getHasPassword() {
		return hasPassword;
	}

	public void setHasPassword(String hasPassword) {
		this.hasPassword = hasPassword;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(String emailVerified) {
		this.emailVerified = emailVerified;
	}

	public String getMobilePhoneNum() {
		return mobilePhoneNum;
	}

	public void setMobilePhoneNum(String mobilePhoneNum) {
		this.mobilePhoneNum = mobilePhoneNum;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getComplete() {
		return complete;
	}

	public void setComplete(String complete) {
		this.complete = complete;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getAuthData() {
		return authData;
	}

	public void setAuthData(String authData) {
		this.authData = authData;
	}

	public String getImportFromParse() {
		return importFromParse;
	}

	public void setImportFromParse(String importFromParse) {
		this.importFromParse = importFromParse;
	}

	public String getMobilePhoneVerified() {
		return mobilePhoneVerified;
	}

	public void setMobilePhoneVerified(String mobilePhoneVerified) {
		this.mobilePhoneVerified = mobilePhoneVerified;
	}

	public Integer getFolloweeNum() {
		return followeeNum;
	}

	public void setFolloweeNum(Integer followeeNum) {
		this.followeeNum = followeeNum;
	}

	public Integer getPublishNum() {
		return publishNum;
	}

	public void setPublishNum(Integer publishNum) {
		this.publishNum = publishNum;
	}

	public Integer getFollowerNum() {
		return followerNum;
	}

	public void setFollowerNum(Integer followerNum) {
		this.followerNum = followerNum;
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

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	public String getQqOpenId() {
		return qqOpenId;
	}

	public void setQqOpenId(String qqOpenId) {
		this.qqOpenId = qqOpenId;
	}

	public String getWeixinOpenId() {
		return weixinOpenId;
	}

	public void setWeixinOpenId(String weixinOpenId) {
		this.weixinOpenId = weixinOpenId;
	}

	public String getWeiboOpenId() {
		return weiboOpenId;
	}

	public void setWeiboOpenId(String weiboOpenId) {
		this.weiboOpenId = weiboOpenId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public boolean isFollowed() {
		return isFollowed;
	}

	public void setFollowed(boolean followed) {
		isFollowed = followed;
	}

	public int getAuth() {
		return auth;
	}

	public void setAuth(int auth) {
		this.auth = auth;
	}

	@Override
	public String toString() {
		return "Member{" +
				"id='" + id + '\'' +
				", salt='" + salt + '\'' +
				", lastVisit=" + lastVisit +
				", email='" + email + '\'' +
				", sesionToken='" + sesionToken + '\'' +
				", password='" + password + '\'' +
				", exp=" + exp +
				", userName='" + userName + '\'' +
				", state=" + state +
				", hasPassword='" + hasPassword + '\'' +
				", level=" + level +
				", emailVerified='" + emailVerified + '\'' +
				", mobilePhoneNum='" + mobilePhoneNum + '\'' +
				", avatar='" + avatar + '\'' +
				", complete='" + complete + '\'' +
				", gender=" + gender +
				", authData='" + authData + '\'' +
				", importFromParse='" + importFromParse + '\'' +
				", mobilePhoneVerified='" + mobilePhoneVerified + '\'' +
				", followeeNum=" + followeeNum +
				", publishNum=" + publishNum +
				", followerNum=" + followerNum +
				", reportNum=" + reportNum +
				", createdAt=" + createdAt +
				", updatedAt=" + updatedAt +
				", sign='" + sign + '\'' +
				", memberNo='" + memberNo + '\'' +
				", qqOpenId='" + qqOpenId + '\'' +
				", weixinOpenId='" + weixinOpenId + '\'' +
				", weiboOpenId='" + weiboOpenId + '\'' +
				", status=" + status +
				", sort=" + sort +
				", type=" + type +
				", isFollowed=" + isFollowed +
				'}';
	}
}
