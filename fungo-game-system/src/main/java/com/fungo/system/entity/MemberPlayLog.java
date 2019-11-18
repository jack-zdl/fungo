package com.fungo.system.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("t_member_play_log")
public class MemberPlayLog extends Model<MemberPlayLog> {

    @TableId(value="id",type = IdType.UUID)
    private String id;

    @TableField("member_phone")
    private String memberPhone;

    @TableField("play_id")
    private String playId;

    @TableField("business_id")
    private String businessId;

    @TableField("coupon_type")
    private String couponType;

    @TableField("coupon_id")
    private String couponId;

    @TableField("pay_type")
    private String payType;

    @TableField("pay_state")
    private String payState;

    /**
     * 功能描述: 支付宝分配给开发者的应用Id
     * @date: 2019/10/25 13:57
     */
    @TableField("app_id")
    private String appId;

//    /**
//     * 功能描述: 支付宝交易号
//     * @date: 2019/10/25 13:57
//     */
//    @TableField("trade_no")
//    private String tradeNo;
//
//    /**
//     * 功能描述: 商户订单号
//     * @date: 2019/10/25 13:57
//     */
//    @TableField("out_trade_no")
//    private String outTradeNo;
    /**
     * 功能描述: 商户业务号
     * @date: 2019/10/25 13:57
     */
    @TableField("out_biz_no")
    private String outBizNo;

//    /**
//     * 功能描述: 买家支付宝用户号
//     * @date: 2019/10/25 13:57
//     */
//    @TableField("buyer_id")
//    private String buyerId;

    /**
     * 功能描述: 买家支付宝账号
     * @date: 2019/10/25 13:57
     */
    @TableField("buyer_logon_id")
    private String buyerLogonId;

//    /**
//     * 功能描述: 卖家支付宝用户号
//     * @date: 2019/10/25 13:57
//     */
//    @TableField("seller_id")
//    private String sellerId;

    /**
     * 功能描述: 卖家支付宝账号
     * @date: 2019/10/25 13:57
     */
    @TableField("seller_email")
    private String sellerRmail;

    /**
     * 功能描述:  交易状态
     * @date: 2019/10/25 14:05
     */
    @TableField("trade_status")
    private String tradeStatus;
//
//    /**
//     * 功能描述:  订单金额
//     * @date: 2019/10/25 14:05
//     */
//    @TableField("total_amount")
//    private String totalAmount;
    /**
     * 功能描述:  实收金额
     * @date: 2019/10/25 14:05
     */
    @TableField("receipt_amount")
    private String receiptAmount;
//    /**
//     * 功能描述:  开票金额
//     * @date: 2019/10/25 14:05
//     */
//    @TableField("invoice_amount")
//    private String invoiceAmount;
    /**
     * 功能描述:  付款金额
     * @date: 2019/10/25 14:05
     */
    @TableField("buyer_pay_amount")
    private String buyerPayAmount;

//    /**
//     * 功能描述:  集分宝金额
//     * @date: 2019/10/25 14:05
//     */
//    @TableField("point_amount")
//    private String pointAmount;


    /**
     * 功能描述:  总退款金额
     * @date: 2019/10/25 14:05
     */
    @TableField("refund_fee")
    private String refundFee;

    /**
     * 功能描述: subject  订单标题  body  商品描述,fund_bill_list 支付金额信息,passback_params 回传参数,voucher_detail_list 优惠券信息
     * @date: 2019/10/25 14:05
     */
    @TableField("content")
    private String content;

    /**
     * 功能描述:  交易付款时间
     * @date: 2019/10/25 14:05
     */
    @TableField("gmt_payment")
    private String gmtPayment;

    /**
     * 功能描述:  交易退款时间
     * @date: 2019/10/25 14:05
     */
    @TableField("gmt_refund")
    private Date gmtRefund;

    /**
     * 功能描述:  gmt_create 交易创建时间 , gmt_close 交易结束时间 , notify_time 通知时间
     * @date: 2019/10/25 14:05
     */
    @TableField("date_content")
    private String dateContent;

    /**
     * 功能描述:  buyer_id 买家支付宝用户号 , seller_id 卖家支付宝用户号 , total_amount 订单金额,invoice_amount 开票金额	,point_amount 集分宝金额
     * @date: 2019/10/25 14:05
     */
    @TableField("money_content")
    private String moneyContent;
    /**
     * 功能描述:  notify_type 通知类型, notify_id	通知校验ID,charset	编码格式,version	接口版本	,sign_type	签名类型,sign	签名
     * @date: 2019/10/25 14:05
     */
    @TableField("header_content")
    private String headerContent;

    @TableField("response_content")
    private String responseContent;

    /**
     * 功能描述: 状态 1 已发送零卡 0 未发送零卡
     * @date: 2019/10/28 10:12
     */
    @TableField("state")
    private String state;

    @TableField("isactive")
    private String isactive;

    @TableField("created_at")
    private Date createdAt;

    @TableField("updated_at")
    private Date updatedAt;

    @TableField("rversion")
    private Integer rversion;

    @TableField("description")
    private String description;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getPlayId() {
        return playId;
    }

    public void setPlayId(String playId) {
        this.playId = playId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOutBizNo() {
        return outBizNo;
    }

    public void setOutBizNo(String outBizNo) {
        this.outBizNo = outBizNo;
    }

    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
    }

    public String getSellerRmail() {
        return sellerRmail;
    }

    public void setSellerRmail(String sellerRmail) {
        this.sellerRmail = sellerRmail;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(String receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public String getBuyerPayAmount() {
        return buyerPayAmount;
    }

    public void setBuyerPayAmount(String buyerPayAmount) {
        this.buyerPayAmount = buyerPayAmount;
    }

    public String getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGmtPayment() {
        return gmtPayment;
    }

    public void setGmtPayment(String gmtPayment) {
        this.gmtPayment = gmtPayment;
    }

    public Date getGmtRefund() {
        return gmtRefund;
    }

    public void setGmtRefund(Date gmtRefund) {
        this.gmtRefund = gmtRefund;
    }

    public String getDateContent() {
        return dateContent;
    }

    public void setDateContent(String dateContent) {
        this.dateContent = dateContent;
    }

    public String getMoneyContent() {
        return moneyContent;
    }

    public void setMoneyContent(String moneyContent) {
        this.moneyContent = moneyContent;
    }

    public String getHeaderContent() {
        return headerContent;
    }

    public void setHeaderContent(String headerContent) {
        this.headerContent = headerContent;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getRversion() {
        return rversion;
    }

    public void setRversion(Integer rversion) {
        this.rversion = rversion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }
}