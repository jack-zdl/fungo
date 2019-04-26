package com.fungo.system.proxy.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.entity.Developer;
import com.fungo.system.proxy.IDeveloperService;
import com.fungo.system.service.DeveloperService;
import com.game.common.dto.DeveloperGame.DeveloperGameOut;
import com.game.common.dto.DeveloperGame.DeveloperGamePageInput;
import com.game.common.dto.DeveloperGame.DeveloperQueryIn;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.AddGameInputBean;
import com.game.common.dto.game.GameHistoryOut;
import com.game.common.dto.game.GameOutBean;
import com.game.common.util.date.DateTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.util.*;

@Service
public class DeveloperServiceImpl implements IDeveloperService {
	@Override
	public ResultDto<String> addGame(MemberUserProfile memberUserPrefile, AddGameInputBean input) {
		return null;
	}

	@Override
	public ResultDto<String> updateGame(MemberUserProfile memberUserPrefile, AddGameInputBean input) {
		return null;
	}

	@Override
	public ResultDto<DeveloperGameOut> gameDetail(String gameId, String userId) {
		return null;
	}

	@Override
	public FungoPageResultDto<GameOutBean> gameList(DeveloperGamePageInput input, String userId) {
		return null;
	}

	@Override
	public FungoPageResultDto<GameHistoryOut> gameHistory(String userId, DeveloperGamePageInput input) {
		return null;
	}

	@Override
	public ResultDto<List<Map<String, Object>>> gemeAnalyzeLog(DeveloperQueryIn input) {
		return null;
	}


	@Override
	public ResultDto<Map<String, Integer>> communityAnalyze(DeveloperQueryIn input) throws ParseException {
		return null;
	}

	@Autowired
	private DeveloperService developerService;
//	@Autowired
//	private DeveloperGameRelService dgrService;
//	@Autowired
//	private GameService gameService;
//	@Autowired
//	private GameReleaseLogService logService;
//	@Autowired
//	private CmmCommunityService communityService;
//	@Autowired
//	private BasLogService baslogService;
//	@Autowired
//	private GameSurveyRelService surveyRelService;
//	@Autowired
//	private GameEvaluationService evaluationService;
//	@Autowired
//	private CmmPostService postService;
//	@Autowired
//	private BasActionService actionService;
//	@Autowired
//	private DeveloperGameRelDao dgrDao;
//	@Autowired
//	private BasTagService tagService;
//	@Autowired
//	private MemberService memberService;
//
//	@Autowired
//	private GameTagService gameTagService;
//	@Autowired
//	private GameDao gameDao;
//	@Autowired
//	private GameReleaseLogDao gameReleaseDao;
//
//
//	@Override
//	@Transactional
//	public ResultDto<String> addGame(MemberUserProfile memberUserPrefile, AddGameInputBean input) {
//		/*
//		 * apk string 否 安装包 isbnId string 否 网络出版号Id isbnImage string 否 核发单图片 copyrightId
//		 * string 否 软件著作权登记号 issueId string 否 游戏备案通知单号 credentials array 否 其他证明文件
//		 * itunesId string 否 appstoreID testNumber number 否 测试人数
//		 *
//		 */
//
//		if (input == null) {
//			return ResultDto.error("13", "添加内容不存在");
//		}
//		//用户有无开发者权限
//		Developer developer = developerService
//				.selectOne(new EntityWrapper<Developer>().eq("member_id", memberUserPrefile.getLoginId()));
//		if (developer == null || developer.getApproveState() != 2) {
//			return ResultDto.error("14", "该用户没有开发者权限");
//		}
//
//		Game game = new Game();
//
//		game.setName(input.getName());
//		if (input.getApk() != null) {
//			game.setApk(input.getApk());
//		}
//		String updateLog = input.getUpdateLog();
//		game.setUpdateLog(updateLog);
//		game.setCoverImage(input.getCoverImage());
//		Date date = new Date();
//		game.setCreatedAt(date);
//		game.setOrigin(input.getOrigin());
//		game.setUpdatedAt(date);
//		game.setCreatedBy(memberUserPrefile.getLoginId());
//		game.setDetail(input.getDetail());
//		game.setDeveloper(developer.getCompanyName());
//		game.setDownloadNum(0);
//		game.setEditedAt(date);
//		game.setIcon(input.getIcon());
//		if(input.getImages() != null) {
//			ObjectMapper mapper = new ObjectMapper();
//			try {
//				game.setImages(mapper.writeValueAsString(input.getImages()));
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//			}
//		}
//		game.setIntro(input.getGameIntro());
//		if (!CommonUtil.isNull(input.getIsbnId())) {
//			game.setIsbnId(input.getIsbnId());
//		}
//		if (!CommonUtil.isNull(input.getItunesId())) {
//			game.setItunesId(input.getItunesId());
//		}
//		game.setMemberId(memberUserPrefile.getLoginId());
//		String version = input.getVersion();
//		int i = version.indexOf(".");
//		String versionMain = version.substring(0, i);
//		String childVersion = version.substring(i + 1);
//		game.setVersionMain(versionMain);
//		game.setVersionChild(childVersion);
//		game.setUpdateLog(input.getUpdateLog());
//
//		ObjectMapper mapper = new ObjectMapper();
//
//
//		game.setTestNumber(input.getTestNumber());
//		game.setTsetDate(input.getTsetDate());
//		game.setVideo(input.getVideo());
//		game.setCopyrightId(input.getCopyrightId());
//		game.setCopyrightImage(input.getCopyrightImage());
//		if(input.getCredentials() != null) {
//			try {
//				game.setCredentials(mapper.writeValueAsString(input.getCredentials()));
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//			}
//		}
//
//		game.setIsbnId(input.getIsbnId());
//		game.setIsbnImage(input.getIsbnImage());
//		game.setIssueId(input.getIssueId());
//		String androidState = input.getAndroidState();
//		String iOState = input.getiOState();
//		 if(androidState != null) {
//			 game.setAndroidState(Integer.parseInt(androidState));
//		 }
//		 if(iOState != null) {
//			 if("2".equals(iOState)) {
//				 return ResultDto.error("-1", "iOS没有预约玩家不能测试");
//			 }
//			 game.setIosState(Integer.parseInt(iOState));
//		 }
//
//		game.setDeveloperId(developer.getId());
//		game.setState(3);
//		game.setGameSize((long)input.getSize());
//		game.insert();
//		try {
//			game.setCompatibility(mapper.writeValueAsString(new ArrayList<String>()));
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//
//		CmmCommunity c = new CmmCommunity();
//		c.setGameId(game.getId());
//		c.setCoverImage(input.getCoverImage());
//		Date d = new Date();
//		c.setCreatedAt(d);
//		c.setUpdatedAt(d);
//		c.setCreatedBy(memberUserPrefile.getLoginId());
//		c.setUpdatedBy(memberUserPrefile.getLoginId());
//		c.setIcon(game.getIcon());
//		c.setIntro(input.getCommunityIntro());
//		c.setName(game.getName());
//		c.setState(0);
//		communityService.insert(c);
//		game.setCommunityId(c.getId());
//		gameService.updateById(game);
//
//		if(input.getTagList() != null || !CommonUtil.isNull(input.getCategoryId())) {
//			ResultDto<String> addGameTag = addGameTag(input.getTagList(),input.getCategoryId(),game.getId());
//			if(!addGameTag.isSuccess()) {
//				throw new BusinessException("-1","添加标签失败:"+addGameTag.getMessage());
//			}
//		}
//
//
//		input.setGameId(game.getId());
//
//		//添加游戏关联
//		addDeveloperGameRel(input.getDeveloperList(), game.getId(), developer.getId());
//
//		GameReleaseLog log = new GameReleaseLog();
//		log.setCommunityId(game.getCommunityId());
//
//		//添加游戏记录
//		ResultDto<String> dto = copyGameTolog(input,memberUserPrefile,log,developer);
//		if(!dto.isSuccess()) {
//			throw new BusinessException("-1",dto.getMessage());
//		}
//
//		return ResultDto.success();
//	}
//
//	@Override
//	@Transactional
//	public ResultDto<String> updateGame(MemberUserProfile memberUserPrefile, AddGameInputBean input) {
//		String gameId = input.getGameId();
//		String loginId = memberUserPrefile.getLoginId();
//
//		//游戏关联表 判断用户是否与该游戏关联
//		DeveloperGameRel rel = dgrService
//				.selectOne(new EntityWrapper<DeveloperGameRel>().eq("member_id", loginId).eq("game_id", gameId));
//		if (rel == null) {
//			return ResultDto.error("234", "当前用户未与目标游戏关联");
//		}
//
//		//判断用户有没有开发者权限
//		Developer developer = developerService.selectById(rel.getDeveloperId());
//		if (developer == null || developer.getApproveState() != 2) {
//			return ResultDto.error("14", "该用户没有开发者权限");
//		}
//		Game game = gameService.selectById(gameId);
//
//		if (CommonUtil.isNull(gameId)) {
//			return ResultDto.error("13", "找不到游戏id");
//		}
//
//		if(game == null) {
//			return ResultDto.error("13", "找不到游戏目标");
//		}
//
//
//		GameReleaseLog log = logService.selectOne(
//				new EntityWrapper<GameReleaseLog>().eq("game_id", gameId).orderBy("created_at", false).last("limit 1"));
//		if (log == null) {
//			return ResultDto.error("231", "目标游戏更新记录不存在");
//		}
//
//
//		Integer approveState = log.getApproveState();
//		if (approveState != 2 && approveState != 3) {
//			return ResultDto.error("232", "该游戏最近一次更新尚未审核");
//		}
//
//		//添加更新记录
//		GameReleaseLog logGame = new GameReleaseLog();
//		if (!CommonUtil.isNull(input.getName())) {
//			logGame.setName(input.getName());
//		}
//
//		List<String> markList = new ArrayList<>();
//
//		if (!CommonUtil.isNull(input.getApk())) {
//			logGame.setApk(input.getApk());
//			if(!input.getApk().equals(game.getApk())) {
//				markList.add("更新了apk");
//			}
//		}
//
//		if (!CommonUtil.isNull(input.getCoverImage())) {
//			logGame.setCoverImage(input.getCoverImage());
//		}
//		Date date = new Date();
//		logGame.setGameId(gameId);
//		logGame.setCreatedAt(date);
//		logGame.setUpdatedAt(date);
//		logGame.setDetail(input.getDetail());
//		logGame.setDeveloper(developer.getCompanyName());
//		logGame.setDownloadNum(0);
//		logGame.setEditedAt(date);
//		logGame.setUpdatedBy(memberUserPrefile.getLoginId());
//		logGame.setCreatedBy(memberUserPrefile.getLoginId());
//		logGame.setIcon(input.getIcon());
//		if(input.getImages() != null) {
//			ObjectMapper mapper = new ObjectMapper();
//			try {
//				logGame.setImages(mapper.writeValueAsString(input.getImages()));
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//			}
//		}
//		logGame.setIntro(input.getGameIntro());
//		if (!CommonUtil.isNull(input.getIsbnId())) {
//			logGame.setIsbnId(input.getIsbnId());
//		}
//		if (!CommonUtil.isNull(input.getItunesId())) {
//			logGame.setItunesId(input.getItunesId());
//		}
//		logGame.setMemberId(memberUserPrefile.getLoginId());
//		String version = input.getVersion();
//		int i = version.indexOf(".");
//		String versionMain = version.substring(0, i);
//		String childVersion = version.substring(i + 1);
//		logGame.setVersionMain(versionMain);
//		logGame.setVersionChild(childVersion);
//
//		logGame.setUpdateLog(input.getUpdateLog());
//		ObjectMapper mapper = new ObjectMapper();
//
//		logGame.setDeveloperId(developer.getId());
//		logGame.setTestNumber(input.getTestNumber());
//		logGame.setTsetDate(input.getTsetDate());
//		logGame.setVideo(input.getVideo());
//		logGame.setCopyrightId(input.getCopyrightId());
//		logGame.setCopyrightImage(input.getCopyrightImage());
//		if(input.getCredentials() != null) {
//			try {
//				logGame.setCredentials(mapper.writeValueAsString(input.getCredentials()));
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//			}
//		}
//		logGame.setItunesId(input.getIssueId());
//		logGame.setIsbnId(input.getIsbnId());
//		logGame.setIsbnImage(input.getIsbnImage());
//		logGame.setIssueId(input.getIssueId());
//
//		String androidState = input.getAndroidState();
//		String iOState = input.getiOState();
//		if(androidState != null) {
//			 if(!game.getAndroidState().toString().equals(input.getAndroidState())) {
//				 String changeMsg = checkUpdateState("安卓",game.getAndroidState(),Integer.parseInt(input.getAndroidState()));
//				 markList.add(changeMsg);
//			 }
//			 logGame.setAndroidState(androidState);
//		 }
//		 if(iOState != null) {
//			 if("2".equals(iOState)) {
//				 int selectCount =
//						 surveyRelService.selectCount(new EntityWrapper<GameSurveyRel>().eq("game_id",game.getId()).eq("member_id",memberUserPrefile.getLoginId()).eq("phone_model","iOS"));
//				 if(selectCount < 1) {
//					 return ResultDto.error("-1", "iOS没有预约玩家不能测试");
//				 }
//			 }
//			 if(!game.getIosState().toString().equals(input.getiOState())) {
//				 String changeMsg = checkUpdateState("苹果",game.getIosState(),Integer.parseInt(input.getiOState()));
//				 markList.add(changeMsg);
//			 }
//			 logGame.setIosState(iOState);
//		 }
//		 markList.add("更新了游戏信息");
//		 try {
//			logGame.setRemark(mapper.writeValueAsString(markList));
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//		 logGame.setGameSize(input.getSize());
//		 logGame.setOrigin(input.getOrigin());
//
//		 //标签
//		 Map<String,Object> tags = new HashMap<>();
//		 tags.put("categoryId",!CommonUtil.isNull(input.getCategoryId())?input.getCategoryId():"");
//		 tags.put("tags", input.getTagList());
//		 try {
//			logGame.setTags(mapper.writeValueAsString(tags));
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//
//		 try {
//			logGame.setCompatibility(mapper.writeValueAsString(new ArrayList<String>()));
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//		logGame.setCommunityId(game.getCommunityId());
//		logGame.setCommunityIntro(input.getCommunityIntro());
//		logGame.setState(1);
//
//		Map<String,String> map = new HashMap<>();
//		map.put("gameId", game.getId());
//		map.put("memberId",developer.getMemberId());
//		//删除原先的关联
//		dgrDao.delLinkedMember(map);
//
//		input.getDeveloperList().remove(developer.getMemberId());
//		addDeveloperGameRel(input.getDeveloperList(), game.getId(), developer.getId());
//
//		logGame.insert();
//
//		return ResultDto.success();
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public ResultDto<DeveloperGameOut> gameDetail(String gameId,String userId) {
//
//		//开发者游戏关联表,判断开发者有没有与该游戏关联
//		DeveloperGameRel rel = dgrService
//				.selectOne(new EntityWrapper<DeveloperGameRel>().eq("member_id", userId).eq("game_id", gameId));
//
//		if (rel == null) {
//			return ResultDto.error("234", "当前开发者未与目标游戏关联");
//		}
//
//		//该游戏最近更新的游戏记录
//		GameReleaseLog log = logService.selectOne(new EntityWrapper<GameReleaseLog>().eq("game_id", gameId).eq("member_id", userId).orderBy("created_at",false).last("limit 1"));
//
//
//		DeveloperGameOut out = new DeveloperGameOut();
//		out.setGameId(log.getId());
//		out.setAndroidState(log.getAndroidState() == null ? 0 : Integer.parseInt(log.getAndroidState()));
//		out.setiOState(log.getIosState() == null ? 0 : Integer.parseInt(log.getIosState()));
//		out.setApk(log.getApk());
//		out.setCheckState(log.getApproveState());
//		String communityId = log.getCommunityId();
//
//		//		CmmCommunity community = communityService.selectById(communityId);
////		if(community != null) {
////			out.setCommunityIntro(community.getIntro());
////		}
//		out.setCommunityIntro(log.getCommunityIntro());
//
//		out.setCoverImage(log.getCoverImage());
//		out.setDetail(log.getDetail());
//		out.setDeveloper(log.getDeveloper());
//		out.setUpdatedAt(DateTools.fmtDate(log.getUpdatedAt()));
//		out.setGameId(gameId);
//		out.setGameIntro(log.getIntro());
//		out.setIcon(log.getIcon());
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			if(log.getImages()!=null) {
//				out.setImages((ArrayList<String>)mapper.readValue(log.getImages(),ArrayList.class));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		out.setIssueId(log.getIssueId());
//		out.setIsbnId(log.getIsbnId());
//		out.setIsbnImage(log.getIsbnImage());
//		out.setItunesId(log.getItunesId());
//		out.setName(log.getName());
//		out.setTestNumber(log.getTestNumber() == null ? 0 : log.getTestNumber());
//		if (log.getTsetDate() != null) {
//			out.setTsetDate(DateTools.fmtDate(log.getTsetDate()));
//		}
//		if(log.getTsetDate() != null) {
//			out.setTsetDate(DateTools.fmtDate(log.getTsetDate()));
//		}
//		out.setUpdateLog(log.getUpdateLog());
//		out.setVideo(log.getVideo());
//		out.setVersion(log.getVersionMain() + "." + log.getVersionChild());
//		out.setCommunityId(communityId);
//		out.setCopyrightId(log.getCopyrightId());
//		out.setCopyrightImage(log.getCopyrightImage());
//
//		out.setOrigin(log.getOrigin());
//		out.setGameSize(log.getGameSize());
//
//		//游戏标签
//		try {
//			Map<String,Object> map = mapper.readValue(log.getTags(), HashMap.class);
//			out.setCategoryId((String)map.get("categoryId"));
//			ArrayList<String> tagList = (ArrayList<String>) map.get("tags");
//			out.setTagList(tagList);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//
////		if(log.getCompatibility() != null) {
////			try {
////				out.setco(mapper.readValue(log.getCompatibility(),ArrayList.class));
////			} catch (IOException e) {
////				e.printStackTrace();
////			}
////		}
//		if(log.getCredentials() != null) {
//			try {
//				out.setCredentials(mapper.readValue(log.getCredentials(),ArrayList.class));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
////		List<DeveloperGameRel> rl = dgrService.selectList(new EntityWrapper<DeveloperGameRel>().eq("game_id",gameId));
////		List<String> developerList = new ArrayList<>();
////		for(DeveloperGameRel gamerel:rl ) {
////			developerList.add(gamerel.getMemberId());
////		}
//		Developer developer = developerService.selectById(rel.getDeveloperId());
//		out.setDeveloperList(getDeveloperList(gameId,developer.getMemberId()));
//		out.setGameLogId(log.getId());
//
//
//		return ResultDto.success(out);
//	}
//
//	@Override
//	public FungoPageResultDto<GameOutBean> gameList(DeveloperGamePageInput input,String userId) {
//
//		//查询游戏开发者关联表
//		List<DeveloperGameRel> relList = dgrService
//				.selectList(new EntityWrapper<DeveloperGameRel>().eq("member_id",userId));
//
//
//		List<Game> gameList = new ArrayList<Game>();
//		FungoPageResultDto<GameOutBean> re = new FungoPageResultDto<GameOutBean>();
//		if(relList != null && relList.size() > 0) {
//			List<String> collect = new ArrayList<>();
////			relList.stream().map(DeveloperGameRel::getGameId).collect(Collectors.toList());
//			for(DeveloperGameRel rel:relList) {
//				if(!CommonUtil.isNull(rel.getGameId())) {
//					collect.add(rel.getGameId());
//				}
//			}
//			Wrapper<Game> wrapper = new EntityWrapper<Game>().in("id", collect);
//			if(!CommonUtil.isNull(input.getKey())) {
//				wrapper.like("name", input.getKey());
//			}
//			Page<Game> page = gameService.selectPage(new Page<>(input.getPage(), input.getLimit()),wrapper);
//			gameList = page.getRecords();
//			PageTools.pageToResultDto(re, page);
//		}
////		if (gameList == null) {
////			return FungoPageResultDto.error("231", "没有找到关联的游戏");
////		}
//		List<GameOutBean> relist = new ArrayList<>();
//		for (Game game : gameList) {
//			GameOutBean out = new GameOutBean();
//			out.setAndroidState(game.getAndroidState() == null ? 0 : game.getAndroidState());
////			out.setCheckState(3);
//			GameReleaseLog log = logService.selectOne(Condition.create().setSqlSelect("id,approve_state").eq("game_id",game.getId()).orderBy("created_at",false));
//			out.setCheckState(log.getApproveState());
//			out.setiOState(game.getIosState() == null ? 0 : game.getIosState());
//			out.setCoverImage(game.getCoverImage());
//			out.setEditedAt(DateTools.fmtDate(game.getEditedAt()));
//			out.setGameId(game.getId());
//			out.setIcon(game.getIcon());
//			out.setName(game.getName());
//			relist.add(out);
//		}
//
//		re.setData(relist);
//		return re;
//	}
//
//	@Override
//	public FungoPageResultDto<GameHistoryOut> gameHistory(String userId,
//			DeveloperGamePageInput input) {
////		Developer developer = developerService.selectOne(new EntityWrapper<Developer>().eq("member_id", userId));
////		if (developer == null) {
////			return FungoPageResultDto.error("14", "该用户没有开发者权限");
////		}
//
//		String gameId = input.getGameId();
//		Page<GameReleaseLog> page = logService.selectPage(new Page<>(input.getPage(), input.getLimit()),
//				new EntityWrapper<GameReleaseLog>().eq("game_id", gameId).eq("member_id", userId));
//		List<GameReleaseLog> list = page.getRecords();
//		List<GameHistoryOut> olist = new ArrayList<>();
//		//
//		ObjectMapper mapper = new ObjectMapper();
//		for (GameReleaseLog log : list) {
//			GameHistoryOut out = new GameHistoryOut();
//			out.setCreatedAt(DateTools.fmtDate(log.getCreatedAt()));
//			out.setMessage(log.getUpdateLog());
//			out.setCheckState(log.getApproveState());
//			out.setVersion(log.getVersionMain() + "." + log.getVersionChild());
//			try {
//				out.setRemark(mapper.readValue(log.getRemark(), ArrayList.class));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			olist.add(out);
//		}
//		FungoPageResultDto<GameHistoryOut> re = new FungoPageResultDto<GameHistoryOut>();
//		PageTools.pageToResultDto(re, page);
//		re.setData(olist);
//		return re;
//	}
//
//
//	@Override
//	public ResultDto<List<Map<String,Object>>> gemeAnalyzeLog(DeveloperQueryIn input) {
//		String gameId = input.getGameId();
//		//浏览 下载 预约 测试 评价 推荐率
//		List<Map<String,Object>> list = new ArrayList<>();
//		//浏览
//		int abrowses = baslogService.selectCount(new EntityWrapper<BasLog>().eq("path", "/api/content/game/"+gameId).eq("channel", "Android"));
//		int ibrowses = baslogService.selectCount(new EntityWrapper<BasLog>().eq("path", "/api/content/game/"+gameId).eq("channel", "iOS"));
//		int totalbrowse = baslogService.selectCount(new EntityWrapper<BasLog>().eq("path", "/api/content/game/"+gameId));
//		int wbrowses = totalbrowse - abrowses -ibrowses;
//		Map<String,Object> browse = new HashMap<>();
//		browse.put("name","浏览量");
//		browse.put("web", wbrowses);
//		browse.put("android", abrowses);
//		browse.put("IOS", ibrowses);
//		browse.put("total", totalbrowse);
//		list.add(browse);
//		//下载
//		int ad = baslogService.selectCount(new EntityWrapper<BasLog>().eq("path", "/api/action/download").eq("channel", "Android").like("input_data",gameId));
//		int id = baslogService.selectCount(new EntityWrapper<BasLog>().eq("path", "/api/action/download").eq("channel", "IOS").like("input_data",gameId));
//		int td = baslogService.selectCount(new EntityWrapper<BasLog>().eq("path", "/api/action/download").like("input_data",gameId));
//		int wd = td - ad -id;
//		Map<String,Object> down = new HashMap<>();
//		down.put("name","下载请求数");
//		down.put("web", wd);
//		down.put("android", ad);
//		down.put("IOS", id);
//		down.put("total", td);
//		list.add(down);
//		//预约
//		int ar = surveyRelService.selectCount(new EntityWrapper<GameSurveyRel>().eq("game_id", gameId).eq("phone_model", "Android"));
//		int ir = surveyRelService.selectCount(new EntityWrapper<GameSurveyRel>().eq("game_id", gameId).eq("phone_model", "IOS"));
//		int tr = surveyRelService.selectCount(new EntityWrapper<GameSurveyRel>().eq("game_id", gameId));
//		int wr = tr -ar -ir;
//		Map<String,Object> appoint = new HashMap<>();
//		appoint.put("name","预约数");
//		appoint.put("web", wr);
//		appoint.put("android", ar);
//		appoint.put("IOS", ir);
//		appoint.put("total", tr);
//		list.add(appoint);
//		//测试
//		Map<String,Object> test = new HashMap<>();
//		test.put("name","测试数");
//		test.put("web", 0);
//		test.put("android", 0);
//		test.put("IOS", 0);
//		test.put("total", 0);
//		list.add(test);
//		//评价
//		int ae = evaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", gameId).eq("phone_model", "Android"));
//		int ie = evaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", gameId).eq("phone_model", "IOS"));
//		int te = evaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", gameId));
//		int we = te -ae - ie;
//		Map<String,Object> eva = new HashMap<>();
//		eva.put("name","评价数");
//		eva.put("web", we);
//		eva.put("android", ae);
//		eva.put("IOS", ie);
//		eva.put("total", te);
//		list.add(eva);
//		//推荐率
//		int ac = evaluationService.selectCount
//				(new EntityWrapper<GameEvaluation>().eq("game_id", gameId).eq("phone_model", "Android").eq("is_recommend", 1));
//		int ic = evaluationService.selectCount
//				(new EntityWrapper<GameEvaluation>().eq("game_id", gameId).eq("phone_model", "IOS").eq("is_recommend", 1));
//		int tc = evaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", gameId).eq("is_recommend", 1));
//		int wc = tc - ac -ic;
//		Map<String,Object> rate = new HashMap<>();
//		rate.put("name","推荐率");
//		rate.put("web", we == 0?"暂无推荐":wc== 0?"0%":rateTransfor(wc,we));
//		rate.put("android", ae == 0?"暂无推荐":ac== 0?"0%":rateTransfor(ac,ae));
//		rate.put("IOS",  ie == 0?"暂无推荐":ic== 0?"0%":rateTransfor(ic,ie));
//		rate.put("total", te == 0?"暂无推荐":tc== 0?"0%":rateTransfor(tc,te));
//		list.add(rate);
//		return ResultDto.success(list);
//
//	}
//
//	public String rateTransfor(int numerator,int Denominator) {
//		double c = (double)numerator / Denominator;
//
//		return String.format("%.2f", c*100) + "%";
//	}
//
	@Override
	/*
	 * (non-Javadoc)
	 * @see com.fungo.game.api.IDeveloperService#messageList()
	 * 开发者消息,暂时没有这个功能
	 */
	public ResultDto<List<Map<String, Object>>> messageList() {


		List<Map<String, Object>> list = new ArrayList<>();
		Map<String,Object> m1 = new HashMap<>();
		m1.put("content","您的游戏'狂扁小朋友'更新成功");
		m1.put("createdAt", DateTools.fmtDate(new Date()));
		m1.put("type", "");

		Map<String,Object> m2 = new HashMap<>();
		m2.put("content","第一届翔游争霸赛正式开幕");
		m2.put("createdAt", "2018-7-17 10:07:32");
		m2.put("type", "");

		Map<String,Object> m3 = new HashMap<>();
		m3.put("content","用户心理学---论怎样骗氪");
		m3.put("createdAt", "2018-7-17 10:07:32");
		m3.put("type", "");

		Map<String,Object> m4 = new HashMap<>();
		m4.put("content","你的app真不错,不过下一秒就是我的了---手把手教你借鉴");
		m4.put("createdAt", "2018-7-17 9:07:32");
		m4.put("type", "");

		Map<String,Object> m5 = new HashMap<>();
		m5.put("content","您的账号被多次举报涉黄,请遵守用户守则,发表健康内容");
		m5.put("createdAt", new Date());
		m5.put("type", "");

		list.add(m1);
		list.add(m2);
		list.add(m3);
		list.add(m4);
		list.add(m5);

		return ResultDto.success(list);
	}
//
//	@Override
//	public ResultDto<Map<String, Integer>> communityAnalyze(DeveloperQueryIn input) throws ParseException {
////		String gameId = input.getGameId();
////		Game game = gameService.selectOne(Condition.create().setSqlSelect("id,community_id").eq("id",gameId));
//		String cid = input.getCommunityId();
//		if(CommonUtil.isNull(cid)) {
//			return ResultDto.error("-1", "社区id不能为空");
//		}
//		Map<String,Integer> map = new HashMap<>();
//		// /api/content/community/55a25aee44d9040044fe8f94
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		Date nowData = format.parse(format.format(new Date()));
//		//浏览
//		int tpv = baslogService.selectCount(new EntityWrapper<BasLog>().eq("path","/api/content/community/"+cid));
//		int dpv = baslogService.selectCount(new EntityWrapper<BasLog>().eq("path","/api/content/community/"+cid).ge("created_at", nowData));
//		map.put("todayPV", dpv);
//		map.put("totalPV", tpv);
//		//文章
//		int tpo = postService.selectCount(new EntityWrapper<CmmPost>().eq("community_id",cid));
//		int dpo = postService.selectCount(new EntityWrapper<CmmPost>().eq("community_id",cid).ge("created_at", nowData));
//		map.put("todayPost", dpo);
//		map.put("totalPost", tpo);
//		//关注 type 5 target_id target_type;
//		int tf = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 5).eq("target_id", cid));
//		int df = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 5).eq("target_id", cid).ge("created_at", nowData));
//		map.put("todayFollow", df);
//		map.put("totalFollow", tf);
//		//热度
//		map.put("todayHotValue", df+dpo);
//		map.put("totalHotValue", tf+tpo);
//
//
//		return ResultDto.success(map);
//	}
//
	//检查是否有权限
	@Override
	public boolean checkDpPower(String memberId) {
		Developer developer = developerService.selectOne(new EntityWrapper<Developer>().eq("member_id", memberId));
		if(developer == null) {
			return false;
		}else if(developer.getApproveState() != 2 ) {
			return false;
		}
		return true;
	}
//
//	public ResultDto<String> copyGameTolog(AddGameInputBean input,MemberUserProfile memberUserPrefile,GameReleaseLog logGame, Developer developer) {
//
//		if (!CommonUtil.isNull(input.getName())) {
//			logGame.setName(input.getName());
//		}
//		if (!CommonUtil.isNull(input.getApk())) {
//			logGame.setApk(input.getApk());
//
//		}
//		if (!CommonUtil.isNull(input.getCoverImage())) {
//			logGame.setCoverImage(input.getCoverImage());
//		}
//		Date date = new Date();
//		logGame.setGameId(input.getGameId());
//		logGame.setCreatedAt(new Date());
//		logGame.setUpdatedAt(date);
//		logGame.setDetail(input.getDetail());
//		logGame.setDeveloper(developer.getCompanyName());
//		logGame.setDownloadNum(0);
//		logGame.setEditedAt(date);
//		logGame.setUpdatedBy(memberUserPrefile.getLoginId());
//		logGame.setCreatedBy(memberUserPrefile.getLoginId());
//		logGame.setIcon(input.getIcon());
//		if(input.getImages() != null) {
//			ObjectMapper mapper = new ObjectMapper();
//			try {
//				logGame.setImages(mapper.writeValueAsString(input.getImages()));
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//			}
//		}
//		logGame.setIntro(input.getGameIntro());
//		if (!CommonUtil.isNull(input.getIsbnId())) {
//			logGame.setIsbnId(input.getIsbnId());
//		}
//		if (!CommonUtil.isNull(input.getItunesId())) {
//			logGame.setItunesId(input.getItunesId());
//		}
//		logGame.setMemberId(memberUserPrefile.getLoginId());
//		String version = input.getVersion();
//		int i = version.indexOf(".");
//		String versionMain = version.substring(0, i);
//		String childVersion = version.substring(i + 1);
//		logGame.setVersionMain(versionMain);
//		logGame.setVersionChild(childVersion);
//
//		logGame.setUpdateLog(input.getUpdateLog());
//		ObjectMapper mapper = new ObjectMapper();
//
//		logGame.setOrigin(input.getOrigin());
//		logGame.setDeveloperId(developer.getId());
//		logGame.setTestNumber(input.getTestNumber());
//		logGame.setTsetDate(input.getTsetDate());
//		logGame.setVideo(input.getVideo());
//		logGame.setCopyrightId(input.getCopyrightId());
//		logGame.setCopyrightImage(input.getCopyrightImage());
//		if(input.getCredentials() != null) {
//			try {
//				logGame.setCredentials(mapper.writeValueAsString(input.getCredentials()));
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//			}
//		}
//		logGame.setItunesId(input.getItunesId());
//		logGame.setIsbnId(input.getIsbnId());
//		logGame.setIsbnImage(input.getIsbnImage());
//		logGame.setIssueId(input.getIssueId());
//		String androidState = input.getAndroidState();
//		String iOState = input.getiOState();
//		 if(androidState != null) {
//			 logGame.setAndroidState(androidState);
//		 }
//		 if(iOState != null) {
//			 logGame.setIosState(iOState);
//		 }
//		 List<String> rm = new ArrayList<>();
//		 rm.add("新增游戏");
//		 try {
//			logGame.setRemark(mapper.writeValueAsString(rm));
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//		 logGame.setGameSize(input.getSize());
//			try {
//				logGame.setCompatibility(mapper.writeValueAsString(new ArrayList<String>()));
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//			}
//		//communityId
//		logGame.setCommunityIntro(input.getCommunityIntro());;
//		 Map<String,Object> tags = new HashMap<>();
//		 tags.put("categoryId",!CommonUtil.isNull(input.getCategoryId())?input.getCategoryId():"");
//		 tags.put("tags", input.getTagList());
//		 try {
//			logGame.setTags(mapper.writeValueAsString(tags));
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//		 logGame.setState(0);
//
//		 logGame.insert();
//
//		return ResultDto.success();
//
//	}
//
//	//检测游戏状态变化
//	private String checkUpdateState(String phone,int preState,int nextState){
//		String pre = getUpdateState(preState);
//		String next = getUpdateState(nextState);
//
//		return phone+"上线状态由'"+pre+"'变更为'"+next+"'";
//	}
//
//	private String getUpdateState(int i) {
//		if(i == 0) {
//			return "待开启";
//		}else if(i == 1) {
//			return "预约";
//		}else if(i == 2) {
//			return "测试";
//		}else if(i == 3) {
//			return "已上线";
//		}else if (i == 4) {
//			return "可下载";
//		}
//
//		return "";
//	}
//
//	private void addDeveloperGameRel(List<String> developerList,String gameId,String developerId) {
//		for(String memberId:developerList) {
//			 DeveloperGameRel rel = new DeveloperGameRel();
//			 rel.setCreatedAt(new Date());
//			 rel.setUpdatedAt(new Date());
//			 rel.setGameId(gameId);
//			 rel.setDeveloperId(developerId);
//			 rel.setMemberId(memberId);
//			 dgrService.insert(rel);
//		}
//	}
//
//	// 转换游戏大小的格式
//	public String formatGameSize(int size) {
//		Double dSize = (double) size;
//		dSize = dSize / 1024 / 1024;
//		if (dSize == 0) {
//			return "未知";
//		} else if (dSize >= 1024) {
//			dSize = dSize / 1024;
//			return String.format("%.2f", dSize) + "G";
//		} else {
//			return String.format("%.2f", dSize) + "M";
//		}
//	}
//
//	@Transactional
//	/**
//	 * 修改游戏标签
//	 *
//	 * @param tags 后台标签id集合
//	 * @param categoryId 分类标签id(官方标签)
//	 * @param gameId
//	 * @return
//	 */
//	public ResultDto<String> addGameTag(List<String> tags,String categoryId,String gameId){
//
//		if (tags.size() > 3) {
//			return ResultDto.error("13", "最多添加3个标签");
//		}
//		// 获取游戏的官方标签(分类) 后台标签 type = 1 type = 2
//		@SuppressWarnings("unchecked")
//		List<GameTag> gameTagList = gameTagService
//				.selectList(Condition.create().setSqlSelect("id,tag_id").eq("game_id", gameId).eq("type", 2));//1
//		@SuppressWarnings("unchecked")
//		GameTag cTag = gameTagService
//				.selectOne(Condition.create().setSqlSelect("id,tag_id").eq("game_id", gameId).eq("type", 1));//2
//		List<String> preTagIdList = new ArrayList<String>();//已有后台标签
//		String preCategory = null;
//		if(gameTagList != null && gameTagList.size()>0) {
//			for(GameTag gameTag:gameTagList) {
//				preTagIdList.add(gameTag.getTagId());
//			}
//		}
//		if(cTag != null) {//是否存在官方标签
//			preCategory = cTag.getTagId();
//		}
//		// 对比，增删
//		List<String> newTagIdList = tags;
//		newTagIdList = new ArrayList<String>(newTagIdList);
//		List<String> tempIdList = new ArrayList<String>();
//		preTagIdList.forEach(s -> tempIdList.add(s));
//
//		// 删除id
//		preTagIdList.removeAll(newTagIdList);
//		// 添加id
//		newTagIdList.removeAll(tempIdList);
//
//		Game game = gameService.selectById(gameId);
//
//		String tagsFormGame = game.getTags();
//		//官方标签处理
//		if(!CommonUtil.isNull(preCategory)) i:{//官方标签存在
//			if(CommonUtil.isNull(categoryId)) {
//				break i;
//			}
//			if(!preCategory.equals(categoryId)) {//如果不同
//				//删旧(type=0)
//				GameTag gameTag = gameTagService
//						.selectOne(new EntityWrapper<GameTag>().eq("tag_id", preCategory).eq("game_id", gameId));
//				gameTag.setType(-1);
//				gameTag.updateById();
//				//添新
//				GameTag ofTag = gameTagService
//						.selectOne(new EntityWrapper<GameTag>().eq("tag_id", categoryId).eq("game_id", gameId));
//				if(ofTag != null) {
//					ofTag.setType(1);
//					gameTagService.updateById(ofTag);
//				}else {
//					GameTag newGameTag = new GameTag();
//					newGameTag.setTagId(categoryId);
//					Date date = new Date();
//					newGameTag.setType(1);
//					newGameTag.setGameId(gameId);
//					newGameTag.setCreatedAt(date);
//					newGameTag.setUpdatedAt(date);
//					gameTagService.insert(newGameTag);
//
//				}
//				BasTag tag = tagService.selectById(categoryId);
//				tagsFormGame = tag.getName();
//			}
//		}else {//官方标签不存在(1)
//			//添新
//			GameTag ofTag = gameTagService //有,改成官方
//					.selectOne(new EntityWrapper<GameTag>().eq("tag_id", categoryId).eq("game_id", gameId));
//			if(ofTag != null) {
//				ofTag.setType(1);
//				gameTagService.updateById(ofTag);
//			}else {
//				GameTag newGameTag = new GameTag();
//				newGameTag.setTagId(categoryId);
//				Date date = new Date();
//				newGameTag.setType(1);
//				newGameTag.setGameId(gameId);
//				newGameTag.setCreatedAt(date);
//				newGameTag.setUpdatedAt(date);
//				gameTagService.insert(newGameTag);
//
//			}
//			BasTag tag = tagService.selectById(categoryId);
//			tagsFormGame = tag.getName();
//		}
//
//		//后台标签处理
//		for (String delTagId : preTagIdList) {// 删除后台标签(type=0)
//			GameTag gameTag = gameTagService
//					.selectOne(new EntityWrapper<GameTag>().eq("tag_id", delTagId).eq("game_id", gameId));
//
//			if(gameTag != null ) {
//				gameTag.setType(0);
//				gameTagService.updateById(gameTag);
//			}
//		}
//		for (String updateId : newTagIdList) {// 更新
//			GameTag gameTag = gameTagService
//					.selectOne(new EntityWrapper<GameTag>().eq("tag_id", updateId).eq("game_id", gameId));
//			if(gameTag == null) {
//				GameTag newGameTag = new GameTag();
//				newGameTag.setTagId(updateId);
//				Date date = new Date();
//				newGameTag.setGameId(gameId);
//				newGameTag.setCreatedAt(date);
//				newGameTag.setUpdatedAt(date);
//				newGameTag.setType(2);
//				gameTagService.insert(newGameTag);
//			}else if(gameTag.getType() == 0) {
//				gameTag.setType(2);
//				gameTagService.updateById(gameTag);
//			}
//
//		}
//
//		gameDao.updateTags(gameId,tagsFormGame);
//
//		return ResultDto.success();
//	}
//
//	//审批通过(未启用,待完善)
//	@Transactional
//	private ResultDto<String> approveAndUpdateGame(String logId,String gameId) {
//
//		int count = logService.selectCount(new EntityWrapper<GameReleaseLog>().eq("game_id", gameId));
//		if(count == 0) {
//
//		}else if(count == 1) {
//
//		}else {//将更新日志覆盖游戏
//			GameReleaseLog log = new GameReleaseLog();
//			Game game = new Game();
//
//			game.setName(log.getName());
//			if (log.getApk() != null) {
//				game.setApk(log.getApk());
//			}
//			String updateLog = log.getUpdateLog();
//			game.setUpdateLog(updateLog);
//			game.setCoverImage(log.getCoverImage());
//			Date date = new Date();
//			game.setCreatedAt(date);
//			game.setOrigin(log.getOrigin());
//			game.setUpdatedAt(date);
//			game.setDetail(log.getDetail());
//			game.setDownloadNum(0);
//			game.setEditedAt(date);
//			game.setIcon(log.getIcon());
//			if(log.getImages() != null) {
//				ObjectMapper mapper = new ObjectMapper();
//				try {
//					game.setImages(mapper.writeValueAsString(log.getImages()));
//				} catch (JsonProcessingException e) {
//					e.printStackTrace();
//				}
//			}
//			game.setIntro(log.getIntro());
//			if (!CommonUtil.isNull(log.getIsbnId())) {
//				game.setIsbnId(log.getIsbnId());
//			}
//			if (!CommonUtil.isNull(log.getItunesId())) {
//				game.setItunesId(log.getItunesId());
//			}
////			String version = log.getVersion();
////			int i = version.indexOf(".");
////			String versionMain = version.substring(0, i);
////			String childVersion = version.substring(i + 1);
////			game.setVersionMain(versionMain);
////			game.setVersionChild(childVersion);
//			game.setUpdateLog(log.getUpdateLog());
//
//			ObjectMapper mapper = new ObjectMapper();
//
//
//			game.setTestNumber(log.getTestNumber());
//			game.setTsetDate(log.getTsetDate());
//			game.setVideo(log.getVideo());
//			game.setCopyrightId(log.getCopyrightId());
//			game.setCopyrightImage(log.getCopyrightImage());
//			if(log.getCredentials() != null) {
//				try {
//					game.setCredentials(mapper.writeValueAsString(log.getCredentials()));
//				} catch (JsonProcessingException e) {
//					e.printStackTrace();
//				}
//			}
////			game.setItunesId(log.getItunesId());
//			game.setIsbnId(log.getIsbnId());
//			game.setIsbnImage(log.getIsbnImage());
//			game.setIssueId(log.getIssueId());
//			String androidState = log.getAndroidState();
//			String iOState = log.getIosState();
//			 if(androidState != null) {
//				 game.setAndroidState(Integer.parseInt(androidState));
//			 }
//			 if(iOState != null) {
//				 if("2".equals(iOState)) {
//					 return ResultDto.error("-1", "iOS没有预约玩家不能测试");
//				 }
//				 game.setIosState(Integer.parseInt(iOState));
//			 }
//
////			game.setState(3);
//			game.setGameSize((long)log.getGameSize());
////			game.insert();
//			try {
//				game.setCompatibility(mapper.writeValueAsString(new ArrayList<String>()));
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//			}
//
//			gameService.updateById(game);
//
//			//tag
//			try {
//				Map<String,Object> tags = mapper.readValue(log.getTags(), HashMap.class);
//				String categoryId = (String) tags.get("categoryId");
//				ArrayList<String> tagList = (ArrayList<String>) tags.get("tags");
//				if(tagList != null || !CommonUtil.isNull(categoryId)) {
//					ResultDto<String> addGameTag = addGameTag(tagList,categoryId,game.getId());
//					if(!addGameTag.isSuccess()) {
//						throw new BusinessException("-1","添加标签失败:"+addGameTag.getMessage());
//					}
//				}
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//		}
//		return ResultDto.success();
//	}
//
//	//开发组成员
//	public List<Map<String, Object>> getDeveloperList(String gameId,String memberId) {
//		List<DeveloperGameRel> relList = dgrService.selectList(new EntityWrapper<DeveloperGameRel>().eq("game_id",gameId));
//		List<String> collect = relList.stream().map(DeveloperGameRel::getMemberId).collect(Collectors.toList());
//		@SuppressWarnings("unchecked")
//		List<Member> memberList = memberService.selectList(Condition.create().setSqlSelect("id,user_name,member_no").in("id", collect));
//		List<Map<String,Object>> developerList = new ArrayList<>();
//		for(Member m:memberList ) {
//			Map<String,Object> map = new HashMap<>();
//			map.put("memberId", m.getId());
//			map.put("username", m.getUserName());
//			map.put("memberNo", m.getMemberNo());
//			if(memberId.equals(m.getId())) {
//				map.put("isDeveloper", true);
//			}else {
//				map.put("isDeveloper", false);
//			}
//			developerList.add(map);
//		}
//		return developerList;
//	}
//
//	//版本号格式
//	public boolean checkVersionFormat(String version) {
//		String regEx_version = "^[0-9]*\\.[0-9]*(\\.[0-9]*)?$";
//		Pattern p_version = Pattern.compile(regEx_version, 0);
//
//		return p_version.matcher(version).find();
//	}


	
}
