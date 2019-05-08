package com.game.common.dto.community;

import com.game.common.api.InputPageDto;

public class CommunityInputPageDto extends InputPageDto {


	private static final long serialVersionUID = 8231698396441444397L;
	private String key_word;
	private String communityId;
	public String getKey_word() {
		return key_word;
	}
	public void setKey_word(String key_word) {
		this.key_word = key_word;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getCommunityId() {
		return communityId;
	}
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	
	
	
	
}
