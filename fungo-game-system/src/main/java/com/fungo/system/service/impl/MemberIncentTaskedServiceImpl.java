package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.fungo.system.entity.*;
import com.fungo.system.service.*;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.consts.FunGoGameConsts;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import com.game.common.enums.NewTaskIdenum;
import com.game.common.enums.oldTaskIdenum;
import com.game.common.repo.cache.facade.FungoCacheTask;
import com.game.common.util.FunGoBeanUtils;
import com.game.common.util.date.DateTools;
import com.game.common.util.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MemberIncentTaskedServiceImpl implements IMemberIncentTaskedService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberIncentTaskedServiceImpl.class);

    @Autowired
    private ScoreGroupService scoreGroupService;

    @Autowired
    private ScoreRuleService scoreRuleService;

    @Autowired
    private IncentTaskedService incentTaskedService;

    @Autowired
    private ScoreLogService scoreLogService;

    @Autowired
    private IMemberPermRankedService iMemberPerRankedService;

    @Autowired
    private FungoCacheTask fungoCacheTask;


    @Cacheable(cacheNames = {FunGoGameConsts.CACHE_EH_NAME}, key = "'" + FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER + "_TaskRule' + #task_type")
    @Override
    public List<Map<String, Object>> getTaskRule(int task_type) throws BusinessException {

        //封装响应的数据集合
        List<Map<String, Object>> scoreGroupMapList = new ArrayList<Map<String, Object>>();

        try {

            LOGGER.info("获取用户任务完整进度-task_type:{}", task_type);

            //第一步获取任务分类 默认查询 task_type =3 分值和虚拟币共有
            if (task_type <= 0) {
                task_type = FunGoGameConsts.TASK_RULE_TASK_TYPE_SCOREANDCOIN;
            }

            Wrapper wrapperGroup = new EntityWrapper<ScoreGroup>();
            Map<String, Object> criteriaMapGroup = new HashMap<String, Object>();
            criteriaMapGroup.put("task_type", task_type);
            criteriaMapGroup.put("is_active", FunGoGameConsts.FUNGO_PUBLIC_IS_ACTIVE_ABLE);
            wrapperGroup.allEq(criteriaMapGroup).orderBy("sort", true);

            //任务分类
            List<ScoreGroup> taskGroupList = scoreGroupService.selectList(wrapperGroup);
            LOGGER.info("获取用户任务完整进度-任务分类Result:{}", JSONObject.toJSONString(taskGroupList));


            //第二步根据任务分类id 查询每个分类下的分类规则数据
            List<String> scoreGroupIdList = new ArrayList<String>();
            for (ScoreGroup scoreGroup : taskGroupList) {
                scoreGroupIdList.add(scoreGroup.getId());
            }

            //根据scoreGroupId查询规则数据
            Wrapper wrapperRule = new EntityWrapper<ScoreRule>();
            Map<String, Object> criteriaMapRule = new HashMap<String, Object>();

            criteriaMapRule.put("is_active", FunGoGameConsts.FUNGO_PUBLIC_IS_ACTIVE_ABLE);
            //criteriaMapRule.put("group_id", scoreGroup.getId());
            //criteriaMapRule.put("task_type", FunGoGameConsts.TASK_RULE_TASK_TYPE_SCORE);

            wrapperRule.allEq(criteriaMapRule).in("group_id", scoreGroupIdList.toArray()).in("task_type", new Object[]{FunGoGameConsts.TASK_RULE_TASK_TYPE_SCORE,
                    FunGoGameConsts.TASK_RULE_TASK_TYPE_COIN_TASK}).orderBy("sort", true);

            //任务规则数据
            List<ScoreRule> ruleListOfAllCategory = scoreRuleService.selectList(wrapperRule);


            //遍历规则分类数据
            for (ScoreGroup scoreGroup : taskGroupList) {

                Map<String, Object> scoreGroupMap = new HashMap<String, Object>();
                scoreGroupMapList.add(scoreGroupMap);

                FunGoBeanUtils.bean2map(scoreGroup, scoreGroupMap, ScoreGroup.class);

                String groupIdFor = scoreGroup.getId();


                //分类下的规则数据
                List<ScoreRule> scoreRuleListOfCategory = new ArrayList<ScoreRule>();
                scoreGroupMap.put("ruleData", scoreRuleListOfCategory);


                //遍历分值类任务规则数据 获取分值任务用户完成进度和成果
                for (ScoreRule scoreRule : ruleListOfAllCategory) {

                    //找到某个规则分类下的 所有规则数据
                    //规则分类数据ID
                    String groupIdOfRule = scoreRule.getGroupId();

                    //规则数据ID
                    String ruleId = scoreRule.getId();
                    if (StringUtils.equalsIgnoreCase(groupIdFor, groupIdOfRule)
                            && scoreRule.getTaskType().intValue() == FunGoGameConsts.TASK_RULE_TASK_TYPE_SCORE
                    ) {

                        scoreRuleListOfCategory.add(scoreRule);
                    }

                    //获取分值任务规则对应的虚拟币规则
                    for (ScoreRule scoreRuleCoin : ruleListOfAllCategory) {

                        String plsTaskId = scoreRuleCoin.getPlsTaskId();
                        if (StringUtils.equalsIgnoreCase(ruleId, plsTaskId)) {
                            scoreRule.setCoinRule(scoreRuleCoin);

                            //虚拟币类任务用户完成进度和成果
                            scoreRule.setCoinRule(scoreRuleCoin);

                            break;
                        }
                    }

                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error("获取用户任务完整进度出现异常",ex);
            throw new BusinessException("-1", "获取用户任务完整进度出现异常");
        }

        return scoreGroupMapList;
    }


    //@Cacheable(cacheNames = {FunGoGameConsts.CACHE_EH_NAME}, key = "'" + FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER + "_TaskProgress' + #memberId")
    @Override
    public List<Map<String, Object>> getMemberTaskProgress(String memberId, int task_type) throws BusinessException {


        //封装响应的数据集合
        List<Map<String, Object>> scoreGroupMapList = null;

        String keyPreffix = FungoCoreApiConstant.FUNGO_CORE_API_TASK_USER_TASK_PROGRESS + "-" + memberId;
        String keySuffix = task_type + "";

        try {

            LOGGER.info("获取用户任务完整进度-task_type:{}", task_type);

            //从redis缓存中获取
            scoreGroupMapList = (List<Map<String, Object>>) fungoCacheTask.getIndexCache(keyPreffix, keySuffix);
//            if (null != scoreGroupMapList && !scoreGroupMapList.isEmpty()) {
//                return scoreGroupMapList;
//            }

            scoreGroupMapList = new ArrayList<Map<String, Object>>();

            //第一步获取任务分类 默认查询 task_type =3 分值和虚拟币共有
            if (task_type <= 0) {
                task_type = FunGoGameConsts.TASK_RULE_TASK_TYPE_SCOREANDCOIN;
            }

            Wrapper wrapperGroup = new EntityWrapper<ScoreGroup>();
            Map<String, Object> criteriaMapGroup = new HashMap<String, Object>();
            criteriaMapGroup.put("task_type", task_type);
            criteriaMapGroup.put("is_active", FunGoGameConsts.FUNGO_PUBLIC_IS_ACTIVE_ABLE);

            wrapperGroup.allEq(criteriaMapGroup).orderBy("sort", true);


            //任务分类
            List<ScoreGroup> taskGroupList = scoreGroupService.selectList(wrapperGroup);
            LOGGER.info("获取用户任务完整进度-任务分类Result:{}", JSONObject.toJSONString(taskGroupList));

            //第二步根据任务分类id 查询每个分类下的分类规则数据
            List<String> scoreGroupIdList = new ArrayList<String>();
            for (ScoreGroup scoreGroup : taskGroupList) {
                scoreGroupIdList.add(scoreGroup.getId());
            }
                  //根据scoreGroupId查询规则数据
            Wrapper wrapperRule = new EntityWrapper<ScoreRule>();
            Map<String, Object> criteriaMapRule = new HashMap<String, Object>();

            criteriaMapRule.put("is_active", FunGoGameConsts.FUNGO_PUBLIC_IS_ACTIVE_ABLE);
            //criteriaMapRule.put("group_id", scoreGroup.getId());
            //criteriaMapRule.put("task_type", FunGoGameConsts.TASK_RULE_TASK_TYPE_SCORE);

            wrapperRule.allEq(criteriaMapRule).in("group_id", scoreGroupIdList.toArray()).in("task_type", new Object[]{FunGoGameConsts.TASK_RULE_TASK_TYPE_SCORE,
                    FunGoGameConsts.TASK_RULE_TASK_TYPE_COIN_TASK}).orderBy("sort", true);

            //任务规则数据
            List<ScoreRule> ruleListOfAllCategory = scoreRuleService.selectList(wrapperRule);


            //第三 步  查询某个用户，完成某个分类下的分类规则数据
            //1 .分值任务用户完成进度和成果
            Wrapper wrapperRuleTaskScore = new EntityWrapper<IncentTasked>();
            Map<String, Object> criteriaMapIncentTaskedScore = new HashMap<String, Object>();
            criteriaMapIncentTaskedScore.put("mb_id", memberId);
            criteriaMapIncentTaskedScore.put("task_type", FunGoGameConsts.TASK_RULE_TASK_TYPE_SCORE);
            wrapperRuleTaskScore.allEq(criteriaMapIncentTaskedScore);
            wrapperRuleTaskScore.orderBy("updated_at", false);

            //获取当前用户已经完成的所有任务
            IncentTasked incentTaskedScore = null;

            IncentTasked incentTaskedScore22 = null;

            List<IncentTasked> incentTaskedListScore = incentTaskedService.selectList(wrapperRuleTaskScore);
            if (null != incentTaskedListScore && !incentTaskedListScore.isEmpty()) {
                incentTaskedScore = incentTaskedListScore.get(0);
            }

            //fix:分值任务-11,fungo币任务-23, 签到-22
            //若分值类没有则，获取 22类型的任务数据
            if (null == incentTaskedScore) {

                criteriaMapIncentTaskedScore.put("task_type", FunGoGameConsts.TASK_RULE_TASK_TYPE_COIN_SIGN_IN);

                wrapperRuleTaskScore = new EntityWrapper<IncentTasked>();
                wrapperRuleTaskScore.allEq(criteriaMapIncentTaskedScore);
                wrapperRuleTaskScore.orderBy("updated_at", false);

                incentTaskedListScore = incentTaskedService.selectList(wrapperRuleTaskScore);

                if (null != incentTaskedListScore && !incentTaskedListScore.isEmpty()) {
                    incentTaskedScore = incentTaskedListScore.get(0);
                    incentTaskedScore22 = incentTaskedScore;
                }
            }

            if (null == incentTaskedScore22) {
                criteriaMapIncentTaskedScore.put("task_type", FunGoGameConsts.TASK_RULE_TASK_TYPE_COIN_SIGN_IN);

                wrapperRuleTaskScore = new EntityWrapper<IncentTasked>();
                wrapperRuleTaskScore.allEq(criteriaMapIncentTaskedScore);
                wrapperRuleTaskScore.orderBy("updated_at", false);

                incentTaskedListScore = incentTaskedService.selectList(wrapperRuleTaskScore);

                if (null != incentTaskedListScore && !incentTaskedListScore.isEmpty()) {
                    incentTaskedScore22 = incentTaskedListScore.get(0);
                }
            }


            //2 .虚拟币任务用户完成进度和成果

            Wrapper wrapperRuleTaskCoin = new EntityWrapper<IncentTasked>();
            Map<String, Object> criteriaMapIncentTaskedCoin = new HashMap<String, Object>();
            criteriaMapIncentTaskedCoin.put("mb_id", memberId);
            criteriaMapIncentTaskedCoin.put("task_type", FunGoGameConsts.TASK_RULE_TASK_TYPE_COIN_TASK);
            wrapperRuleTaskCoin.allEq(criteriaMapIncentTaskedCoin);

            wrapperRuleTaskCoin.orderBy("updated_at", false);

            //获取当前用户已经完成的所有任务
            IncentTasked incentTaskedCoin = null;
            IncentTasked incentTaskedCoin22 = null;

            List<IncentTasked> incentTaskedListCoin = incentTaskedService.selectList(wrapperRuleTaskCoin);
            if (null != incentTaskedListCoin && !incentTaskedListCoin.isEmpty()) {
                incentTaskedCoin = incentTaskedListCoin.get(0);
            }

            //若Fungo币类任务成果没有，则获取 22类型的任务数据
            if (null == incentTaskedCoin) {

                criteriaMapIncentTaskedCoin.put("task_type", FunGoGameConsts.TASK_RULE_TASK_TYPE_COIN_SIGN_IN);

                wrapperRuleTaskCoin = new EntityWrapper<IncentTasked>();
                wrapperRuleTaskCoin.allEq(criteriaMapIncentTaskedCoin);
                wrapperRuleTaskCoin.orderBy("updated_at", false);

                incentTaskedListCoin = incentTaskedService.selectList(wrapperRuleTaskCoin);
                if (null != incentTaskedListCoin && !incentTaskedListCoin.isEmpty()) {
                    incentTaskedCoin = incentTaskedListCoin.get(0);
                    incentTaskedCoin22 = incentTaskedCoin;
                }
            }

            if (null == incentTaskedCoin22) {

                criteriaMapIncentTaskedCoin.put("task_type", FunGoGameConsts.TASK_RULE_TASK_TYPE_COIN_SIGN_IN);

                wrapperRuleTaskCoin = new EntityWrapper<IncentTasked>();
                wrapperRuleTaskCoin.allEq(criteriaMapIncentTaskedCoin);
                wrapperRuleTaskCoin.orderBy("updated_at", false);

                incentTaskedListCoin = incentTaskedService.selectList(wrapperRuleTaskCoin);
                if (null != incentTaskedListCoin && !incentTaskedListCoin.isEmpty()) {
                    incentTaskedCoin22 = incentTaskedListCoin.get(0);
                }
            }


            //权益规则与功能授权关系列表
            List<IncentMbPermRanked> incentMbPermRankedList = iMemberPerRankedService.getIncentMbPermRankedList();

            //遍历规则分类数据
            for (ScoreGroup scoreGroup : taskGroupList) {

                Map<String, Object> scoreGroupMap = new HashMap<String, Object>();
                scoreGroupMapList.add(scoreGroupMap);
                FunGoBeanUtils.bean2map(scoreGroup, scoreGroupMap, ScoreGroup.class);

                String groupIdFor = scoreGroup.getId();

                //分类下的规则数据
                List<ScoreRule> scoreRuleListOfCategory = new ArrayList<ScoreRule>();
                scoreGroupMap.put("ruleData", scoreRuleListOfCategory);

                //遍历分值类任务规则数据 获取分值任务用户完成进度和成果
                for (ScoreRule scoreRule : ruleListOfAllCategory) {

                    //找到某个规则分类下的 所有规则数据
                    //规则分类数据ID
                    String groupIdOfRule = scoreRule.getGroupId();
                    //规则数据ID
                    String ruleId = scoreRule.getId();

                    if (StringUtils.equalsIgnoreCase(groupIdFor, groupIdOfRule) && scoreRule.getTaskType().intValue() == FunGoGameConsts.TASK_RULE_TASK_TYPE_SCORE) {

                        scoreRuleListOfCategory.add(scoreRule);

                        //分值类规则用户的完成进度数据
                        //分值任务规则，用户完成进度和成果
                        IncentTaskedOut taskedOutScore = getMbIncentTaskedOutWithScore(memberId, scoreRule, incentTaskedScore, scoreGroup.getTaskFlag(), incentTaskedCoin22);
                        if (null != taskedOutScore) {
                            Map<String, Object> taskedOutScoreMap = new HashMap<String, Object>();
                            FunGoBeanUtils.bean2map(taskedOutScore, taskedOutScoreMap, IncentTaskedOut.class);

                            scoreRule.setIncentTaskedOut(taskedOutScoreMap);
                        }

                    }


                    //获取分值任务规则对应的虚拟币规则
                    for (ScoreRule coinRule : ruleListOfAllCategory) {

                        String plsTaskId = coinRule.getPlsTaskId();

                        if (StringUtils.equalsIgnoreCase(groupIdFor, coinRule.getGroupId())
                                && StringUtils.equalsIgnoreCase(ruleId, plsTaskId)
                                && coinRule.getTaskType().intValue() == FunGoGameConsts.TASK_RULE_TASK_TYPE_COIN_TASK) {

                            //分值类任务 对应的虚拟币类任务
                            scoreRule.setCoinRule(coinRule);

                            //虚拟币任务用户完成进度和成果
                            IncentTaskedOut taskedOutCoin = getMbIncentTaskedOutWithCoin(memberId, coinRule, incentTaskedCoin, scoreGroup.getTaskFlag(), incentTaskedCoin22);

                            if (null != taskedOutCoin) {

                                Map<String, Object> taskedOutCoinMap = new HashMap<String, Object>();
                                FunGoBeanUtils.bean2map(taskedOutCoin, taskedOutCoinMap, IncentTaskedOut.class);

                                coinRule.setIncentTaskedOut(taskedOutCoinMap);
                            }
                            break;
                        }
                    }


                    //任务page
//                  List<IncentMbPermRanked> incentMbPermRankedList
                    for (IncentMbPermRanked permRanked : incentMbPermRankedList) {

                        Integer permTaskIdt = permRanked.getTaskCodeIdt();
                        Integer scoreIdt = scoreRule.getCodeIdt();

                        int permTaskIdt_i = -1;
                        int scoreIdt_i = -1;

                        if (null != permTaskIdt) {
                            permTaskIdt_i = permTaskIdt.intValue();
                        }
                        if (null != scoreIdt) {
                            scoreIdt_i = scoreIdt.intValue();
                        }
                        if (permTaskIdt_i != -1 && scoreIdt_i != -1) {
                            if (permTaskIdt_i == scoreIdt_i) {
                                scoreRule.setToLinkUrl(permRanked.getToLinkUrl());
                                scoreRule.setLevelLimit(permRanked.getRankId());
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error("获取用户任务完整进度出现异常",ex);
            throw new BusinessException("-1", "获取用户任务完整进度出现异常");
        }
        //把数据缓存
        fungoCacheTask.excIndexCache(true, keyPreffix, keySuffix, scoreGroupMapList, FungoCacheTask.REDIS_EXPIRE_DEFAULT);
        return scoreGroupMapList;

    }

    @Override
    public IncentTaskedOut getTasked(String memberId, int task_type, String task_id) {
        IncentTaskedOut taskedOut = null;
        try {
            Wrapper incentTaskedEntityWrapper = new EntityWrapper<IncentTasked>();
            Map<String, Object> criteriaMapRule = new HashMap<String, Object>();
            criteriaMapRule.put("mb_id", memberId);
            criteriaMapRule.put("task_type", task_type);
            incentTaskedEntityWrapper.allEq(criteriaMapRule);
            incentTaskedEntityWrapper.orderBy("updated_at", false);
            //已经完成的数据
            List<IncentTasked> incentTaskedList = incentTaskedService.selectList(incentTaskedEntityWrapper);



            if (null != incentTaskedList && !incentTaskedList.isEmpty()) {
                IncentTasked incentTasked = incentTaskedList.get(0);
                // 获取数据示例：
                // [  { 1:taskid, 2:taskname,  3:score,  4:type,  5:count ,6:date,  7:incomie_freg_type}  ]
                // (任务id,任务名称，获取的分值|虚拟币数，任务类型，完成数量,任务完成时间,收益频率类型)
                String taskIdtIdsJson = incentTasked.getTaskIdtIds();
                //从历史完成的任务中，获取待查找任务id的进度
                if (StringUtils.isNotBlank(taskIdtIdsJson)) {
                    //获取已完成历史任务集合
                    JSONArray jsonArray = JSONObject.parseArray(taskIdtIdsJson);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        //获取taskId
                        String taskId_done = object.getString("1");
                        //type
                        String type = String.valueOf(object.get("4"));
                        if (StringUtils.equalsIgnoreCase(task_id, taskId_done) && StringUtils.equalsIgnoreCase(type, String.valueOf(task_type))) {
                            taskedOut = new IncentTaskedOut();
                            //taskname
                            String taskname = object.getString("2");
                            //score
                            String score = String.valueOf(object.get("3"));
                            //count
                            String count = String.valueOf(object.get("5"));
                            //doneDate
                            String doneDate = String.valueOf(object.get("6"));
                            taskedOut.setTaskId(task_id);
                            taskedOut.setTaskName(taskname);
                            taskedOut.setScore(score);
                            taskedOut.setTaskType(task_type);
                            taskedOut.setDoneDate(doneDate);
                            if (StringUtils.isNotBlank(count)) {
                                taskedOut.setTaskedCount(Integer.parseInt(count));
                            }
                            return taskedOut;
                        }
                    }
                }
                for (int i = 1; i < incentTaskedList.size(); i++) {
                    IncentTasked incentTaskedDel = incentTaskedList.get(i);
                    if (null != incentTaskedDel) {
                        if (incentTasked.getId().longValue() != incentTaskedDel.getId().longValue()) {
                            //incentTaskedDel.deleteById();
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("用户任务成果表",e);
        }
        return taskedOut;
    }


    /**
     * 获取用户执行分值任务的进度
     *
     * @param memberId
     * @param taskId
     * @param taskGroupFlag 任务组flag
     * @return
     */
    private IncentTaskedOut getMbIncentTaskedOutWithScore(String memberId, ScoreRule scoreRule, IncentTasked incentTasked, Integer taskGroupFlag, IncentTasked incentTaskedScore22) {

        IncentTaskedOut taskedOut = null;

        try {

            if (null == incentTasked) {

                return taskedOut;
            }

            String taskId = scoreRule.getId();

            int taskType = scoreRule.getTaskType();

            //获取数据示例：[  { 1:taskid, 2:taskname,  3:score,  4:type,  5:count ,6:date,  7:incomie_freg_type}  ]
            //               (任务id,任务名称，获取的分值|虚拟币数，任务类型，完成数量,任务完成时间,收益频率类型)
            String taskIdtIdsJson = incentTasked.getTaskIdtIds();
            //从历史完成的任务中，获取待查找任务id的进度
            if (StringUtils.isNotBlank(taskIdtIdsJson)) {
                //获取已完成历史任务集合
                JSONArray jsonArray = JSONObject.parseArray(taskIdtIdsJson);
                if (null != jsonArray && !jsonArray.isEmpty()) {

                    for (int i = 0; i < jsonArray.size(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        //获取taskId
                        String taskId_done = object.getString("1");
                        //type
                        String type = String.valueOf(object.get("4"));
                        //taskname
                        String taskname = object.getString("2");
                        //score
                        String score = String.valueOf(object.get("3"));
                        //count
                        String count = String.valueOf(object.get("5"));
                        //doneDate
                        String doneDate = String.valueOf(object.get("6"));
                        //基于任务的状态更新情况
                        //1.新手任务，起始编号 1701-关注3位Fun友，验证是否关注了3位好友
                        if (FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code() == taskGroupFlag.intValue()) {


                            //若历史的新手任务已经做了，则不在显示
                            boolean isFinishOldTask = isFinishOldTask(taskId, taskId_done,memberId);
                            if (!isFinishOldTask) {
                                isFinishOldTask = this.isHasOldTaskV243(incentTaskedScore22, taskId);
                            }

                            if (isFinishOldTask) {

                                taskedOut = new IncentTaskedOut();

                                taskedOut.setTaskId(taskId_done);
                                taskedOut.setTaskName(taskname);
                                taskedOut.setScore(score);
                                taskedOut.setTaskType(taskType);
                                taskedOut.setDoneDate(doneDate);
                                if (StringUtils.isNotBlank(count) && !StringUtils.equalsIgnoreCase("0", count)) {
                                    taskedOut.setTaskedCount(Integer.parseInt(count));
                                } else {
                                    taskedOut.setTaskedCount(scoreRule.getMax());
                                }

                                return taskedOut;
                            }
                        }
                        if (StringUtils.equalsIgnoreCase(taskId, taskId_done) &&
                                StringUtils.equalsIgnoreCase(type, String.valueOf(taskType))
                        ) {

                            taskedOut = new IncentTaskedOut();

                            //基于任务的状态更新情况
                            //1.新手任务，起始编号 1701-关注3位Fun友，验证是否关注了3位好友
                            if (FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code() == taskGroupFlag.intValue()) {

                                boolean watchThreeMember = this.isWatchThreeMember(taskId_done, count);
                                if (!watchThreeMember) {
                                    return null;
                                }
                                //2.每日任务，起始编号 8485
                            } else if (FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code() == taskGroupFlag.intValue()) {
                                boolean isCurrentDate = this.isCurrentDate(doneDate);
                                if (!isCurrentDate) {
                                    return null;
                                }

                                //2.每周任务，起始编号 8706
                            } else if (FunGoIncentTaskV246Enum.TASK_GROUP_WEEKLY.code() == taskGroupFlag.intValue()) {
                                if (object.containsKey("8")) {
                                    String target_ids = (String) object.get("8");
                                    boolean isfinisWithWeekly = this.isFinisWithWeekly(taskId_done, target_ids, doneDate);
                                    if (!isfinisWithWeekly) {
                                        return null;
                                    }
                                } else {
                                    return null;
                                }
                            }

                            //
                            String incomie_freg_type = "";

                            //刷新任务次数
                            if (object.get("7") != null) {
                                incomie_freg_type = String.valueOf(object.get("7"));
                            } else {
                                ScoreRule rule = scoreRuleService.selectById(taskId_done);
                                if (rule != null) {
                                    incomie_freg_type = rule.getIncomieFregType().toString();
                                }
                            }

                            if ("1".equals(incomie_freg_type)) {

                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                //今天的日期
                                Date todayDate = format.parse(format.format(new Date()));
                                //任务最后完成日期
                                Date taskDate = format.parse(doneDate);
                                if (taskDate.before(todayDate)) {
                                    count = "0";
                                }
                            }

                            taskedOut.setTaskId(taskId);
                            taskedOut.setTaskName(taskname);
                            taskedOut.setScore(score);
                            taskedOut.setTaskType(taskType);
                            taskedOut.setDoneDate(doneDate);

                            if (StringUtils.isNotBlank(count)) {
                                taskedOut.setTaskedCount(Integer.parseInt(count));
                            }
                            break;
                        }
                    }

                }


            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error("获取用户执行分值任务的进度",ex);
        }

        return taskedOut;
    }


    /**
     * 从V2.4.5版本之前的任务中查找是否完成了绑定QQ/微信/微博等新手任务
     * @param incentTaskedScore22
     * @param newTaskId
     * @return
     */
    private boolean isHasOldTaskV243(IncentTasked incentTaskedScore22, String newTaskId) {
        boolean isFinis = false;
        if (null == incentTaskedScore22) {

            return isFinis;
        }


        //获取数据示例：[  { 1:taskid, 2:taskname,  3:score,  4:type,  5:count ,6:date,  7:incomie_freg_type}  ]
        //               (任务id,任务名称，获取的分值|虚拟币数，任务类型，完成数量,任务完成时间,收益频率类型)
        String taskIdtIdsJson = incentTaskedScore22.getTaskIdtIds();
        //从历史完成的任务中，获取待查找任务id的进度
        if (StringUtils.isNotBlank(taskIdtIdsJson)) {
            //获取已完成历史任务集合
            JSONArray jsonArray = JSONObject.parseArray(taskIdtIdsJson);
            if (null != jsonArray && !jsonArray.isEmpty()) {

                for (int i = 0; i < jsonArray.size(); i++) {

                    JSONObject object = jsonArray.getJSONObject(i);
                    //获取taskId
                    String taskId_done = object.getString("1");
                    isFinis = isFinishOldTask(newTaskId, taskId_done,incentTaskedScore22.getMbId());

                    break;
                }
            }
        }
        return isFinis;
    }


    /**
     * 获取用户执行虚拟币任务的进度
     *
     * @param memberId
     * @param taskId
     * @param taskGroupFlag 任务组flag
     * @return
     */
    private IncentTaskedOut getMbIncentTaskedOutWithCoin(String memberId, ScoreRule scoreRule, IncentTasked
            incentTasked, Integer taskGroupFlag, IncentTasked incentTaskedCoin22) {

        IncentTaskedOut taskedOut = null;

        try {

            if (null == incentTasked) {

                return taskedOut;
            }

            String taskId = scoreRule.getId();
            int taskType = scoreRule.getTaskType();

            //获取数据示例：[  { 1:taskid, 2:taskname,  3:score,  4:type,  5:count ,6:date,  7:incomie_freg_type}  ]
            //               (任务id,任务名称，获取的分值|虚拟币数，任务类型，完成数量,任务完成时间,收益频率类型)
            String taskIdtIdsJson = incentTasked.getTaskIdtIds();
            //从历史完成的任务中，获取待查找任务id的进度
            if (StringUtils.isNotBlank(taskIdtIdsJson)) {
                //获取已完成历史任务集合
                JSONArray jsonArray = JSONObject.parseArray(taskIdtIdsJson);
                if (null != jsonArray && !jsonArray.isEmpty()) {

                    for (int i = 0; i < jsonArray.size(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);


                        //获取taskId
                        String taskId_done = object.getString("1");
                        //type
                        String type = String.valueOf(object.get("4"));

                        //taskname
                        String taskname = object.getString("2");

                        //score
                        String score = String.valueOf(object.get("3"));

                        //count
                        String count = String.valueOf(object.get("5"));

                        //doneDate
                        String doneDate = String.valueOf(object.get("6"));

                        //基于任务的状态更新情况
                        //1.新手任务，起始编号 1701-关注3位Fun友，验证是否关注了3位好友
                        if (FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code() == taskGroupFlag.intValue()) {

                            //若历史的新手任务已经做了，则不在显示
                            boolean isFinishOldTask = isFinishOldTask(taskId, taskId_done , memberId);
                            if (!isFinishOldTask) {
                                isFinishOldTask = this.isHasOldTaskV243(incentTaskedCoin22, taskId);
                            }

                            if (isFinishOldTask) {

                                taskedOut = new IncentTaskedOut();

                                taskedOut.setTaskId(taskId_done);
                                taskedOut.setTaskName(taskname);
                                taskedOut.setScore(score);
                                taskedOut.setTaskType(taskType);
                                taskedOut.setDoneDate(doneDate);

                                if (StringUtils.isNotBlank(count) && !StringUtils.equalsIgnoreCase("0", count)) {
                                    taskedOut.setTaskedCount(Integer.parseInt(count));
                                } else {
                                    taskedOut.setTaskedCount(scoreRule.getMax());
                                }
                                return taskedOut;
                            }
                        }

                        if (StringUtils.equalsIgnoreCase(taskId, taskId_done) &&
                                StringUtils.equalsIgnoreCase(type, String.valueOf(taskType))
                        ) {

                            taskedOut = new IncentTaskedOut();
                            //基于任务的状态更新情况
                            //1.新手任务，起始编号 1701-关注3位Fun友，验证是否关注了3位好友
                            if (FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code() == taskGroupFlag.intValue()) {


                                boolean watchThreeMember = this.isWatchThreeMember(taskId_done, count);
                                if (!watchThreeMember) {
                                    return null;
                                }
                                //2.每日任务，起始编号 8485
                            } else if (FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code() == taskGroupFlag.intValue()) {
                                boolean isCurrentDate = this.isCurrentDate(doneDate);
                                if (!isCurrentDate) {
                                    return null;
                                }

                                //2.每周任务，起始编号 8706
                            } else if (FunGoIncentTaskV246Enum.TASK_GROUP_WEEKLY.code() == taskGroupFlag.intValue()) {
                                if (object.containsKey("8")) {
                                    String target_ids = (String) object.get("8");
                                    boolean isfinisWithWeekly = this.isFinisWithWeekly(taskId_done, target_ids, doneDate);
                                    if (!isfinisWithWeekly) {
                                        return null;
                                    }
                                } else {
                                    return null;
                                }
                            }


                            //
                            String incomie_freg_type = "";

                            //刷新任务次数
                            if (object.get("7") != null) {
                                incomie_freg_type = String.valueOf(object.get("7"));
                            } else {
                                ScoreRule rule = scoreRuleService.selectById(taskId_done);
                                if (rule != null) {
                                    incomie_freg_type = rule.getIncomieFregType().toString();
                                }
                            }

                            if ("1".equals(incomie_freg_type)) {

                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                //今天的日期
                                Date todayDate = format.parse(format.format(new Date()));
                                //任务最后完成日期
                                Date taskDate = format.parse(doneDate);
                                if (taskDate.before(todayDate)) {
                                    count = "0";
                                }
                            }

                            taskedOut.setTaskId(taskId);
                            taskedOut.setTaskName(taskname);
                            taskedOut.setScore(score);
                            taskedOut.setTaskType(taskType);
                            taskedOut.setDoneDate(doneDate);

                            if (StringUtils.isNotBlank(count)) {
                                taskedOut.setTaskedCount(Integer.parseInt(count));
                            }
                            break;
                        }
                    }
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error("获取用户执行虚拟币任务的进度",ex);
        }
        return taskedOut;
    }


    /**
     * 新手任务-关注3位Fun友-分值任务和虚拟币任务是否完成关注3位好友
     * @param taskId
     * @param count
     * @return
     */
    private boolean isWatchThreeMember(String taskId, String count) {
        boolean isSuccess = true;
        //分值任务
        if (StringUtils.equalsIgnoreCase("fafad75fea5b49628dcc286ff1b9fd6c", taskId)) {
            if (StringUtils.isBlank(count)) {
                isSuccess = false;
            }
            int i_count = Integer.parseInt(count);
            if (i_count < 3) {
                isSuccess = false;
            }
            //虚拟币任务
        } else if (StringUtils.equalsIgnoreCase("e7a0eeec90244af0b94871c996cf4cbf", taskId)) {
            if (StringUtils.isBlank(count)) {
                isSuccess = false;
            }
            int i_count = Integer.parseInt(count);
            if (i_count < 3) {
                isSuccess = false;
            }
        }
        return isSuccess;
    }


    /**
     * 验证每日任务的完成日期是否是当前日期
     * @param taskDoneDate
     * @return
     */
    private boolean isCurrentDate(String taskDoneDate) {
        boolean isSuccess = true;
        if (StringUtils.isBlank(taskDoneDate)) {
            isSuccess = false;
        }
        String currentDateStr = DateTools.fmtSimpleDateToString(new Date());
        long interval = DateTools.getDaySub(taskDoneDate, currentDateStr);
        //不是当天
        if (0 != interval) {
            isSuccess = false;
        }
        return isSuccess;
    }


    /**
     * 每周任务:
     *    完成日期是否在本周内
     *    有2条游戏评论上热门/安利墙 任务是否完成
     *    有2篇文章上推荐/置顶  任务是否完成
     * @param taskId
     * @param target_ids
     * @param taskDoneDate
     * @return
     */
    private boolean isFinisWithWeekly(String taskId, String target_ids, String taskDoneDate) {
        boolean isSuccess = true;

        //先验证任务完成日期是否在本周
        Date taskDoneDate_d = DateTools.str2Date(taskDoneDate, null);
        boolean thisWeek = DateTools.isThisWeek(taskDoneDate_d);
        if (thisWeek) {

            //分值任务-有2条游戏评论上热门/安利墙
            if (StringUtils.equalsIgnoreCase("704e5284df9c4f0a8a4d8e5350a4e03d", taskId)) {
                if (StringUtils.isBlank(target_ids)) {
                    return false;
                }
                String[] ids = target_ids.split(",");
                isSuccess = ids.length >= 2;
                // 分值任务-有2篇文章上推荐/置顶
            } else if (StringUtils.equalsIgnoreCase("730efc1b39204542aeba600042ca1cf3", taskId)) {
                if (StringUtils.isBlank(target_ids)) {
                    return false;
                }
                String[] ids = target_ids.split(",");
                isSuccess = ids.length >= 2;
            } else
                //虚拟币
                //有2条游戏评论上热门/安利墙
                if (StringUtils.equalsIgnoreCase("4b510082259e4de893b3805dee23e6b2", taskId)) {
                    if (StringUtils.isBlank(target_ids)) {
                        return false;
                    }
                    String[] ids = target_ids.split(",");
                    isSuccess = ids.length >= 2;
                    //有2篇文章上推荐/置顶
                } else if (StringUtils.equalsIgnoreCase("d9f06944418440eeb80397134e21ffbf", taskId)) {
                    if (StringUtils.isBlank(target_ids)) {
                        return false;
                    }
                    String[] ids = target_ids.split(",");
                    isSuccess = ids.length >= 2;
                }
        } else {
            isSuccess = false;
        }
        return isSuccess;
    }


    /**
     * 验证新的版本任务，历史版本是否已经完成
     * @param newTaskId
     * @param taskId_done
     * @return
     */
    private boolean isFinishOldTask(String newTaskId, String taskId_done,String mb_id) {

        //修改头像
        //新版本任务
        if (StringUtils.equalsIgnoreCase(newTaskId, NewTaskIdenum.HEAD_EXP.getKey()) || StringUtils.equalsIgnoreCase(newTaskId, NewTaskIdenum.HEAD_CION.getKey())) {
            if (StringUtils.equalsIgnoreCase(taskId_done, oldTaskIdenum.HEAD_EXP.getKey()) || StringUtils.equalsIgnoreCase(taskId_done, oldTaskIdenum.HEAD_COIN.getKey())) {
                return true;
            }else {
                return queryTaskLog(mb_id,  oldTaskIdenum.HEAD_EXP.getKey());
            }

        } else
        //修改昵称
        if (StringUtils.equalsIgnoreCase(newTaskId, NewTaskIdenum.NICKNAME_EXP.getKey()) || StringUtils.equalsIgnoreCase(newTaskId, NewTaskIdenum.NICKNAME_COIN.getKey())) {
            if (StringUtils.equalsIgnoreCase(taskId_done, oldTaskIdenum.NICKNAME_EXP.getKey()) || StringUtils.equalsIgnoreCase(taskId_done, oldTaskIdenum.NICKNAME_COIN.getKey())) {

                return true;
            }else {
                return queryTaskLog(mb_id, oldTaskIdenum.NICKNAME_EXP.getKey());
            }

        } else
        //绑定QQ
        if (StringUtils.equalsIgnoreCase(newTaskId, NewTaskIdenum.BINDING_QQ_EXP.getKey()) || StringUtils.equalsIgnoreCase(newTaskId, NewTaskIdenum.BINDING_QQ_COIN.getKey())) {
            if (StringUtils.equalsIgnoreCase(taskId_done, oldTaskIdenum.BINDING_EXP.getKey()) || StringUtils.equalsIgnoreCase(taskId_done, oldTaskIdenum.BINDING_COIN.getKey())) {
                return true;
            }else {
                return queryTaskLog(mb_id, oldTaskIdenum.BINDING_EXP.getKey());
            }
        } else
        //绑定微信
        if (StringUtils.equalsIgnoreCase(newTaskId, NewTaskIdenum.BINDING_WX_EXP.getKey()) || StringUtils.equalsIgnoreCase(newTaskId, NewTaskIdenum.BINDING_WX_COIN.getKey())) {
            if (StringUtils.equalsIgnoreCase(taskId_done, oldTaskIdenum.BINDING_EXP.getKey()) || StringUtils.equalsIgnoreCase(taskId_done, oldTaskIdenum.BINDING_COIN.getKey())) {
                return true;
            }else {
                return queryTaskLog(mb_id, oldTaskIdenum.BINDING_EXP.getKey());
            }
        } else
        //绑定微博
        if (StringUtils.equalsIgnoreCase(newTaskId, NewTaskIdenum.BINDING_WB_EXP.getKey()) || StringUtils.equalsIgnoreCase(newTaskId, NewTaskIdenum.BINDING_WB_COIN.getKey())) {
            if (StringUtils.equalsIgnoreCase(taskId_done, oldTaskIdenum.BINDING_EXP.getKey()) || StringUtils.equalsIgnoreCase(taskId_done, oldTaskIdenum.BINDING_COIN.getKey())) {
                return true;
            }else {
                return queryTaskLog(mb_id, oldTaskIdenum.BINDING_EXP.getKey());
            }
        } else
        //修改个人简介
        if (StringUtils.equalsIgnoreCase(newTaskId, NewTaskIdenum.RESUME_EXP.getKey()) || StringUtils.equalsIgnoreCase(newTaskId, NewTaskIdenum.RESUME_COIN.getKey())) {
            if (StringUtils.equalsIgnoreCase(taskId_done, oldTaskIdenum.RESUME_EXP.getKey()) || StringUtils.equalsIgnoreCase(taskId_done, oldTaskIdenum.RESUME_COIN.getKey())) {
                return true;
            }else {
                return queryTaskLog(mb_id, oldTaskIdenum.RESUME_EXP.getKey());
            }
        }
        return false;
    }


    /**
     * 查询用户已经完成的任务日志记录
     * 根据用户id和任务id
     * @param mb_id
     * @param taskId
     * @return
     */
    private boolean queryTaskLog(String mb_id, String taskId) {
        if (StringUtils.isBlank(mb_id) || StringUtils.isBlank(taskId)) {
            return false;
        }
        EntityWrapper<ScoreLog> scoreLogEntityWrapper = new EntityWrapper<>();
        scoreLogEntityWrapper.eq("member_id", mb_id);
        scoreLogEntityWrapper.eq("rule_id", taskId);

        int taskedLogCount = scoreLogService.selectCount(scoreLogEntityWrapper);

        return taskedLogCount > 0;
    }

    //---------
}
