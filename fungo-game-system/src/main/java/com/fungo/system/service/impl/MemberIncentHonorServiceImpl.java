package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.fungo.system.service.IMemberIncentHonorService;
import com.fungo.system.service.IMemberIncentRuleRankService;
import com.game.common.consts.FunGoGameConsts;
import com.game.common.util.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MemberIncentHonorServiceImpl implements IMemberIncentHonorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberIncentHonorServiceImpl.class);

    @Autowired
    private IMemberIncentRuleRankService iMemberIncentRuleRankService;

    @Autowired
    private MemberIncentHonorServiceCache memberIncentHonorServiceCache;


    @Override
    public List<Map<String, Object>> getMemberIncentHonor(String memberId) throws BusinessException {

        LOGGER.info("查询用户荣誉数据-memberId:{}", memberId);

        if (StringUtils.isBlank(memberId)) {
            return null;
        }

        List<Map<String, Object>> incentRuleList = this.getIncentRuleCache();
        if (null == incentRuleList || incentRuleList.isEmpty()) {
            return null;
        }

        LOGGER.info("获取荣誉规则数据-honorRuleMapList:{}", JSON.toJSONString(incentRuleList));

        List<Map<String, Object>> honorRuleMapList = memberIncentHonorServiceCache.getMemberIncentHonorWithCache(memberId, incentRuleList);

        return honorRuleMapList;
    }


    /**
     * 获取荣誉规则数据
     * @return
     */
    private List<Map<String, Object>> getIncentRuleCache() {

        //获取荣誉规则
        Long rankGroupId = 0L;
        int rankType = FunGoGameConsts.INCENT_RULE_RANK_TYPE_HONORS;
        int rankFlag = FunGoGameConsts.INCENT_RULE_RANK_FLAG_HONORS_NULL;

        //获取荣誉规则
        List<Map<String, Object>> incentRuleList = iMemberIncentRuleRankService.getIncentRuleForCustomCache(rankGroupId, rankType, rankFlag, true);

        return incentRuleList;
    }


    //-----
}
