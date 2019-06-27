package com.game.common.dto.game;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApiModel(value = "游戏详情", description = "游戏详情")
public class GameOut {

    private List<String> tags = new ArrayList<>();
    private String category_tag;
    private String update_log;
    private String itunes_id;
    private List<String> compatibility = new ArrayList<>();
    private String version_child;
    private List<String> images = new ArrayList<>();
    private String apk;
    private String release_image;
    private String name;
    private String cover_image;
    private String icon;
    private String link_community;
    private String author;
    private String origin;
    private String intro;
    private String developer;
    private String version_main;
    private String isbn_id;
    private String detail;
    private String version;
    private String objectId;
    private String createdAt;
    private String updatedAt;
    private String game_size;
    private String link_url;
    private int state;
    private int image_ratio;
    private int image_width;
    private int image_height;
    private int recent_day_count;
    private int recommend_total_count;
    private int recommend_recent_count;
    private double recommend_total_rate;
    private double recommend_recent_rate;
    private int evaluation_num;
    private boolean is_download;
    private int download_num;
    private int iosState;
    private int androidState;
    private String video;
    /**
     * appV2.5 | PCV2.0新增字段
     * 小fun说数据
     */
    private String fungoTalk;

    /**
     * 游戏礼包数量
     */
    private Integer goodsCount;

    /**
     * 游戏关联的圈子id
     */
    private String link_circle;


    @ApiModelProperty(value = "游戏评分(v2.4)", example = "")
    private double rating;
    @ApiModelProperty(value = "游戏评分比例列表(v2.4)", example = "")
    private List<RatingBean> ratingList = new ArrayList<>();
    @ApiModelProperty(value = "游戏特征比例列表(v2.4)", example = "")
    private List<TraitBean> traitList = new ArrayList<>();

    @ApiModelProperty(value = "包名", example = "")
    private String packageName;

    public int getDownload_num() {
        return download_num;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setDownload_num(int download_num) {
        this.download_num = download_num;
    }

    @ApiModelProperty(value = "是否同意条款", example = "")
    private boolean isClause = false;
    @ApiModelProperty(value = "是否绑定appleId", example = "")
    private boolean isBinding = false;
    @ApiModelProperty(value = "是否预约", example = "")
    private boolean isMake;

    public boolean isMake() {
        return isMake;
    }

    public void setMake(boolean isMake) {
        this.isMake = isMake;
    }

    public int getIosState() {
        return iosState;
    }

    public void setIosState(int iosState) {
        this.iosState = iosState;
    }

    public int getAndroidState() {
        return androidState;
    }

    public void setAndroidState(int androidState) {
        this.androidState = androidState;
    }

    private List<Map<String, Object>> recommend_list = new ArrayList<>();


    public List<Map<String, Object>> getRecommend_list() {
        return recommend_list;
    }

    public void setRecommend_list(List<Map<String, Object>> recommend_list) {
        this.recommend_list = recommend_list;
    }

    public Boolean getIs_download() {
        return is_download;
    }

    public void setIs_download(Boolean is_download) {
        this.is_download = is_download;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getUpdate_log() {
        return update_log;
    }

    public void setUpdate_log(String update_log) {
        this.update_log = update_log;
    }

    public String getItunes_id() {
        return itunes_id;
    }

    public void setItunes_id(String itunes_id) {
        this.itunes_id = itunes_id;
    }

    public String getVersion_child() {
        return version_child;
    }

    public void setVersion_child(String version_child) {
        this.version_child = version_child;
    }

    public String getApk() {
        return apk;
    }

    public void setApk(String apk) {
        this.apk = apk;
    }

    public String getRelease_image() {
        return release_image;
    }

    public void setRelease_image(String release_image) {
        this.release_image = release_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLink_community() {
        return link_community;
    }

    public void setLink_community(String link_community) {
        this.link_community = link_community;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getVersion_main() {
        return version_main;
    }

    public void setVersion_main(String version_main) {
        this.version_main = version_main;
    }

    public String getIsbn_id() {
        return isbn_id;
    }

    public void setIsbn_id(String isbn_id) {
        this.isbn_id = isbn_id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public String getGame_size() {
        return game_size;
    }

    public void setGame_size(String game_size) {
        this.game_size = game_size;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getImage_ratio() {
        return image_ratio;
    }

    public void setImage_ratio(Integer image_ratio) {
        this.image_ratio = image_ratio;
    }

    public Integer getImage_width() {
        return image_width;
    }

    public void setImage_width(Integer image_width) {
        this.image_width = image_width;
    }

    public Integer getImage_height() {
        return image_height;
    }

    public void setImage_height(Integer image_height) {
        this.image_height = image_height;
    }

    public Integer getRecent_day_count() {
        return recent_day_count;
    }

    public void setRecent_day_count(Integer recent_day_count) {
        this.recent_day_count = recent_day_count;
    }

    public Integer getRecommend_total_count() {
        return recommend_total_count;
    }

    public void setRecommend_total_count(Integer recommend_total_count) {
        this.recommend_total_count = recommend_total_count;
    }

    public Integer getRecommend_recent_count() {
        return recommend_recent_count;
    }

    public void setRecommend_recent_count(Integer recommend_recent_count) {
        this.recommend_recent_count = recommend_recent_count;
    }


    public double getRecommend_total_rate() {
        return recommend_total_rate;
    }

    public void setRecommend_total_rate(double recommend_total_rate) {
        this.recommend_total_rate = recommend_total_rate;
    }

    public double getRecommend_recent_rate() {
        return recommend_recent_rate;
    }

    public void setRecommend_recent_rate(double recommend_recent_rate) {
        this.recommend_recent_rate = recommend_recent_rate;
    }

    public int getEvaluation_num() {
        return evaluation_num;
    }

    public void setEvaluation_num(int evaluation_num) {
        this.evaluation_num = evaluation_num;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public boolean isClause() {
        return isClause;
    }

    public void setClause(boolean isClause) {
        this.isClause = isClause;
    }

    public boolean isBinding() {
        return isBinding;
    }

    public void setBinding(boolean isBinding) {
        this.isBinding = isBinding;
    }

    public List<String> getCompatibility() {
        return compatibility;
    }

    public void setCompatibility(List<String> compatibility) {
        this.compatibility = compatibility;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<TraitBean> getTraitList() {
        return traitList;
    }

    public void setTraitList(List<TraitBean> traitList) {
        this.traitList = traitList;
    }

    public List<RatingBean> getRatingList() {
        return ratingList;
    }

    public void setRatingList(List<RatingBean> ratingList) {
        this.ratingList = ratingList;
    }

    public String getFungoTalk() {
        return fungoTalk;
    }

    public void setFungoTalk(String fungoTalk) {
        this.fungoTalk = fungoTalk;
    }


    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getLink_circle() {
        return link_circle;
    }

    public void setLink_circle(String link_circle) {
        this.link_circle = link_circle;
    }

    //------------
}
