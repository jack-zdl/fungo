package com.fungo.system.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *      登录绑定第三方账号出参数据封装
 * </p>
 *
 * @author mxf
 * @since 2018-12-03
 */
@Setter
@Getter
@ToString
public class MemberSNSBindOutput implements Serializable {

    /**
     * 绑定状态
     * -1 默认状态无业务意义
     *  1 绑定成功
     *  2 该SNS已被注册为fungo账号， 是否注销原fungo账号
     *  3 该SNS已被其他手机号认证成功的fungo账号绑定，是否解绑原fungo账号，重新绑定
     */
    private Integer bindState = -1;

    /**
     * 平台类型
     * -1  默认状态无业务意义
     * 0 微博
     * 1 微信
     * 4 QQ
     */
    private Integer snsType = -1;

    /**
     * 当bindState=3时的，手机号(多个手机号用,分隔)
     */
    private String phones;

//---
}
