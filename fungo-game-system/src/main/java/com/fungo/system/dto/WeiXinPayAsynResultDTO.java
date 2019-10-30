package com.fungo.system.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p> 微信异步支付回调接口入参</p>
 * @Date: 2019/10/28
 */
@Setter
@Getter
@ToString
public class WeiXinPayAsynResultDTO {

    private String appid;

    private String mch_id;

    private String device_info;

    private String nonce_str;

    private String sign;

    private String result_code;

    private String err_code;

    private String err_code_des;

    private String openid;

    private String is_subscribe;

    private String trade_type;

    private String bank_type;

    private String total_fee;

    private String fee_type;

    private String cash_fee;

    private String cash_fee_type;

    private  String coupon_fee;

    private String coupon_count;

    private String coupon_id;

    private String transaction_id;

    private String out_trade_no;

    private String attach;

    private String time_end;
}
