package com.fungo.system.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.entity.*;
import com.fungo.system.service.*;
import com.game.common.common.MemberIncentCommonUtils;
import com.game.common.consts.FunGoGameConsts;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import com.game.common.util.StringUtil;
import com.game.common.util.date.DateTools;
import com.game.common.util.PKUtil;
import org.apache.commons.lang.StringUtils;
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
public class MemberIncentEverydayTaskServiceImpl implements IMemberIncentEverydayTaskService {

    private static final Logger logger = LoggerFactory.getLogger(MemberIncentEverydayTaskServiceImpl.class);

    private static final String REDISCACHE_KEY_PREFIX = "IncentEverydayTask";


    //用户业务类
    @Autowired
    private MemberService memberService;

    //用户任务成果Dao层业务类
    @Autowired
    private IncentTaskedService incentTaskService;

    //获取用户已任务完成情况业务层
    @Autowired
    private IMemberIncentTaskedService iMemberIncentTaskedService;

    @Autowired
    private IncentRankedService rankedService;

    @Autowired
    private ScoreLogService scoreLogService;

    @Autowired
    private IMemberAccountScoreDaoService accountScoreDaoService;

    @Autowired
    private IncentAccountCoinDaoService accountCoinService;

    @Autowired
    private IMemberIncentTaskRuleService iMemberIncentTaskRuleService;

    @Autowired
    private IMemberIncentRuleRankService iMemberIncentRuleRankService;


    @Value("${sys.config.fungo.cluster.index}")
    private String clusterIndex;


    //json解析
    private static ObjectMapper mapper = new ObjectMapper();


    @Transactional
    @Override
    public Map<String, Object> exTask(String mb_id, int task_group_flag, int task_type, int task_code_idt, String target_id) throws Exception {
        logger.info("---member-mb_id:{}--执行每日任务--task_type:{}----task_code_idt:{}", mb_id, task_type, task_code_idt);
        Map<String, Object> resMap = new IdentityHashMap<String, Object>();
        resMap.put("success", true);

        //1.先验证用户的等级是否符合任务规则要求
        //获取任务规则数据
        ScoreRule scoreRule = iMemberIncentTaskRuleService.getScoreRule(task_code_idt);
        //获取用户历史等级成就数据
        IncentRanked ranked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", mb_id).eq("rank_type", FunGoGameConsts.INCENT_RULE_RANK_TYPE_LEVEL));
        boolean isHasPerm = isHasPerm(scoreRule, ranked);
        logger.info("---member-mb_id:{}--执行每日任务--task_type:{}----task_code_idt:{}--先验证用户的等级是否符合任务规则要求-isHasPerm:{}", mb_id, task_type, task_code_idt, isHasPerm);
        if (!isHasPerm) {
            resMap.put("success", false);
            resMap.put("msg", "用户等级不符合任务要求");
            return resMap;
        }

         /*
          11 任务 获取经验值
          23 任务 获取fungo币
          分享文章 | 分享游戏任务要给出收益提示
         */
        switch (task_type) {
            case 11:
                int expTaskStatus = this.exExpTask(mb_id, task_group_flag, task_type, task_code_idt, scoreRule, ranked);
                logger.info("---member-mb_id:{}--执行每日任务---经验值任务-task_type:{}----task_code_idt:{}--执行结果expTaskStatus:{}", mb_id, task_type, task_code_idt, expTaskStatus);
                if (-1 == expTaskStatus) {
                    resMap.put("success", false);
                } else {
                    String msg = isHasTips(scoreRule);
                    resMap.put("msg", msg);
                }
                break;
            case 23:
                int coinTaskStatus = this.exCoinTask(mb_id, task_group_flag, task_type, task_code_idt);
                logger.info("---member-mb_id:{}--执行每日任务---fungo币任务-task_type:{}----task_code_idt:{}--执行结果coinTaskStatus:{}", mb_id, task_type, task_code_idt, coinTaskStatus);
                if (-1 == coinTaskStatus) {
                    resMap.put("success", false);
                } else {
                    String msg = isHasTips(scoreRule);
                    resMap.put("msg", msg);
                }
                break;
        }

        logger.info("---member-mb_id:{}--执行每日任务--task_type:{}----task_code_idt:{}---执行结果:{}", mb_id, task_type, task_code_idt, JSON.toJSONString(resMap));

        return resMap;
    }


    @Transactional
    @Override
    public Map<String, Object> exTask(String mb_id, int task_group_flag, int task_type, int task_code_idt) throws Exception {

        logger.info("---member-mb_id:{}--执行每日任务--task_type:{}----task_code_idt:{}", mb_id, task_type, task_code_idt);
        Map<String, Object> resMap = new IdentityHashMap<String, Object>();
        resMap.put("success", true);

        //1.先验证用户的等级是否符合任务规则要求
        //获取任务规则数据
        ScoreRule scoreRule = iMemberIncentTaskRuleService.getScoreRule(task_code_idt);
        //获取用户历史等级成就数据
        IncentRanked ranked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", mb_id).eq("rank_type", FunGoGameConsts.INCENT_RULE_RANK_TYPE_LEVEL));
        boolean isHasPerm = isHasPerm(scoreRule, ranked);
        logger.info("---member-mb_id:{}--执行每日任务--task_type:{}----task_code_idt:{}--先验证用户的等级是否符合任务规则要求-isHasPerm:{}", mb_id, task_type, task_code_idt, isHasPerm);
        if (!isHasPerm) {
            resMap.put("success", false);
            resMap.put("msg", "用户等级不符合任务要求");
            return resMap;
        }

         /*
          11 任务 获取经验值
          23 任务 获取fungo币
          分享文章 | 分享游戏任务要给出收益提示
         */
        switch (task_type) {
            case 11:
                int expTaskStatus = this.exExpTask(mb_id, task_group_flag, task_type, task_code_idt, scoreRule, ranked);
                logger.info("---member-mb_id:{}--执行每日任务---经验值任务-task_type:{}----task_code_idt:{}--执行结果expTaskStatus:{}", mb_id, task_type, task_code_idt, expTaskStatus);
                if (-1 == expTaskStatus) {
                    resMap.put("success", false);
                } else {
                    String msg = isHasTips(scoreRule);
                    resMap.put("msg", msg);
                }
                break;
            case 23:
                int coinTaskStatus = this.exCoinTask(mb_id, task_group_flag, task_type, task_code_idt);
                logger.info("---member-mb_id:{}--执行每日任务---fungo币任务-task_type:{}----task_code_idt:{}--执行结果coinTaskStatus:{}", mb_id, task_type, task_code_idt, coinTaskStatus);
                if (-1 == coinTaskStatus) {
                    resMap.put("success", false);
                } else {
                    String msg = isHasTips(scoreRule);
                    resMap.put("msg", msg);
                }
                break;
        }

        logger.info("---member-mb_id:{}--执行每日任务--task_type:{}----task_code_idt:{}---执行结果:{}", mb_id, task_type, task_code_idt, JSON.toJSONString(resMap));

        return resMap;
    }


    /**
     * 给出有收益提示任务的提示数据
     * @param scoreRule
     * @return
     */
    private String isHasTips(ScoreRule scoreRule) {
        String tips = "";

        //分享1篇好文章
        //coin
        if (scoreRule.getCodeIdt().intValue() == FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_SHARE_1_ARTICLE_COIN.code()) {

            tips = FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_SHARE_1_ARTICLE_COIN.statusDesc() + FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_SHARE_1_ARTICLE_COIN.message();
            tips = tips.replace("A", String.valueOf(scoreRule.getScore()));

            //exp
        } else if (scoreRule.getCodeIdt().intValue() == FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_SHARE_1_ARTICLE_EXP.code()) {

            tips = FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_SHARE_1_ARTICLE_EXP.statusDesc() + FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_SHARE_1_ARTICLE_EXP.message();
            tips = tips.replace("B", String.valueOf(scoreRule.getScore()));

            //分享1款好游
            // coin
        } else if (scoreRule.getCodeIdt().intValue() == FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_SHARE_1_GAME_COIN.code()) {

            tips = FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_SHARE_1_GAME_COIN.statusDesc() + FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_SHARE_1_GAME_COIN.message();
            tips = tips.replace("A", String.valueOf(scoreRule.getScore()));

            //exp
        } else if (scoreRule.getCodeIdt().intValue() == FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_SHARE_1_GAME_EXP.code()) {

            tips = FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_SHARE_1_GAME_EXP.statusDesc() + FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_SHARE_1_GAME_EXP.message();
            tips = tips.replace("B", String.valueOf(scoreRule.getScore()));

            //第1次发文章
            // coin
        } else if (scoreRule.getCodeIdt().intValue() == FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_ARTICLE_COIN.code()) {

            tips = FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_ARTICLE_COIN.statusDesc() + FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_ARTICLE_COIN.message();
            tips = tips.replace("A", String.valueOf(scoreRule.getScore()));

            //exp
        } else if (scoreRule.getCodeIdt().intValue() == FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_ARTICLE_EXP.code()) {

            tips = FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_ARTICLE_EXP.statusDesc() + FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_ARTICLE_EXP.message();
            tips = tips.replace("B", String.valueOf(scoreRule.getScore()));

            //第1次发心情
            // coin
        } else if (scoreRule.getCodeIdt().intValue() == FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_MOOD_COIN.code()) {

            tips = FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_MOOD_COIN.statusDesc() + FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_MOOD_COIN.message();
            tips = tips.replace("A", String.valueOf(scoreRule.getScore()));

            //exp
        } else if (scoreRule.getCodeIdt().intValue() == FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_MOOD_EXP.code()) {

            tips = FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_MOOD_EXP.statusDesc() + FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_MOOD_EXP.message();
            tips = tips.replace("B", String.valueOf(scoreRule.getScore()));

            //第1次发评论
            //coin
        } else if (scoreRule.getCodeIdt().intValue() == FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_COIN.code()) {

            tips = FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_COIN.statusDesc() + FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_COIN.message();
            tips = tips.replace("A", String.valueOf(scoreRule.getScore()));

            //exp
        } else if (scoreRule.getCodeIdt().intValue() == FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_EXP.code()) {
            tips = FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_EXP.statusDesc() + FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_EXP.message();
            tips = tips.replace("B", String.valueOf(scoreRule.getScore()));
        }

        return tips;
    }


    /**
     * 执行经验值任务
     *   因为涉及事务的问题，多个业务表的DB层,  c u d 在业务层方法内部执行
     * @param mb_id
     * @param task_group_flag
     * @param task_type
     * @param task_code_idt
     * @return 1 首次成功   -1 任务失败  2 任务重复完成
     */
    private int exExpTask(String mb_id, int task_group_flag, int task_type, int task_code_idt, ScoreRule scoreRule, IncentRanked ranked) throws Exception {

        //1.获取任务规则数据
        if (null == scoreRule) {
            logger.info("---member-mb_id:{}--执行每日任务---经验值任务---scoreRule:{}", scoreRule);
            return -1;
        }

        //2.验证是否完成过这个任务
        boolean isAddEquit = this.isAddEquitWithSingleAction(mb_id, task_group_flag, scoreRule);
        logger.info("---member-mb_id:{}--执行每日任务---经验值任务--task_code_idt:{}--验证是否完成过这个任务-isAddEquitWithSingleAction:{}",
                mb_id, task_type, task_code_idt, isAddEquit);
        if (!isAddEquit) {
            return -1;
        }


        //没有执行过
        //3.更新用户经验值账户
        int accountScore = updateAccountScore(mb_id, scoreRule);


        //4.更新用户表的等级和经验值数据
        this.updateMemberLevelAndScore(mb_id, accountScore);

        //5.更新用户任务成果表
        this.updateMemberIncentTasked(mb_id, task_type, scoreRule);

        //6.更新用户成就表的等级数据
        this.updateMemberIncentRanked(mb_id, accountScore, ranked);


        //7.添加任务执行日志
        this.addTaskedLog(mb_id, scoreRule);

        return 1;
    }


    /**
     * 执行Fungo币任务
     * @param mb_id
     * @param task_group_flag
     * @param task_type
     * @param task_code_idt
     * @return 1 首次成功   -1 任务失败  2 任务重复完成
     */
    private int exCoinTask(String mb_id, int task_group_flag, int task_type, int task_code_idt) throws Exception {


        //1.获取任务规则数据
        ScoreRule scoreRule = iMemberIncentTaskRuleService.getScoreRule(task_code_idt);
        if (null == scoreRule) {
            return -1;
        }

        //2.验证是否完成过这个任务

        boolean isAddEquit = this.isAddEquitWithSingleAction(mb_id, task_group_flag, scoreRule);
        logger.info("---member-mb_id:{}--执行每日任务---Fungo币任务--task_code_idt:{}--验证是否完成过这个任务-isAddEquitWithSingleAction:{}",
                mb_id, task_type, task_code_idt, isAddEquit);
        if (!isAddEquit) {
            return -1;
        }


        //没有执行过
        //3.更新用户fungo币账户
        this.updateAccountCoin(mb_id, scoreRule);

        //4.更新用户任务成果表
        this.updateMemberIncentTasked(mb_id, task_type, scoreRule);

        //5.添加任务执行日志
        this.addTaskedLog(mb_id, scoreRule);

        return 1;
    }


    /**
     *  修改用户fungo币账户
     * @param mb_id
     * @param scoreRule
     */
    private int updateAccountCoin(String mb_id, ScoreRule scoreRule) throws IOException {
        logger.info("执行新手任务---经验值任务--开始修改用户fungo币账户-scoreRule:{}", JSON.toJSONString(scoreRule));
        if(StringUtil.isNull(mb_id)){
            logger.error("修改fungo币失败 用户id为空");
            return -1;
        }
        Integer coinCount = scoreRule.getScore();
        //更新账户.
        // eq("account_group_id", FunGoGameConsts.INCENT_ACCOUNT_TYPE_COIN_ID)
        IncentAccountCoin coinAccount = accountCoinService.selectOne(new EntityWrapper<IncentAccountCoin>().eq("mb_id", mb_id));
        if (coinAccount == null) {
            Member member = memberService.selectById(mb_id);
            coinAccount = accountScoreDaoService.createAccountCoin(mb_id);
        }

        logger.info("执行新手任务---经验值任务--用户fungo币账户历史数据-scoreAccount:{}", JSON.toJSONString(coinAccount));

        Long lastCasVersion = coinAccount.getCasVersion();
        //初始化
        if (null == lastCasVersion) {
            lastCasVersion = 0L;
        }

        coinAccount.setCasVersion(lastCasVersion + 1);
        coinAccount.setCoinUsable(coinAccount.getCoinUsable().add(new BigDecimal(coinCount)));
        coinAccount.setUpdatedAt(new Date());

        Map<String, Object> criteriaMap = new HashMap<String, Object>();
        criteriaMap.put("mb_id", mb_id);
        criteriaMap.put("cas_version", lastCasVersion);
        EntityWrapper<IncentAccountCoin> coinAccountEntityWrapper = new EntityWrapper<IncentAccountCoin>();

        coinAccountEntityWrapper.allEq(criteriaMap);

        logger.info("执行新手任务---经验值任务--用户fungo币账户新数据-scoreAccount:{}", JSON.toJSONString(coinAccount));
        boolean updateStatus = accountCoinService.update(coinAccount, coinAccountEntityWrapper);
        logger.info("执行新手任务---经验值任务--用户经验值账户更新结果-updateStatus:{}", updateStatus);
        if (updateStatus) {
            return 1;
        }
        return -1;
    }


    /**
     * 修改用户经验值账户
     * @param mb_id
     * @param scoreRule
     * @return 返回用户经验值账户最新可用余额，出现异常返回 -1
     */
    private int updateAccountScore(String mb_id, ScoreRule scoreRule) throws IOException {
        logger.info("执行每日任务---经验值任务--开始修改用户经验值账户-scoreRule:{}", JSON.toJSONString(scoreRule));
        if(StringUtil.isNull(mb_id)){
            logger.error("修改经验值失败 用户id为空");
            return -1;
        }
        //加积分
        Integer score = scoreRule.getScore();
        //更新账户
        IncentAccountScore scoreAccount = accountScoreDaoService.selectOne(new EntityWrapper<IncentAccountScore>().eq("mb_id", mb_id).eq("account_group_id",
                FunGoGameConsts.INCENT_ACCOUNT_TYPE_SCORE_ID));
        if (scoreAccount == null) {
            Member member = memberService.selectById(mb_id);
            scoreAccount = accountScoreDaoService.createAccountScore(member, FunGoGameConsts.INCENT_ACCOUNT_TYPE_SCORE);
        }

        logger.info("执行每日任务---经验值任务--用户经验值账户历史数据-scoreAccount:{}", JSON.toJSONString(scoreAccount));

        Long lastCasVersion = scoreAccount.getCasVersion();
        //初始化
        if (null == lastCasVersion) {
            lastCasVersion = 0L;
        }
        scoreAccount.setCasVersion(lastCasVersion + 1);
        scoreAccount.setScoreUsable(scoreAccount.getScoreUsable().add(new BigDecimal(score)));
        scoreAccount.setUpdatedAt(new Date());

        Map<String, Object> criteriaMap = new HashMap<String, Object>();
        criteriaMap.put("mb_id", mb_id);
        criteriaMap.put("cas_version", lastCasVersion);
        EntityWrapper<IncentAccountScore> scoreAccountEntityWrapper = new EntityWrapper<IncentAccountScore>();

        scoreAccountEntityWrapper.allEq(criteriaMap);

        logger.info("执行每日任务---经验值任务--用户经验值账户新数据-scoreAccount:{}", JSON.toJSONString(scoreAccount));

        boolean updateStatus = accountScoreDaoService.update(scoreAccount, scoreAccountEntityWrapper);

        logger.info("执行每日任务---经验值任务--用户经验值账户更新结果-updateStatus:{}", updateStatus);
        if (updateStatus) {
            return scoreAccount.getScoreUsable().intValue();
        }
        return -1;
    }


    /**
     * 更新用户任务成果表
     * @param mb_id
     * @param task_type
     * @param scoreRule
     * @throws IOException
     */
    private void updateMemberIncentTasked(String mb_id, int task_type, ScoreRule scoreRule) throws IOException {

        //获取用户当前任务的历史记录
        IncentTasked incentTaskedMb = getIncentTaskedWithMember(mb_id, task_type);

        logger.info("执行每日任务---经验值任务--开始更新用户任务成果表-获取用户当前任务的历史记录-incentTaskedMb:{}", incentTaskedMb);
        //1.任务存在历史记录
        if (null != incentTaskedMb) {
            exUpdateMbTaskedData(task_type, scoreRule, incentTaskedMb);
            //2.不存在该类型任务的历史记录
        } else {
            exAddMbTaskedData(mb_id, scoreRule);
        }
    }


    /**
     * 更新用户成就表的等级数据
     * @param mb_id
     * @param accountScore 经验值账户可用经验值
     */
    private void updateMemberIncentRanked(String mb_id, int accountScore, IncentRanked ranked) throws IOException {

        //获取历史等级成就数据

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

        //执行等级荣誉成就日志记录，获取等级荣誉规则数据
        IncentRuleRank rankRule = iMemberIncentRuleRankService.getIncentRuleRank(newLevel);
        addRankLog(mb_id, rankRule);

    }


    /**
     * 添加成就日志记录
     * @param userId
     * @param rankRule
     */
    private void addRankLog(String userId, IncentRuleRank rankRule) {
        IncentRankedLog rankLog = new IncentRankedLog();

        Integer clusterIndex_i = Integer.parseInt(clusterIndex);

        rankLog.setId(PKUtil.getInstance(clusterIndex_i).longPK());

        rankLog.setMbId(userId);
        rankLog.setGainTime(new Date());

        rankLog.setProduceType(1);

        rankLog.setRankCode(rankRule.getRankCode());

        rankLog.setRankGroupId(Long.parseLong(rankRule.getRankGroupId()));

        rankLog.setRankIdt(rankRule.getRankIdt());

        rankLog.setRankRuleId(rankRule.getId());

        rankLog.setUpdatedAt(new Date());
        rankLog.setCreatedAt(new Date());


        boolean isInsert = rankLog.insert();

        logger.info("--更新用户成就表的等级数据--等级成就数据日志---IncentRankedLog:{}---isInsert:{}", JSON.toJSONString(rankLog), isInsert);
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
     * 添加任务执行成功日志
     * @param mb_id
     * @param scoreRule
     */
    private void addTaskedLog(String mb_id, ScoreRule scoreRule) {

        ScoreLog newLog = new ScoreLog();

        newLog.setMemberId(mb_id);

        newLog.setType(scoreRule.getCode());

        newLog.setTaskType(scoreRule.getTaskType());
        newLog.setCodeIdt(scoreRule.getCodeIdt());

        newLog.setScore(scoreRule.getScore());
        newLog.setGroupId(scoreRule.getGroupId());

        newLog.setRuleId(scoreRule.getId());
        newLog.setRuleName(scoreRule.getName());

        newLog.setProduceType(1);
        newLog.setCreatedAt(new Date());
        newLog.setUpdatedAt(new Date());

        boolean isInsert = newLog.insert();
        logger.info("---添加任务执行成功日志---ScoreLog:{}---isInsert:{}", JSON.toJSONString(newLog), isInsert);
    }


    /**
     * 更新用户任务成果表:
     *                  用户无该任务历史记录执行添加
     * @param mb_id
     * @param scoreRule
     * @throws JsonProcessingException
     */
    private void exAddMbTaskedData(String mb_id, ScoreRule scoreRule) throws JsonProcessingException {

        logger.info("执行每日任务---经验值任务--开始更新用户任务成果表---用户无该任务历史记录执行添加...");

        IncentTasked incentTaskedMb = new IncentTasked();

        incentTaskedMb.setCurrentTaskId(scoreRule.getId());
        incentTaskedMb.setCurrentTaskName(scoreRule.getName());
        incentTaskedMb.setTaskType(scoreRule.getTaskType());
        incentTaskedMb.setMbId(mb_id);

        ArrayList<Map<String, Object>> taskList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("1", scoreRule.getId());
        map.put("2", scoreRule.getName());
        map.put("3", scoreRule.getScore());
        map.put("4", scoreRule.getTaskType());
        map.put("5", 1);
        map.put("6", DateTools.fmtDate(new Date()));
        map.put("7", scoreRule.getIncomieFregType());

        taskList.add(map);

        incentTaskedMb.setTaskIdtIds(mapper.writeValueAsString(taskList));
        incentTaskedMb.setCreatedAt(new Date());
        incentTaskedMb.setUpdatedAt(new Date());

        Integer clusterIndex_i = Integer.parseInt(clusterIndex);
        incentTaskedMb.setId(PKUtil.getInstance(clusterIndex_i).longPK());

        boolean isInsert = incentTaskedMb.insert();

        logger.info("执行每日任务---经验值任务--开始更新用户任务成果表---用户无该任务历史记录执行添加--isInsert:{}----incentTaskedMb:{}",
                isInsert, JSON.toJSONString(incentTaskedMb));
    }

    /**
     * 执行更新用户任务记录表
     * @param task_type
     * @param scoreRule
     * @param incentTaskedMb
     * @throws IOException
     */
    private void exUpdateMbTaskedData(int task_type, ScoreRule scoreRule, IncentTasked incentTaskedMb) throws IOException {
        //json格式:[   {1:taskid,2:taskname,3:score,4:type,5:count,6:date,7:incomie_freg_type}   ]
        incentTaskedMb.setCurrentTaskId(scoreRule.getId());
        incentTaskedMb.setCurrentTaskName(scoreRule.getName());
        incentTaskedMb.setTaskType(task_type);

        String taskIdtIds = incentTaskedMb.getTaskIdtIds();

        //获取历史任务记录
        ArrayList<Map<String, Object>> taskIdtIdsList = mapper.readValue(taskIdtIds, ArrayList.class);

        int taskCount = 1;
        boolean isContain = false;
        boolean isAdd = true;
        //遍历历史任务记录中，是否存在当前任务
        for (Map<String, Object> map : taskIdtIdsList) {
            String oldTaskId = (String) map.get("1");
            //验证当前任务是否存在记录
            if (StringUtils.equalsIgnoreCase(oldTaskId, scoreRule.getId())) {
                //有相同的任务则更新最后完成的时间
                map.put("6", DateTools.fmtDate(new Date()));
                isAdd = false;
                isContain = true;
                break;
            }
        }

        logger.info("执行每日任务---经验值任务--开始更新用户任务成果表-----验证当前任务是否存在记录--isContain:{}", isContain);

        //若记录不存在 或者 任务最后一次完成日期 与 当前日期不是同一天
        if (isAdd) {

            //把当前任务添加到任务记录中
            Map<String, Object> currenttaskedMap = new HashMap<String, Object>();
            currenttaskedMap.put("1", scoreRule.getId());
            currenttaskedMap.put("2", scoreRule.getName());
            currenttaskedMap.put("3", scoreRule.getScore());
            currenttaskedMap.put("4", scoreRule.getTaskType());
            currenttaskedMap.put("5", taskCount);

            currenttaskedMap.put("6", DateTools.fmtDate(new Date()));
            currenttaskedMap.put("7", scoreRule.getIncomieFregType());
            taskIdtIdsList.add(currenttaskedMap);

            incentTaskedMb.setTaskIdtIds(mapper.writeValueAsString(taskIdtIdsList));
            incentTaskedMb.setUpdatedAt(new Date());
            //执行更新
            boolean isUpdate = incentTaskedMb.updateById();
            logger.info("执行每日任务---经验值任务--开始更新用户任务成果表--执行更新-isUpdate:{}----incentTaskedMb:{}", isUpdate, JSON.toJSONString(incentTaskedMb));

        }
        if (isContain) {
            incentTaskedMb.setTaskIdtIds(mapper.writeValueAsString(taskIdtIdsList));
            incentTaskedMb.setUpdatedAt(new Date());
            //执行更新
            boolean isUpdate = incentTaskedMb.updateById();
            logger.info("执行每日任务---经验值任务--开始更新用户任务成果表--执行更新-isUpdate:{}----incentTaskedMb:{}", isUpdate, JSON.toJSONString(incentTaskedMb));
        }
    }


    /**
     * 获取用户历史任务记录
     * @param mb_id 用户ID
     * @param task_type 任务类型
     */
    private IncentTasked getIncentTaskedWithMember(String mb_id, int task_type) {

        IncentTasked incentTaskedMb = null;
        EntityWrapper<IncentTasked> taskedEntityWrapper = new EntityWrapper<IncentTasked>();
        taskedEntityWrapper.eq("mb_id", mb_id);
        taskedEntityWrapper.eq("task_type", task_type);
        taskedEntityWrapper.orderBy("updated_at", false);


        List<IncentTasked> incentTaskedList = incentTaskService.selectList(taskedEntityWrapper);
        if (null != incentTaskedList && !incentTaskedList.isEmpty()) {
            incentTaskedMb = incentTaskedList.get(0);

            for (int i = 1; i < incentTaskedList.size(); i++) {
                IncentTasked incentTaskedDel = incentTaskedList.get(i);
                if (null != incentTaskedDel) {
                    if (incentTaskedMb.getId().longValue() != incentTaskedDel.getId().longValue()) {
                        //incentTaskedDel.deleteById();
                    }
                }
            }
        }
        return incentTaskedMb;
    }


    /**
     *  验证用户执行当前任务是否产生收益
     * @param mb_id
     * @param task_group_flag
     * @param scoreRule
     * @return
     * @throws Exception
     */
    private boolean isAddEquitWithSingleAction(String mb_id, int task_group_flag, ScoreRule scoreRule) throws Exception {

        boolean isAddEq = true;

        int task_type = scoreRule.getTaskType();
        int task_code_idt = scoreRule.getCodeIdt();
        String task_id = scoreRule.getId();


        //用户任务成果表获取
        IncentTaskedOut tasked = iMemberIncentTaskedService.getTasked(mb_id, task_type, task_id);

        if (null == tasked) {

            //从任务日志库查询是否有记录
            List<ScoreLog> scoreLogList = scoreLogService.getScoreLogWithMbAndTask(mb_id, task_type, task_code_idt);

            if (null != scoreLogList && !scoreLogList.isEmpty()) {

                ScoreLog scoreLog = scoreLogList.get(0);


                //获取最后一次完成日期与当前日期是否同一天
                String finishDate = DateTools.fmtDate(scoreLog.getCreatedAt());
                String currentDateStr = DateTools.fmtSimpleDateToString(new Date());
                long interval = DateTools.getDaySub(finishDate, currentDateStr);
                if (0 == interval) {
                    isAddEq = false;
                }
            }

        } else {

            //获取最后一次完成日期与当前日期是否同一天
            String finishDate = tasked.getDoneDate();
            String currentDateStr = DateTools.fmtSimpleDateToString(new Date());
            long interval = DateTools.getDaySub(finishDate, currentDateStr);
            if (0 == interval) {
                isAddEq = false;
            }


        }

        return isAddEq;
    }


    /**
     * 验证用户的级别是否符合任务授权规则
     * @param scoreRule
     * @param ranked
     * @return true符合，fasle不符合
     */
    private boolean isHasPerm(ScoreRule scoreRule, IncentRanked ranked) {
        if (null == ranked) {
            return true;
        }
        int authLevel = scoreRule.getAuthLevel();
        if (authLevel > 0) {
            Long currentRankId = ranked.getCurrentRankId();
            if (currentRankId.intValue() >= authLevel) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }


    //-----------
}

