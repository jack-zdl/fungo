package com.fungo.system.service;

import com.fungo.system.dto.MemberSNSBindInput;
import com.fungo.system.dto.MemberSNSBindOutput;
import com.game.common.dto.ResultDto;

/**
 * <p>
 *      用户第三方SNS业务层
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public interface IMemberSNSService {


    /**
     *绑定第三方SNS平台账号
     * @param mb_id 登录用户id
     * @param bindInput 入参
     * @return
     */
    public ResultDto<MemberSNSBindOutput> bindThirdSNSWithLogged(String mb_id, MemberSNSBindInput bindInput) throws Exception;


}
