package com.fungo.system.service;


import com.game.common.util.exception.BusinessException;

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
public interface IMemberIncentHonorService {


    /**
     *   获取用户荣誉数据
     * @param memberId 会员ID
     * @return
     * @throws BusinessException
     */
    public List<Map<String, Object>> getMemberIncentHonor(String memberId) throws BusinessException;


}
