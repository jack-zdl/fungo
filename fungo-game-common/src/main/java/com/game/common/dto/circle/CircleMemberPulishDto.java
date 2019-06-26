package com.game.common.dto.circle;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class CircleMemberPulishDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int postNum;
	private int commentNum;
	private int evaNum;
	private int postCommentNum;
	private int postLikeNum;
	private String memberId;
	private int gamecommentNum;
	private int likeNum;
	
}
