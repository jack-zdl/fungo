package com.game.common.dto.community;


import com.game.common.dto.AuthorBean;

import java.util.ArrayList;
import java.util.List;

public class EvaluationBean {
	private String content;
	private String phone_model;
	private boolean is_recommend;
	private AuthorBean author;
	private List<String> images =new ArrayList<String>();
	private List<String> replys=new ArrayList<String>();
	private int reply_count=0;
	private boolean reply_more=false;
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
	public String getPhone_model() {
		return phone_model;
	}
	public void setPhone_model(String phone_model) {
		this.phone_model = phone_model;
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
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public List<String> getReplys() {
		return replys;
	}
	public void setReplys(List<String> replys) {
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
