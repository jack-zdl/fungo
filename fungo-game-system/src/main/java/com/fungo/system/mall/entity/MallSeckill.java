package com.fungo.system.mall.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 商城秒杀信息表
 * </p>
 *
 * @author mxf
 * @since 2019-01-14
 */
@TableName("t_mall_seckill")
public class MallSeckill extends Model<MallSeckill> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private Long id;
    /**
     * 商品表ID
     */
	@TableField("goods_id")
	private Long goodsId;
    /**
     * 商品名称
     */
	@TableField("goods_name")
	private String goodsName;
    /**
     * 秒杀价格-货币  加密保存
     */
	@TableField("seckill_price_cy")
	private String seckillPriceCy;
    /**
     * 秒杀价格-虚拟币(fungo币) 加密保存
     */
	@TableField("seckill_price_vcy")
	private String seckillPriceVcy;
    /**
     * 秒杀总库存 加密保存
     */
	@TableField("total_stock")
	private String totalStock;
    /**
     * 剩余库存 加密保存
     */
	@TableField("residue_stock")
	private String residueStock;
    /**
     * 秒杀开始时间
     */
	@TableField("start_time")
	private Date startTime;
    /**
     * 秒杀结束时间
     */
	@TableField("end_time")
	private Date endTime;
    /**
     * 乐观锁
     */
	@TableField("cas_version")
	private Integer casVersion;
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


	/**
	 * 扩展字段4
	 */
	@TableField(exist = false)
	private String ext4;
	/**
	 * 扩展字段5
	 */
	@TableField(exist = false)
	private String ext5;
	/**
	 * 扩展字段6
	 */
	@TableField(exist = false)
	private String ext6;



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

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getSeckillPriceCy() {
		return seckillPriceCy;
	}

	public void setSeckillPriceCy(String seckillPriceCy) {
		this.seckillPriceCy = seckillPriceCy;
	}

	public String getSeckillPriceVcy() {
		return seckillPriceVcy;
	}

	public void setSeckillPriceVcy(String seckillPriceVcy) {
		this.seckillPriceVcy = seckillPriceVcy;
	}

	public String getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(String totalStock) {
		this.totalStock = totalStock;
	}

	public String getResidueStock() {
		return residueStock;
	}

	public void setResidueStock(String residueStock) {
		this.residueStock = residueStock;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getCasVersion() {
		return casVersion;
	}

	public void setCasVersion(Integer casVersion) {
		this.casVersion = casVersion;
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


	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public String getExt5() {
		return ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}

	public String getExt6() {
		return ext6;
	}

	public void setExt6(String ext6) {
		this.ext6 = ext6;
	}
}
