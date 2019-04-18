package com.game.common.bean;

import java.io.Serializable;

public class MemberPulishFromCommunity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int postNum;
	private int commentNum;
	private int evaNum;
	private String memberId;
	
	public int getPostNum() {
		return postNum;
	}
	public void setPostNum(int postNum) {
		this.postNum = postNum;
	}
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public int getEvaNum() {
		return evaNum;
	}
	public void setEvaNum(int evaNum) {
		this.evaNum = evaNum;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
}
