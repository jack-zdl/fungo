package com.fungo.system.service;

/**
 * <p>
 * <p>
 *      主键或者唯一编号生成类
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public interface IPKNoService {



    /**
     * 获取用户id
     * @param mb_id
     * @return
     */
    public String genUniqueMbNo(String mb_id);


}
