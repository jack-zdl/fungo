package com.fungo.games.controller;


import com.fungo.games.service.IGameService;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.TagInput;
import com.game.common.dto.game.TagSelectOut;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value="",description="标签")
public class TagController {
	
//	@Autowired
//	private ITagService tagService;

	@Autowired
	private IGameService iGameService;
	
	/*@ApiOperation(value="获得全部游戏标签", notes="")
	@RequestMapping(value="/api/tag/taglist", method= {RequestMethod.POST , RequestMethod.GET})
	public ResultDto<List> getTagListAll(@Anonymous MemberUserProfile memberUserPrefile) {
		return tagService.getTagListAll();
		
	}*/
	
	@ApiOperation(value="根据游戏ID获取游戏标签列表(2.4修改)", notes="")
	@RequestMapping(value="/api/tag/game/taglist", method= RequestMethod.POST)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "gameId",value = "游戏id",paramType = "form",dataType = "string"),
		@ApiImplicitParam(name = "filter",value = "过滤字段",paramType = "form",dataType = "string")
	})
	public ResultDto<List> getGameTagList(@Anonymous MemberUserProfile memberUserPrefile,
										  @RequestBody TagInput tagInput) {
		String userId = "";
		if(memberUserPrefile != null) {
			userId = memberUserPrefile.getLoginId();
		}
		String gameId = tagInput.getGameId();
		return iGameService.getGameTagList(gameId,userId);
	}



	@ApiOperation(value="新增游戏标签", notes="")
	@RequestMapping(value="/api/tag/game/addtag", method= RequestMethod.POST)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "idList",value = "标签列表",paramType = "form",dataType = "array"),
		@ApiImplicitParam(name = "gameId",value = "游戏id",paramType = "form",dataType = "string")
	})
	public ResultDto<String> addGameTag(MemberUserProfile memberUserPrefile,@RequestBody TagInput tagInput) {
		String userId = memberUserPrefile.getLoginId();
		String[] idList = tagInput.getIdList();
		String gameId = tagInput.getGameId();
		try {
			return iGameService.addGameTag(idList, userId, gameId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultDto.error("-1", "操作失败");
		} 
	}
	
	@ApiOperation(value="发表游戏态度", notes="")
	@RequestMapping(value="/api/tag/game/addattitude", method= RequestMethod.POST)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "tagRelId",value = "游戏标签id",paramType = "form",dataType = "string"),
		@ApiImplicitParam(name = "attitude",value = "态度",paramType = "form",dataType = "int")
	})
	public ResultDto<String> addGameTagAttitude(MemberUserProfile memberUserPrefile,@RequestBody TagInput tagInput) {
		String userId = "";
		Integer attitude = tagInput.getAttitude();
		String tagRelId = tagInput.getTagRelId();
		if(memberUserPrefile != null) {
			userId = memberUserPrefile.getLoginId();
		}
		return iGameService.addGameTagAttitude(userId,tagRelId,attitude);
		
	}
	
	@ApiOperation(value="获取热门游戏标签", notes="")
	@RequestMapping(value="/api/tag/hotlist", method= RequestMethod.POST)
	public ResultDto<List> getHotTagList(@Anonymous MemberUserProfile memberUserPrefile) {
		return iGameService.getHotTagList();
	}
	
	@ApiOperation(value="获取游戏预选标签", notes="")
	@RequestMapping(value="/api/tag/taglist/selected", method= RequestMethod.POST)
	public ResultDto<TagSelectOut> getSelectTagList(MemberUserProfile memberUserPrefile, @RequestBody TagInput tagInput) {
		return iGameService.getSelectTagList(memberUserPrefile, tagInput);
	}
	
}
