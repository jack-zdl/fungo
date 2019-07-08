package com.fungo.system.mall.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author mxf
 * @since 2019-01-14
 */
@TableName("t_mall_order")
public class MallOrder extends Model<MallOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private Long id;
    /**
     * 订单号
     */
	@TableField("order_sn")
	private String orderSn;
    /**
     * 用户id
     */
	@TableField("mb_id")
	private String mbId;
    /**
     * 用户名称
     */
	@TableField("mb_name")
	private String mbName;
    /**
     * 用户手机号
     */
	@TableField("mb_mobile")
	private String mbMobile;
    /**
     * 订单状态:  订单状态:  -1 未确认 ,1 已确认 , 2  已取消 -用户 ,3  无效 ,4  退货；5 交易成功 ， 6 已取消-运营方,7 部分商品交易成功
     */
	@TableField("order_status")
	private Integer orderStatus;
    /**
     * 支付状态:  -1 未付款 , 1 付款中 , 2 已付款  , 3 已冻结余额
     */
	@TableField("pay_status")
	private Integer payStatus;
    /**
     * 商品配送情况  -1 未发货 , 1 已发货 , 2 已收货 ,3 备货中，4 无收货信息，5 有收货信息
     */
	@TableField("shipping_status")
	private Integer shippingStatus;
    /**
     * 商品总金额-货币
     */
	@TableField("goods_amount_cy")
	private BigDecimal goodsAmountCy;
    /**
     * 商品总金额-虚拟币(fungo)
     */
	@TableField("goods_amount_vcy")
	private Long goodsAmountVcy;
    /**
     * 收货人的姓名
     */
	@TableField("consignee_name")
	private String consigneeName;
    /**
     * 收货人地址
     */
	@TableField("csg_address")
	private String csgAddress;
    /**
     * 收货人手机
     */
	@TableField("csg_mobile")
	private String csgMobile;
    /**
     * 订单生成时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 订单确认时间
     */
	@TableField("confirm_time")
	private Date confirmTime;
    /**
     * 订单支付时间
     */
	@TableField("pay_time")
	private Date payTime;
    /**
     * 订单配送时间
     */
	@TableField("shipping_time")
	private Date shippingTime;
    /**
     * 订单修改时间
     */
	@TableField("updated_time")
	private Date updatedTime;
    /**
     * 扩展字段1
     */
	private String ext1;
    /**
     * 扩展字段2
     */
	private String ext2;
    /**
     * 扩展字段3
     */
	private String ext3;


	/**
	 * 订单类型： 1 商城(默认) , 2 游戏礼包
	 */
	@TableField("order_type")
	private Integer orderType;



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getShippingTime() {
		return shippingTime;
	}

	public void setShippingTime(Date shippingTime) {
		this.shippingTime = shippingTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
}
