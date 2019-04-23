package com.fungo.system.dto;


import java.util.ArrayList;
import java.util.List;

public class CommentOut {
	private String content;
	private boolean is_host;
	private int floor;
	private AuthorBean author;
	private List<String> replys=new ArrayList<String>();
	private int reply_count=0;
	private boolean reply_more=false;
	private String objectId;
	private String createdAt;
	private String updatedAt;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isIs_host() {
		return is_host;
	}
	public void setIs_host(boolean is_host) {
		this.is_host = is_host;
	}
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
	
	public AuthorBean getAuthor() {
		return author;
	}
	public void setAuthor(AuthorBean author) {
		this.author = author;
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
	
}
