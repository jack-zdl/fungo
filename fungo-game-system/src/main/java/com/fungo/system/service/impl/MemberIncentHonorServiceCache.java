package com.fungo.system.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.fungo.system.dto.IncentRuleRankOut;
import com.fungo.system.entity.IncentRanked;
import com.fungo.system.entity.IncentRuleRank;
import com.fungo.system.service.IMemberIncentRankedDaoService;
import com.game.common.consts.FunGoGameConsts;
import com.game.common.util.FunGoEHCacheUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * <p>
 *      用户权益-荣誉数据业务层
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@Component
public class MemberIncentHonorServiceCache {


    @Autowired
    private IMemberIncentRankedDaoService iMemberIncentRankedDaoService;


    public List<Map<String, Object>> getMemberIncentHonorWithCache(String memberId, List<Map<String, Object>> incentRuleList) {


        List<Map<String, Object>> honorRuleMapListResult = new ArrayList<Map<String, Object>>();

        String cacheKey = FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER + "_IncentHonor_" + memberId;

        try {

            Object cacheObj = FunGoEHCacheUtils.get(FunGoGameConsts.CACHE_EH_NAME, cacheKey);
            if (null != cacheObj) {
                List list = JSONObject.parseObject(String.valueOf(cacheObj), List.class);
                return list;
            }


            honorRuleMapListResult.addAll(incentRuleList);
            incentRuleList.clear();

            //获取某个会员的荣誉成就数据
            //封装查询规则的条件
            Wrapper wrapperCriteria = new EntityWrapper<IncentRuleRank>();

            Map<String, Object> criteriaMap = new HashMap<String, Object>();
            criteriaMap.put("mb_id", memberId);
            criteriaMap.put("rank_type", FunGoGameConsts.INCENT_RULE_RANK_TYPE_HONORS);
            wrapperCriteria.allEq(criteriaMap);

            IncentRanked incentRanked = iMemberIncentRankedDaoService.selectOne(wrapperCriteria);


            //遍历所有分类下荣誉规则数据
            for (Map<String, Object> HonorRuleMap : honorRuleMapListResult) {

                //某个分类荣誉数据集合
                List rankRuleDataList = (List) HonorRuleMap.get("rankRuleData");

                if (null != rankRuleDataList && !rankRuleDataList.isEmpty()) {

                    for (Object rankRule : rankRuleDataList) {

                        IncentRuleRankOut ruleRankOut = new IncentRuleRankOut();
                        JSONObject rankRuleJsonObject = null;

                        //避免对象转换出现异常
                        if (rankRule instanceof IncentRuleRankOut) {

                            ruleRankOut = (IncentRuleRankOut) rankRule;

                        } else {

                            rankRuleJsonObject = (JSONObject) rankRule;

                            ruleRankOut.setDignityId(String.valueOf(rankRuleJsonObject.get("dignityId")));
                            ruleRankOut.setDignityName(String.valueOf(rankRuleJsonObject.get("dsignityName")));
                            ruleRankOut.setImage(String.valueOf(rankRuleJsonObject.get("image")));
                            Object stateObj = rankRuleJsonObject.get("state");
                            if (null != stateObj) {
                                ruleRankOut.setState(Integer.parseInt(String.valueOf(stateObj)));
                            }
                            ruleRankOut.setRankIntro(String.valueOf(rankRuleJsonObject.get("rankIntro")));

                            Object sortObj = rankRuleJsonObject.get("sort");
                            if (null != sortObj) {
                                ruleRankOut.setSort(Integer.parseInt(String.valueOf(sortObj)));
                            }

                            Object grantRateObj = rankRuleJsonObject.get("grantRate");
                            if (null != grantRateObj) {
                                ruleRankOut.setGrantRate(Float.parseFloat(String.valueOf(grantRateObj)));
                            }
                            ruleRankOut.setRankGroupId(String.valueOf(rankRuleJsonObject.get("rankGroupId")));

                            Object rankFlagObj = rankRuleJsonObject.get("rankFlag");
                            if (null != rankFlagObj) {
                                ruleRankOut.setRankFlag(Integer.parseInt(String.valueOf(rankFlagObj)));
                            }

                            Object rankTypeObj = rankRuleJsonObject.get(" rankType");
                            if (null != rankTypeObj) {
                                ruleRankOut.setRankType(Integer.parseInt(String.valueOf(rankTypeObj)));
                            }
                        }

                        String dignityId = ruleRankOut.getDignityId();
                        //String rankType_For = String.valueOf(ruleRankOut.getRankType().intValue());

                        if (null != incentRanked) {

                            // 数据示例：[{1:rankId,2:rankName,3:type,4:accreditDate} ]
                            // (荣誉id,荣誉名称，荣誉类型，获取荣誉时间)
                            String rankIdtIdsJson = incentRanked.getRankIdtIds();

                            //从历史获取的荣誉中，获取待查找荣誉id的进度
                            if (StringUtils.isNotBlank(rankIdtIdsJson)) {

                                JSONArray jsonArray = JSONObject.parseArray(rankIdtIdsJson);
                                if (null != jsonArray && !jsonArray.isEmpty()) {

                                    for (int i = 0; i < jsonArray.size(); i++) {

                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String rankId_comp = object.getString("1");
                                        if (StringUtils.equalsIgnoreCase(dignityId, rankId_comp)) {

                                            String rankName_comp = object.getString("2");
                                            String type_comp = object.getString("3");
                                            String accreditDate_comp = object.getString("4");

                                            Map<String, Object> honorCompMap = new HashMap<>();

                                            honorCompMap.put("dignityId", rankId_comp);
                                            honorCompMap.put("dignityName", rankName_comp);
                                            honorCompMap.put("rankType", type_comp);
                                            honorCompMap.put("accreditDate", accreditDate_comp);

                                            if (null != rankRuleJsonObject) {
                                                rankRuleJsonObject.put("accreditHonor", honorCompMap);

                                            } else {
                                                ruleRankOut.setAccreditHonor(honorCompMap);
                                            }

                                            break;
                                        }
                                    }

                                }
                            }
                        }
                    }

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //把数据缓存
        FunGoEHCacheUtils.put(FunGoGameConsts.CACHE_EH_NAME, cacheKey, JSONObject.toJSONString(honorRuleMapListResult));

        return honorRuleMapListResult;
    }


}
