package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.entity.*;
import com.fungo.system.service.*;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.consts.MemberActionTypeConsts;
import com.game.common.consts.MemberIncentSignInConsts;
import com.game.common.consts.MemberIncentTaskConsts;
import com.game.common.dto.ResultDto;
import com.game.common.dto.user.MemberOutBean;
import com.game.common.repo.cache.facade.FungoCacheMember;
import com.game.common.repo.cache.facade.FungoCacheTask;
import com.game.common.util.date.DateTools;
import com.game.common.util.exception.BusinessException;
import com.game.common.util.PKUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;


@Service
public class MemberIncentSignInTaskServiceImpl implements IMemberIncentSignInTaskService {


    private static final Logger logger = LoggerFactory.getLogger(MemberIncentSignInTaskServiceImpl.class);


    @Autowired
    private MemberService memberService;

    @Autowired
    private ScoreGroupService scoreGroupService;

    @Autowired
    private ScoreRuleService scoreRuleService;

    @Autowired
    private IncentTaskedService incentTaskedService;

    @Autowired
    private ScoreLogService scoreLogService;

    @Autowired
    private IMemberAccountScoreDaoService memberAccountDaoService;

    @Autowired
    private IncentAccountCoinDaoService accountCoinService;

    @Autowired
    private FungoCacheMember fungoCacheMember;

    @Autowired
    private FungoCacheTask fungoCacheTask;

    @Value("${sys.config.fungo.cluster.index}")
    private String clusterIndex;


    @Override
    public ResultDto<Map<String, Object>> getMemberSignInInfo(String mb_id) throws BusinessException {

        Map<String, Object> data = new IdentityHashMap<>();
        boolean isChecked = false;
        int days = 0;
        try {
            //获取用户最后的签到数据
            IncentTasked incentTasked = getMbLastSignInData(mb_id);

            if (null != incentTasked) {
                String taskIdtIds = incentTasked.getTaskIdtIds();
                if (StringUtils.isNotBlank(taskIdtIds)) {
                    Map<String, Object> signMap = JSON.parseObject(taskIdtIds);
                    Long interval = -1L;
                    //最后一次签到时间
                    String lastSignInDateStr = (String) signMap.get("6");
                    if (StringUtils.isNotBlank(lastSignInDateStr)) {
                        String currentDateStr = DateTools.fmtSimpleDateToString(new Date());
                        interval = DateTools.getDaySub(lastSignInDateStr, currentDateStr);
                    }

                    //最后签到天数 5:count
                    Integer signInCountDays = (Integer) signMap.get("5");
                    int signInCountDays_i = 0;
                    if (null != signInCountDays) {
                        signInCountDays_i = signInCountDays.intValue();
                    }

                    // 今日已经签到过
                    if (0 == interval) {
                        isChecked = true;
                        days = signInCountDays_i % 30;
                     //连续签到
                    } else if (1 == interval) {
                        days = signInCountDays_i % 30;
                    }

                    //计算未来 2 / 7 / 14 / 21 / 28 天签到可以获取的fungo币数量
                    int allCanSignDay = 0;
                    int canObtainCoin = 0;
                    if (days < 2) {
                        allCanSignDay = (2 - days);
                        canObtainCoin = 30;//isCanObtainCoin(days + 1, allCanSignDay);
                        data.put("tip", "再签" + allCanSignDay + "天可得到" + canObtainCoin + "Fun的币奖励哦!");
                    } else if (days < 7) {
                        allCanSignDay = (7 - days);
                       /* if (2 <= days && days < 6) {
                            canObtainCoin = isCanObtainCoin(days + 1, allCanSignDay - 1);
                            canObtainCoin += isCanObtainCoin(7, 1);
                        } else if (6 <= days && days < 7) {
                            canObtainCoin += isCanObtainCoin(7, 1);
                        }*/
                        canObtainCoin = 50;
                        data.put("tip", "再签" + allCanSignDay + "天可得到" + canObtainCoin + "Fun的币奖励哦!");
                    } else if (days < 14) {
                        allCanSignDay = (14 - days);
                       /* if (7 <= days && days < 13) {
                            canObtainCoin = isCanObtainCoin(days + 1, allCanSignDay - 1);
                            canObtainCoin += isCanObtainCoin(14, 1);
                        } else if (13 <= days && days < 14) {
                            canObtainCoin += isCanObtainCoin(14, 1);
                        }*/
                        canObtainCoin = 50;
                        data.put("tip", "再签" + allCanSignDay + "天可得到" + canObtainCoin + "Fun的币奖励哦!");
                    } else if (days < 21) {
                        allCanSignDay = (21 - days);
                      /*  if (14 <= days && days < 20) {
                            canObtainCoin = isCanObtainCoin(days + 1, allCanSignDay - 1);
                            canObtainCoin += isCanObtainCoin(21, 1);
                        } else if (20 <= days && days < 21) {
                            canObtainCoin += isCanObtainCoin(21, 1);
                        }*/
                        canObtainCoin = 50;
                        data.put("tip", "再签" + allCanSignDay + "天可得到" + canObtainCoin + "Fun的币奖励哦!");
                    } else if (days < 28) {
                        allCanSignDay = (28 - days);
                      /*  if (21 <= days && days < 27) {
                            canObtainCoin = isCanObtainCoin(days + 1, allCanSignDay - 1);
                            canObtainCoin += isCanObtainCoin(28, 1);
                        } else if (27 <= days && days < 28) {
                            canObtainCoin += isCanObtainCoin(28, 1);
                        }*/
                        canObtainCoin = 50;
                        data.put("tip", "再签" + allCanSignDay + "天可得到" + canObtainCoin + "Fun的币奖励哦!");
                    } else if (days < 30) {
                        allCanSignDay = (30 - days);
                      /*  if (days == 28) {
                            canObtainCoin = isCanObtainCoin(29, 1);
                            canObtainCoin += isCanObtainCoin(30, 1);
                        } else if (days == 29) {
                            canObtainCoin += isCanObtainCoin(30, 1);
                        }*/
                        canObtainCoin = 150;
                        data.put("tip", "再签" + allCanSignDay + "天可得到" + canObtainCoin + "Fun的币奖励哦!");
                    }
                }
            } else {
                //计算未来 2 / 7 / 14 / 21 / 28 天签到可以获取的fungo币数量
                int allCanSignDay = 0;
                int canObtainCoin = 0;
                if (days < 2) {
                    allCanSignDay = (2 - days);
                    canObtainCoin = 30;//isCanObtainCoin(days + 1, allCanSignDay);
                    data.put("tip", "再签" + allCanSignDay + "天可得到" + canObtainCoin + "Fun的币奖励哦!");
                }
            }

        } catch (Exception ex) {
            logger.error("用户获取签到信息出现异常", ex);
            throw new BusinessException("-1", "服务器非常繁忙，请耐心等一下");
        }
        data.put("is_check", isChecked);
        data.put("continue_days", days);
        return ResultDto.success(data);

    }


    /**
     * 根据剩余签到天数，可到fungo数量
     * @param signedDays 已签到天数
     * @param canSignDays  剩余可签到天数
     * @return
     */
    private int isCanObtainCoin(int signedDays, int canSignDays) {
        if (signedDays <= 0 || canSignDays <= 0) {
            return 0;
        }
        //signedDays + 1是表示下一天
        ScoreRule scoreRule = getscoreRule(signedDays);

        return scoreRule.getScore() * canSignDays;
    }


    @Transactional
    @Override
    public ResultDto exSignIn(String mb_id) throws BusinessException {
        try {

            //from redis cache 获取用户信息
            Member member = null;
            MemberOutBean memberOutBean = (MemberOutBean) fungoCacheMember.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + mb_id, "");
            if (null == memberOutBean) {
                member = memberService.selectById(mb_id);
                if (null == member) {
                    return ResultDto.error("126", "用户不存在");
                }
            } else {
                member = new Member();
                member.setId(memberOutBean.getMb_id());
                member.setMobilePhoneNum(memberOutBean.getMobilePhoneNumber());
                member.setUserName(memberOutBean.getUsername());
            }

            //获取用户最后的签到数据
            //若没有记录，则表示是第一次签到
            IncentTasked incentTasked = getMbLastSignInData(mb_id);
            if (null == incentTasked) {
                //执行签到： 第一次
                this.exFirstSignIn(member);
            } else {
                //执行签到：连续签到
                return this.exSuccessionSignIn(member, incentTasked);
            }
        } catch (Exception ex) {
            logger.error("用户执行签到出现异常", ex);
            throw new BusinessException("-1", "服务器非常繁忙，请耐心等一下");
        }
        return ResultDto.success("签到成功");
    }


    /**
     * 用户第一次签到
     * @param member
     */
    private void exFirstSignIn(Member member) {

        //在t_incent_tasked 表中添加签到成果记录
        // task_idt_ids字段 会员完成的任务Ids，json格式:
        // [{1:taskid,2:taskname,3:score,4:type,5:count,6:date,7:incomie_freg_type} ]，
        // (任务id,任务名称，获取的分值|虚拟币数，任务类型，完成数量,任务完成时间,任务频率 )

        //获取签到规则组和规则数据
        ScoreGroup signInTaskGroup = this.getSignInTaskGroup();

        ScoreRule scoreRuleCurrent = this.getscoreRule(1);

        String taskGroupId = "-1";
        if (null != signInTaskGroup) {
            taskGroupId = signInTaskGroup.getId();
        }

        Integer score = 0;
        if (null != scoreRuleCurrent) {
            score = scoreRuleCurrent.getScore();
        }

        //1.计算本次签到能获取的Fungo币
        if (score > 0) {
            this.addCoinWithMbSignIn(member, score);
        }

        //2.记录用户成功签到数据
        this.exAddIncentTasked(member, scoreRuleCurrent, 1);

        //3.记录用户签到日志
        if (null != scoreRuleCurrent) {
            this.addLog(member.getId(), "1", score, taskGroupId, scoreRuleCurrent.getTaskType(),
                    scoreRuleCurrent.getId(), scoreRuleCurrent.getName(), scoreRuleCurrent.getCodeIdt());
        }
    }


    /**
     * 连续签到
     * @param member
     * @param incentTasked
     */
    private ResultDto exSuccessionSignIn(Member member, IncentTasked incentTasked) throws Exception {

        logger.info("用户id:{}---连续签到计算前-历史签到IncentTasked:{}", member.getId(), JSON.toJSONString(incentTasked));

        //获取签到记录
        String taskIdtIds = incentTasked.getTaskIdtIds();

        if (StringUtils.isBlank(taskIdtIds)) {
            logger.info("------------------------历史签到记录不存在,执行按第一天签到--------------------------");
            //执行按第一天签到
            this.exSuccessionSignIn(member, incentTasked, 1);
            return ResultDto.success("签到成功");
        }

        Map<String, Object> signMap = JSON.parseObject(taskIdtIds);

        Long interval = -1L;
        int signInCountDays_i = 0;

        //最后一次签到时间
        String lastSignInDateStr = (String) signMap.get("6");
        if (StringUtils.isNotBlank(lastSignInDateStr)) {
            String currentDateStr = DateTools.fmtSimpleDateToString(new Date());
            interval = DateTools.getDaySub(lastSignInDateStr, currentDateStr);
        }

        logger.info("-----签到间隔天数:{}", interval);

        // 今日已经签到过
        if (0 == interval) {

            return ResultDto.error("99", "今日已签到");

            //连续签到
        } else if (1 == interval) {

            //最后签到天数 5:count
            Integer signInCountDays = (Integer) signMap.get("5");

            if (null != signInCountDays) {
                signInCountDays_i = signInCountDays.intValue();
            }

            //是否已签到30天 5:count置为1，从1开始计算签到天数
            signInCountDays_i += 1;
            if (signInCountDays_i >= 31) {
                signInCountDays_i = 1;
            }
            //断签  5:count(签到天数) 置为 1，从 1 开始计算签到天数
        } else if (interval > 1) {
            signInCountDays_i = 1;
        }

        //记录连续签到数据
        this.exSuccessionSignIn(member, incentTasked, signInCountDays_i);

        return ResultDto.success("签到成功");
    }


    /**
     * 记录用户连续签到数据：
     *          签到成果、获取的fungo、签到日志等
     * @param member 登录用户对象
     * @param incentTasked 历史签到成果数据对象
     * @param signInCountDays_i 当前签到天数
     */
    private void exSuccessionSignIn(Member member, IncentTasked incentTasked, int signInCountDays_i) {

        logger.info("-----exSuccessionSignIn记录用户连续签到数据开始---incentTasked:{}", JSON.toJSONString(incentTasked));
        //获取签到规则组和规则数据
        ScoreGroup signInTaskGroup = this.getSignInTaskGroup();
        ScoreRule scoreRuleCurrent = this.getscoreRule(signInCountDays_i);

        String taskGroupId = "-1";
        if (null != signInTaskGroup) {
            taskGroupId = signInTaskGroup.getId();
        }

        Integer score = 0;
        if (null != scoreRuleCurrent) {
            score = scoreRuleCurrent.getScore();
        }

        //-------------------------记录获取fungo，成功签到数据，签到日志等数据----------------------------
        //1.计算本次签到能获取的Fungo币
        if (score > 0) {
            this.addCoinWithMbSignIn(member, score);
        }

        //2.修改用户成功签到数据
        this.exUpdateIncentTasked(member, scoreRuleCurrent, incentTasked, signInCountDays_i);

        //3.记录用户签到日志
        if (null != scoreRuleCurrent) {
            this.addLog(member.getId(), "1", score, taskGroupId, scoreRuleCurrent.getTaskType(),
                    scoreRuleCurrent.getId(), scoreRuleCurrent.getName(), scoreRuleCurrent.getCodeIdt());
        }

        logger.info("-----exSuccessionSignIn记录用户连续签到数据结束");
    }


    /**
     * 在用户任务完成表中记录用户签到成果
     * @param member
     * @param scoreRule
     * @param days
     */
    private void exAddIncentTasked(Member member, ScoreRule scoreRule, int days) {
        if (null == member) {
            return;
        }

        if (null == scoreRule) {
            return;
        }


        //获取签到任务规则数据
        Date currentDate = new Date();

        String ruleId = scoreRule.getId();
        String ruleName = scoreRule.getName();
        Integer score = scoreRule.getScore();
        Integer taskType = scoreRule.getTaskType();

        String currentDateStr = DateTools.fmtSimpleDateToString(currentDate);

        Map<String, Object> taskedMap = new IdentityHashMap<String, Object>();


        //1 taskid
        taskedMap.put("1", ruleId);
        // 2:taskname
        taskedMap.put("2", ruleName);
        // 3:score
        taskedMap.put("3", score);
        // 4:type
        taskedMap.put("4", taskType);
        // 5:count
        taskedMap.put("5", days);
        // 6:date
        taskedMap.put("6", currentDateStr);


        int clusterIndex_i = Integer.parseInt(clusterIndex);

        IncentTasked incentTasked = new IncentTasked();

        incentTasked.setId(PKUtil.getInstance(clusterIndex_i).longPK());
        incentTasked.setMbId(member.getId());

        incentTasked.setCurrentTaskId(ruleId);
        incentTasked.setCurrentTaskName(ruleName);

        incentTasked.setTaskType(taskType);
        incentTasked.setTaskIdtIds(JSON.toJSONString(taskedMap));

        incentTasked.setCreatedAt(currentDate);
        incentTasked.setUpdatedAt(currentDate);

        boolean isInsert = incentTasked.insert();

        fungoCacheTask.excIndexCache(true, FungoCacheTask.TASK_CACHE_KEY_MEMBER_CHECKIN, member.getId(), incentTasked, FungoCacheTask.REDIS_EXPIRE_24_DAYS);

        logger.info("=======用户id:{}----第一次签到成功，添加签到成果IncentTaskedData:{}---isInsert:", member.getId(), JSON.toJSONString(incentTasked), isInsert);

    }


    /**
     * 更新用户已签到成果数据
     * @param member
     * @param scoreRule
     */
    private void exUpdateIncentTasked(Member member, ScoreRule scoreRule, IncentTasked incentTasked, int days) {
        if (null == member) {
            return;
        }

        if (null == scoreRule) {
            return;
        }

        if (days <= 0) {
            return;
        }


        //获取签到任务规则数据
        Date currentDate = new Date();

        String ruleId = scoreRule.getId();
        String ruleName = scoreRule.getName();
        Integer score = scoreRule.getScore();
        Integer taskType = scoreRule.getTaskType();

        String currentDateStr = DateTools.fmtSimpleDateToString(currentDate);

        Map<String, Object> taskedMap = new IdentityHashMap<String, Object>();


        //1 taskid
        taskedMap.put("1", ruleId);
        // 2:taskname
        taskedMap.put("2", ruleName);
        // 3:score
        taskedMap.put("3", score);
        // 4:type
        taskedMap.put("4", taskType);
        // 5:count
        taskedMap.put("5", days);
        // 6:date
        taskedMap.put("6", currentDateStr);


        incentTasked.setCurrentTaskId(ruleId);
        incentTasked.setCurrentTaskName(ruleName);

        incentTasked.setTaskIdtIds(JSON.toJSONString(taskedMap));

        incentTasked.setUpdatedAt(currentDate);

        //update
        boolean isUpdate = incentTasked.updateById();

        fungoCacheTask.excIndexCache(true, FungoCacheTask.TASK_CACHE_KEY_MEMBER_CHECKIN, member.getId(), incentTasked, FungoCacheTask.REDIS_EXPIRE_24_DAYS);

        logger.info("=======用户id:{}----连续签到成功，修改签到成果exUpdateIncentTasked:{}---isUpdate:", member.getId(), JSON.toJSONString(incentTasked), isUpdate);

    }


    /**
     * 获取用户最后的签到数据
     * @param mb_id
     * @return
     */
    private IncentTasked getMbLastSignInData(String mb_id) {

        IncentTasked incentTasked = null;
        //from cache
        String keyPrefix = FungoCacheTask.TASK_CACHE_KEY_MEMBER_CHECKIN;
        incentTasked = (IncentTasked) fungoCacheTask.getIndexCache(keyPrefix, mb_id);
        if (null == incentTasked) {
            //from db
            EntityWrapper<IncentTasked> taskedEntityWrapper = new EntityWrapper<>();
            taskedEntityWrapper.eq("mb_id", mb_id);
            taskedEntityWrapper.eq("task_type", MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_CHECKIN_CODE_IDT_V246);
            incentTasked = incentTaskedService.selectOne(taskedEntityWrapper);

            //redis cache
            fungoCacheTask.excIndexCache(true, keyPrefix, mb_id, incentTasked, FungoCacheTask.REDIS_EXPIRE_24_DAYS);

        }
        return incentTasked;
    }


    /**
     * 获取当前有效的签到任务组
     *
     * @return
     */
    private ScoreGroup getSignInTaskGroup() {

        ScoreGroup scoreGroup = null;

        //from redis
        String keyPrefix = "getSignInTaskGroupAndTaskRuleData";
        String keySuffix = "scoreGroup";

        scoreGroup = (ScoreGroup) fungoCacheTask.getIndexCache(keyPrefix, keySuffix);
        if (null != scoreGroup) {
            return scoreGroup;
        }

        //查询当前有效的签到任务组
        EntityWrapper<ScoreGroup> groupEntityWrapper = new EntityWrapper<>();
        groupEntityWrapper.eq("task_type", MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_CHECKIN_CODE_IDT_V246);
        /*
         * 是否启用:
         *          0-未启动
         *          1-启用
         */
        groupEntityWrapper.eq("is_active", 1);
        scoreGroup = scoreGroupService.selectOne(groupEntityWrapper);
        if (null != scoreGroup) {
            fungoCacheTask.excIndexCache(true, keyPrefix, keySuffix, scoreGroup, FungoCacheTask.REDIS_EXPIRE_3_MONTHS);
        }
        return scoreGroup;
    }


    /**
     * 获取当前有效的任务规则数据
     *
     * @return
     */
    private List<ScoreRule> getSignInTaskRuleData() {

        List<ScoreRule> scoreRuleList = null;
        //from redis
        String keyPrefix = "getSignInTaskGroupAndTaskRuleData";
        String keySuffix = "scoreRuleList";
        scoreRuleList = (List<ScoreRule>) fungoCacheTask.getIndexCache(keyPrefix, keySuffix);
        if (null != scoreRuleList && !scoreRuleList.isEmpty()) {
            return scoreRuleList;
        }

        //查询签到任务组下的所有签到规则数据
        ScoreGroup taskGroup = getSignInTaskGroup();
        if (null == taskGroup) {
            return scoreRuleList;
        }

        EntityWrapper<ScoreRule> scoreRuleEntityWrapper = new EntityWrapper<>();
        scoreRuleEntityWrapper.eq("group_id", taskGroup.getId());
        /*
         * 是否启用:
         *          0-未启动
         *          1-启用
         */
        scoreRuleEntityWrapper.eq("is_active", 1);
        scoreRuleEntityWrapper.eq("task_type", MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_CHECKIN_CODE_IDT_V246);
        scoreRuleList = scoreRuleService.selectList(scoreRuleEntityWrapper);
        if (null != scoreRuleList && !scoreRuleList.isEmpty()) {
            fungoCacheTask.excIndexCache(true, keyPrefix, keySuffix, scoreRuleList, FungoCacheTask.REDIS_EXPIRE_3_MONTHS);
        }

        return scoreRuleList;
    }


    /**
     * 用户签到成功后，添加fungo币
     * @param member
     * @param funCoin
     */
    private void addCoinWithMbSignIn(Member member, int funCoin) {

        //安全性校验
        if (funCoin <= 0) {
            return;
        }

        //获取用户的fungo账号
        IncentAccountCoin accountCoin = accountCoinService.selectOne(new EntityWrapper<IncentAccountCoin>().eq("mb_id", member.getId()).eq("account_group_id", 3));

        //若用户没有fungo币账户，则创建
        if (null == accountCoin) {
            accountCoin = memberAccountDaoService.createAccountCoin(member.getId());
        }

        accountCoin.setCoinUsable(accountCoin.getCoinUsable().add(new BigDecimal(funCoin)));
        accountCoin.setUpdatedAt(new Date());

        boolean isUpdate = accountCoin.updateById();

        logger.info("=======用户id:{}----第一次签到-----获取fungo币数量:{}--isUpdate:{}", member.getId(), JSON.toJSONString(accountCoin), isUpdate);

        fungoCacheTask.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INCENTS_FORTUNE_COIN_POST + member.getId(), "", null);
        //刷新fun总数
        fungoCacheTask.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + member.getId(), "",null);
    }


    /**
     * 记录用户签到日志
     * @param mb_id 用户id
     * @param days 当前签到天数
     * @param funCoin  获取的fungo币数量
     * @param taskGroupId  签到任务分组ID
     * @param taskType 签到任务类型码
     * @param taskRuleId 签到规则ID
     * @param taskRuleName 签到规则名称
     * @param taskRuleCodeIdt 签到规则码
     */
    private void addLog(String mb_id, String days, Integer funCoin, String taskGroupId, Integer taskType, String taskRuleId, String taskRuleName, Integer taskRuleCodeIdt) {

        // 日志记录
        ScoreLog newLog = new ScoreLog();

        newLog.setMemberId(mb_id);

        newLog.setGroupId(taskGroupId);
        newLog.setRuleId(taskRuleId);
        newLog.setRuleName(taskRuleName);

        //签到动态类型 LOGIN,从V2.4.6修改为 signIn
        newLog.setType(MemberActionTypeConsts.MEMBER_ACTIOIN_TYPE_CEHCKIN);

        newLog.setTaskType(taskType);
        newLog.setCodeIdt(taskRuleCodeIdt);

        newLog.setInfo("{\"days\":" + days + "}");
        newLog.setScore(funCoin);

        newLog.setCreatedAt(new Date());
        newLog.setUpdatedAt(new Date());

        scoreLogService.insert(newLog);
    }


    /**
     * 基于当前签到天数，获取签到等级编码
     * @param signInDays
     * @return
     */
    private int getTaskIdtWithSignInDays(int signInDays) {
        int code_idt = -1;

        //签到1天
        if (1 == signInDays) {

            code_idt = MemberIncentSignInConsts.SIGN_IN_RULE_GRADE_CODE_IDT_1;

            //连续签到2天
        } else if (2 == signInDays) {

            code_idt = MemberIncentSignInConsts.SIGN_IN_RULE_GRADE_CODE_IDT_2;
            //连续签到3～6天
        } else if (3 <= signInDays && signInDays <= 6) {

            code_idt = MemberIncentSignInConsts.SIGN_IN_RULE_GRADE_CODE_IDT_3_6;

            //连续签到7天
        } else if (7 == signInDays) {

            code_idt = MemberIncentSignInConsts.SIGN_IN_RULE_GRADE_CODE_IDT_7;

            //连续签到8～13天
        } else if (8 <= signInDays && signInDays <= 13) {

            code_idt = MemberIncentSignInConsts.SIGN_IN_RULE_GRADE_CODE_IDT_8_13;

            //连续签到14天
        } else if (14 == signInDays) {

            code_idt = MemberIncentSignInConsts.SIGN_IN_RULE_GRADE_CODE_IDT_14;

            //连续签到15～20天
        } else if (15 <= signInDays && signInDays <= 20) {

            code_idt = MemberIncentSignInConsts.SIGN_IN_RULE_GRADE_CODE_IDT_15_20;

            //连续签到21天
        } else if (21 == signInDays) {

            code_idt = MemberIncentSignInConsts.SIGN_IN_RULE_GRADE_CODE_IDT_21;

            //连续签到22～27天
        } else if (22 <= signInDays && signInDays <= 27) {

            code_idt = MemberIncentSignInConsts.SIGN_IN_RULE_GRADE_CODE_IDT_22_27;

            //连续签到28天
        } else if (28 == signInDays) {

            code_idt = MemberIncentSignInConsts.SIGN_IN_RULE_GRADE_CODE_IDT_28;

            //连续签到29天
        } else if (29 == signInDays) {

            code_idt = MemberIncentSignInConsts.SIGN_IN_RULE_GRADE_CODE_IDT_29;

            //连续签到30天
        } else if (30 == signInDays) {
            code_idt = MemberIncentSignInConsts.SIGN_IN_RULE_GRADE_CODE_IDT_30;
        }
        return code_idt;
    }


    /**
     * 基于当前签到天数获取，当前的签到规则数据
     * @param days
     * @return
     */
    private ScoreRule getscoreRule(int days) {

        int current_task_idt_code = this.getTaskIdtWithSignInDays(days);

        //获取规则数据
        List<ScoreRule> signInTaskRuleList = getSignInTaskRuleData();
        if (null != signInTaskRuleList && !signInTaskRuleList.isEmpty()) {
            for (ScoreRule scoreRule : signInTaskRuleList) {
                Integer codeIdt = scoreRule.getCodeIdt();
                int codeIdt_i = 0;
                if (null != codeIdt) {
                    codeIdt_i = codeIdt.intValue();
                }
                if (current_task_idt_code == codeIdt_i) {
                    return scoreRule;
                }
            }
        }
        return null;
    }

    //----------
}
