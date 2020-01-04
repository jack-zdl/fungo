package com.fungo.system.service.portal.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.dao.BasActionDao;
import com.fungo.system.dao.IncentRuleRankGroupDao;
import com.fungo.system.dao.MemberCircleMapper;
import com.fungo.system.dto.FollowInptPageDao;
import com.fungo.system.entity.*;
import com.fungo.system.feign.CommunityFeignClient;
import com.fungo.system.service.*;
import com.fungo.system.service.portal.PortalSystemIUserService;
import com.game.common.api.InputPageDto;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.MemberCmmCircleDto;
import com.game.common.enums.AbstractResultEnum;
import com.game.common.repo.cache.facade.FungoCacheMember;
import com.game.common.util.CommonUtil;
import com.game.common.util.PageTools;
import com.game.common.util.date.DateTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/6/11 14:31
 */
@Service
public class PortalSystemUserServiceImpl implements PortalSystemIUserService {

    private static final Logger logger = LoggerFactory.getLogger(PortalSystemUserServiceImpl.class);

    @Autowired
    private FungoCacheMember fungoCacheMember;
    @Autowired
    private MemberFollowerService followService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private CommunityFeignClient communityFeignClient;
    @Autowired
    private BasActionDao actionDao;
    @Autowired
    private IMemberIncentRuleRankService IRuleRankService;
    @Autowired
    private BasActionService actionService;
    @Autowired
    private IncentRankedService rankedService;
    @Autowired
    private IncentRuleRankService rankRuleService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private IncentRuleRankGroupDao incentRuleRankGroupDao;
    @Autowired
    private MemberCircleMapper memberCircleMapper;


    @Override
    public AuthorBean getUserCard(String cardId, String memberId) throws IOException {
        AuthorBean author = null;
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_CARD + cardId;
        author = (AuthorBean) fungoCacheMember.getIndexCache(keyPrefix, memberId);
        if (null != author) {
            return author;
        }
        author = iUserService.getAuthor(cardId);
        if (!CommonUtil.isNull(memberId)) {
//			BasAction action=actionService.selectOne(new EntityWrapper<BasAction>().eq("type", "5").eq("member_id",memberId).eq("target_id", cardId).notIn("state", "-1"));
            MemberFollower one = followService.selectOne(new EntityWrapper<MemberFollower>().eq("member_id", memberId).eq("follower_id", cardId).andNew("state = {0}", 1).or(" state =  {0}", 2));
            if (one != null) {
                author.setIs_followed(true);
//                PC2.0新增相互关注业务添加字段 mutualFollowed
                if (one.getState().equals(2)){
                    author.setMutualFollowed("1");
                }
            }
        }
//        PC2.0新增浏览量 根据跟用户ID获取文章的浏览量
        ResultDto<Integer> resultDto = communityFeignClient.getPostBoomWatchNumByCardId(cardId);
        if (resultDto != null){
            author.setWatchNum(resultDto.getData() == null ? 0 : resultDto.getData());
        }

        ObjectMapper mapper = new ObjectMapper();

        //荣誉,身份图片
        List<IncentRanked> list = rankedService.selectList(new EntityWrapper<IncentRanked>().eq("mb_id", cardId));
        for (IncentRanked ranked : list) {
            if (ranked.getRankType() == 1) {
                String rankIdtIds = ranked.getRankIdtIds();
                List<HashMap<String,Object>> medalList = mapper.readValue(rankIdtIds, ArrayList.class);
                author.setHonorNumber( medalList.size());
                IncentRuleRank rank = rankRuleService.selectById(ranked.getCurrentRankId());//最近获得
//                author.setLevel(ranked.getCurrentRankId().intValue());
                String rankImgs = rank.getRankImgs();
                ArrayList<HashMap<String, Object>> urlList = mapper.readValue(rankImgs, ArrayList.class);
                author.setDignityImg((String) urlList.get(0).get("url"));
            } else if (ranked.getRankType() == 2) {
                String rankIdtIds = ranked.getRankIdtIds();
                List<HashMap<String,Object>> list1 = mapper.readValue(rankIdtIds, ArrayList.class);
                List<List<HashMap<String,Object>>> statusLists = new ArrayList<>(  );
                int groupLevel = 0;
                int circleLevel = 0;
                for (HashMap<String,Object> map : list1){
                    Integer rankId = (Integer) map.get( "1" );
                    IncentRuleRank rank = rankRuleService.selectById(rankId);//最近获得
                    String rankImgs = rank.getRankImgs();
                    ArrayList<HashMap<String, Object>> urlList = null;
                    IncentRuleRankGroup incentRuleRankGroup = incentRuleRankGroupDao.selectById( rank.getRankGroupId());
                    groupLevel =  incentRuleRankGroup.getAuth() > groupLevel ? incentRuleRankGroup.getAuth() : groupLevel;
                    circleLevel =  incentRuleRankGroup.getAuth() == 2 ? incentRuleRankGroup.getAuth() : circleLevel;
                    try {
                        urlList = mapper.readValue(rankImgs, ArrayList.class);
                        urlList.stream().forEach( s ->{
                            s.put( "auth", incentRuleRankGroup.getAuth());
                            s.put( "group", incentRuleRankGroup.getId());
                        } );
                        statusLists.add( urlList );
                    } catch (IOException e) {
                        logger.error( "對象轉換异常",e );
                    }
                }
                author.setGroupLevel(groupLevel);
                author.setCircleLevel( circleLevel );
                author.setStatusImgs(statusLists);
            } else if (ranked.getRankType() == 3) {
                //找出获得的荣誉合集 (勛章之類的)
                String rankIdtIds = ranked.getRankIdtIds();
                ArrayList<HashMap<String, Object>> rankList = mapper.readValue(rankIdtIds, ArrayList.class);
                Collections.reverse(rankList);
                List<String> honorImgList = new ArrayList<>();
                int i = 0;
                ArrayList<String> groupIdList = new ArrayList<>();
                //取前三位
                for (HashMap<String, Object> map : rankList) {
                    IncentRuleRank rank = rankRuleService.selectById(Long.parseLong(map.get("1") + ""));
                    //同一荣誉取等级最高的一个
                    if (rank != null && !groupIdList.contains(rank.getRankGroupId())) {
                        groupIdList.add(rank.getRankGroupId());
                        if (rank.getRankImgs() != null) {
                            ArrayList<HashMap<String, Object>> urlkList = mapper.readValue(rank.getRankImgs(), ArrayList.class);
                            honorImgList.add((String) urlkList.get(0).get("url"));
                        }
                        i++;
                        if (i > 2) {
                            break;
                        }
                    }
                }
                author.setHonorImgList(honorImgList);
            }
        }
        if(!CommonUtil.isNull( memberId)  ){
            if(cardId.equals( memberId)){
                author.setGroupStatus( 0);
            }else {
                int circleLevel = 0;
                List<IncentRanked> cardIdlist = rankedService.selectList(new EntityWrapper<IncentRanked>().eq("mb_id", memberId));
                for (IncentRanked ranked : cardIdlist) {
                    if (ranked.getRankType() == 2) {
                        String rankIdtIds = ranked.getRankIdtIds();
                        List<HashMap<String,Object>> list1 = mapper.readValue(rankIdtIds, ArrayList.class);
                        List<List<HashMap<String,Object>>> statusLists = new ArrayList<>(  );
                        int groupLevel = 0;
                        for (HashMap<String,Object> map : list1){
                            Integer rankId = (Integer) map.get( "1" );
                            IncentRuleRank rank = rankRuleService.selectById(rankId);//最近获得
                            String rankImgs = rank.getRankImgs();
                            ArrayList<HashMap<String, Object>> urlList = null;
                            IncentRuleRankGroup incentRuleRankGroup = incentRuleRankGroupDao.selectById( rank.getRankGroupId());
                            groupLevel =  incentRuleRankGroup.getAuth() > groupLevel ? incentRuleRankGroup.getAuth() : groupLevel;
                            circleLevel =  incentRuleRankGroup.getAuth() == 2 ? incentRuleRankGroup.getAuth() : circleLevel;
                            try {
                                urlList = mapper.readValue(rankImgs, ArrayList.class);
                                urlList.stream().forEach( s ->{
                                    s.put( "auth", incentRuleRankGroup.getAuth());
                                    s.put( "group", incentRuleRankGroup.getId());
                                    s.put( "groupNmae", incentRuleRankGroup.getGroupName());
                                } );
                                statusLists.add( urlList );
                            } catch (IOException e) {
                                logger.error( "對象轉換异常",e );
                            }
                        }
                    }
                }
                if(circleLevel == 2 && circleLevel > author.getCircleLevel()){
                    author.setGroupStatus(1);
                }
            }
        }
        MemberCmmCircleDto memberCmmCircleDto = memberCircleMapper.selectMemberCircleByUserId(cardId);
        if(memberCmmCircleDto != null && !CommonUtil.isNull(  memberCmmCircleDto.getId() ) ){
            author.setMemberCmmCircleDto( memberCmmCircleDto);
        }
        //redis cache
        fungoCacheMember.excIndexCache(true, keyPrefix, memberId, author);
        return author;
    }

    @Override
    public FungoPageResultDto<Map<String, Object>> getFollower(String myId, String memberId, FollowInptPageDao inputPage) throws Exception {
        FungoPageResultDto<Map<String, Object>> re = null;
        try {
            //from redis cache
            String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_FOLLW + myId;
            String keySuffix = JSON.toJSONString(inputPage);
            re = (FungoPageResultDto<Map<String, Object>>) fungoCacheMember.getIndexCache(keyPrefix, memberId + keySuffix);
            if (null != re && null != re.getData() && re.getData().size() > 0) {
                return re;
            }

            re = new FungoPageResultDto<>();

            List<Map<String, Object>> list =  null ;
            Page p = new Page(inputPage.getPage(), inputPage.getLimit());
            if (0 == inputPage.getType()) {//关注的用户
                list = actionDao.getFollowerUser(p, memberId);
                ObjectMapper mapper = new ObjectMapper();
                List<IncentRuleRank> levelRankList = IRuleRankService.getLevelRankList();
                for (Map<String, Object> map : list) {
                    map.put("is_followed", false);
                    BasAction action = actionService.selectOne(new EntityWrapper<BasAction>().eq("type", "5").eq("member_id", myId).eq("target_id", (String) map.get("objectId")).ne("state", "-1"));
                    if (action != null) {
                        map.put("is_followed", true);
                    }
                    BasAction otherAction = actionService.selectOne(new EntityWrapper<BasAction>().eq("type", "5").eq("member_id", (String) map.get("objectId")).eq("target_id", myId).ne("state", "-1"));
                    if(otherAction != null){
                        map.put("is_mutual_followed", true);
                    }
                    String rankImgs = getLevelRankUrl((int) map.get("level"), levelRankList);
                    ArrayList<HashMap<String, Object>> urlList = mapper.readValue(rankImgs, ArrayList.class);
                    map.put("dignityImg", (String) urlList.get(0).get("url"));
                    //用户官方身份
                    IncentRanked ranked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", (String) map.get("objectId")).eq("rank_type", 2));
                    if (ranked != null) {
                        IncentRuleRank rank = rankRuleService.selectById(ranked.getCurrentRankId());//最近获得
                        if (rank != null) {
                            String rankinf = rank.getRankImgs();
                            ArrayList<HashMap<String, Object>> infolist = mapper.readValue(rankinf, ArrayList.class);
                            map.put("statusImg", infolist);
                        } else {
                            map.put("statusImg", new ArrayList<>());
                        }
                    } else {
                        map.put("statusImg", new ArrayList<>());
                    }
                }
                re.setData(list);
                PageTools.pageToResultDto(re, p);
            } else if (4 == inputPage.getType()) {//关注的社区
                // @todo 5.22
                List<String> idlist = actionDao.getFollowerCommunity(memberId);
                FungoPageResultDto<Map<String, Object>> resultDto = communityFeignClient.getFollowerCommunity(p.getPages(),p.getLimit(),idlist);
                list = resultDto.getData();
//            list = communityProxyService.getFollowerCommunity(p, idlist);    //actionDao.getFollowerCommunity(p, memberId);
                for (Map<String, Object> map : list) {
                    map.put("is_followed", false);
                    BasAction action = actionService.selectOne(new EntityWrapper<BasAction>().eq("type", "5").eq("member_id", myId).eq("target_id", (String) map.get("objectId")).ne("state", "-1"));
                    if (action != null) {
                        map.put("is_followed", true);
                    }
                }
                re.setData(list);
                PageTools.newPageToResultDto(re, resultDto.getCount(),resultDto.getPages(),inputPage.getPage());
            }
            //redis cache
            fungoCacheMember.excIndexCache(true, keyPrefix, keySuffix, re);
            return re;
        }catch (Exception e){
            logger.error("pc获取用户关注",e);
        }
        return null;
    }

    @Override
    public FungoPageResultDto<Map<String, Object>> getFollowee(String myId, String memberId, InputPageDto inputPage) throws Exception {
        FungoPageResultDto<Map<String, Object>> re = null;
        try{
            re = new FungoPageResultDto<>();
            List<Map<String, Object>> list = new ArrayList<>();

            Page<BasAction> plist = actionService.selectPage(new Page<BasAction>(inputPage.getPage(), inputPage.getLimit()), new EntityWrapper<BasAction>().eq("type", "5").eq("target_id", memberId).notIn("state", "-1"));
            List<BasAction> list1 = plist.getRecords();
            ObjectMapper mapper = new ObjectMapper();
            List<IncentRuleRank> levelRankList = IRuleRankService.getLevelRankList();
            for (BasAction basAction : list1) {
                Map<String, Object> map = new HashMap<String, Object>();
                Member m = memberService.selectById(basAction.getMemberId());
                //fix bug: m 为空导致的NPE [by mxf 2019-04-01]
                if (null == m) {
                    continue;
                }
                //end
//			IncentRanked ranked = incentRankedService.selectOne(new EntityWrapper<IncentRanked>().eq("rank_type",1).eq("mb_id",basAction.getMemberId()));
                map.put("sign", m.getSign());
                map.put("username", m.getUserName());
                map.put("level", m.getLevel());
                map.put("avatar", m.getAvatar());
                map.put("is_followed", false);
                map.put("member_no", m.getMemberNo());
                BasAction action = actionService.selectOne(new EntityWrapper<BasAction>().eq("type", "5").eq("member_id", myId).eq("target_id", m.getId()).ne("state", "-1"));
                if (action != null) {
                    map.put("is_followed", true);
                }
                BasAction otherAction = actionService.selectOne(new EntityWrapper<BasAction>().eq("type", "5").eq("member_id", m.getId()).eq("target_id", myId).ne("state", "-1"));
                if (otherAction != null) {
                    map.put("is_mutual_followed", true);
                }
                String rankImgs = getLevelRankUrl(m.getLevel(), levelRankList);
                ArrayList<HashMap<String, Object>> urlList = mapper.readValue(rankImgs, ArrayList.class);
                map.put("dignityImg", (String) urlList.get(0).get("url"));
                //用户官方身份
                IncentRanked ranked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", m.getId()).eq("rank_type", 2));
                if (ranked != null) {
                    IncentRuleRank rank = rankRuleService.selectById(ranked.getCurrentRankId());//最近获得
                    if (rank != null) {
                        String rankinf = rank.getRankImgs();
                        ArrayList<HashMap<String, Object>> infolist = mapper.readValue(rankinf, ArrayList.class);
                        map.put("statusImg", infolist);
                    } else {
                        map.put("statusImg", new ArrayList<>());
                    }
                } else {
                    map.put("statusImg", new ArrayList<>());
                }
                map.put("objectId", m.getId());
                map.put("createdAt", DateTools.fmtDate(m.getCreatedAt()));
                map.put("updatedAt", DateTools.fmtDate(m.getUpdatedAt()));
                list.add(map);
            }
            list = list.stream().distinct().collect( Collectors.toList() );
            re.setData(list);
            PageTools.pageToResultDto(re, plist);
        }catch (Exception e){
            logger.error("pc端获取用户粉丝异常",e);
            re = FungoPageResultDto.FungoPageResultDtoFactory.buildError("pc端获取用户粉丝异常"+ AbstractResultEnum.CODE_SYSTEM_TWO.getFailevalue());
        }
        return re;
    }


    public String getLevelRankUrl(int level, List<IncentRuleRank> levelRankList) {
        String url = "";
        for (IncentRuleRank ruleRank : levelRankList) {
            if (ruleRank.getRankIdt() == level) {
                url = ruleRank.getRankImgs();
                break;
            }
        }
        return url;
    }

}
