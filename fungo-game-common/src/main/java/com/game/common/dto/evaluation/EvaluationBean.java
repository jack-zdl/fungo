package com.game.common.dto.evaluation;


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

	/**
	 * 功能描述: 游戏评价数据
	 * @auther: dl.zhang
	 * @date: 2019/8/5 11:39
	 */
	private int trait1;
	private int trait2;
	private int trait3;
	private int trait4;
	private int trait5;
	
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

	public int getTrait1() {
		return trait1;
	}

	public void setTrait1(int trait1) {
		this.trait1 = trait1;
	}

	public int getTrait2() {
		return trait2;
	}

	public void setTrait2(int trait2) {
		this.trait2 = trait2;
	}

	public int getTrait3() {
		return trait3;
	}

	public void setTrait3(int trait3) {
		this.trait3 = trait3;
	}

	public int getTrait4() {
		return trait4;
	}

	public void setTrait4(int trait4) {
		this.trait4 = trait4;
	}

	public int getTrait5() {
		return trait5;
	}

	public void setTrait5(int trait5) {
		this.trait5 = trait5;
	}
}
