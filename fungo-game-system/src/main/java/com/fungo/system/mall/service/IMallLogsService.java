package com.fungo.system.mall.service;


import com.game.common.dto.mall.MallLogsDto;

/**
 * <p>
 *  商城日志服务实现类
 * </p>
 *
 * @author mxf
 * @since 2019-01-18
 */
public interface IMallLogsService {


    /**
     * 记录商城的访问日志
     * @param logsDto
     */
    public void addMallLog(MallLogsDto logsDto);


}
