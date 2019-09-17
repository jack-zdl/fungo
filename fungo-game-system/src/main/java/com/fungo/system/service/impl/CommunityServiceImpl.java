package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.dao.MemberDao;
import com.fungo.system.entity.BasAction;
import com.fungo.system.entity.Member;
import com.fungo.system.facede.ICommunityProxyService;
import com.fungo.system.facede.IGameProxyService;
import com.fungo.system.service.BasActionService;
import com.fungo.system.service.ICommunityService;
import com.fungo.system.service.MemberService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommunityServiceImpl implements ICommunityService {


    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityServiceImpl.class);

    @Autowired
    private MemberService menberService;

    @Autowired
    private BasActionService actionService;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ICommunityProxyService communityProxyService;

    @Autowired
    private IGameProxyService iGameProxyService;

    //@Cacheable(cacheNames = {FunGoGameConsts.CACHE_EH_NAME}, key = "'" + FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER + "_recommendMembers'+ #currentMb_id")
    @Override
    public List<Member> getRecomMembers(int limit, String currentMb_id) {

        List<Member> recommendedMbsList = new ArrayList<>();

        //查询当前登录用户关注的所有用户
        List<Member> watchMebmberList = null;
        List<String> wathMbsSet = new ArrayList<>();
        if (StringUtils.isNotBlank(currentMb_id)) {
            watchMebmberList = this.getWatchMebmber(0, currentMb_id);
            if (null != watchMebmberList && !watchMebmberList.isEmpty()) {
                for (Member member : watchMebmberList) {
                    member.setFollowed(true);
                    wathMbsSet.add(member.getId());
                }
            }
        }
//        LOGGER.info("查询当前登录用户关注的所有用户:{}", wathMbsSet.toString());

        //先获取官方推荐和符合条件推荐用户
        List<Member> ml1 = getRecommeMembers(limit, currentMb_id, wathMbsSet);

        int recommeMbs = 0;
        if (null != ml1 && !ml1.isEmpty()) {
            recommeMbs = ml1.size();
            for (Member member : ml1) {
                member.setFollowed(false);
            }
        }

        if (null != ml1 && !ml1.isEmpty()) {
            recommendedMbsList.addAll(ml1);
            ml1.clear();
        }

        //及时刷新出推荐用户
        //若推荐用户不足limit数量，查询该用户已关注用户
        limit -= recommeMbs;
        if (limit > 0 && null != watchMebmberList && !watchMebmberList.isEmpty()) {
            for (int i = 0; i < limit; i++) {
                if (i < watchMebmberList.size()) {
                    recommendedMbsList.add(watchMebmberList.get(i));
                }
            }

        }
        LOGGER.info("===========recommendedMbsList:{}", recommendedMbsList);
        return recommendedMbsList;

    }

    /**
     * 查询当前登录用户所关注的其他用户
     * @param limit
     * @param currentMb_id
     * @return
     */
    private List<Member> getWatchMebmber(int limit, String currentMb_id) {

        EntityWrapper actionEntityWrapper = new EntityWrapper<BasAction>();
        actionEntityWrapper.setSqlSelect("target_id as targetId");

        //target_type 0 关注用户
        actionEntityWrapper.eq("state", "0").eq("type", 5).eq("member_id", currentMb_id).eq("target_type", 0);
        actionEntityWrapper.orderBy("created_at", false);
        if (limit > 0) {
            actionEntityWrapper.last("limit " + limit);
        }
        List<BasAction> watchMebmberIdsList = actionService.selectList(actionEntityWrapper);

        if (null != watchMebmberIdsList && !watchMebmberIdsList.isEmpty()) {
            StringBuffer mbIds = new StringBuffer();
            for (BasAction basAction : watchMebmberIdsList) {
                if (null != basAction) {
                    mbIds = mbIds.append(basAction.getTargetId());
                    mbIds = mbIds.append(",");
                }
            }
            mbIds.deleteCharAt(mbIds.length() - 1);

            //查询出符合推荐条件用户的详情数据
            if (mbIds.length() > 0) {
                List<Member> watchMebmberList = menberService.selectList(new EntityWrapper<Member>().in("id", mbIds.toString()).eq("state", 0));
                return watchMebmberList;
            }
        }
        return null;
    }


    /**
     * 获取官方推荐和符合条件推荐用户
     *       玩家推荐规则：
     *          规则一 玩家推荐：
     *           发布文章数 大于10 或者 游戏评论数大于14
     *          规则二 玩家已关注列表替换规则：
     *           1.显示已经关注用户数量：最大10个
     *           2.若有新的推荐用户，且未关注，替换到玩家已关注列表的前面。且 该类别数量恒定10人
     * @param limit
     * @param currentMb_id 当前登录用户ID
     * @param wathMbsSet 当前登录用户，已经关注的用户IDS
     * @return
     */
    private List<Member> getRecommeMembers(int limit, String currentMb_id, List<String> wathMbsSet) {
        //查官方推荐和符合条件推荐用户
        //推荐用户最大数
        long limitSize = 10L;
        if (limit > 0) {
            limitSize = limit;
        }
        limitSize = 100;
        //发布文章数10条
        long sendArticles = 10L;
        //发布评论数14条
        long sendComments = 14L;


        /*
         * 1.找出官方推荐玩家 (数据库状态)
         * 2.发布文章数>10或游戏评论>14 （精选文章/评论 *5）
         * 3.合并去重
         */
        //fix: 从用户中查询出所有被推荐的用户，且状态是0(正常)  [by mxf 2019-01-09]
        EntityWrapper memberSqlWrapper = new EntityWrapper<Member>();
        memberSqlWrapper.eq("type", 2).eq("state", 0);

        //去除当前登录用户和已经关注用户
        if (StringUtils.isNotBlank(currentMb_id) || !wathMbsSet.isEmpty()) {
            wathMbsSet.add(currentMb_id);
            memberSqlWrapper.notIn("id", wathMbsSet);
        }
        //

        memberSqlWrapper.orderBy("sort", false);
        memberSqlWrapper.last("limit " + limitSize);

        List<Member> ml1 = menberService.selectList(memberSqlWrapper);

        if (null == ml1 || ml1.isEmpty() || ml1.size() < limitSize) {

            //官方推荐用户id
            List<String> reIds = new ArrayList<>();

            for (Member member : ml1) {
                reIds.add(member.getId());
            }

            //fix:查询 符合 发布文章数>10或游戏评论>14 （精选文章/评论 *5)的用户 的业务逻辑修改 [by mxf 2019-01-09]
            //条件用户
            //List<HashMap<String, Object>> members = memberDao.getRecommendMembers();
            Set<String> members = new HashSet<>();


            //1.先查询发布文章数大于10条的用户
            // @todo 5.22
            List<String> sendArticleMembers = communityProxyService.getRecommendMembersFromCmmPost(sendArticles,limitSize, wathMbsSet); //memberDao.getRecommendMembersFromCmmPost(sendArticles, limitSize, wathMbsSet);
//            LOGGER.info("查询发布文章数大于10条的用户:{}", sendArticleMembers.toString());

            if (null != sendArticleMembers && !sendArticleMembers.isEmpty()) {
                members.addAll(sendArticleMembers);
                sendArticleMembers.clear();
            }

            //2.再查询发布游戏评论>大于14条的，前10名用户
            // @todo 5.22
            List<String> sendCommentMembers = iGameProxyService.getRecommendMembersFromEvaluation(Long.valueOf(sendComments).intValue(), Long.valueOf(limitSize).intValue(), wathMbsSet);   //memberDao.getRecommendMembersFromEvaluation(sendComments, limitSize, wathMbsSet);

//            LOGGER.info("查询发布游戏评论>大于14条的，前10名用户:{}", sendCommentMembers.toString());
            if (null != sendCommentMembers && !sendCommentMembers.isEmpty()) {
                members.addAll(sendCommentMembers);
                sendCommentMembers.clear();
            }

            int ormCount = 0;
            if (null == ml1) {
                ml1 = new ArrayList<Member>();
            } else {
                ormCount = ml1.size();
            }

            //所有符合推荐条件用户id
            List<String> memberIds = new ArrayList<>();

            //查询未包含在官方推荐中且符合推荐条件的用户id
            for (String mb_id : members) {
                int remMebs = ormCount + memberIds.size();
                if (remMebs == limitSize) {
                    break;
                }
                if (!reIds.contains(mb_id)) {
                    memberIds.add(mb_id);
                }
            }

            //查询出符合推荐条件用户的详情数据
            if (memberIds.size() > 0 ) {
                List<Member> recommendList = menberService.selectList(new EntityWrapper<Member>().in("id", memberIds).eq("state", 0));
                //.gt( "sort",0 )
                //                        .orderBy( "sort",false ).last( "10")
                ml1.addAll(recommendList);
            }

//            if(ml1.size() < 10){
                List<String> ids = ml1.stream().map( Member::getId ).collect( Collectors.toList());
                ml1 = memberDao.getUnfollerMemberList(ids,currentMb_id);
//                List<Member> sortMemberList = menberService.selectList(new EntityWrapper<Member>().notIn("id", ids).eq("state", 0).gt( "sort",0 )
//                        .orderBy( "sort",false ).last( "10"));
//                ml1.addAll(sortMemberList);
//            }
            //若此时还没有足够人数则
            if(ml1.size() < 10){
                return ml1;
            }
        }
        //---end
        return ml1.subList(0, 10);
    }

    //-------
}
