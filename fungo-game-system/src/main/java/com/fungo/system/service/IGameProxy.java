package com.fungo.system.service;

import com.game.common.dto.ResultDto;
import java.io.IOException;
import java.text.ParseException;

public interface IGameProxy {
	public void addNotice(int eventType, String memberId, String target_id, int target_type, String information,
						  String appVersion, String replyToId,String commentId)throws Exception;
	public ResultDto<String> addScore(int eventType, String memberId, String target_id, int target_type) throws IOException, ParseException ;
//	public int addsc(int taskType, int icode, String memberId, String target_id, int target_type) throws Exception;
	String getMemberID(String target_id, int target_type);
	int addTaskCore(int eventType, String memberId, String target_id, int target_type) throws Exception;
}
