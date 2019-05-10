package com.fungo.community.service;


import com.game.common.dto.ResultDto;
import com.game.common.dto.community.MoodBean;
import com.game.common.dto.community.MoodInput;
import com.sun.corba.se.spi.ior.ObjectId;

public interface IMoodService {
	//发布心情
	public ResultDto<ObjectId> addMood(String memberId, MoodInput input) throws Exception;
	//删除心情
	public ResultDto<String> delMood(String memberId, String moodId);
	//心情详情
	public ResultDto<MoodBean> getMood(String memberId, String moodId) throws Exception;
}
