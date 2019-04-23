package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.fungo.system.dto.IncentRuleRankOut;
import com.fungo.system.entity.IncentRanked;
import com.fungo.system.entity.IncentRuleRank;
import com.fungo.system.entity.IncentRuleRankGroup;
import com.fungo.system.entity.Member;
import com.fungo.system.service.*;
import com.game.common.consts.FunGoGameConsts;
import com.game.common.consts.Setting;
import com.game.common.repo.cache.facade.FungoCacheIncentRule;
import com.game.common.util.exception.BusinessException;
import com.game.common.util.CommonUtils;
import com.game.common.util.FunGoEHCacheUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberIncentRuleRankServiceImpl implements IMemberIncentRuleRankService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MemberIncentRuleRankServiceImpl.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private IMemberIncentRankedDaoService iMemberIncentRankedDaoService;


    @Autowired
    private IMembertIncentRuleRankDaoService iMembertIncentRuleRankDaoService;

    @Autowired
    private IMemberIncentRuleRankGroupDaoService iMemberIncentRuleRankGroupDaoService;

    @Autowired
    private IGameProxy iGameProxy;

    @Autowired
    private FungoCacheIncentRule fungoCacheIncentRule;


    /**
     * 该功能ehcache key 前缀
     */
    private static final String CACHEKEY_GETINCENTRULE = FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER + "IncentRule_";


    @Override
    public void transfMemberLevel(String mbId) throws BusinessException {

        try {
            LOGGER.info("------------迁移用户-mbId:{}等级数据开始......", mbId);
            Member member = memberService.selectOne(new EntityWrapper<Member>().eq("id", mbId));
            if (null != member) {

                //获取用户id
                String mb_id = member.getId();

                //用户名
                String user_name = member.getUserName();

                //手机号
                String phone = member.getMobilePhoneNum();

                if (StringUtils.isNotBlank(phone)) {
                    user_name = phone;
                }


                //用户等级
                Integer level = member.getLevel();


                IncentRanked incentRanked = new IncentRanked();
                incentRanked.setId(Long.parseLong(CommonUtils.getRandomDigit(15)));
                incentRanked.setMbId(mb_id);
                incentRanked.setMbUserName(user_name);

                incentRanked.setCurrentRankId(level.longValue());
                incentRanked.setCurrentRankName("Lv" + level.longValue());

                ArrayList oldLevelList = new ArrayList();
                for (long j = 1; j <= level; j++) {
                    //{rankId,rankName}
                    Map<String, String> levelMap = new HashMap<>();
                    levelMap.put("rankId", j + "");
                    levelMap.put("rankName", "Lv" + j);

                    oldLevelList.add(levelMap);
                }

                String rankIdtIds = JSONObject.toJSONString(oldLevelList);
                incentRanked.setRankIdtIds(rankIdtIds);

                incentRanked.setRankType(1);
                incentRanked.setExt1("");
                incentRanked.setExt2("");
                incentRanked.setExt3("");

                iMemberIncentRankedDaoService.insert(incentRanked);


            }

            LOGGER.info("------------迁移用户等级数据结束......");
        } catch (BusinessException ex) {
            ex.printStackTrace();

        }

    }

    @Override
    public void transfMemberLevel() throws BusinessException {

        try {
            LOGGER.info("------------迁移用户等级数据开始......");


            List<Member> memberList = memberService.selectList(new EntityWrapper<Member>());
            if (null != memberList && !memberList.isEmpty()) {

                for (int i = 0; i < memberList.size(); i++) {

                    Member member = memberList.get(i);

                    //获取用户id
                    String mb_id = member.getId();

                    //用户名
                    String user_name = member.getUserName();

                    //手机号
                    String phone = member.getMobilePhoneNum();

                    if (StringUtils.isNotBlank(phone)) {
                        user_name = phone;
                    }


                    //用户等级
                    Integer level = member.getLevel();


                    IncentRanked incentRanked = new IncentRanked();
                    incentRanked.setId(Long.parseLong(CommonUtils.getRandomDigit(15)));
                    incentRanked.setMbId(mb_id);
                    incentRanked.setMbUserName(user_name);

                    incentRanked.setCurrentRankId(level.longValue());
                    incentRanked.setCurrentRankName("Lv" + level.longValue());

                    ArrayList oldLevelList = new ArrayList();
                    for (long j = 1; j <= level; j++) {
                        //{rankId,rankName}
                        Map<String, String> levelMap = new HashMap<>();
                        levelMap.put("rankId", j + "");
                        levelMap.put("rankName", "Lv" + j);

                        oldLevelList.add(levelMap);
                    }

                    String rankIdtIds = JSONObject.toJSONString(oldLevelList);
                    incentRanked.setRankIdtIds(rankIdtIds);

                    incentRanked.setRankType(1);
                    incentRanked.setExt1("");
                    incentRanked.setExt2("");
                    incentRanked.setExt3("");

                    iMemberIncentRankedDaoService.insert(incentRanked);

                }
            }

            LOGGER.info("------------迁移用户等级数据结束......");
        } catch (BusinessException ex) {
            ex.printStackTrace();

        }
    }


    @Override
    public void transfMemberThird(Map<String, String> authMap) {

        try {

            LOGGER.info("------------迁移用户历史第三方任务任务值和fungo币数据开始......");
            String mb_id = null;
            if (null != authMap && !authMap.isEmpty()) {
                mb_id = authMap.get("mb_id");
            }

            //1.从t_member表中查询所有用户
            List<Member> memberList = memberService.selectList(new EntityWrapper<Member>());
            if (null != memberList && !memberList.isEmpty()) {

                for (int i = 0; i < memberList.size(); i++) {

                    Member member = memberList.get(i);

                    String memberId = member.getId();

//                    if (StringUtils.isNotBlank(mb_id)){
//                        memberId = mb_id;
//                    }

                    //qq  qq_open_id
                    String qq_open_id = member.getQqOpenId();

                    // 微信
                    String weixin_open_id = member.getWeixinOpenId();

                    //微博
                    String weibo_open_id = member.getWeiboOpenId();

                    LOGGER.info("迁移用户等级数据,memberId:" + memberId);

                    //更新用户经验值账户 和 fungo币账户
                    //taskName:绑定QQ/微信/微博
                    //经验值taskId: 4a421f7498dc406d85a9878a36ea6edd
                    //fungo币taskId: 93ce12c813894196aff25537ecb0cefd
                    //qq
                    if (null != qq_open_id) {

                        //完成绑定一条  经验值 + 2
                        //完成绑定一条  fungo币 + 100
                        iGameProxy.addTaskCore(Setting.ACTION_TYPE_BIND_THIRDPARTY, memberId, "", -1);
                    }

                    //微信
                    if (null != weixin_open_id) {
                        //完成绑定一条  经验值 + 2
                        //完成绑定一条  fungo币 + 100
                        iGameProxy.addTaskCore(Setting.ACTION_TYPE_BIND_THIRDPARTY, memberId, "", -1);

                    }

                    //微博
                    if (null != weibo_open_id) {
                        //完成绑定一条  经验值 + 2
                        //完成绑定一条  fungo币 + 100
                        iGameProxy.addTaskCore(Setting.ACTION_TYPE_BIND_THIRDPARTY, memberId, "", -1);

                    }

                }

            }

            LOGGER.info("------------迁移用户历史第三方任务任务值和fungo币数据结束......");
        } catch (Exception ex) {
            LOGGER.error("------------迁移用户历史第三方任务任务值和fungo币数据出现异常");
            ex.printStackTrace();
            throw new BusinessException("-1", "迁移用户历史第三方任务任务值和fungo币数据出现异常");
        }

    }


    @Cacheable(cacheNames = {FunGoGameConsts.CACHE_EH_NAME}, key = "'" + CACHEKEY_GETINCENTRULE + "' + #rankGroupId  + #rankType  + #rankFlag +#isIncludeChild")
    @Override
    public List<Map<String, Object>> getIncentRule(Long rankGroupId, int rankType, int rankFlag, boolean isIncludeChild) throws BusinessException {

        try {

            //先构建分组数据
            List<Map<String, Object>> rankResultList = getRuleRankDataFromDB(rankType, isIncludeChild);

            return rankResultList;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BusinessException("-1", "获取权益规则出现异常");
        }
    }


    @Override
    public List<Map<String, Object>> getIncentRuleForCustomCache(Long rankGroupId, int rankType, int rankFlag, boolean isIncludeChild) throws BusinessException {
        try {

            //先从缓存中取数据
            String cacheKey = CACHEKEY_GETINCENTRULE + rankGroupId + rankType + rankFlag + isIncludeChild;
            Object cacheObj = FunGoEHCacheUtils.get(FunGoGameConsts.CACHE_EH_NAME, cacheKey);
            if (null != cacheObj) {
                List list = JSONObject.parseObject(String.valueOf(cacheObj), List.class);
                return list;
            }

            List<Map<String, Object>> rankResultList = getRuleRankDataFromDB(rankType, isIncludeChild);

            //把数据缓存
            FunGoEHCacheUtils.put(FunGoGameConsts.CACHE_EH_NAME, CACHEKEY_GETINCENTRULE + rankGroupId + rankType + rankFlag + isIncludeChild, JSONObject.toJSONString(rankResultList));

            return rankResultList;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BusinessException("-1", "获取权益规则出现异常");
        }
    }


    /**
     * 从DB获取荣誉规则数据
     * @param rankType
     * @param isIncludeChild
     * @return
     */
    private List<Map<String, Object>> getRuleRankDataFromDB(int rankType, boolean isIncludeChild) {
        //先构建分组数据
        Wrapper wrapperGroup = new EntityWrapper<IncentRuleRankGroup>();
        Map<String, Object> criteriaMapGroup = new HashMap<String, Object>();

        //包含子节点
        if (isIncludeChild) {
            criteriaMapGroup.put("rank_type", rankType);
        } else {
            criteriaMapGroup.put("rank_type", rankType);
            criteriaMapGroup.put("group_parent_id", FunGoGameConsts.FUNGO_PUBLIC_PARENT_ID_IS_NULL);
        }

        wrapperGroup.allEq(criteriaMapGroup).ne("group_parent_id", FunGoGameConsts.FUNGO_PUBLIC_PARENT_ID_IS_NULL).
                orderBy("sort", true);
        //获取规则分组数据
        List<IncentRuleRankGroup> rankRulegroupList = iMemberIncentRuleRankGroupDaoService.selectList(wrapperGroup);

        if (null == rankRulegroupList || rankRulegroupList.isEmpty()) {
            return null;
        }

        List<Map<String, Object>> rankResultList = new ArrayList<Map<String, Object>>();

        //查询分类下的规则数据
        List<Long> groupIdList = new ArrayList<Long>();
        for (IncentRuleRankGroup rankGroup : rankRulegroupList) {
            groupIdList.add(rankGroup.getId());
        }

        //封装查询规则的条件
        Wrapper wrapper = new EntityWrapper<IncentRuleRank>();

        Map<String, Object> criteriaMap = new HashMap<String, Object>();
        //criteriaMap.put("rank_group_id", String.valueOf(rankGroup.getId()) );
        criteriaMap.put("rank_type", rankType);
        criteriaMap.put("is_active", FunGoGameConsts.FUNGO_PUBLIC_IS_ACTIVE_ABLE);

        wrapper.in("rank_group_id", groupIdList.toArray()).allEq(criteriaMap).orderBy("sort", true);
        List<IncentRuleRank> ruleRankOutList = iMembertIncentRuleRankDaoService.selectList(wrapper);

        for (IncentRuleRankGroup rankGroup : rankRulegroupList) {

            Long rankGroupIdData = rankGroup.getId();
            String rankGroupIdData_str = String.valueOf(rankGroupIdData.longValue());

            Map<String, Object> rankRuleGroupMap = new HashMap<String, Object>();

            rankResultList.add(rankRuleGroupMap);

            rankRuleGroupMap.put("ruleRankGroupName", rankGroup.getGroupName());
            rankRuleGroupMap.put("ruleRankGroupId", rankGroup.getId());

            if (null != ruleRankOutList && !ruleRankOutList.isEmpty()) {

                List<IncentRuleRankOut> rankOutList = new ArrayList<IncentRuleRankOut>();

                for (IncentRuleRank ruleRank : ruleRankOutList) {

                    //
                    String ruleRankParentId_Group = ruleRank.getRankGroupId();
                    if (StringUtils.equalsIgnoreCase(rankGroupIdData_str, ruleRankParentId_Group)) {

                        IncentRuleRankOut ruleRankOut = new IncentRuleRankOut();

                        ruleRankOut.setDignityId(String.valueOf(ruleRank.getId()));
                        ruleRankOut.setDignityName(ruleRank.getRankName());
                        ruleRankOut.setImage(ruleRank.getRankImgs());
                        ruleRankOut.setState(ruleRank.getIsActive());

                        ruleRankOut.setRankIntro(ruleRank.getRankIntro());

                        ruleRankOut.setSort(ruleRank.getSort());
                        ruleRankOut.setGrantRate(ruleRank.getGrantRate());
                        ruleRankOut.setRankGroupId(ruleRank.getRankGroupId());
                        ruleRankOut.setRankFlag(rankGroup.getRankFlag());
                        ruleRankOut.setRankType(ruleRank.getRankType());

                        if (!rankRuleGroupMap.containsKey("rankFlag")) {
                            rankRuleGroupMap.put("rankFlag", rankGroup.getRankFlag());
                        }

                        rankOutList.add(ruleRankOut);
                    }
                }

                rankRuleGroupMap.put("rankRuleData", rankOutList);
            }
        }
        return rankResultList;
    }


    @Cacheable(cacheNames = {FunGoGameConsts.CACHE_EH_NAME}, key = "'" + CACHEKEY_GETINCENTRULE + "LevelRank'")
    @Override
    public List<IncentRuleRank> getLevelRankList() {
        List<IncentRuleRank> list = iMembertIncentRuleRankDaoService.selectList(new EntityWrapper<IncentRuleRank>().eq("rank_type", FunGoGameConsts.INCENT_RULE_RANK_TYPE_LEVEL));
        return list;
    }


    @Override
    public IncentRuleRank getIncentRuleRank(int rank_idt) {
        IncentRuleRank incentRuleRank = null;
        try {

            String keyPrefix = "incentRuleRankList";
            String keySuffix = "all";

            //from redis cache
            List<IncentRuleRank> incentRuleRankList = (List<IncentRuleRank>) fungoCacheIncentRule.getIndexCache(keyPrefix, keySuffix);

            if (null == incentRuleRankList || incentRuleRankList.isEmpty()) {
                incentRuleRankList = iMembertIncentRuleRankDaoService.selectList(new EntityWrapper<IncentRuleRank>());
            }

            if (null != incentRuleRankList && !incentRuleRankList.isEmpty()) {

                //redis cache
                fungoCacheIncentRule.excIndexCache(true, keyPrefix, keySuffix, incentRuleRankList, FungoCacheIncentRule.REDIS_EXPIRE_30_DAYS);

                for (IncentRuleRank ruleRank : incentRuleRankList) {
                    if (rank_idt == ruleRank.getRankIdt()) {
                        incentRuleRank = ruleRank;
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return incentRuleRank;
    }

    //--------
}
