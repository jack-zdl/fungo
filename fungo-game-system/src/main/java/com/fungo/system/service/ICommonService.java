package com.fungo.system.service;

import com.game.common.dto.ActionInput;
import com.game.common.dto.FeedbackBean;
import com.game.common.dto.ResultDto;

import java.util.List;
import java.util.Map;

public interface ICommonService {
	
	//获取举报类型
	public ResultDto<List<String>> getReportType(String type) ;
	
	//弃用
	public ResultDto<Map<String,String>> getAppConfig() ;
	
	//弃用
	public ResultDto<Map<String,String>> checkUpdate() ;
	
	//反馈
	public ResultDto<String> feedback(String memberId, FeedbackBean feedBack) throws Exception;
	
	//弃用
	public ResultDto<String> followUser(ActionInput input);
	
	//弃用
	public ResultDto<String> unFollowUser(ActionInput input);

//	ResultDto<String> uploadFile();
}
