package com.fungo.games.controller;

import com.fungo.games.service.IDeveloperService;
import com.game.common.dto.DeveloperGame.DeveloperGamePageInput;
import com.game.common.dto.DeveloperGame.DeveloperQueryIn;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.GameHistoryOut;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@Api(value="",description="开发者")
public class DeveloperController {
	/*@Autowired
	private DeveloperService developerService;*/
	@Autowired
	private IDeveloperService iDeveloperService;
	
	/*@ApiOperation(value="绑定开发者信息", notes="")
	@RequestMapping(value="/api/developer/addUser", method= RequestMethod.POST)
	@ApiImplicitParams({})
	public ResultDto<String> addDeveloper (MemberUserProfile memberUserPrefile,@RequestBody DeveloperBean input){
		Developer developer=developerService.selectOne(new EntityWrapper<Developer>().eq("member_id",memberUserPrefile.getLoginId()));
		if(developer!=null) {
			return ResultDto.error("7001", "对不起，您已绑定开发者信息");
		}else {
			developer=new Developer();
			developer.setBusinessLicense(input.getBusinessLicense());
			developer.setBusinessLicenseImage(input.getBusinessLicenseImage());
//			developer.setBusinessPermitLimitDate(input.getBusinessPermitLimitDate());
			developer.setCompanyFullName(input.getCompanyFullName());
			developer.setCompanyName(input.getCompanyName());
			developer.setCompanyShortName(input.getCompanyShortName());
			developer.setCreatedAt(new Date());
			developer.setLiaisonAdress(input.getLiaisonAdress());
			developer.setLiaisonEmail(input.getLiaisonEmail());
			developer.setLiaisonIdImageBack(input.getLiaisonIdImageBack());
			developer.setLiaisonIdNumber(input.getLiaisonIdNumber());
			developer.setLiaisonIdImageFront(input.getLiaisonIdImageFront());
			developer.setLiaisonName(input.getLiaisonName());
			developer.setLiaisonPhone(input.getLiaisonPhone());
			developer.setLogo(input.getLogo());
			developer.setMemberId(memberUserPrefile.getLoginId());
			developer.setState(0);
			developer.setUpdatedAt(new Date());
			developer.setApproveState(2);
			developerService.insert(developer);

		}
		ResultDto<String> re=new ResultDto<String>();
		re.setMessage("绑定成功");
		return re;
	}

	@ApiOperation(value="开发者游戏列表", notes="")
	@RequestMapping(value="/api/developer/gameList", method= RequestMethod.POST)
	@ApiImplicitParams({})
	public FungoPageResultDto<GameOutBean> gameList (MemberUserProfile memberUserPrefile,@RequestBody DeveloperGamePageInput input){

		
		return iDeveloperService.gameList(input,memberUserPrefile.getLoginId());
	}
	
	@ApiOperation(value="开发者消息", notes="")
	@RequestMapping(value="/api/developer/messageList", method= RequestMethod.POST)
	@ApiImplicitParams({})
	public ResultDto<List<Map<String,Object>>> messageList (MemberUserProfile memberUserPrefile,@RequestBody DeveloperBean input){

		
		return iDeveloperService.messageList();
	}
	@ApiOperation(value="开发者游戏详情", notes="")
	@RequestMapping(value="/api/developer/gameDetail/{gameId}", method= RequestMethod.POST)
	@ApiImplicitParams({})
	public ResultDto<DeveloperGameOut> gameDetail (MemberUserProfile memberUserPrefile,@PathVariable("gameId") String gameId){
		boolean b = iDeveloperService.checkDpPower(memberUserPrefile.getLoginId());
		if(!b) {
			return ResultDto.error("-1", "没有开发者权限");
		}
		
		return iDeveloperService.gameDetail(gameId,memberUserPrefile.getLoginId());
	}*/
	
	/*@ApiOperation(value="开发者上传游戏", notes="")
	@RequestMapping(value="/api/developer/addGame", method= RequestMethod.POST)
	@ApiImplicitParams({})
	public ResultDto<String> addGame(MemberUserProfile memberUserPrefile,@RequestBody AddGameInputBean input){

		
		return iDeveloperService.addGame(memberUserPrefile, input);
	}*/
	
	@ApiOperation(value="游戏统计数据", notes="")
	@RequestMapping(value="/api/developer/gemeAnalyze", method= RequestMethod.POST)
	@ApiImplicitParams({})
	public ResultDto<List<Map<String,Object>>> gemeAnalyze(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody DeveloperQueryIn input){
		return iDeveloperService.gemeAnalyzeLog(input);
	}
	
	/*@ApiOperation(value="社区统计数据", notes="")
	@RequestMapping(value="/api/developer/communityAnalyze", method= RequestMethod.POST)
	@ApiImplicitParams({})
	public ResultDto<Map<String,Integer>> communityAnalyze(@Anonymous MemberUserProfile memberUserPrefile,@RequestBody DeveloperQueryIn input) throws ParseException{

		
		return iDeveloperService.communityAnalyze(input);
	}*/
	@ApiOperation(value="游戏更新历史", notes="")
	@RequestMapping(value="/api/developer/gameHistory", method= RequestMethod.POST)
	@ApiImplicitParams({})
	public FungoPageResultDto<GameHistoryOut> gameHistory(MemberUserProfile memberUserPrefile, @RequestBody DeveloperGamePageInput input){
//		boolean b = iDeveloperService.checkDpPower(memberUserPrefile.getLoginId());
//		if(!b) {
//			return FungoPageResultDto.error("-1", "没有开发者权限");
//		}
		return iDeveloperService.gameHistory(memberUserPrefile.getLoginId(),input);
	}
	
	/*@ApiOperation(value="开发者更新游戏", notes="")
	@RequestMapping(value="/api/developer/updateGame", method= RequestMethod.POST)
	@ApiImplicitParams({})
	public ResultDto<String> updateGame(MemberUserProfile memberUserPrefile,@RequestBody AddGameInputBean input){

		
		return iDeveloperService.updateGame(memberUserPrefile,input);
	}*/
	
	
	/*public ResultDto<String> updateGameState(DeveloperQueryIn input){
		
		return null;
//		return iDeveloperService.updateGameState();
	}*/

}
