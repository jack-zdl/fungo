package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.fungo.system.entity.*;
import com.fungo.system.function.UserTaskFilterService;
import com.fungo.system.service.*;
import com.game.common.consts.FunGoGameConsts;
import com.game.common.dto.ResultDto;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import com.game.common.enums.NewTaskStatusEnum;
import com.game.common.enums.oldTaskIdenum;
import com.game.common.util.CommonUtil;
import com.game.common.util.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class MemberIncentRiskServiceImpl implements IMemberIncentRiskService {

    private static final Logger logger = LoggerFactory.getLogger(MemberIncentRiskServiceImpl.class);

    //权益规则与功能授权关系Dao层业务
    @Autowired
    private IMemberIncentPermRankedDaoService iMemberIncentPermRankedDaoService;
    @Autowired
    private ScoreGroupService scoreGroupService;
    @Autowired
    private ScoreRuleService scoreRuleService;
    @Autowired
    private IncentTaskedService incentTaskedService;
    @Autowired
    private ScoreLogService scoreLogService;
    @Autowired
    private UserTaskFilterService userTaskFilterService;

    @Override
    public boolean isMatchLevel(String rank_id, String task_id) throws BusinessException {

        try {

            if (StringUtils.isBlank(rank_id) || StringUtils.isBlank(task_id)) {
                throw new BusinessException("-1", "参数不能为空");
            }

            Wrapper<IncentMbPermRanked> wrapper = new EntityWrapper<IncentMbPermRanked>().eq("rank_id", rank_id).eq("task_id", task_id).eq("rank_type", 1);
            int count = iMemberIncentPermRankedDaoService.selectCount(wrapper);
            if (count > 0) {
                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BusinessException("-1", "用户等级风控业务模块异常");
        }

        return false;
    }

    //-----------

    @Override
    public boolean isMatchLevel(String rank_id, int fun_idt) throws BusinessException {
        try {
//            if (StringUtils.isBlank(rank_id) || StringUtils.isBlank(task_id)) {
//                throw new BusinessException("-1", "参数不能为空");
//            }
            Wrapper<IncentMbPermRanked> wrapper = new EntityWrapper<IncentMbPermRanked>().eq("rank_id", rank_id).eq("fun_idt", fun_idt).eq("rank_type", 1);
            IncentMbPermRanked permRanked = iMemberIncentPermRankedDaoService.selectOne(wrapper);
            if (permRanked == null) {
                return true;
            } else {
                int level = Integer.parseInt(rank_id);
                if (level >= Integer.parseInt(permRanked.getRankId())) {
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BusinessException("-1", "用户等级风控业务模块异常");
        }
        return false;
    }

    @Override
    public ResultDto<Map<String, Object>> checkeUnfinshedNoviceTask(String userId, String os) {
        ResultDto<Map<String, Object>> re = new ResultDto<Map<String, Object>>();
        /*
          11 任务 获取经验值
          23 任务 获取fungo币
          分享文章 | 分享游戏任务要给出收益提示
         */
        userTaskFilterService.updateUserTask( userId);
        //用户是否存在
        //查出全部新手任务
        //V2.4.6 新手任务flag 1701
        ScoreGroup scoreGroup = scoreGroupService.selectOne(new EntityWrapper<ScoreGroup>().eq("task_flag", FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code()).eq("is_active", 1));
        if (scoreGroup == null) {
            return ResultDto.error("-1", "找不到任务");
        }

        if (scoreGroup.getIsActive() == 0) {
            return ResultDto.error("-1", "任务未启用");
        }


        //统一查找经验值任务
        Wrapper<ScoreRule> wrapper = new EntityWrapper<ScoreRule>().eq("is_active", 1).eq("group_id", scoreGroup.getId()).and("pls_task_id is null");

        List<ScoreRule> noviceTaskList = scoreRuleService.selectList(wrapper);

        if (noviceTaskList.size() == 0) {
            return ResultDto.error("-1", "找不到任务");
        }

        int unfinishedCount = 1;
        //新手任务种类
//        int unfinishedCount = noviceTaskList.size();

        //查出用户任务列表

        Map<String, Object> map = new HashMap<>();


        //查询用户已经完成的分值类任务
        IncentTasked tasked = getIncentTaskedExp(userId);
        String ext2 = !CommonUtil.isNull(tasked.getExt2()) ? tasked.getExt2() : "0";
        Integer status = Integer.valueOf(ext2);
        if( ( (status & Integer.valueOf( NewTaskStatusEnum.JOINOFFICIALCIRLCE_EXP.getKey() )) == Integer.valueOf( NewTaskStatusEnum.JOINOFFICIALCIRLCE_EXP.getKey() ))
            && ( (status & Integer.valueOf( NewTaskStatusEnum.JOINOFFICIALCIRLCE_EXP.getKey() )) == Integer.valueOf( NewTaskStatusEnum.FOLLOWOFFICIALUSER_EXP.getKey() ))
            && ( (status & Integer.valueOf( NewTaskStatusEnum.JOINOFFICIALCIRLCE_EXP.getKey() )) == Integer.valueOf( NewTaskStatusEnum.EDITUSER_EXP.getKey() ))
            && ( (status & Integer.valueOf( NewTaskStatusEnum.JOINOFFICIALCIRLCE_EXP.getKey() )) == Integer.valueOf( NewTaskStatusEnum.BROWSESHOP_EXP.getKey() ))
        ){
            unfinishedCount = 0;
        }

        //fix:分值任务-11,fungo币任务-23, 签到-22
        //若分值类没有则，获取 22类型的任务数据
//        IncentTasked tasked22 = getIncentTasked22(userId);


//        //分值类
//        if (null != tasked) {
//            unfinishedCount = getUnfinishedNewbieTaskCount(noviceTaskList, unfinishedCount, tasked);
//        }
//
//        //22 分值和签到合一类
//        if (null != tasked22) {
//            unfinishedCount = getUnfinishedNewbieTaskCount(noviceTaskList, unfinishedCount, tasked22);
//        }
//        //第三步 去掉 V2.4.6版本之前的新手任务
//        int oldNewabTaskCount = this.queryTaskLog(userId);
//
//        unfinishedCount -= oldNewabTaskCount;

        if (unfinishedCount <= 0) {
            map.put("hasNotiveTask", false);
        } else {
            map.put("hasNotiveTask", true);
        }
        map.put("unfinishedCount", unfinishedCount);


        re.setData(map);

        return re;
    }

    /**
     * 获取未完成的新手任务
     * @param noviceTaskList
     * @param unfinishedCount
     * @param tasked
     * @return
     */
    private int getUnfinishedNewbieTaskCount(List<ScoreRule> noviceTaskList, int unfinishedCount, IncentTasked tasked) {
        List<Map> taskList = JSON.parseArray(tasked.getTaskIdtIds(), Map.class);

        //有无未完成的新手任务
        //任务有没有达成最大次数
        if (null != taskList && !taskList.isEmpty()) {

            for (Map<String, Object> m : taskList) {

                for (ScoreRule rule : noviceTaskList) {

                    if (rule.getId().equals((String) m.get("1"))) {
                        if ((int) m.get("5") >= rule.getMax()) {

                            unfinishedCount = unfinishedCount - 1;
                        }
                    }
                }

            }
        }
        return unfinishedCount;
    }


    /**
     * 获取用户分值类完成任务
     * @param userId
     * @return
     */
    private IncentTasked getIncentTaskedExp(String userId) {
        IncentTasked incentTasked = null;
        Wrapper wrapperRuleTaskScore = new EntityWrapper<IncentTasked>();
        Map<String, Object> criteriaMapIncentTaskedScore = new HashMap<String, Object>();
        criteriaMapIncentTaskedScore.put("mb_id", userId);
        criteriaMapIncentTaskedScore.put("task_type", FunGoGameConsts.TASK_RULE_TASK_TYPE_SCORE);
        wrapperRuleTaskScore.allEq(criteriaMapIncentTaskedScore);
        wrapperRuleTaskScore.orderBy("updated_at", false);

        List<IncentTasked> incentTaskedListScore = incentTaskedService.selectList(wrapperRuleTaskScore);
        if (null != incentTaskedListScore && !incentTaskedListScore.isEmpty()) {
            incentTasked = incentTaskedListScore.get(0);
        }
        return incentTasked;
    }


    /**
     * 获取用户22 类型完成任务
     * @param userId
     * @return
     */
    private IncentTasked getIncentTasked22(String userId) {
        IncentTasked incentTasked = null;
        Map<String, Object> criteriaMapIncentTaskedScore = new HashMap<String, Object>();
        criteriaMapIncentTaskedScore.put("task_type", FunGoGameConsts.TASK_RULE_TASK_TYPE_COIN_SIGN_IN);

        Wrapper wrapperRuleTaskScore = new EntityWrapper<IncentTasked>();
        wrapperRuleTaskScore.allEq(criteriaMapIncentTaskedScore);
        wrapperRuleTaskScore.orderBy("updated_at", false);

        List<IncentTasked> incentTaskedListScore = incentTaskedService.selectList(wrapperRuleTaskScore);

        if (null != incentTaskedListScore && !incentTaskedListScore.isEmpty()) {
            incentTasked = incentTaskedListScore.get(0);
        }
        return incentTasked;
    }


    /**
     * 查询用户已经完成的任务日志记录
     * 根据用户id和任务id
     * @param mb_id
     * @return
     */
    private int queryTaskLog(String mb_id) {

        if (StringUtils.isBlank(mb_id)) {
            return 0;
        }

        List<String> oldNewableTaskId = new ArrayList<>();

        oldNewableTaskId.add(oldTaskIdenum.HEAD_EXP.getKey());
        oldNewableTaskId.add(oldTaskIdenum.NICKNAME_EXP.getKey());
        oldNewableTaskId.add(oldTaskIdenum.RESUME_EXP.getKey());

        EntityWrapper<ScoreLog> scoreLogEntityWrapper = new EntityWrapper<>();
        scoreLogEntityWrapper.eq("member_id", mb_id);
        scoreLogEntityWrapper.in("rule_id", oldNewableTaskId);

        //-----------------------------------------------------------------------
        //查看历史 修改头像 、 修改昵称、修改个人简介任务
        int taskedLogCount = scoreLogService.selectCount(scoreLogEntityWrapper);

        EntityWrapper<ScoreLog> scoreLogEntityWrapperBind = new EntityWrapper<>();
        scoreLogEntityWrapperBind.eq("member_id", mb_id);
        scoreLogEntityWrapperBind.eq("rule_id", oldTaskIdenum.BINDING_EXP.getKey());

        //V2.4.3新手任务 绑定QQ/微信/微博 一个任务项 在V2.4.6中拆分了 绑定QQ  、 微信 、微博 等3个任务项
        int taskedLogCountBind = scoreLogService.selectCount(scoreLogEntityWrapperBind);
        if (taskedLogCountBind > 0){
            taskedLogCountBind = 3;
        }
        return taskedLogCount + taskedLogCountBind;
    }


}
