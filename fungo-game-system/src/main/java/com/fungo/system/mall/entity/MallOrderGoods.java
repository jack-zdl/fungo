package com.fungo.system.mall.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单的商品信息表
 * </p>
 *
 * @author mxf
 * @since 2019-01-14
 */
@TableName("t_mall_order_goods")
public class MallOrderGoods extends Model<MallOrderGoods> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private Long id;

	/**
	 * 用户id
	 */
	@TableField("mb_id")
	private String mbId;

    /**
     * 订单id
     */
	@TableField("order_id")
	private Long orderId;
    /**
     * 分类ID, 关联product_cates表ID
     */
	@TableField("goods_cid")
	private Long goodsCid;
    /**
     * 商品分类名称
     */
	@TableField("goods_cid_title")
	private String goodsCidTitle;
    /**
     * 商品id
     */
	@TableField("goods_id")
	private Long goodsId;
    /**
     * 商品名称
     */
	@TableField("goods_name")
	private String goodsName;
    /**
     * 商品编码
     */
	@TableField("goods_sn")
	private String goodsSn;
    /**
     * 商品的购买数量
     */
	@TableField("goods_number")
	private Long goodsNumber;
    /**
     *  商品售价-货币 不加密
     */
	@TableField("goods_price_cy")
	private BigDecimal goodsPriceCy;
    /**
     *  商品售价-虚拟币(fungo币)  不加密
     */
	@TableField("goods_price_vcy")
	private Long goodsPriceVcy;
    /**
     * 商品详情信息json
	 * 若是虚拟信息保存商品本身信息和兑换卡信息{"goodsInfo":"" , "cardInfo:"}
     */
	@TableField("goods_att")
	private String goodsAtt;

    /**
		 商品类型
		 1 实物
		 2 虚拟物品
		 21 零卡
		 22 京东卡
		 23 QB卡
		 3 游戏礼包
   
     */
	@TableField("goods_type")
	private Integer goodsType;
    /**
     * 创建人ID(用户)
     */
	@TableField("creator_id")
	private String creatorId;
    /**
     * 创建人名称(用户)
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

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getGoodsCid() {
		return goodsCid;
	}

	public void setGoodsCid(Long goodsCid) {
		this.goodsCid = goodsCid;
	}

	public String getGoodsCidTitle() {
		return goodsCidTitle;
	}

	public void setGoodsCidTitle(String goodsCidTitle) {
		this.goodsCidTitle = goodsCidTitle;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public Long getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Long goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public BigDecimal getGoodsPriceCy() {
		return goodsPriceCy;
	}

	public void setGoodsPriceCy(BigDecimal goodsPriceCy) {
		this.goodsPriceCy = goodsPriceCy;
	}

	public Long getGoodsPriceVcy() {
		return goodsPriceVcy;
	}

	public void setGoodsPriceVcy(Long goodsPriceVcy) {
		this.goodsPriceVcy = goodsPriceVcy;
	}

	public String getGoodsAtt() {
		return goodsAtt;
	}

	public void setGoodsAtt(String goodsAtt) {
		this.goodsAtt = goodsAtt;
	}

	public Integer getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(Integer goodsType) {
		this.goodsType = goodsType;
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

	public String getMbId() {
		return mbId;
	}

	public void setMbId(String mbId) {
		this.mbId = mbId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
