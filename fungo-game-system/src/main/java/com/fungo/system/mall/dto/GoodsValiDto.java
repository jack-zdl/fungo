package com.fungo.system.mall.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * <p>礼包管理页面查询校验类</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/4/1
 */
public class GoodsValiDto {

    @Min(value = 0,message="礼品分类数值不对")
    private Long goodsCid;

    @Min(value = 0,message = "礼品主键数值不对")
    private Long goodsId;

    @Min(value = 0,message = "礼品类型不对")
    @Max(value = 30,message = "礼品类型不对")
    private Integer type;

    @Length(min=0,max=128,message = "用户昵称长度不符")
    private String mbNickName;

    private String mbId;

    private Date exchangeDateStart;

    private Date exchangeDateEnd;

    private String exchangeDateStartStr;

    private String exchangeDateEndStr;

    @Min(value = 0,message = "序型不对")
    @Max(value = 2,message = "序型不对")
    private int orderType;
    //当前页
    private int currentPage;
    // 每页显示的总条数
    private int pageSize;

    private int startLimit;

    private int endLimit;

    public Long getGoodsCid() {
        return goodsCid;
    }

    public void setGoodsCid(Long goodsCid) {
        this.goodsCid = goodsCid;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMbNickName() {
        return mbNickName;
    }

    public void setMbNickName(String mbNickName) {
        this.mbNickName = mbNickName;
    }

    public Date getExchangeDateStart() {
        return exchangeDateStart;
    }

    public void setExchangeDateStart(Date exchangeDateStart) {
        this.exchangeDateStart = exchangeDateStart;
    }

    public Date getExchangeDateEnd() {
        return exchangeDateEnd;
    }

    public void setExchangeDateEnd(Date exchangeDateEnd) {
        this.exchangeDateEnd = exchangeDateEnd;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getMbId() {
        return mbId;
    }

    public void setMbId(String mbId) {
        this.mbId = mbId;
    }

    public String getExchangeDateStartStr() {
        return exchangeDateStartStr;
    }

    public void setExchangeDateStartStr(String exchangeDateStartStr) {
        this.exchangeDateStartStr = exchangeDateStartStr;
    }

    public String getExchangeDateEndStr() {
        return exchangeDateEndStr;
    }

    public void setExchangeDateEndStr(String exchangeDateEndStr) {
        this.exchangeDateEndStr = exchangeDateEndStr;
    }

    public int getStartLimit() {
        return startLimit;
    }

    public void setStartLimit(int startLimit) {
        this.startLimit = startLimit;
    }

    public int getEndLimit() {
        return endLimit;
    }

    public void setEndLimit(int endLimit) {
        this.endLimit = endLimit;
    }
}
