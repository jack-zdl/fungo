package com.game.common.dto;

import com.game.common.api.InputPageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 游戏
 * </p>
 *
 * @author lzh
 * @since 2019-01-02
 */
public class GameDto extends InputPageDto  implements Serializable{

    private static final long serialVersionUID = 1L;

	private String id;
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
	private String updateLog;
    /**
     * apk url
     */
	private String apk;
    /**
     * 下载数
     */
	private Integer downloadNum;
    /**
     * 苹果商店
     */
	private String itunesId;
	private String compatibility;
    /**
     * 游戏版本
     */
	private String versionChild;
    /**
     * 图片s
     */
	private String images;
	private Long gameSize;
    /**
     * 版本介绍图片
     */
	private String releaseImage;
    /**
     * 名称
     */
	private String name;
    /**
     * 游戏图标
     */
	private String coverImage;
    /**
     * 图片样式类型，0横，1竖
     */
	private Integer imageRatio;
    /**
     * 图标
     */
	private String icon;
    /**
     * 编辑时间
     */
	private Date editedAt;
    /**
     * 社区ID
     */
	private String communityId;
    /**
     * 会员
     */
	private String memberId;
    /**
     * 国家
     */
	private String origin;
    /**
     * 推荐数
     */
	private Integer recommendNum;
    /**
     * 反对数
     */
	private Integer unrecommendNum;
    /**
     * 状态 0：上线，1：下架，-1：删除，3待审核
     */
	private Integer state;
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
	private String versionMain;

	private String isbnId;
    /**
     * 详情
     */
	private String detail;
    /**
     * 创建时间
     */
	private Date createdAt;
    /**
     * 更新时间
     */
	private Date updatedAt;
    /**
     * 创建人
     */
	private String createdBy;
    /**
     * 更新人
     */
	private String updatedBy;
    /**
     * IOS状态 0:待开启，1预约。2.测试，3已上线,4：可下载
     */
	private Integer iosState;
    /**
     * 安卓状态 0:待开启，1预约。2.测试，3已上线
     */
	private Integer androidState;
    /**
     * 开发者ID
     */
	private String developerId;
    /**
     * 测试数量
     */
	private Integer testNumber;
    /**
     * 测试日期
     */
	private Date tsetDate;
    /**
     * 核发单图片
     */
	private String isbnImage;
    /**
     * 软件著作权登记号
     */
	private String copyrightId;
    /**
     * 软件著作权照片
     */
	private String copyrightImage;
    /**
     * 其他证明文件
     */
	private String credentials;
    /**
     * 游戏备案通知单号
     */
	private String issueId;
    /**
     * 安卓 包名称
     */
	private String androidPackageName;
    /**
     * 游戏评论数
     */
	private Integer commentNum;



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Long getGameSize() {
		return gameSize;
	}

	public void setGameSize(Long gameSize) {
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

	public Integer getRecommendNum() {
		return recommendNum;
	}

	public void setRecommendNum(Integer recommendNum) {
		this.recommendNum = recommendNum;
	}

	public Integer getUnrecommendNum() {
		return unrecommendNum;
	}

	public void setUnrecommendNum(Integer unrecommendNum) {
		this.unrecommendNum = unrecommendNum;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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

	public Integer getIosState() {
		return iosState;
	}

	public void setIosState(Integer iosState) {
		this.iosState = iosState;
	}

	public Integer getAndroidState() {
		return androidState;
	}

	public void setAndroidState(Integer androidState) {
		this.androidState = androidState;
	}

	public String getDeveloperId() {
		return developerId;
	}

	public void setDeveloperId(String developerId) {
		this.developerId = developerId;
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

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

	public String getAndroidPackageName() {
		return androidPackageName;
	}

	public void setAndroidPackageName(String androidPackageName) {
		this.androidPackageName = androidPackageName;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	@Override
	public String toString() {
		return "Game{" +
				"id='" + id + '\'' +
				", tags='" + tags + '\'' +
				", video='" + video + '\'' +
				", updateLog='" + updateLog + '\'' +
				", apk='" + apk + '\'' +
				", downloadNum=" + downloadNum +
				", itunesId='" + itunesId + '\'' +
				", compatibility='" + compatibility + '\'' +
				", versionChild='" + versionChild + '\'' +
				", images='" + images + '\'' +
				", gameSize=" + gameSize +
				", releaseImage='" + releaseImage + '\'' +
				", name='" + name + '\'' +
				", coverImage='" + coverImage + '\'' +
				", imageRatio=" + imageRatio +
				", icon='" + icon + '\'' +
				", editedAt=" + editedAt +
				", communityId='" + communityId + '\'' +
				", memberId='" + memberId + '\'' +
				", origin='" + origin + '\'' +
				", recommendNum=" + recommendNum +
				", unrecommendNum=" + unrecommendNum +
				", state=" + state +
				", intro='" + intro + '\'' +
				", developer='" + developer + '\'' +
				", versionMain='" + versionMain + '\'' +
				", isbnId='" + isbnId + '\'' +
				", detail='" + detail + '\'' +
				", createdAt=" + createdAt +
				", updatedAt=" + updatedAt +
				", createdBy='" + createdBy + '\'' +
				", updatedBy='" + updatedBy + '\'' +
				", iosState=" + iosState +
				", androidState=" + androidState +
				", developerId='" + developerId + '\'' +
				", testNumber=" + testNumber +
				", tsetDate=" + tsetDate +
				", isbnImage='" + isbnImage + '\'' +
				", copyrightId='" + copyrightId + '\'' +
				", copyrightImage='" + copyrightImage + '\'' +
				", credentials='" + credentials + '\'' +
				", issueId='" + issueId + '\'' +
				", androidPackageName='" + androidPackageName + '\'' +
				", commentNum=" + commentNum +
				'}';
	}
}
