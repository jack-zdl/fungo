package com.game.common.dto.mall;


import java.io.Serializable;

/**
 * <p>
 *  商品信息封装Bean
 * </p>
 *
 * @author mxf
 * @since 2019-01-14
 */
public class MallGoodsOutBean implements Serializable {


    /**
     * 主键，19位
     */
    private String id;

    /**
     * 分类ID, 关联product_cates表ID
     */
    private String cid;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 秒杀价格-货币
     */
    private String seckillPriceCy;
    /**
     * 秒杀价格-虚拟币(fungo币)
     */
    private String seckillPriceVcy;

    /**
     * 剩余库存
     */
    private String residueStock;

    /**
     * 商品编码
     */
    private String goodsSn;

    /**
     * 产品状态 :
     -1 已删除 ，1 已 下架  ，  2 已 上架
     */
    private Integer goodsStatus;

    /**
     * 商品类型
     1 实物
     2 虚拟物品
     21 零卡
     22 京东卡
     23 QB卡
     */
    private Integer goodsType;


    /**
     * 商品主图,存储图片url集合,json格式：
     [{
     url,
     status:图标的状态( 1 标准、2 点亮、3 置灰),
     size: 图标的尺寸( 1 大图、2中、3 小图),
     style:图片式样 (1 标准，2 特别效果-底部阴影 )
     }]
     */
    private String mainImg;
    /**
     * 商品简介
     */
    private String goodsIntro;
    /**
     * 排序
     */
    private Integer sort;

    /**
     * 秒杀开始时间
     */
    private String startTime;
    /**
     * 秒杀结束时间
     */
    private String endTime;

    /**
     * 游戏礼包中，登录用户是否已经购买
     * true 已经购买
     * false 未购买
     */
    private boolean is_buy;

    /**
     * 游戏ID
     */
    private String gameId;

    /**
     * 有效期信息描述
     */
    private String validPeriodIntro;


    /**
     * 使用方法描述
     */
    private String usageDesc;


    public String getValidPeriodIntro() {
        return validPeriodIntro;
    }

    public void setValidPeriodIntro(String validPeriodIntro) {
        this.validPeriodIntro = validPeriodIntro;
    }

    public String getUsageDesc() {
        return usageDesc;
    }

    public void setUsageDesc(String usageDesc) {
        this.usageDesc = usageDesc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
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

    public String getResidueStock() {
        return residueStock;
    }

    public void setResidueStock(String residueStock) {
        this.residueStock = residueStock;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public boolean isIs_buy() {
        return is_buy;
    }

    public void setIs_buy(boolean is_buy) {
        this.is_buy = is_buy;
    }


    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return "MallGoodsOutBean{" +
                "id='" + id + '\'' +
                ", cid='" + cid + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", seckillPriceCy='" + seckillPriceCy + '\'' +
                ", seckillPriceVcy='" + seckillPriceVcy + '\'' +
                ", residueStock='" + residueStock + '\'' +
                ", goodsSn='" + goodsSn + '\'' +
                ", goodsStatus=" + goodsStatus +
                ", goodsType=" + goodsType +
                ", mainImg='" + mainImg + '\'' +
                ", goodsIntro='" + goodsIntro + '\'' +
                ", sort=" + sort +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", is_buy=" + is_buy +
                '}';
    }


    //----------
}
