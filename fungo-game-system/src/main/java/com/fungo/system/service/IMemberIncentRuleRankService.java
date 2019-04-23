package com.fungo.system.service;


import com.fungo.system.entity.IncentRuleRank;
import com.game.common.util.exception.BusinessException;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *      用户级别、身份、荣誉规则业务层
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public interface IMemberIncentRuleRankService {


    /**
     * 迁移V2.4.2 会员等级数据 到  t_incent_ranked（用户权益-等级、身份、荣誉已获取汇总表）
     * 旧版本用户全部迁移
     */
    public void transfMemberLevel();

    /**
     * 迁移指定用户
     * 迁移V2.4.2 会员等级数据 到  t_incent_ranked（用户权益-等级、身份、荣誉已获取汇总表）
     */
    public void transfMemberLevel(String mb_id);


    /**
     *迁移V2.4.2 用户历史第三方任务任务值和fungo币数据迁移
     */
    public void transfMemberThird(Map<String, String> authMap);



    /**
     * 获取身份规则数据
     * @param rankGroupId 属于的规则种类ID
     * @param rankType  权益类型
     *                          1 级别
     *                          2 身份
     *                          3 荣誉
     *                          4 特权服务
     * @param rankFlag 身份标识
     *                          1 官方类型
     *                          2 评测师类型
     *                          3 普通类型
     * @param isIncludeChild  是否包含子节点 true包含 false不包含
     * @return
     */
    public  List<Map<String, Object>>  getIncentRule(Long rankGroupId, int rankType, int rankFlag, boolean isIncludeChild) throws BusinessException;



    /**
     * 获取身份规则数据(自定义缓存)
     * @param rankGroupId 属于的规则种类ID
     * @param rankType  权益类型
     *                          1 级别
     *                          2 身份
     *                          3 荣誉
     *                          4 特权服务
     * @param rankFlag 身份标识
     *                          1 官方类型
     *                          2 评测师类型
     *                          3 普通类型
     * @param isIncludeChild  是否包含子节点 true包含 false不包含
     * @return
     */
    public  List<Map<String, Object>>  getIncentRuleForCustomCache(Long rankGroupId, int rankType, int rankFlag, boolean isIncludeChild) throws BusinessException;


    /**
     * 获取等级规则
     * @return
     */
	List<IncentRuleRank> getLevelRankList();


    /**
     * 根据用户级别、身份、荣誉规则的id，获取数据详情
     * @param rank_idt 级别、身份、荣誉规则的规则编码(int型)
     * @return
     */
	public IncentRuleRank getIncentRuleRank(int rank_idt);


}
