package com.fungo.games.controller.portal;


import com.fungo.games.service.IGameService;
import com.fungo.games.service.ITagService;
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
public class PortalGamesTagController {
	
	@Autowired
	private ITagService iTagService;

	@ApiOperation(value="获得全部游戏标签", notes="")
	@RequestMapping(value="/api/portal/games/tag/taglist", method= {RequestMethod.POST , RequestMethod.GET})
	public ResultDto<List> getTagListAll(@Anonymous MemberUserProfile memberUserPrefile) {
		return iTagService.getTagListAll();
		
	}

	
}
