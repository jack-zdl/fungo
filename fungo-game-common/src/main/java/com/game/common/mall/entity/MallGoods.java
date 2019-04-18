package com.game.common.mall.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 商品信息表（基本信息）
 * </p>
 *
 * @author mxf
 * @since 2019-01-14
 */
@TableName("t_mall_goods")
public class MallGoods extends Model<MallGoods> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，19位
     */
	private Long id;
    /**
     * 分类ID, 关联product_cates表ID
     */
	private Long cid;
    /**
     * 商品名称
     */
	@TableField("goods_name")
	private String goodsName;
    /**
     * 商品定价-货币
     */
	@TableField("market_price_cy")
	private BigDecimal marketPriceCy;
    /**
     * 商品定价-虚拟币(fungo币)
     */
	@TableField("market_price_vcy")
	private Long marketPriceVcy;
    /**
     * 商品编码
     */
	@TableField("goods_sn")
	private String goodsSn;
    /**
     * 产品状态 :              
-1 已删除 ，1 已 下架  ，  2 已 上架
     */
	@TableField("goods_status")
	private Integer goodsStatus;
    /**
     * 商品类型
				1 实物
				2 虚拟物品
				   21 零卡
				   22 京东卡
				   23 QB卡
     */
	@TableField("goods_type")
	private Integer goodsType;
    /**
     * 关键字,方便用户搜索
     */
	private String keywords;
    /**
     * 商品主图,存储图片url集合,json格式：
[{
   url,
   status:图标的状态( 1 标准、2 点亮、3 置灰),
   size: 图标的尺寸( 1 大图、2中、3 小图),
   style:图片式样 (1 标准，2 特别效果-底部阴影 )
}]
     */
	@TableField("main_img")
	private String mainImg;
    /**
     * 商品简介
     */
	@TableField("goods_intro")
	private String goodsIntro;
    /**
     * 排序
     */
	private Integer sort;
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

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public BigDecimal getMarketPriceCy() {
		return marketPriceCy;
	}

	public void setMarketPriceCy(BigDecimal marketPriceCy) {
		this.marketPriceCy = marketPriceCy;
	}

	public Long getMarketPriceVcy() {
		return marketPriceVcy;
	}

	public void setMarketPriceVcy(Long marketPriceVcy) {
		this.marketPriceVcy = marketPriceVcy;
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public Integer getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public Integer getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(Integer goodsType) {
		this.goodsType = goodsType;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getMainImg() {
		return mainImg;
	}

	public void setMainImg(String mainImg) {
		this.mainImg = mainImg;
	}

	public String getGoodsIntro() {
		return goodsIntro;
	}

	public void setGoodsIntro(String goodsIntro) {
		this.goodsIntro = goodsIntro;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
