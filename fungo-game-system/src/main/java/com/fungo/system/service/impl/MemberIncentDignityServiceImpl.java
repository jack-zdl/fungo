package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.fungo.system.dto.IncentRuleRankOut;
import com.fungo.system.entity.IncentRanked;
import com.fungo.system.entity.IncentRuleRank;
import com.fungo.system.service.IMemberIncentDignityService;
import com.fungo.system.service.IMemberIncentRankedDaoService;
import com.fungo.system.service.IMemberIncentRuleRankService;
import com.game.common.consts.FunGoGameConsts;
import com.game.common.util.date.DateTools;
import com.game.common.util.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberIncentDignityServiceImpl implements IMemberIncentDignityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberIncentDignityServiceImpl.class);


    @Autowired
    private IMemberIncentRankedDaoService iMemberIncentRankedDaoService;


    @Autowired
    private IMemberIncentRuleRankService iMemberIncentRuleRankService;


    @Cacheable(cacheNames = {FunGoGameConsts.CACHE_EH_NAME } ,key = "'" + FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER +"_IncentDignity' + #memberId")
    @Override
    public Map<String, Object> getMemberIncentDignity(String memberId) throws BusinessException {

        Map<String, Object> dignityMap = null;

        try {

            LOGGER.info("查询用户身份数据-memberId:{}", memberId);

            if (StringUtils.isBlank(memberId)) {
                return null;
            }

            //获取某个会员的最高身份成就数据
            //封装查询规则的条件
            Wrapper wrapperCriteria = new EntityWrapper<IncentRuleRank>();

            Map<String, Object> criteriaMap = new HashMap<String, Object>();
            criteriaMap.put("mb_id", memberId);
            criteriaMap.put("rank_type", FunGoGameConsts.INCENT_RULE_RANK_TYPE_DIGNITY);
            wrapperCriteria.allEq(criteriaMap);
            IncentRanked incentRanked = iMemberIncentRankedDaoService.selectOne(wrapperCriteria);


            //获取所有身份规则数据
            Long rankGroupId = 0L;
            int rankType = FunGoGameConsts.INCENT_RULE_RANK_FLAG_HONORS_EVALUATIONER;
            int rankFlag = FunGoGameConsts.INCENT_RULE_RANK_FLAG_HONORS_NULL;
            List<Map<String, Object>> allRuleRankList = iMemberIncentRuleRankService.getIncentRule(rankGroupId, rankType, rankFlag, true);


            if (null != incentRanked) {

                dignityMap = new HashMap<String, Object>();

                Long  currentRankId =  incentRanked.getCurrentRankId();
                String currentRankName = incentRanked.getCurrentRankName();
                String createDate = "";
                if (null != incentRanked.getCreatedAt() ) {
                    createDate =  DateTools.fmtDate(incentRanked.getCreatedAt());
                }

                dignityMap.put("memberId" , memberId);
                dignityMap.put("dignityId" , currentRankId);
                dignityMap.put("dignityName" , currentRankName);
                dignityMap.put("accreditDate" , createDate);

                //封装当前身份数据
                //遍历身份分类集合
               rankCateMap: for (Map<String, Object> rankCategoryMap : allRuleRankList) {

                     //身份分类下身份数据集合
                     List<IncentRuleRankOut> rankList  = (List<IncentRuleRankOut>) rankCategoryMap.get("rankRuleData");

                     if (null != rankList && !rankList.isEmpty()) {

                        //具体集合规则数据
                         for (IncentRuleRankOut ruleRank : rankList) {

                            //是否是当前身份数据
                             String rankIdStr = ruleRank.getDignityId();
                             Long rankId = Long.parseLong(rankIdStr);

                             if (currentRankId.longValue() == rankId.longValue() ) {

                                 dignityMap.put("image" , ruleRank.getImage());
                                 dignityMap.put("rankIntro" , ruleRank.getRankIntro());
                                 dignityMap.put("rankType" , ruleRank.getRankType());
                                 dignityMap.put("rankFlag" , ruleRank.getRankFlag());

                                 break rankCateMap;
                             }
                         }
                     }
                }

                //历史身份ids
                //String rankIdtIds = incentRanked.getRankIdtIds();
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        return dignityMap;
    }

    //-------
}
