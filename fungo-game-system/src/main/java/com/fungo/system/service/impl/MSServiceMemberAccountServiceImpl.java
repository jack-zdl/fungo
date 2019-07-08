package com.fungo.system.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.entity.IncentAccountScore;
import com.fungo.system.entity.IncentRanked;
import com.fungo.system.entity.Member;
import com.fungo.system.service.IMemberAccountScoreDaoService;
import com.fungo.system.service.IncentRankedService;
import com.fungo.system.service.MSServiceMemberAccountService;
import com.fungo.system.service.MemberService;
import com.game.common.common.MemberIncentCommonUtils;
import com.game.common.consts.FunGoGameConsts;
import com.game.common.util.PKUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class MSServiceMemberAccountServiceImpl implements MSServiceMemberAccountService {

    private static final Logger logger = LoggerFactory.getLogger(MSServiceMemberAccountServiceImpl.class);


    @Autowired
    private IMemberAccountScoreDaoService accountScoreDaoService;


    //用户业务类
    @Autowired
    private MemberService memberService;

    @Autowired
    private IncentRankedService rankedService;

    //json解析
    private static ObjectMapper mapper = new ObjectMapper();


    @Value("${sys.config.fungo.cluster.index}")
    private String clusterIndex;


    @Transactional
    @Override
    public boolean subtractAccountScore(String mb_id, Integer score) throws IOException {


        if (score <= 0) {
            return false;
        }

        if (StringUtils.isBlank(mb_id)) {
            return false;
        }

        try {
            //更新账户
            IncentAccountScore scoreAccount = accountScoreDaoService.selectOne(new EntityWrapper<IncentAccountScore>().eq("mb_id", mb_id).eq("account_group_id",
                    FunGoGameConsts.INCENT_ACCOUNT_TYPE_SCORE_ID));
            if (scoreAccount == null) {
                return false;
            }

            logger.info("扣减用户积分--用户经验值账户历史数据-scoreAccount:{}", JSON.toJSONString(scoreAccount));

            Long lastCasVersion = scoreAccount.getCasVersion();

            //初始化
            if (null == lastCasVersion) {
                lastCasVersion = 0L;
            }

            scoreAccount.setCasVersion(lastCasVersion + 1);
            scoreAccount.setScoreUsable(scoreAccount.getScoreUsable().subtract(new BigDecimal(score)));
            scoreAccount.setUpdatedAt(new Date());

            Map<String, Object> criteriaMap = new HashMap<String, Object>();
            criteriaMap.put("mb_id", mb_id);
            criteriaMap.put("cas_version", lastCasVersion);


            EntityWrapper<IncentAccountScore> scoreAccountEntityWrapper = new EntityWrapper<IncentAccountScore>();

            scoreAccountEntityWrapper.allEq(criteriaMap);

            logger.info("扣减用户积分--用户经验值账户新数据-scoreAccount:{}", JSON.toJSONString(scoreAccount));

            boolean updateStatus = accountScoreDaoService.update(scoreAccount, scoreAccountEntityWrapper);

            logger.info("扣减用户积分--用户经验值账户更新结果-updateStatus:{}", updateStatus);

            //更新用户等级
            //4.更新用户表的等级和经验值数据
            int accountScore = scoreAccount.getScoreUsable().intValue();
            this.updateMemberLevelAndScore(mb_id, accountScore);

            //5.更新用户成就表的等级数据
            this.updateMemberIncentRanked(mb_id, accountScore);


            return updateStatus;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


    /**
     * 更新用户等级和经验值数据
     * @param mb_id
     * @param accountScore
     */
    private void updateMemberLevelAndScore(String mb_id, int accountScore) {

        if (accountScore > 0) {

            Member oldMember = this.memberService.selectById(mb_id);
            logger.info("--更新用户等级和经验值数据--开始-oldMember:{}", oldMember);

            Member memberUpdate = new Member();
            memberUpdate.setId(mb_id);

            //获取用户最新等级
            int level = MemberIncentCommonUtils.getLevel(accountScore);
            if (level != oldMember.getLevel()) {
                memberUpdate.setLevel(level);
            }
            memberUpdate.setExp(accountScore);
            boolean isUpdate = memberUpdate.updateById();
            logger.info("--更新用户等级和经验值数据--结束--isUpdate:{}---memberUpdate:{}", isUpdate, JSON.toJSONString(memberUpdate));
        }
    }


    /**
     * 更新用户成就表的等级数据
     * @param mb_id
     * @param accountScore 经验值账户可用经验值
     */
    private void updateMemberIncentRanked(String mb_id, int accountScore) throws IOException {

        //获取历史等级成就数据
        IncentRanked ranked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", mb_id).eq("rank_type", FunGoGameConsts.INCENT_RULE_RANK_TYPE_LEVEL));

        int newLevel = MemberIncentCommonUtils.getLevel(accountScore);
        if (newLevel <= 0) {
            return;
        }
        //存在等级数据 update
        if (null != ranked) {

            logger.info("--更新用户成就表的等级数据--获取历史等级成就数据---ranked{}:---newLevel:{}", JSON.toJSONString(ranked), newLevel);

            Long oldLevel = ranked.getCurrentRankId();//记录中的等级

            if (oldLevel.intValue() != newLevel) {

                ranked.setCurrentRankId((long) newLevel);
                ranked.setCurrentRankName("Lv" + newLevel);

                String rankIdtIds = ranked.getRankIdtIds();
                List<HashMap<String, String>> rankList = mapper.readValue(rankIdtIds, ArrayList.class);

                boolean add = true;
                for (HashMap m : rankList) {
                    if (m.get("rankId").equals(newLevel + "")) {
                        add = false;
                    }
                }
                if (add) {
                    HashMap<String, String> newMap = new HashMap<String, String>();
                    newMap.put("rankId", newLevel + "");
                    newMap.put("rankName", "Lv" + newLevel);
                    rankList.add(newMap);
                    ranked.setRankIdtIds(mapper.writeValueAsString(rankList));
                }

                ranked.setRankType(FunGoGameConsts.INCENT_RULE_RANK_TYPE_LEVEL);
                ranked.setUpdatedAt(new Date());

                boolean isUpdate = ranked.updateById();
                logger.info("--更新用户成就表的等级数据--更新等级成就数据---newRanked{}:---isUpdate:{}", JSON.toJSONString(ranked), isUpdate);
            }
            //不存在 add (由于用户注册后，就会初始化等级数据，所以如下代码，只是罕见意外情况的补救)
        } else {

            ranked = new IncentRanked();

            Integer clusterIndex_i = Integer.parseInt(clusterIndex);
            ranked.setId(PKUtil.getInstance(clusterIndex_i).longPK());

            ranked.setCurrentRankId((long) newLevel);
            ranked.setCurrentRankName("Lv" + newLevel);

            List<HashMap<String, String>> rankList = new ArrayList<>();

            HashMap<String, String> newMap = new HashMap<String, String>();
            newMap.put("rankId", newLevel + "");
            newMap.put("rankName", "Lv" + newLevel);
            rankList.add(newMap);

            ranked.setRankIdtIds(mapper.writeValueAsString(rankList));

            ranked.setRankType(FunGoGameConsts.INCENT_RULE_RANK_TYPE_LEVEL);
            ranked.setCreatedAt(new Date());
            ranked.setUpdatedAt(new Date());

            boolean isInsert = ranked.insert();

            logger.info("--更新用户成就表的等级数据--新增等级成就数据---newRanked:{}---isInsert:{}", JSON.toJSONString(ranked), isInsert);
        }

    }

    //----------
}
