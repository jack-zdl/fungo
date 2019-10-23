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
 * 游戏
 * </p>
 *
 * @author lzh
 * @since 2019-01-02
 */
@TableName("t_game")
public class Game extends Model<Game> {

    private static final long serialVersionUID = 1L;
	@TableId(value = "id",type = IdType.UUID)
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
	@TableField("update_log")
	private String updateLog;
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
	@TableField("game_size")
	private Long gameSize;
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
     * 图片样式类型，0横，1竖
     */
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
     * 推荐数
     */
	@TableField("recommend_num")
	private Integer recommendNum;
    /**
     * 反对数
     */
	@TableField("unrecommend_num")
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
	 * 小fun说
	 */
	@TableField("fungo_talk")
	private String fungoTalk;
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
     * IOS状态 0:待开启，1预约。2.测试，3已上线,4：可下载
     */
	@TableField("ios_state")
	private Integer iosState;
    /**
     * 安卓状态 0:待开启，1预约。2.测试，3已上线
     */
	@TableField("android_state")
	private Integer androidState;
    /**
     * 开发者ID
     */
	@TableField("developer_id")
	private String developerId;
    /**
     * 测试数量
     */
	@TableField("test_number")
	private Integer testNumber;
    /**
     * 测试日期
     */
	@TableField("tset_date")
	private Date tsetDate;
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
     * 其他证明文件
     */
	private String credentials;
    /**
     * 游戏备案通知单号
     */
	@TableField("issue_id")
	private String issueId;
    /**
     * 安卓 包名称
     */
	@TableField("android_package_name")
	private String androidPackageName;
    /**
     * 游戏评论数
     */
	@TableField("comment_num")
	private Integer commentNum;


	/**
	 * 虚假下载量
	 */
	@TableField("boom_download_num")
	private Long boomDownloadNum;


	/**
	 * 游戏编号
	 */
	@TableField("game_idt_sn")
	private Long gameIdtSn;

	//----------------------零卡接入新加字段------------------
	@TableField("tab_game_id")
	private String tabGameId;

	@TableField("google_game_id")
	private String googleGameId;

	/**
	 *  视频连接是否最新
	 */
	@TableField("video_url_latest")
	private Boolean videoUrlLatest;

	/**
	 *  是否免费
	 */
	@TableField("cost_free")
	private Boolean costFree;
	/**
	 * 厂商
	 */
	private String company;
	/**
	 * 发行商
	 */
	private String publisher;

	/**
	 *  安卓状态描述
	 */
	@TableField("android_status_desc")
	private String androidStatusDesc;
	/**
	 * ios 状态描述
	 */
	@TableField("ios_status_desc")
	private String iosStatusDesc;


	/**
	 *  谷歌游戏副名称
	 */
	@TableField("google_deputy_name")
	private String googleDeputyName;

	@TableField("can_fast")
	private Boolean canFast;

	@TableField("week_download_num")
	private Long weekDownloadNum;

	@TableField("score")
	private Double score;


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

	public Long getBoomDownloadNum() {
		return boomDownloadNum;
	}

	public void setBoomDownloadNum(Long boomDownloadNum) {
		this.boomDownloadNum = boomDownloadNum;
	}

	public String getFungoTalk() {
		return fungoTalk;
	}

	public void setFungoTalk(String fungoTalk) {
		this.fungoTalk = fungoTalk;
	}


	public Long getGameIdtSn() {
		return gameIdtSn;
	}

	public void setGameIdtSn(Long gameIdtSn) {
		this.gameIdtSn = gameIdtSn;
	}


	public String getTabGameId() {
		return tabGameId;
	}

	public void setTabGameId(String tabGameId) {
		this.tabGameId = tabGameId;
	}

	public String getGoogleGameId() {
		return googleGameId;
	}

	public void setGoogleGameId(String googleGameId) {
		this.googleGameId = googleGameId;
	}

	public Boolean getVideoUrlLatest() {
		return videoUrlLatest;
	}

	public void setVideoUrlLatest(Boolean videoUrlLatest) {
		this.videoUrlLatest = videoUrlLatest;
	}

	public Boolean getCostFree() {
		return costFree;
	}

	public void setCostFree(Boolean costFree) {
		this.costFree = costFree;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getAndroidStatusDesc() {
		return androidStatusDesc;
	}

	public void setAndroidStatusDesc(String androidStatusDesc) {
		this.androidStatusDesc = androidStatusDesc;
	}

	public String getIosStatusDesc() {
		return iosStatusDesc;
	}

	public void setIosStatusDesc(String iosStatusDesc) {
		this.iosStatusDesc = iosStatusDesc;
	}

	public String getGoogleDeputyName() {
		return googleDeputyName;
	}

	public void setGoogleDeputyName(String googleDeputyName) {
		this.googleDeputyName = googleDeputyName;
	}

	public Boolean getCanFast() {
		return canFast;
	}

	public void setCanFast(Boolean canFast) {
		this.canFast = canFast;
	}

	public Long getWeekDownloadNum() {
		return weekDownloadNum;
	}

	public void setWeekDownloadNum(Long weekDownloadNum) {
		this.weekDownloadNum = weekDownloadNum;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
