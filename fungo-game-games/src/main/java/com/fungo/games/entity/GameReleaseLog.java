package com.fungo.games.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 游戏版本日志审批
 * </p>
 *
 * @author lzh
 * @since 2018-11-19
 */
@TableName("t_game_release_log")
public class GameReleaseLog extends Model<GameReleaseLog> {

    private static final long serialVersionUID = 1L;
	@TableId(value = "id",type = IdType.UUID)
	private String id;
	@TableField("recommend_num")
	private Integer recommendNum;
    /**
     * 标签
     */
	private String tags;
    /**
     * 视频地址
     */
	private String video;
    /**
     * 更新日志
     */
	@TableField("update_log")
	private String updateLog;
	@TableField("unrecommend_num")
	private Integer unrecommendNum;
    /**
     * apk url
     */
	private String apk;
    /**
     * 下载数
     */
	@TableField("download_num")
	private Integer downloadNum;
    /**
     * 苹果商店
     */
	@TableField("itunes_id")
	private String itunesId;
	private String compatibility;
    /**
     * 游戏版本
     */
	@TableField("version_child")
	private String versionChild;
    /**
     * 图片s
     */
	private String images;
    /**
     * 游戏大小
     */
	@TableField("game_size")
	private Integer gameSize;
    /**
     * 版本介绍图片
     */
	@TableField("release_image")
	private String releaseImage;
    /**
     * 名称
     */
	private String name;
    /**
     * 游戏图标
     */
	@TableField("cover_image")
	private String coverImage;
    /**
     * 状态
     */
	private Integer state;
	@TableField("image_ratio")
	private Integer imageRatio;
    /**
     * 图标
     */
	private String icon;
    /**
     * 编辑时间
     */
	@TableField("edited_at")
	private Date editedAt;
    /**
     * 社区ID
     */
	@TableField("community_id")
	private String communityId;
    /**
     * 会员
     */
	@TableField("member_id")
	private String memberId;
    /**
     * 国家
     */
	private String origin;
    /**
     * 介绍
     */
	private String intro;
    /**
     * 开发者
     */
	private String developer;
    /**
     * 主版本号
     */
	@TableField("version_main")
	private String versionMain;
	@TableField("isbn_id")
	private String isbnId;
    /**
     * 详情
     */
	private String detail;
    /**
     * 创建时间
     */
	@TableField("created_at")
	private Date createdAt;
    /**
     * 更新时间
     */
	@TableField("updated_at")
	private Date updatedAt;
    /**
     * 创建人
     */
	@TableField("created_by")
	private String createdBy;
    /**
     * 更新人
     */
	@TableField("updated_by")
	private String updatedBy;
    /**
     * 游戏主id
     */
	@TableField("game_id")
	private String gameId;
    /**
     * 开发者ID
     */
	@TableField("developer_id")
	private String developerId;
    /**
     * 审批状态 0未审核 1审核中 2通过 3审核失败
     */
	@TableField("approve_state")
	private Integer approveState;
    /**
     * 审批意见
     */
	@TableField("approve_info")
	private String approveInfo;
    /**
     * 核发单图片
     */
	@TableField("isbn_image")
	private String isbnImage;
    /**
     * 软件著作权登记号
     */
	@TableField("copyright_id")
	private String copyrightId;
    /**
     * 软件著作权照片
     */
	@TableField("copyright_image")
	private String copyrightImage;
    /**
     * 游戏备案通知单号
     */
	@TableField("issue_id")
	private String issueId;
    /**
     * 其他证明文件
     */
	private String credentials;
    /**
     * 安卓状态 0: 待开启 1: 预约 2: 测试 3: 已上线
     */
	@TableField("android_state")
	private String androidState;
    /**
     * iOS状态
     */
	@TableField("ios_state")
	private String iosState;
    /**
     * 测试人数
     */
	@TableField("test_number")
	private Integer testNumber;
    /**
     * 测试开启时间
     */
	@TableField("tset_date")
	private Date tsetDate;
    /**
     * 更新备注
     */
	private String remark;
    /**
     * 社区简介
     */
	@TableField("community_intro")
	private String communityIntro;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getRecommendNum() {
		return recommendNum;
	}

	public void setRecommendNum(Integer recommendNum) {
		this.recommendNum = recommendNum;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getUpdateLog() {
		return updateLog;
	}

	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}

	public Integer getUnrecommendNum() {
		return unrecommendNum;
	}

	public void setUnrecommendNum(Integer unrecommendNum) {
		this.unrecommendNum = unrecommendNum;
	}

	public String getApk() {
		return apk;
	}

	public void setApk(String apk) {
		this.apk = apk;
	}

	public Integer getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(Integer downloadNum) {
		this.downloadNum = downloadNum;
	}

	public String getItunesId() {
		return itunesId;
	}

	public void setItunesId(String itunesId) {
		this.itunesId = itunesId;
	}

	public String getCompatibility() {
		return compatibility;
	}

	public void setCompatibility(String compatibility) {
		this.compatibility = compatibility;
	}

	public String getVersionChild() {
		return versionChild;
	}

	public void setVersionChild(String versionChild) {
		this.versionChild = versionChild;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public Integer getGameSize() {
		return gameSize;
	}

	public void setGameSize(Integer gameSize) {
		this.gameSize = gameSize;
	}

	public String getReleaseImage() {
		return releaseImage;
	}

	public void setReleaseImage(String releaseImage) {
		this.releaseImage = releaseImage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getImageRatio() {
		return imageRatio;
	}

	public void setImageRatio(Integer imageRatio) {
		this.imageRatio = imageRatio;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Date getEditedAt() {
		return editedAt;
	}

	public void setEditedAt(Date editedAt) {
		this.editedAt = editedAt;
	}

	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public String getVersionMain() {
		return versionMain;
	}

	public void setVersionMain(String versionMain) {
		this.versionMain = versionMain;
	}

	public String getIsbnId() {
		return isbnId;
	}

	public void setIsbnId(String isbnId) {
		this.isbnId = isbnId;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getDeveloperId() {
		return developerId;
	}

	public void setDeveloperId(String developerId) {
		this.developerId = developerId;
	}

	public Integer getApproveState() {
		return approveState;
	}

	public void setApproveState(Integer approveState) {
		this.approveState = approveState;
	}

	public String getApproveInfo() {
		return approveInfo;
	}

	public void setApproveInfo(String approveInfo) {
		this.approveInfo = approveInfo;
	}

	public String getIsbnImage() {
		return isbnImage;
	}

	public void setIsbnImage(String isbnImage) {
		this.isbnImage = isbnImage;
	}

	public String getCopyrightId() {
		return copyrightId;
	}

	public void setCopyrightId(String copyrightId) {
		this.copyrightId = copyrightId;
	}

	public String getCopyrightImage() {
		return copyrightImage;
	}

	public void setCopyrightImage(String copyrightImage) {
		this.copyrightImage = copyrightImage;
	}

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public String getAndroidState() {
		return androidState;
	}

	public void setAndroidState(String androidState) {
		this.androidState = androidState;
	}

	public String getIosState() {
		return iosState;
	}

	public void setIosState(String iosState) {
		this.iosState = iosState;
	}

	public Integer getTestNumber() {
		return testNumber;
	}

	public void setTestNumber(Integer testNumber) {
		this.testNumber = testNumber;
	}

	public Date getTsetDate() {
		return tsetDate;
	}

	public void setTsetDate(Date tsetDate) {
		this.tsetDate = tsetDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCommunityIntro() {
		return communityIntro;
	}

	public void setCommunityIntro(String communityIntro) {
		this.communityIntro = communityIntro;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
