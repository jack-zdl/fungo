package com.fungo.games.controller.portal;


import com.fungo.games.service.IEvaluateService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.evaluation.*;
import com.game.common.util.ValidateUtils;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="",description="评价接口")
public class PortalGamesEvaluateController {
	@Autowired
	private IEvaluateService evaluateService;
	
	/****************************************************游戏评论管理*********************************************************************************/
	
	@ApiOperation(value="发表评价（游戏）2.4修改", notes="")
	@RequestMapping(value="/api/portal/games/content/evaluation", method= RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "content",value = "内容",paramType = "form",dataType = "string"),
			@ApiImplicitParam(name = "images",value = "图片列表",paramType = "path",dataType = "List<String>"),
			@ApiImplicitParam(name = "is_recommend",value = "是否推荐",paramType = "form",dataType = "boolean"),
			@ApiImplicitParam(name = "phone_model",value = "平台类型",paramType = "form",dataType = "string"),
			@ApiImplicitParam(name = "target_id",value = "游戏id",paramType = "form",dataType = "string")
	})
	public ResultDto<EvaluationBean> addGameEvaluation(MemberUserProfile memberUserPrefile, @RequestBody EvaluationInput commentInput) throws Exception {
		return this.evaluateService.addGameEvaluation(memberUserPrefile.getLoginId(), commentInput);
	}
	
	@ApiOperation(value="游戏评价详情", notes="")
	@RequestMapping(value="/api/portal/games/content/evaluation/{evaluationId}", method= RequestMethod.GET)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "evaluation_id",value = "心情id",paramType = "path",dataType = "string"),
			@ApiImplicitParam(name = "user_id",value = "用户id",paramType = "path",dataType = "string")
	})
	public ResultDto<EvaluationOutBean> getEvaluationDetail(@Anonymous MemberUserProfile memberUserPrefile, @PathVariable("evaluationId") String commentId) {
		ValidateUtils.is(commentId).notNull();
		String memberId="";
		if(memberUserPrefile!=null) {
			memberId=memberUserPrefile.getLoginId();
		}
		return this.evaluateService.getEvaluationDetail(memberId, commentId);
	}
	
	
	@ApiOperation(value="游戏评价列表", notes="")
	@RequestMapping(value="/api/portal/games/content/evaluations", method= RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "comment_id",value = "心情id",paramType = "path",dataType = "string"),
			@ApiImplicitParam(name = "user_id",value = "用户id",paramType = "path",dataType = "string")
	})
	public FungoPageResultDto<EvaluationOutPageDto> getEvaluationList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody EvaluationInputPageDto pagedto) {
		String memberId="";
		if(memberUserPrefile!=null) {
			memberId=memberUserPrefile.getLoginId();
		}
		return this.evaluateService.getEvaluationList(memberId, pagedto);
	}
	
	@ApiOperation(value="安利墙游戏评价详情", notes="")
	@RequestMapping(value="/api/portal/games/content/anliEvaluation/{evaluationId}", method= RequestMethod.GET)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "evaluation_id",value = "心情id",paramType = "path",dataType = "string"),
			@ApiImplicitParam(name = "user_id",value = "用户id",paramType = "path",dataType = "string")
	})
	public ResultDto<EvaluationOutBean> anliEvaluationDetail(@Anonymous MemberUserProfile memberUserPrefile, @PathVariable("evaluationId") String evaluationId){
		String memberId = "";
		if(memberUserPrefile != null) {
			memberId = memberUserPrefile.getLoginId();
		}
		return evaluateService.anliEvaluationDetail(memberId,evaluationId);
	}

	/**********************************************回复*******************************************************************/

	
}
