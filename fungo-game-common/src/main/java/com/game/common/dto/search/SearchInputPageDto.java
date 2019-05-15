package com.game.common.dto.search;


import com.game.common.api.InputPageDto;

public class SearchInputPageDto extends InputPageDto {
	private static final long serialVersionUID = 1L;
	
	private String key_word;
	
	private String tag;
	
	private String sorts;



	public String getKey_word() {
		return key_word;
	}

	public void setKey_word(String key_word) {
		this.key_word = key_word;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getSorts() {
		return sorts;
	}

	public void setSorts(String sort) {
		this.sorts = sorts;
	}
	
	
}
