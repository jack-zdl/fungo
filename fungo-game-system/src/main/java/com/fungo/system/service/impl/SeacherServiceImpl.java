package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.entity.IncentRuleRank;
import com.fungo.system.entity.Member;
import com.fungo.system.proxy.IGameProxyService;
import com.fungo.system.proxy.IMemeberProxyService;
import com.fungo.system.service.*;
import com.game.common.cache.GuavaCache;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.search.SearCount;
import com.game.common.util.PageTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.*;

@Service
public class SeacherServiceImpl implements ISeacherService {

	@Autowired
	private BasConfigService configService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private BasActionService actionService;



	@Autowired
	private IMemberIncentRuleRankService IRuleRankService;
	@Autowired
	private IMemberService iMemberService;
	@Autowired
	private IUserService iuserService;
	@Autowired
	private IGameProxyService iGameProxyService;
	@Autowired
	private IMemeberProxyService iMemeberProxyService;

//	@Autowired
//	private ITagService tagService;

	@Override
	// 可修改
	public ResultDto<List<String>> getKeywords() throws JsonProcessingException, IOException {
//		List<BasConfig> configList = configService.selectList(new EntityWrapper<BasConfig>().eq("key_name", "HOT_SEARCH"));
//		if (configList == null || configList.isEmpty()) {
//			return ResultDto.error("261", "找不到搜索内容");
//		}
//		ObjectMapper map = new ObjectMapper();
//		String valueInfo = configList.get(0).getValueInfo();
//		String[] split = map.readTree(valueInfo).get("list").toString().replace("[", "").replaceAll("]", "").replace("\"", "").split(",");
//		List<String> list = new ArrayList<String>(Arrays.asList(split));
//
//		if(list.size() > 8) {
//			list = list.subList(0, 8);
//		}
//		ResultDto<List<String>> re = new ResultDto<>();
//		re.setData(list);
//		List<HotValue> vlist = gameDao.getHotValue();
//		if(vlist == null || vlist.size() == 0) {
//			return ResultDto.success();
//		}
		List<String> list = new ArrayList<>();
		//从缓存中获得
		Object o = GuavaCache.get("hotSearchWord");
		if(o != null) {
			list = (List<String>) o;
		}else {
			list.add("绝地求生");
			list.add("王者荣耀");
			list.add("元气骑士");
			list.add("QQ飞车");
		}
		
		return ResultDto.success(list);
	}
	

	@Override
	public FungoPageResultDto<AuthorBean> searchUsers(String keyword, int page, int limit, String userId) throws Exception{
		@SuppressWarnings("unchecked")
		 
		Page<Member> userPage= memberService.selectPage(new Page<>(page,limit),Condition.create()
				.setSqlSelect("id,user_name,avatar,sign,level,created_at,updated_at,member_no").where("state = {0}", 0)
				.like("user_name", keyword));
		List<Member> userList = userPage.getRecords();
//		if (userList == null || userList.size() == 0) {
//			return FungoPageResultDto.error("126", "找不到符合条件的用户");
//		}
		if(userList == null) {
			userList = new ArrayList<>();
		}
		
		List<IncentRuleRank> rankList = IRuleRankService.getLevelRankList();
		List<AuthorBean> dataList = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		for (Member user : userList) {
//			UserSearchOut out = new UserSearchOut();
//			out.setObjectId(user.getId());
//			out.setUsername(user.getUserName());
//			out.setAvatar(user.getAvatar());
//			out.setLevel(user.getLevel());
//			out.setSign(user.getSign());
//			out.setCreatedAt(DateTools.fmtDate(user.getCreatedAt()));
//			out.setUpdatedAt(DateTools.fmtDate(user.getUpdatedAt()));
//			out.setMemberNo(user.getMemberNo());
//			if (!"".equals(userId) && memberService.selectById(userId) != null) {
//				int actionCount = actionService.selectCount(new EntityWrapper<BasAction>()
//						.eq("target_id", user.getId()).eq("type", 5).eq("member_id", userId).and("state = 0"));
//				if (actionCount > 0) {
//					out.setIs_followed(true);
//				} else {
//					out.setIs_followed(false);
//				}
//			}
//			String rankImgs = iMemberService.getLevelRankUrl(user.getLevel(), rankList);
//			ArrayList<HashMap<String,Object>> urlList = mapper.readValue(rankImgs, ArrayList.class);
//			out.setDignityImg((String)urlList.get(0).get("url"));
			AuthorBean author = iuserService.getUserCard(user.getId(), userId);
			
			dataList.add(author);
		}
		FungoPageResultDto<AuthorBean> re=new FungoPageResultDto<AuthorBean>();
		PageTools.pageToResultDto(re, userPage);
		re.setData(dataList);
		return re;
	}


	@Override
	public ResultDto<SearCount> getSearchCount(String keyword) {
		// @todo
        CmmPostDto cmmPostParam = new CmmPostDto();
        cmmPostParam.setState(1);cmmPostParam.setTitle(keyword);cmmPostParam.setContent(keyword);
	    int postCount = iMemeberProxyService.CmmPostCount(cmmPostParam) ;
//				postService.selectCount(new EntityWrapper<CmmPost>().where("state = {0}", 1).andNew("title like '%"+keyword+"%'")
//						.or("content like " + "'%"+ keyword + "%'").or("content like "+ "'%" + keyword+ "%'"));
		//@todo
        GameDto gameDto = new GameDto();
        gameDto.setState(0); gameDto.setName(keyword);
        int gameCount =   iGameProxyService.getGameSelectCountByLikeNameAndState(gameDto);//gameService.selectCount(new EntityWrapper<Game>().where("state = {0}", 0).like("name", keyword));
		int userCount = memberService.selectCount(new EntityWrapper<Member>().where("state = {0}", 0).like("user_name", keyword));
		SearCount searchCount = new SearCount();
		searchCount.setGameCount(gameCount);
		searchCount.setPostCount(postCount);
		searchCount.setUserCount(userCount);
		searchCount.setKeyword(keyword);

		return ResultDto.success(searchCount);
	}

	
}
