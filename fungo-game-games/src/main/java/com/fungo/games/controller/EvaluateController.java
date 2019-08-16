package com.fungo.games.controller;


import com.fungo.games.service.IEvaluateService;
import com.fungo.games.service.impl.EvaluateServiceImpl;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.evaluation.*;
import com.game.common.enums.AbstractResultEnum;
import com.game.common.enums.CommonEnum;
import com.game.common.repo.cache.facade.FungoCacheGame;
import com.game.common.util.StringUtil;
import com.game.common.util.ValidateUtils;
import com.game.common.util.annotation.Anonymous;
import com.game.common.vo.DelObjectListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.game.common.consts.FungoCoreApiConstant.FUNGO_CORE_API_GAME_EVALUATIONS;

@RestController
@Api(value="",description="评价接口")
public class EvaluateController {

	private static final Logger logger = LoggerFactory.getLogger( EvaluateController.class);

	@Autowired
	private FungoCacheGame fungoCacheGame;
	@Autowired
	private IEvaluateService evaluateService;
	
	/****************************************************游戏评论管理*********************************************************************************/
	
	@ApiOperation(value="发表评价（游戏）2.4修改", notes="")
	@RequestMapping(value="/api/content/evaluation", method= RequestMethod.POST)
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
	@RequestMapping(value="/api/content/evaluation/{evaluationId}", method= RequestMethod.GET)
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

	/**
	 * 功能描述: 删除评论需要登陆
	 * @auther: dl.zhang
	 * @date: 2019/8/12 13:42
	 */
	@ApiOperation(value="删除游戏评价详情", notes="")
	@RequestMapping(value="/api/content/evaluation", method= RequestMethod.DELETE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "evaluation_ids",value = "心情id集合",paramType = "path",dataType = "string"),
	})
	public ResultDto<String> delEvaluationDetail(MemberUserProfile memberUserPrefile,@RequestBody DelObjectListVO commentIds) {
		try {
			String memberId = memberUserPrefile.getLoginId();
			List<String> commentIdList = null;
			if(commentIds.getCommentIds().size() > 0 ){
				commentIdList = commentIds.getCommentIds();
			}else {
				return ResultDto.ResultDtoFactory.buildWarning( AbstractResultEnum.CODE_GAME_FOUR.getKey(),AbstractResultEnum.CODE_GAME_FOUR.getFailevalue());
			}
			ResultDto<String>  resultDto =  this.evaluateService.delEvaluationDetail(memberId, commentIdList);
			if(CommonEnum.SUCCESS.code().equals(String.valueOf(resultDto.getStatus()))){
				fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_GAME_EVALUATIONS, "", null);
			}
			return resultDto;
		}catch (Exception e){
			logger.error( "delEvaluationDetail异常",e );
			return ResultDto.ResultDtoFactory.buildError("删除游戏评价详情失败，代码异常");
		}
	}
	
	
	@ApiOperation(value="游戏评价列表", notes="")
	@RequestMapping(value="/api/content/evaluations", method= RequestMethod.POST)
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
	@RequestMapping(value="/api/content/anliEvaluation/{evaluationId}", method= RequestMethod.GET)
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
