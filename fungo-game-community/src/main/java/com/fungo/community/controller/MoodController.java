package com.fungo.community.controller;


import com.fungo.community.service.IMoodService;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ObjectId;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.MoodBean;
import com.game.common.dto.community.MoodInput;
import com.game.common.util.StringUtil;
import com.game.common.util.annotation.Anonymous;
import com.game.common.util.annotation.LogicCheck;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.DELETE;

/**
 * 心情
 * @author sam
 *
 */
@RestController
@Api(value="",description="心情")
public class MoodController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MoodController.class);

	@Autowired
	private IMoodService moodService;




	@ApiOperation(value="发布心情", notes="")
	@PostMapping(value="/api/content/mood")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cover_image",value = "图片列表",paramType = "form",dataType = "string"),
	        @ApiImplicitParam(name = "content",value = "内容",paramType = "form",dataType = "string"),
			@ApiImplicitParam(name = "videoId", value = "视频id,  可选", paramType = "form", dataType = "string")
	})
	@LogicCheck(loginc = {"BANNED_TEXT"})
	public ResultDto<ObjectId> addMood(MemberUserProfile memberUserPrefile, @RequestBody MoodInput input) throws Exception {
		if(StringUtil.isNull(input.getContent())){
			return ResultDto.error("-1","内容不可为空");
		}
		return moodService.addMood(memberUserPrefile.getLoginId(),input);
	}



	
	@ApiOperation(value="删心情", notes="")
	@DeleteMapping(value="/api/content/mood/{moodId}")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "moodId",value = "心情id",paramType = "path",dataType = "string")
	})
	public ResultDto<String> delMood(MemberUserProfile memberUserPrefile, @PathVariable("moodId") String moodId) {
		return this.moodService.delMood(memberUserPrefile.getLoginId(), moodId);
	}



	
	@ApiOperation(value="获取心情", notes="")
	@GetMapping(value="/api/content/mood/{moodId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "moodId",value = "心情id",paramType = "path",dataType = "string")
	})
	public ResultDto<MoodBean> getMood(@Anonymous MemberUserProfile memberUserPrefile, @PathVariable("moodId") String moodId) throws Exception {
		String memberId="";
		if(memberUserPrefile!=null) {
			memberId=memberUserPrefile.getLoginId();
		}
	
		return this.moodService.getMood(memberId, moodId);
	}
	

	//----------
}
