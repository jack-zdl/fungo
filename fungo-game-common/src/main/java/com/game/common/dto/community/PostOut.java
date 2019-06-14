package com.game.common.dto.community;

import com.game.common.dto.AuthorBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostOut {

	private String objectId;
	private String tags;
	private List<String> images = new ArrayList<>();
	private String cover_image;
	private String title;
	private String html;
	private String createdAt;
	private String updatedAt;
	private String content;
	private String html_origin;
	private String origin;
	private String link_url;
	private int comment_num;
	private int state;
	private int like_num;
	private int watch_num;
	private int report_num;
	private int collect_num;
	private boolean is_liked;
	private boolean is_collected;
	private Map<String,Object> link_community = new HashMap<>();

	/**
	 * 用户基本资料
	 */
	private AuthorBean author;
	private String video;
	private String html_url;
	private String txt;

	/**
	 * 游戏链接列表(2.4.3) 内容 gameId gameName gameIcon
	 */
	private List<Map<String,Object>> gameList = new ArrayList<>();

	/**
	 * 视频详情(2.4.3)
	 */
	private List<StreamInfo> videoList = new ArrayList<>();

	/**
	 * 帖子状态 1:普通 2:精华 3:置顶
	 */
	private int type;

    /**
     * 视频封面
	 */
	private String videoCoverImage;


	
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getLink_url() {
		return link_url;
	}
	public void setLink_url(String link_url) {
		this.link_url = link_url;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public void setComment_num(int comment_num) {
		this.comment_num = comment_num;
	}
	public void setState(int state) {
		this.state = state;
	}
	public void setLike_num(int like_num) {
		this.like_num = like_num;
	}
	public void setWatch_num(int watch_num) {
		this.watch_num = watch_num;
	}
	public void setReport_num(int report_num) {
		this.report_num = report_num;
	}
	public void setCollect_num(int collect_num) {
		this.collect_num = collect_num;
	}
	public void setIs_liked(boolean is_liked) {
		this.is_liked = is_liked;
	}
	public void setIs_collected(boolean is_collected) {
		this.is_collected = is_collected;
	}
	public String getCover_image() {
		return cover_image;
	}
	public void setCover_image(String cover_image) {
		this.cover_image = cover_image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getComment_num() {
		return comment_num;
	}
	public void setComment_num(Integer comment_num) {
		this.comment_num = comment_num;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getLike_num() {
		return like_num;
	}
	public void setLike_num(Integer like_num) {
		this.like_num = like_num;
	}
	public Integer getWatch_num() {
		return watch_num;
	}
	public void setWatch_num(Integer watch_num) {
		this.watch_num = watch_num;
	}
	public Boolean getIs_liked() {
		return is_liked;
	}
	public void setIs_liked(Boolean is_liked) {
		this.is_liked = is_liked;
	}
	public Boolean getIs_collected() {
		return is_collected;
	}
	public void setIs_collected(Boolean is_collected) {
		this.is_collected = is_collected;
	}

	public Integer getReport_num() {
		return report_num;
	}
	public void setReport_num(Integer report_num) {
		this.report_num = report_num;
	}
	public Integer getCollect_num() {
		return collect_num;
	}
	public void setCollect_num(Integer collect_num) {
		this.collect_num = collect_num;
	}
	public String getHtml_origin() {
		return html_origin;
	}
	public void setHtml_origin(String html_origin) {
		this.html_origin = html_origin;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public String getHtml_url() {
		return html_url;
	}
	public void setHtml_url(String html_url) {
		this.html_url = html_url;
	}
	public String getTxt() {
		return txt;
	}
	public void setTxt(String txt) {
		this.txt = txt;
	}
	public List<Map<String, Object>> getGameList() {
		return gameList;
	}
	public void setGameList(List<Map<String, Object>> gameList) {
		this.gameList = gameList;
	}
	public Map<String, Object> getLink_community() {
		return link_community;
	}
	public void setLink_community(Map<String, Object> link_community) {
		this.link_community = link_community;
	}

	public AuthorBean getAuthor() {
		return author;
	}
	public void setAuthor(AuthorBean author) {
		this.author = author;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getVideoCoverImage() {
		return videoCoverImage;
	}
	public void setVideoCoverImage(String videoCoverImage) {
		this.videoCoverImage = videoCoverImage;
	}


	public boolean isIs_liked() {
		return is_liked;
	}

	public boolean isIs_collected() {
		return is_collected;
	}

	public List<StreamInfo> getVideoList() {
		return videoList;
	}

	public void setVideoList(List<StreamInfo> videoList) {
		this.videoList = videoList;
	}
}
