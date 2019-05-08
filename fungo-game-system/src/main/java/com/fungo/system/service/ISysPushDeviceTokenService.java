package com.fungo.system.service;

import com.game.common.dto.GameSysPushDeviceTokenInput;
import com.game.common.util.exception.BusinessException;

/**
 * <p>
 *     消息推送目标设备token处理业务层
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public interface ISysPushDeviceTokenService {


    /**
     *  消息推送目标设备token数据处理
     * @param gameSysPushDeviceTokenInput
     * @return
     */
    public boolean addDeviceToken(GameSysPushDeviceTokenInput gameSysPushDeviceTokenInput) throws BusinessException;
}
