package com.fungo.games.service.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.games.dao.GameDao;
import com.fungo.games.dao.GameReleaseLogDao;
import com.fungo.games.entity.Game;
import com.fungo.games.entity.GameEvaluation;
import com.fungo.games.entity.GameReleaseLog;
import com.fungo.games.entity.GameSurveyRel;
import com.fungo.games.service.*;
import com.game.common.dto.DeveloperGame.DeveloperGamePageInput;
import com.game.common.dto.DeveloperGame.DeveloperQueryIn;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.AddGameInputBean;
import com.game.common.dto.game.GameHistoryOut;
import com.game.common.util.CommonUtil;
import com.game.common.util.PageTools;
import com.game.common.util.date.DateTools;
import com.game.common.util.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DeveloperServiceImpl implements IDeveloperService {
	/*@Autowired
	private DeveloperService developerService;
	@Autowired
	private DeveloperGameRelService dgrService;*/
	/*@Autowired
	private GameService gameService;*/
	@Autowired
	private GameReleaseLogService logService;
	/*@Autowired
	private CmmCommunityService communityService;
	@Autowired
	private BasLogService baslogService;*/
	@Autowired
	private GameSurveyRelService surveyRelService;
	@Autowired
	private GameEvaluationService evaluationService;
	/*@Autowired
	private CmmPostService postService;
	@Autowired
	private BasActionService actionService;
	@Autowired
	private DeveloperGameRelDao dgrDao;
	@Autowired
	private BasTagService tagService;
	@Autowired
	private MemberService memberService;*/

	/*@Autowired
	private GameTagService gameTagService;
	@Autowired
	private GameDao gameDao;
	@Autowired
	private GameReleaseLogDao gameReleaseDao;*/


	@Override
	public FungoPageResultDto<GameHistoryOut> gameHistory(String userId,
														  DeveloperGamePageInput input) {
//		Developer developer = developerService.selectOne(new EntityWrapper<Developer>().eq("member_id", userId));
//		if (developer == null) {
//			return FungoPageResultDto.error("14", "该用户没有开发者权限");
//		}

		String gameId = input.getGameId();
		Page<GameReleaseLog> page = logService.selectPage(new Page<>(input.getPage(), input.getLimit()),
				new EntityWrapper<GameReleaseLog>().eq("game_id", gameId).eq("member_id", userId));
		List<GameReleaseLog> list = page.getRecords();
		List<GameHistoryOut> olist = new ArrayList<>();
		//
		ObjectMapper mapper = new ObjectMapper();
		for (GameReleaseLog log : list) {
			GameHistoryOut out = new GameHistoryOut();
			out.setCreatedAt(DateTools.fmtDate(log.getCreatedAt()));
			out.setMessage(log.getUpdateLog());
			out.setCheckState(log.getApproveState());
			out.setVersion(log.getVersionMain() + "." + log.getVersionChild());
			try {
				out.setRemark(mapper.readValue(log.getRemark(), ArrayList.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
			olist.add(out);
		}
		FungoPageResultDto<GameHistoryOut> re = new FungoPageResultDto<GameHistoryOut>();
		PageTools.pageToResultDto(re, page);
		re.setData(olist);
		return re;
	}
	
	@Override
	public ResultDto<List<Map<String,Object>>> gemeAnalyzeLog(DeveloperQueryIn input) {
		String gameId = input.getGameId();
		//浏览 下载 预约 测试 评价 推荐率
		List<Map<String,Object>> list = new ArrayList<>();
		//浏览
		int abrowses = 0;//baslogService.selectCount(new EntityWrapper<BasLog>().eq("path", "/api/content/game/"+gameId).eq("channel", "Android"));
		int ibrowses = 0;//baslogService.selectCount(new EntityWrapper<BasLog>().eq("path", "/api/content/game/"+gameId).eq("channel", "iOS"));
		int totalbrowse = 0;//baslogService.selectCount(new EntityWrapper<BasLog>().eq("path", "/api/content/game/"+gameId));
		int wbrowses = totalbrowse - abrowses -ibrowses;
		Map<String,Object> browse = new HashMap<>();
		browse.put("name","浏览量");
		browse.put("web", wbrowses);
		browse.put("android", abrowses);
		browse.put("IOS", ibrowses);
		browse.put("total", totalbrowse);
		list.add(browse);
		//下载
		int ad = 0;//baslogService.selectCount(new EntityWrapper<BasLog>().eq("path", "/api/action/download").eq("channel", "Android").like("input_data",gameId));
		int id = 0;//baslogService.selectCount(new EntityWrapper<BasLog>().eq("path", "/api/action/download").eq("channel", "IOS").like("input_data",gameId));
		int td = 0;//baslogService.selectCount(new EntityWrapper<BasLog>().eq("path", "/api/action/download").like("input_data",gameId));
		int wd = td - ad -id;
		Map<String,Object> down = new HashMap<>();
		down.put("name","下载请求数");
		down.put("web", wd);
		down.put("android", ad);
		down.put("IOS", id);
		down.put("total", td);
		list.add(down);
		//预约
		int ar = surveyRelService.selectCount(new EntityWrapper<GameSurveyRel>().eq("game_id", gameId).eq("phone_model", "Android"));
		int ir = surveyRelService.selectCount(new EntityWrapper<GameSurveyRel>().eq("game_id", gameId).eq("phone_model", "IOS"));
		int tr = surveyRelService.selectCount(new EntityWrapper<GameSurveyRel>().eq("game_id", gameId));
		int wr = tr -ar -ir;
		Map<String,Object> appoint = new HashMap<>();
		appoint.put("name","预约数");
		appoint.put("web", wr);
		appoint.put("android", ar);
		appoint.put("IOS", ir);
		appoint.put("total", tr);
		list.add(appoint);
		//测试
		Map<String,Object> test = new HashMap<>();
		test.put("name","测试数");
		test.put("web", 0);
		test.put("android", 0);
		test.put("IOS", 0);
		test.put("total", 0);
		list.add(test);
		//评价
		int ae = evaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", gameId).eq("phone_model", "Android"));
		int ie = evaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", gameId).eq("phone_model", "IOS"));
		int te = evaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", gameId));
		int we = te -ae - ie;
		Map<String,Object> eva = new HashMap<>();
		eva.put("name","评价数");
		eva.put("web", we);
		eva.put("android", ae);
		eva.put("IOS", ie);
		eva.put("total", te);
		list.add(eva);
		//推荐率
		int ac = evaluationService.selectCount
				(new EntityWrapper<GameEvaluation>().eq("game_id", gameId).eq("phone_model", "Android").eq("is_recommend", 1));
		int ic = evaluationService.selectCount
				(new EntityWrapper<GameEvaluation>().eq("game_id", gameId).eq("phone_model", "IOS").eq("is_recommend", 1));
		int tc = evaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", gameId).eq("is_recommend", 1));
		int wc = tc - ac -ic;
		Map<String,Object> rate = new HashMap<>();
		rate.put("name","推荐率");
		rate.put("web", we == 0?"暂无推荐":wc== 0?"0%":rateTransfor(wc,we));
		rate.put("android", ae == 0?"暂无推荐":ac== 0?"0%":rateTransfor(ac,ae));
		rate.put("IOS",  ie == 0?"暂无推荐":ic== 0?"0%":rateTransfor(ic,ie));
		rate.put("total", te == 0?"暂无推荐":tc== 0?"0%":rateTransfor(tc,te));
		list.add(rate);
		return ResultDto.success(list);
		
	}
	
	public String rateTransfor(int numerator,int Denominator) {
		double c = (double)numerator / Denominator;
		
		return String.format("%.2f", c*100) + "%";
	}


	
}
