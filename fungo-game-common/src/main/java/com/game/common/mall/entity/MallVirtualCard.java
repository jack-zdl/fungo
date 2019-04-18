package com.game.common.mall.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 虚拟卡卡号库
 * </p>
 *
 * @author mxf
 * @since 2019-01-14
 */
@TableName("t_mall_virtual_card")
public class MallVirtualCard extends Model<MallVirtualCard> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private Long id;
    /**
     * 该卡号对应的商品ID
     */
	@TableField("goods_id")
	private Long goodsId;
    /**
     * 卡号:加密
     */
	@TableField("card_sn")
	private String cardSn;
    /**
     * 卡号密码:加密
     */
	@TableField("card_pwd")
	private String cardPwd;
    /**
     * 截至使用日期，如：2019-12-31
     */
	@TableField("card_end_date")
	private Date cardEndDate;
    /**
     * 有效期描述，如：2019.01.11-2022.01.11
     */
	@TableField("valid_period_intro")
	private String validPeriodIntro;

	/**
	 * 价值RMB面额
	 */
	@TableField("value_rmb")
	private Integer valueRmb;


    /**
     * 是否卖出: -1 否 ; 1 是
     */
	@TableField("is_saled")
	private Integer isSaled;

    /**
     * 卖出该卡号的订单Id
     */
	@TableField("order_id")
	private Long orderId;

	/**
	 * 用户id
	 */
	@TableField("mb_id")
	private String mbId;

    /**
     * 数据有效性验证
	 *  卡号明码 + 密码明码的 crc32取值
     */
	@TableField("card_crc32")
	private Long cardCrc32;

	/**
	 * 卡的类型
			 21 零卡
			 22 京东卡
			 23 QB卡
	 */
	@TableField("card_type")
	private Integer cardType;

    /**
     * 创建人ID
     */
	@TableField("creator_id")
	private String creatorId;
    /**
     * 创建人名称
     */
	@TableField("creator_name")
	private String creatorName;
    /**
     * 创建时间
     */
	@TableField("created_at")
	private Date createdAt;
    /**
     * 更新时间
     */
	@TableField("updated_at")
	private Date updatedAt;
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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getCardSn() {
		return cardSn;
	}

	public void setCardSn(String cardSn) {
		this.cardSn = cardSn;
	}

	public String getCardPwd() {
		return cardPwd;
	}

	public void setCardPwd(String cardPwd) {
		this.cardPwd = cardPwd;
	}

	public Date getCardEndDate() {
		return cardEndDate;
	}

	public void setCardEndDate(Date cardEndDate) {
		this.cardEndDate = cardEndDate;
	}

	public String getValidPeriodIntro() {
		return validPeriodIntro;
	}

	public void setValidPeriodIntro(String validPeriodIntro) {
		this.validPeriodIntro = validPeriodIntro;
	}

	public Integer getIsSaled() {
		return isSaled;
	}

	public void setIsSaled(Integer isSaled) {
		this.isSaled = isSaled;
	}



	public Long getCardCrc32() {
		return cardCrc32;
	}

	public void setCardCrc32(Long cardCrc32) {
		this.cardCrc32 = cardCrc32;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
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

	public Integer getValueRmb() {
		return valueRmb;
	}

	public void setValueRmb(Integer valueRmb) {
		this.valueRmb = valueRmb;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getMbId() {
		return mbId;
	}

	public void setMbId(String mbId) {
		this.mbId = mbId;
	}

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
