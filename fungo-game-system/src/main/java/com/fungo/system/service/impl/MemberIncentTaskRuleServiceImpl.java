package com.fungo.system.service.impl;



import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.entity.ScoreGroup;
import com.fungo.system.entity.ScoreRule;
import com.fungo.system.service.IMemberIncentTaskRuleService;
import com.fungo.system.service.ScoreGroupService;
import com.fungo.system.service.ScoreRuleService;
import com.game.common.repo.cache.facade.FungoCacheTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberIncentTaskRuleServiceImpl implements IMemberIncentTaskRuleService {

    private static final Logger logger = LoggerFactory.getLogger(MemberIncentTaskRuleServiceImpl.class);

    @Autowired
    private ScoreGroupService scoreGroupService;

    @Autowired
    private ScoreRuleService scoreRuleService;

    @Autowired
    private FungoCacheTask fungoCacheTask;


    @Override
    public List<ScoreGroup> getScoreGroups() {
        List<ScoreGroup> scoreGroupList = null;
        try {
            String keyPrefix = "ScoreGroupsV2.4.6_Cloud";

            scoreGroupList = (List<ScoreGroup>) fungoCacheTask.getIndexCache(keyPrefix, "");
            if (null != scoreGroupList && !scoreGroupList.isEmpty()) {
                return scoreGroupList;
            }

            EntityWrapper<ScoreGroup> entityWrapper = new EntityWrapper<ScoreGroup>();
            entityWrapper.eq("task_type", 3);
            entityWrapper.eq("is_active", 1);

            scoreGroupList = scoreGroupService.selectList(entityWrapper);
            if (null != scoreGroupList && !scoreGroupList.isEmpty()) {
                fungoCacheTask.excIndexCache(true, keyPrefix, "", scoreGroupList, FungoCacheTask.REDIS_EXPIRE_3_MONTHS);
            }

        } catch (Exception ex) {
            logger.error("获取当前有效的任务组出现异常", ex);
        }
        return scoreGroupList;
    }


    @Override
    public List<ScoreRule> getScoreRules(String[] taskGroupIds) {

        List<ScoreRule> scoreRuleList = null;
        try {
            String keyPrefix = "ScoreRuleListV2.4.6_Cloud";

           // Object indexCache = fungoCacheTask.getIndexCache(keyPrefix, "");


            scoreRuleList = (List<ScoreRule>) fungoCacheTask.getIndexCache(keyPrefix, "");
            if (null != scoreRuleList && !scoreRuleList.isEmpty()) {
                return scoreRuleList;
            }

            //获取任务分组ids
            ArrayList<String> groupIdList = new ArrayList<String>();
            if (null == taskGroupIds || taskGroupIds.length == 0) {
                List<ScoreGroup> scoreGroupList = this.getScoreGroups();
                for (ScoreGroup scoreGroup : scoreGroupList) {
                    groupIdList.add(scoreGroup.getId());
                }
            }

            EntityWrapper<ScoreRule> entityWrapper = new EntityWrapper<ScoreRule>();
            entityWrapper.in("group_id", groupIdList.toArray());
            entityWrapper.eq("is_active", 1);

            scoreRuleList = scoreRuleService.selectList(entityWrapper);
            if (null != scoreRuleList && !scoreRuleList.isEmpty()) {
                fungoCacheTask.excIndexCache(true, keyPrefix, "", scoreRuleList, FungoCacheTask.REDIS_EXPIRE_3_MONTHS);
            }

        } catch (Exception ex) {
            logger.error("获取当前有效的任务组下的所有任务规则数据", ex);
        }
        return scoreRuleList;
    }


    @Override
    public ScoreGroup getScoreGroups(int task_flag) {
        List<ScoreGroup> scoreGroupList = this.getScoreGroups();
        for (ScoreGroup scoreGroup : scoreGroupList) {
            if (scoreGroup.getTaskFlag() == task_flag) {
                return scoreGroup;
            }
        }
        return null;
    }


    @Override
    public ScoreRule getScoreRule(int code_idt) {
        List<ScoreRule> scoreRuleList = this.getScoreRules(null);
        if(scoreRuleList!=null&&!scoreRuleList.isEmpty()){
            for (ScoreRule scoreRule : scoreRuleList) {
                if (code_idt == scoreRule.getCodeIdt()) {
                    return scoreRule;
                }
            }
        }
        return null;
    }


    //------------
}
