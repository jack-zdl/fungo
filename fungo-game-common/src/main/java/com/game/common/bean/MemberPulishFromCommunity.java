package com.game.common.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class MemberPulishFromCommunity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int postNum;
	private int commentNum;
	private int evaNum;
	private String memberId;
	private int likeNum;
	

	
	
}
