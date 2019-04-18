package com.fungo.system.service;


import com.fungo.system.dto.LogCollectInput;

import java.util.Map;

/**
 * <p>
 *      app和web系统运行 | 用户操作日志收集业务层
 *      绑定
 *      解绑
 * </p>
 * since:V2.4.6
 * @author mxf
 * @since 2018-12-04
 */
public interface ISystemLogCollectService {


    /**
     * 收集用户操作数据
     * @param collectInput
     * @return
     */
    public Map<String,Object> collectLog(LogCollectInput collectInput) ;

}
