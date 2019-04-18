package com.fungo.system.service;


import com.fungo.api.ResultDto;
import com.fungo.framework.exceptions.BusinessException;

import java.util.Map;

/**
 * <p>
 *    用户成长体系之
 *                  签到任务业务:
 *                      1.V2.4.6版本规则:
 *                       签到天数	获取Fun币	备注
 *                         1	    10	        第一次签及轮询30天后签到及断签 每天加值
 *                         2	    30	        连续签到第2天 每天加值
 *                         3～6	    10	        连续签到第3～6天 每天加值
 *                         7	    50	        连续签到第7天 每天加值
 *                         8～13	    20	        连续签到第8～13天 每天加值
 *                         14	    50	        连续签到第14天 每天加值
 *                         15～20	30	        连续签到第15～20天 每天加值
 *                         21   	50	        连续签到21天 每天加值
 *                         22～27	30	        连续签到第22～27天 每天加值
 *                         28	    50	        连续签到第28天 每天加值
 *                         29	    30	        连续签到第29天每天加值
 *                         30	    150	        连续签到第30天 每天加值
 * </p>
 *
 * @author mxf
 * @since 2019-03-23
 */
public interface IMemberIncentSignInTaskService {


    /**
     * 用户执行签到
     * @param mb_id 登录用ID
     * @return
     */
    public ResultDto exSignIn(String mb_id) throws BusinessException;


    /**
     * 用户获取签到信息
     * @param mb_id
     * @return
     * @throws BusinessException
     */
    public ResultDto<Map<String, Object>> getMemberSignInInfo(String mb_id) throws BusinessException;

}
