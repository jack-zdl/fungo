package com.fungo.system.service.portal;

import com.fungo.system.dto.*;
import com.fungo.system.entity.IncentRuleRank;
import com.game.common.api.InputPageDto;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.MyCommentBean;
import com.game.common.dto.community.MyPublishBean;

import java.util.List;
import java.util.Map;

public interface PortalSystemIMemberService {

	//获取点赞我的
	FungoPageResultDto<Map<String,Object>> getLikeNotice(String memberId, InputPageDto inputPage, String appVersion) throws Exception;
	//获取评论我的
	FungoPageResultDto<Map<String,Object>> getCommentNotice(String memberId, InputPageDto inputPage, String appVersion) throws Exception;
	//获取系统消息
	FungoPageResultDto<SysNoticeBean> getSystemNotice(String memberId, InputPageDto inputPage);
	


}
