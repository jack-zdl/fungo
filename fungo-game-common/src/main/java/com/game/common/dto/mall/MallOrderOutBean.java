package com.game.common.dto.mall;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *    用户订单输出信息封装Bean
 * </p>
 *
 * @author mxf
 * @since 2019-01-14
 */
public class MallOrderOutBean implements Serializable {

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 用户id
     */
    private String mbId;

    /**
     * 订单对应的商品列表
     */
    private List<MallGoodsOutBean> goodsList;

    /**
     * 购买到的虚拟卡信息
     */
    private Map<String,Object> cardInfo = new HashMap<String,Object>();

    /**
     * 购买成功提示信息
     */
    private String sucMsg;

    /**
     * 用户名称
     */
    private String mbName;

    /**
     * 用户手机号
     */
    private String mbMobile;

    /**
     * 订单状态:  -1 未确认 ,1   已确认 , 2  已取消  ,3  无效 ,4  退货；
     */
    private Integer orderStatus;

    /**
     * 支付状态:  -1 未付款 , 1 付款中 , 2 已付款
     */
    private Integer payStatus;

    /**
     * 商品配送情况  -1 未发货 , 1 已发货 , 2 已收货 ,3 备货中
     */
    private Integer shippingStatus;

    /**
     * 商品总金额-货币
     */
    private BigDecimal goodsAmountCy;

    /**
     * 商品总金额-虚拟币(fungo)
     */
    private Long goodsAmountVcy;

    /**
     * 收货人的姓名
     */
    private String consigneeName;

    /**
     * 收货人地址
     */
    private String csgAddress;

    /**
     * 收货人手机
     */
    private String csgMobile;

    /**
     * 订单生成时间
     */
    private String createTime;

    /**
     * 订单确认时间
     */
    private String confirmTime;

    /**
     * 订单支付时间
     */
    private String payTime;

    /**
     * 订单配送时间
     */
    private String shippingTime;

    /**
     * 订单修改时间
     */
    private String updatedTime;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getMbId() {
        return mbId;
    }

    public void setMbId(String mbId) {
        this.mbId = mbId;
    }

    public List<MallGoodsOutBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<MallGoodsOutBean> goodsList) {
        this.goodsList = goodsList;
    }

    public String getMbName() {
        return mbName;
    }

    public void setMbName(String mbName) {
        this.mbName = mbName;
    }

    public String getMbMobile() {
        return mbMobile;
    }

    public void setMbMobile(String mbMobile) {
        this.mbMobile = mbMobile;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(Integer shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public BigDecimal getGoodsAmountCy() {
        return goodsAmountCy;
    }

    public void setGoodsAmountCy(BigDecimal goodsAmountCy) {
        this.goodsAmountCy = goodsAmountCy;
    }

    public Long getGoodsAmountVcy() {
        return goodsAmountVcy;
    }

    public void setGoodsAmountVcy(Long goodsAmountVcy) {
        this.goodsAmountVcy = goodsAmountVcy;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getCsgAddress() {
        return csgAddress;
    }

    public void setCsgAddress(String csgAddress) {
        this.csgAddress = csgAddress;
    }

    public String getCsgMobile() {
        return csgMobile;
    }

    public void setCsgMobile(String csgMobile) {
        this.csgMobile = csgMobile;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(String shippingTime) {
        this.shippingTime = shippingTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Map<String, Object> getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(Map<String, Object> cardInfo) {
        this.cardInfo = cardInfo;
    }

    public String getSucMsg() {
        return sucMsg;
    }

    public void setSucMsg(String sucMsg) {
        this.sucMsg = sucMsg;
    }

    @Override
    public String toString() {
        return "MallOrderOutBean{" +
                "orderId='" + orderId + '\'' +
                ", orderSn='" + orderSn + '\'' +
                ", mbId='" + mbId + '\'' +
                ", goodsList=" + goodsList +
                ", cardInfo=" + cardInfo +
                ", sucMsg='" + sucMsg + '\'' +
                ", mbName='" + mbName + '\'' +
                ", mbMobile='" + mbMobile + '\'' +
                ", orderStatus=" + orderStatus +
                ", payStatus=" + payStatus +
                ", shippingStatus=" + shippingStatus +
                ", goodsAmountCy=" + goodsAmountCy +
                ", goodsAmountVcy=" + goodsAmountVcy +
                ", consigneeName='" + consigneeName + '\'' +
                ", csgAddress='" + csgAddress + '\'' +
                ", csgMobile='" + csgMobile + '\'' +
                ", createTime='" + createTime + '\'' +
                ", confirmTime='" + confirmTime + '\'' +
                ", payTime='" + payTime + '\'' +
                ", shippingTime='" + shippingTime + '\'' +
                ", updatedTime='" + updatedTime + '\'' +
                '}';
    }
}
