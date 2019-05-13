package com.game.common.dto.evaluation;



import com.game.common.dto.AuthorBean;

import java.util.ArrayList;
import java.util.List;

public class EvaluationOutPageDto {
	private String content;
	private List<String> images=new ArrayList<String>();
	private String phone_model;
	private int like_num;
	private boolean is_recommend;
	private AuthorBean author;
	private List<ReplyBean> replys=new ArrayList<ReplyBean>();;
	private int reply_count;
	private boolean reply_more;
	private boolean is_liked;
	private String objectId;
	private String createdAt;
	private String updatedAt;
	private int rating;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public String getPhone_model() {
		return phone_model;
	}
	public void setPhone_model(String phone_model) {
		this.phone_model = phone_model;
	}
	public int getLike_num() {
		return like_num;
	}
	public void setLike_num(int like_num) {
		this.like_num = like_num;
	}
	public boolean isIs_recommend() {
		return is_recommend;
	}
	public void setIs_recommend(boolean is_recommend) {
		this.is_recommend = is_recommend;
	}
	public AuthorBean getAuthor() {
		return author;
	}
	public void setAuthor(AuthorBean author) {
		this.author = author;
	}
	
	public List<ReplyBean> getReplys() {
		return replys;
	}
	public void setReplys(List<ReplyBean> replys) {
		this.replys = replys;
	}
	public int getReply_count() {
		return reply_count;
	}
	public void setReply_count(int reply_count) {
		this.reply_count = reply_count;
	}
	public boolean isReply_more() {
		return reply_more;
	}
	public void setReply_more(boolean reply_more) {
		this.reply_more = reply_more;
	}
	public boolean isIs_liked() {
		return is_liked;
	}
	public void setIs_liked(boolean is_liked) {
		this.is_liked = is_liked;
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
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}

}
