package com.fungo.system.service.portal.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.entity.IncentRuleRank;
import com.fungo.system.entity.Member;
import com.fungo.system.facede.IGameProxyService;
import com.fungo.system.facede.IMemeberProxyService;
import com.fungo.system.service.*;
import com.fungo.system.service.portal.PortalSystemISeacherService;
import com.fungo.system.service.portal.PortalSystemIUserService;
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
import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("all")
@Service
public class PortalSystemSeacherServiceImpl implements PortalSystemISeacherService {
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
	@Autowired
	private PortalSystemIUserService portalSystemIUserService;

//	@Autowired
//	private ITagService tagService;


	@Override
	public FungoPageResultDto<AuthorBean> searchUsers(String keyword, int page, int limit, String userId) throws Exception{
		@SuppressWarnings("unchecked")
		 
		Page<Member> userPage= memberService.selectPage(new Page<>(page,limit),Condition.create()
				.setSqlSelect("id,user_name as userName,avatar,sign,level,created_at as createdAt,updated_at as updatedAt,member_no as memberNo").where("state = {0}", 0)
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
			AuthorBean author = portalSystemIUserService.getUserCard(user.getId(), userId);
			dataList.add(author);
		}
		FungoPageResultDto<AuthorBean> re=new FungoPageResultDto<AuthorBean>();
		re.setData(dataList);
		PageTools.pageToResultDto(re, userPage);
		return re;
	}

}
