package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.dao.BasLogDao;
import com.fungo.system.entity.BasFilterword;
import com.fungo.system.entity.IncentRuleRank;
import com.fungo.system.entity.Member;
import com.fungo.system.facede.IGameProxyService;
import com.fungo.system.facede.IMemeberProxyService;
import com.fungo.system.service.*;
import com.game.common.bean.HotValue;
import com.game.common.cache.GuavaCache;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.search.SearCount;
import com.game.common.util.PageTools;
import com.game.common.util.SensitiveWordUtil;
import com.game.common.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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

    @Autowired
    private BasFilterwordService filterService;

    @Autowired
    private BasLogDao basLogDao;
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
        if (o != null) {
            list = (List<String>) o;
        } else {
            list.add("绝地求生");
            list.add("王者荣耀");
            list.add("元气骑士");
            list.add("QQ飞车");
        }

        return ResultDto.success(list);
    }

    @Async
    public void updateGameKeywords() {

        System.out.println("........执行获取关键字过滤.");
        initFilterWord();

        System.out.println("........执行游戏热值计算.");

        List<HotValue> vlist = basLogDao.getHotValue();
        if (vlist != null && vlist.size() != 0) {
            List<String> list = new ArrayList<>();
            for (HotValue v : vlist) {
                list.add(v.getGameName());
            }
            GuavaCache.put("hotSearchWord", list);
        }

    }


    @Override
    public FungoPageResultDto<AuthorBean> searchUsers(String keyword, int page, int limit, String userId) throws Exception {

        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
        }


        Page<Member> searchPage = new Page<>(page, limit);

//        Wrapper wrapperSearch = Condition.create()
//                .setSqlSelect("id,user_name as userName,avatar,sign,level,created_at as createdAt,updated_at as updatedAt,member_no as memberNo");
//
//        wrapperSearch.where("state = {0}", 0);
//
//        wrapperSearch.like("member_no", keyword);
//        wrapperSearch.orNew("user_name like '%" + keyword + "%'");
//
//
//        String orderByStr = "";
//        //是数字
//        if (StringUtil.checkNum(keyword)) {
//            //数字
//            orderByStr = "LOCATE('" + keyword + "',member_no) DESC , LOCATE('" + keyword + "',user_name) DESC";
//        } else {
//            //非数字
//            orderByStr = "REPLACE(user_name,'" + keyword + "','')";
//        }
//
//        wrapperSearch.orderBy(orderByStr);

        Page<Member> userPage = memberService.selectPage(searchPage, getWrapper(keyword));


        List<Member> userList = userPage.getRecords();


        if (userList == null) {
            userList = new ArrayList<>();
        }

        List<IncentRuleRank> rankList = IRuleRankService.getLevelRankList();
        List<AuthorBean> dataList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (Member user : userList) {

            AuthorBean author = iuserService.getUserCard(user.getId(), userId);

            dataList.add(author);
        }
        FungoPageResultDto<AuthorBean> re = new FungoPageResultDto<AuthorBean>();
        re.setData(dataList);
        PageTools.pageToResultDto(re, userPage);
        return re;
    }


    @Override
    public ResultDto<SearCount> getSearchCount(String keyword) {
        // @todo
        CmmPostDto cmmPostParam = new CmmPostDto();
        cmmPostParam.setState(1);
        cmmPostParam.setTitle(keyword);
        cmmPostParam.setContent(keyword);
        int postCount = iMemeberProxyService.CmmPostCount(cmmPostParam);
//				postService.selectCount(new EntityWrapper<CmmPost>().where("state = {0}", 1).andNew("title like '%"+keyword+"%'")
//						.or("content like " + "'%"+ keyword + "%'").or("content like "+ "'%" + keyword+ "%'"));
        //@todo
        GameDto gameDto = new GameDto();
        gameDto.setState(0);
        gameDto.setName(keyword);
        int gameCount = iGameProxyService.getGameSelectCountByLikeNameAndState(gameDto);//gameService.selectCount(new EntityWrapper<Game>().where("state = {0}", 0).like("name", keyword));
        int userCount = memberService.selectCount(getWrapper(keyword));
        SearCount searchCount = new SearCount();
        searchCount.setGameCount(gameCount);
        searchCount.setPostCount(postCount);
        searchCount.setUserCount(userCount);
        searchCount.setKeyword(keyword);

        return ResultDto.success(searchCount);
    }

    private Wrapper getWrapper(String keyword){
        Wrapper wrapperSearch = Condition.create()
                .setSqlSelect("id,user_name as userName,avatar,sign,level,created_at as createdAt,updated_at as updatedAt,member_no as memberNo");

        wrapperSearch.where("state = {0}", 0);

        wrapperSearch.like("member_no", keyword);
        wrapperSearch.orNew("user_name like '%" + keyword + "%'");


        String orderByStr = "";
        //是数字
        if (StringUtil.checkNum(keyword)) {
            //数字
            orderByStr = "LOCATE('" + keyword + "',member_no) DESC , LOCATE('" + keyword + "',user_name) DESC";
        } else {
            //非数字
            orderByStr = "REPLACE(user_name,'" + keyword + "','')";
        }

        wrapperSearch.orderBy(orderByStr);
        return wrapperSearch;
    }

    private void initFilterWord() {
        Set<String> filterWord = new HashSet<>();
        List<BasFilterword> wordList = filterService.selectList(Condition.create().setSqlSelect("key_word as keyWord"));
        for (BasFilterword word : wordList) {
            filterWord.add(word.getKeyWord());
        }
        SensitiveWordUtil.init(filterWord);
    }

}
