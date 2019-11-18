package com.fungo.system.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * <p>阿里支付异步通知</p>
 * https://docs.open.alipay.com/204/105301/
 * @Date: 2019/10/25
 */
@Setter
@Getter
@ToString
public class ALiPayAsynResultDTO {

    /**
     * 功能描述: 通知时间
     * @date: 2019/10/25 11:45
     */
    private Date notify_time;

    /**
     * 功能描述: 通知类型
     * @date: 2019/10/25 11:45
     */
    private String notify_type;

    /**
     * 功能描述: 通知校验ID
     * @date: 2019/10/25 11:45
     */
    private String notify_id;

    /**
     * 功能描述: 支付宝分配给开发者的应用Id
     * @date: 2019/10/25 11:45
     */
    private String app_id;

    /**
     * 功能描述: 编码格式
     * @date: 2019/10/25 11:45
     */
    private String charset;

    /**
     * 功能描述: 接口版本
     * @date: 2019/10/25 11:45
     */
    private String version;

    /**
     * 功能描述: 签名类型
     * @date: 2019/10/25 11:45
     */
    private String sign_type;

    /**
     * 功能描述: 签名
     * @date: 2019/10/25 11:45
     */
    private String sign;

    /**
     * 功能描述: 支付宝交易号  (交易订单号唯一)
     * @date: 2019/10/25 11:45
     */
    private String trade_no;

    /**
     * 功能描述: 商户订单号
     * @date: 2019/10/25 11:45
     */
    private String out_trade_no;

    /**
     * 功能描述: 商户业务号
     * @date: 2019/10/25 11:45
     */
    private String out_biz_no;

    /**
     * 功能描述: 买家支付宝用户号
     * @date: 2019/10/25 11:45
     */
    private String buyer_id;

    /**
     * 功能描述: 买家支付宝账号
     * @date: 2019/10/25 11:45
     */
    private String buyer_logon_id;

    /**
     * 功能描述: 卖家支付宝用户号
     * @date: 2019/10/25 11:45
     */
    private String seller_id;

    /**
     * 功能描述: 卖家支付宝账号
     * @date: 2019/10/25 11:45
     */
    private String seller_email;

    /**
     * 功能描述: 交易状态
     * @date: 2019/10/25 11:45
     */
    private String trade_status;

    /**
     * 功能描述: 订单金额
     * @date: 2019/10/25 11:45
     */
    private double total_amount;

    /**
     * 功能描述: 实收金额
     * @date: 2019/10/25 11:45
     */
    private double receipt_amount;

    /**
     * 功能描述: 开票金额
     * @date: 2019/10/25 11:45
     */
    private double invoice_amount;

    /**
     * 功能描述: 付款金额
     * @date: 2019/10/25 11:45
     */
    private double buyer_pay_amount;

    /**
     * 功能描述: 集分宝金额
     * @date: 2019/10/25 11:45
     */
    private double point_amount;

    /**
     * 功能描述: 总退款金额
     * @date: 2019/10/25 11:45
     */
    private double refund_fee;

    /**
     * 功能描述: 订单标题
     * @date: 2019/10/25 11:45
     */
    private String subject;

    /**
     * 功能描述: 商品描述
     * @date: 2019/10/25 11:45
     */
    private String body;

    /**
     * 功能描述: 交易创建时间
     * @date: 2019/10/25 11:45
     */
    private Date gmt_create;

    /**
     * 功能描述: 交易付款时间
     * @date: 2019/10/25 11:45
     */
    private Date gmt_payment;

    /**
     * 功能描述: 交易退款时间
     * @date: 2019/10/25 11:45
     */
    private Date gmt_refund;

    /**
     * 功能描述: 交易结束时间
     * @date: 2019/10/25 11:45
     */
    private Date gmt_close;

    /**
     * 功能描述: 支付金额信息
     * @date: 2019/10/25 11:45
     */
    private String fund_bill_list;

    /**
     * 功能描述: 回传参数
     * @date: 2019/10/25 11:45
     */
    private String passback_params;

    /**
     * 功能描述: 优惠券信息
     * @date: 2019/10/25 11:45
     */
    private String voucher_detail_list;
}

