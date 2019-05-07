package com.fungo.system.service;


import com.game.common.util.exception.BusinessException;

import java.util.Map;

/**
 * <p>
 * <p>
 *      用户权益-身份数据业务层
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public interface IMemberIncentDignityService {


    /**
     *   获取用户身份数据
     * @param memberId 会员ID
     * @return
     * @throws BusinessException
     */
    public  Map<String, Object> getMemberIncentDignity(String memberId) throws BusinessException;

}
