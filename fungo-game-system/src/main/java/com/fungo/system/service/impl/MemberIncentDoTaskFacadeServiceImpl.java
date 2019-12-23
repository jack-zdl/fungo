package com.fungo.system.service.impl;


import com.alibaba.fastjson.JSON;

import com.fungo.system.service.*;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.repo.cache.facade.FungoCacheMember;
import com.game.common.repo.cache.facade.FungoCacheTask;
import com.game.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.IdentityHashMap;
import java.util.Map;

@Service(value = "memberIncentDoTaskFacadeServiceImpl")
public class MemberIncentDoTaskFacadeServiceImpl implements IMemberIncentDoTaskFacadeService {

    private static final Logger logger = LoggerFactory.getLogger(MemberIncentDoTaskFacadeServiceImpl.class);

    //新手任务
    @Autowired
    private IMemberIncentNewbieTaskService iMemberIncentNewbieTaskService;

    //每日任务
    @Autowired
    private IMemberIncentEverydayTaskService iMemberIncentEverydayTaskService;

    //每周任务
    @Autowired
    private IMemberIncentWeeklyTaskService iMemberIncentWeeklyTaskService;

    //精品任务
    @Autowired
    private IMemberIncentExcellentTaskService iMemberIncentExcellentTaskService;

    @Autowired
    private FungoCacheMember fungoCacheMember;

    @Autowired
    private FungoCacheTask fungoCacheTask;


    @Override
    public Map<String, Object> exTask(String mb_id, int task_group_flag, int task_type, int type_code_idt) {

        Map<String, Object> resMap = new IdentityHashMap<String, Object>();
        if(StringUtil.isNull(mb_id)){
            logger.error("用户id为null，任务执行失败");
            resMap.put("-1", "服务器非常繁忙，请耐心等一下");
            resMap.put("success", false);
            return resMap;
        }

        try {
            logger.info("执行任务-member:{}---task_group_flag:{}---task_type:{}---type_code_idt:{}", mb_id, task_group_flag, task_type, type_code_idt);
            if (type_code_idt <= 0) {
                resMap.put("-1", "请选择要完成的任务!");
                resMap.put("success", false);
                return resMap;
            }
            switch (task_group_flag) {
                //新手任务，起始编号 1701
                case 1701:
                    resMap = this.iMemberIncentNewbieTaskService.exTask(mb_id, task_group_flag, task_type, type_code_idt);
                    break;


                //每日任务，起始编号 8485
                case 8485:
                    resMap = this.iMemberIncentEverydayTaskService.exTask(mb_id, task_group_flag, task_type, type_code_idt);
                    break;


                //每周任务，起始编号 8706
                case 8706:
                    resMap.put("-1", "每周任务已于 V2.7 版本取消");
                    resMap.put("success", false);
                    //resMap = this.iMemberIncentWeeklyTaskService.exTask(mb_id, task_group_flag, task_type, type_code_idt);
                    break;
                //精品任务，起始编号 9728
                case 9728:
                    resMap = this.iMemberIncentExcellentTaskService.exTask(mb_id, task_group_flag, task_type, type_code_idt);
                    break;
            }

        } catch (Exception ex) {
            logger.error("用户id:{} ,执行任务出现异常", mb_id, ex);
            resMap.put("-1", "服务器非常繁忙，请耐心等一下");
            resMap.put("success", false);
        }
        logger.info("执行任务结果：{}", JSON.toJSONString(resMap));

        //clear redis cache
        // 个人资料
        fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + mb_id, "", null);
        //获取用户fungo比账号获取|消费明细
        fungoCacheTask.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INCENTS_FORTUNE_COIN_POST + mb_id, "", null);
        //更新个人任务
        fungoCacheTask.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_TASK_USER_TASK_PROGRESS + "-" + mb_id, "", null);
        //我的等级信息
        fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_RANKS_LEVEL + mb_id, "", null);
        return resMap;
    }

    @Override
    public Map<String, Object> exTask(String mb_id, int task_group_flag, int task_type, int type_code_idt, String target_id) {
        Map<String, Object> resMap = new IdentityHashMap<String, Object>();

        try {
            logger.info("执行任务-member:{}---task_group_flag:{}---task_type:{}---type_code_idt:{}", mb_id, task_group_flag, task_type, type_code_idt);
            if (type_code_idt <= 0) {
                resMap.put("-1", "请选择要完成的任务!");
                resMap.put("success", false);
                return resMap;
            }
            switch (task_group_flag) {
                //新手任务，起始编号 1701
                case 1701:
                    resMap = this.iMemberIncentNewbieTaskService.exTask(mb_id, task_group_flag, task_type, type_code_idt, target_id);
                    break;


                //每日任务，起始编号 8485
                case 8485:
                    resMap = this.iMemberIncentEverydayTaskService.exTask(mb_id, task_group_flag, task_type, type_code_idt, target_id);
                    break;


                //每周任务，起始编号 8706
                case 8706:
                    resMap = this.iMemberIncentWeeklyTaskService.exTask(mb_id, task_group_flag, task_type, type_code_idt, target_id);
                    break;


                //精品任务，起始编号 9728
                case 9728:
                    resMap = this.iMemberIncentExcellentTaskService.exTask(mb_id, task_group_flag, task_type, type_code_idt, target_id);
                    break;
            }

        } catch (Exception ex) {
            logger.error("用户id:{} ,执行任务出现异常", mb_id, ex);
            resMap.put("-1", "服务器非常繁忙，请耐心等一下");
            resMap.put("success", false);
        }
        logger.info("执行任务结果：{}", JSON.toJSONString(resMap));

        //clear redis cache
        // 个人资料
        fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + mb_id, "", null);
        //获取用户fungo比账号获取|消费明细
        fungoCacheTask.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INCENTS_FORTUNE_COIN_POST + mb_id, "", null);
        //更新个人任务
        fungoCacheTask.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_TASK_USER_TASK_PROGRESS + "-" + mb_id, "", null);
        //我的等级信息
        fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_RANKS_LEVEL + mb_id, "", null);
        return resMap;
    }

    //------
}
